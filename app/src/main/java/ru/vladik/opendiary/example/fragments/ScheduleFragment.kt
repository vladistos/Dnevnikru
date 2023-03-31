package ru.vladik.opendiary.example.fragments

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
import ru.vladik.opendiary.R
import ru.vladik.opendiary.adapters.recyclerview.DayPickerAdapter
import ru.vladik.opendiary.adapters.recyclerview.ScheduleAdapter
import ru.vladik.opendiary.databinding.FragmentScheduleBinding
import ru.vladik.opendiary.dnevnikapi.DiaryApi
import ru.vladik.opendiary.dnevnikapi.models.extended.ExtendedWeek
import ru.vladik.opendiary.example.datasets.ExampleDatasets
import ru.vladik.opendiary.util.DateHelper
import ru.vladik.opendiary.viewmodels.fragments.FragmentScheduleViewModel
import java.util.*
import kotlin.math.abs

/**
 * # Фрагмент, который отвечает за отображение расписания.
 *
 * @property mBinding [FragmentScheduleBinding]
 * @property mViewModel [FragmentScheduleViewModel]
 * @property currentTimestamp [String]
 * @property api [DiaryApi]
 * @property resp [ExtendedWeek]
 */

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

        val scheduleAdapter = ScheduleAdapter(ExampleDatasets.ExampleSchedule)
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

        private val mThreshold = 500

        override fun onFling(velocityX: Int, velocityY: Int): Boolean {
            if (abs(velocityX) < mThreshold) return false
            val toLeft = velocityX < 0
            val week = if (toLeft) mViewModel.decrementViewingWeek() else mViewModel.incrementViewingWeek()
            scrollWeek(toLeft, adapter, week.toTypedArray())
            return true
        }

    }

}