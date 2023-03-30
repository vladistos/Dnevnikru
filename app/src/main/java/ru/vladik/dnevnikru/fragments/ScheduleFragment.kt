package ru.vladik.dnevnikru.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnFlingListener
import com.faltenreich.skeletonlayout.createSkeleton
import ru.vladik.dnevnikru.R
import ru.vladik.dnevnikru.adapters.recyclerview.CustomSkeleton
import ru.vladik.dnevnikru.adapters.recyclerview.DayPickerAdapter
import ru.vladik.dnevnikru.adapters.recyclerview.ScheduleAdapter
import ru.vladik.dnevnikru.adapters.recyclerview.applyCustomSkeleton
import ru.vladik.dnevnikru.databinding.FragmentScheduleBinding
import ru.vladik.dnevnikru.databinding.ScheduleItemLoadingBinding
import ru.vladik.dnevnikru.dnevnikapi.DiaryApi
import ru.vladik.dnevnikru.dnevnikapi.models.extended.ExtendedWeek
import ru.vladik.dnevnikru.dnevnikapi.models.v2.LessonV2
import ru.vladik.dnevnikru.dnevnikapi.models.v2.MarkV2
import ru.vladik.dnevnikru.dnevnikapi.models.v7.ScheduleV7
import ru.vladik.dnevnikru.util.DateHelper
import ru.vladik.dnevnikru.viewmodels.errorhandling.DiaryApiResourceListener
import ru.vladik.dnevnikru.viewmodels.errorhandling.ResourceObserver
import ru.vladik.dnevnikru.viewmodels.fragments.FragmentScheduleViewModel
import java.util.*
import kotlin.math.abs

class ScheduleFragment : Fragment() {

    private lateinit var mBinding: FragmentScheduleBinding
    private lateinit var mViewModel: FragmentScheduleViewModel
    private var currentTimestamp: String = ""
    private var api: DiaryApi? = null
    private var resp: ExtendedWeek = ExtendedWeek()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentScheduleBinding.inflate(inflater, container, false)
        mViewModel = ViewModelProvider(requireActivity())[FragmentScheduleViewModel::class.java]
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = mBinding.run {

        daysRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        scheduleRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        val scheduleAdapter = ScheduleAdapter()
        val dayPickerAdapter = DayPickerAdapter(mViewModel.week)

        scheduleRecyclerView.adapter = scheduleAdapter

        dayPickerAdapter.onItemClickListener = { _, data, _ ->
            val week = mViewModel.setSelectedDay(data)
            dayPickerAdapter.replaceData(*week.toTypedArray())
        }

        (requireActivity() as AppCompatActivity).apply {
            addMenuProvider(ScheduleFragmentMenuProvider(dayPickerAdapter), viewLifecycleOwner,
                Lifecycle.State.RESUMED)

            setSupportActionBar(toolbar)
        }

        daysRecyclerView.onFlingListener = DaysRecyclerViewOnFlingListener(dayPickerAdapter)
        daysRecyclerView.adapter = dayPickerAdapter

        buttonLeft.setOnClickListener {
            scrollWeek(true, dayPickerAdapter, mViewModel.decrementViewingWeek().toTypedArray())
        }

        buttonRight.setOnClickListener {
            scrollWeek(false, dayPickerAdapter, mViewModel.incrementViewingWeek().toTypedArray())
        }


        mViewModel.getDiaryForLastUser(requireContext())
        mViewModel.prevWeekStartToGetNext.observe(requireActivity()) {
            getScheduleIfNeeded()
        }
        mViewModel.diaryApi.observeValue(requireActivity(), DiaryApiResourceListener(requireActivity()) { api, _ ->
            this@ScheduleFragment.api = api
            getScheduleIfNeeded()
        })
        mViewModel.selectedDay.observe(requireActivity()) { refreshDay(scheduleAdapter) }


        mViewModel.schedule.observeValue(requireActivity(), ScheduleObserver(scheduleAdapter))
        mViewModel.lessonsV2.observeValue(requireActivity(), LessonsV2Observer(scheduleAdapter))
        mViewModel.marksV2ByLessonId.observeValue(requireActivity(), MarksObserver(scheduleAdapter))
    }

    private inner class MarksObserver(val scheduleAdapter: ScheduleAdapter) : ResourceObserver<Map<Long, ArrayList<MarkV2>>>() {
        override fun onReady(v: Map<Long, ArrayList<MarkV2>>) {
            this@ScheduleFragment.resp.marksV2 = v
            refreshDay(scheduleAdapter)
        }
    }

    private inner class LessonsV2Observer(val scheduleAdapter: ScheduleAdapter) : ResourceObserver<Map<Long, LessonV2>>() {
        override fun onReady(v: Map<Long, LessonV2>) {
            resp.lessonsV2 = v
            refreshDay(scheduleAdapter)
        }
    }

    private inner class ScheduleObserver(val scheduleAdapter: ScheduleAdapter) : ResourceObserver<ScheduleV7>() {

        private var mSkeleton: CustomSkeleton<ScheduleItemLoadingBinding>? = null

        override fun onReady(v: ScheduleV7) = mBinding.scheduleRecyclerView.run {
            if (v.weeks.isNullOrEmpty() || v.weeks[0].days.isNullOrEmpty() ||
                requireNotNull(v.weeks[0].days).size < 7) return@run
            this@ScheduleFragment.resp.days = requireNotNull(v.weeks[0].days)
            refreshDay(scheduleAdapter) {
                mSkeleton?.showOriginal()
            }
        }

        override fun onProgress(p: Float) {
            if (mSkeleton == null) {
                mSkeleton =
                    mBinding.scheduleRecyclerView.applyCustomSkeleton({ layoutInflater, parent ->
                        ScheduleItemLoadingBinding.inflate(layoutInflater, parent, false)
                    }, { holder ->
                        holder.binding.apply {
                            lessonNumber.createSkeleton().showSkeleton()
                            skeleton1.showSkeleton()
                            skeleton2.showSkeleton()
                            skeleton3.showSkeleton()
                        }
                    })
            }
            mSkeleton?.showSkeleton()
        }
    }

    private fun refreshDay(adapter: ScheduleAdapter, post: (() -> Unit)? = null) = mBinding.scheduleRecyclerView.run {
        if (resp.days == null) return@run
        val days = requireNotNull(resp.extendedDays)
        val dayNumber = mViewModel.selectedDay.value!!.ruIndex - 1
        adapter.replaceData(*days[dayNumber].getExtendedLessons(resp.lessonsV2, resp.marksV2).orEmpty().toTypedArray(), callback = post)
    }

    private fun getScheduleIfNeeded() {
        if (currentTimestamp == mViewModel.prevWeekStartToGetNext.value || api == null)
            return
        currentTimestamp = mViewModel.prevWeekStartToGetNext.value.toString()
        val api = requireNotNull(api)

        mViewModel.getSchedule(api)
        mViewModel.getV2Lessons(api)
        mViewModel.getV2Marks(api)
    }

    private fun scrollWeek(toLeft: Boolean, adapter: DayPickerAdapter, week: Array<DateHelper.Day>) = mBinding.run {
        if (toLeft) adapter.insertDataToBeginning(*week) {
            daysRecyclerView.smoothScrollToPosition(0)
        } else adapter.addData(*week) {
            daysRecyclerView.smoothScrollToPosition(adapter.itemCount - 1)
        }
        val listener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState != RecyclerView.SCROLL_STATE_IDLE) return
                recyclerView.removeOnScrollListener(this)
                adapter.replaceData(*week)
            }
        }
        daysRecyclerView.addOnScrollListener(listener)
    }

    private inner class ScheduleFragmentMenuProvider(val adapter: DayPickerAdapter) : MenuProvider {

        private val mListener =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.ROOT).apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }

                val week = mViewModel.setSelectedDay(DateHelper.Day(calendar, calendar))
                adapter.replaceData(*week.toTypedArray())
            }

        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.fragment_schedule_menu, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            if (menuItem.itemId == R.id.calendar_item) {
                val day = requireNotNull(mViewModel.selectedDay.value).calendarDay
                val picker = DatePickerDialog(requireContext(), mListener, day.get(Calendar.YEAR),
                day.get(Calendar.MONTH), day.get(Calendar.DAY_OF_MONTH))
                picker.show()
            }
            return true
        }
    }

    private inner class DaysRecyclerViewOnFlingListener(val adapter: DayPickerAdapter) : OnFlingListener() {

        private val mThreshold = 900

        override fun onFling(velocityX: Int, velocityY: Int): Boolean {
            if (abs(velocityX) < mThreshold) return false
            val toLeft = velocityX < 0
            val week = if (toLeft) mViewModel.decrementViewingWeek() else mViewModel.incrementViewingWeek()
            scrollWeek(toLeft, adapter, week.toTypedArray())
            return true
        }

    }

}