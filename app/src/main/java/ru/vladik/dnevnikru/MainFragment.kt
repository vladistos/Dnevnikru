package ru.vladik.dnevnikru

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.faltenreich.skeletonlayout.createSkeleton
import io.getstream.avatarview.coil.loadImage
import ru.vladik.dnevnikru.adapters.recyclerview.CustomSkeleton
import ru.vladik.dnevnikru.adapters.recyclerview.FeedPostAdapter
import ru.vladik.dnevnikru.adapters.recyclerview.MarksAdapter
import ru.vladik.dnevnikru.adapters.recyclerview.applyCustomSkeleton
import ru.vladik.dnevnikru.databinding.FeedPostLoadingBinding
import ru.vladik.dnevnikru.databinding.FragmentMainBinding
import ru.vladik.dnevnikru.databinding.MarkElementLoadingBinding
import ru.vladik.dnevnikru.dnevnikapi.DiaryApi
import ru.vladik.dnevnikru.dnevnikapi.models.v2.RecentMarksResp
import ru.vladik.dnevnikru.dnevnikapi.models.v7.UserFeedResponse
import ru.vladik.dnevnikru.ext.getDp
import ru.vladik.dnevnikru.ext.isNotAdded
import ru.vladik.dnevnikru.func.showErrDialog
import ru.vladik.dnevnikru.viewmodels.errorhandling.DiaryApiResourceListener
import ru.vladik.dnevnikru.viewmodels.errorhandling.ResourceObserver
import ru.vladik.dnevnikru.viewmodels.fragments.FragmentMainViewModel
import ru.vladik.dnevnikru.viewmodels.launchToLiveData
import kotlin.math.abs
import kotlin.math.min

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var mBinding: FragmentMainBinding
    private lateinit var mViewModel: FragmentMainViewModel

    private var mMarksRecyclerViewSkeleton: CustomSkeleton<MarkElementLoadingBinding>? = null
    private var mPostsRecyclerViewSkeleton: CustomSkeleton<FeedPostLoadingBinding>? = null

    private lateinit var mMarksRecyclerViewAdapter: MarksAdapter
    private lateinit var mPostsRecyclerViewAdapter: FeedPostAdapter

    private lateinit var mDiaryObserver: ResourceObserver<DiaryApi>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mDiaryObserver = getDiaryObserver(requireActivity())
        mBinding = FragmentMainBinding.bind(view)
        mViewModel = ViewModelProvider(requireActivity())[FragmentMainViewModel::class.java]
        mViewModel.getDiaryForLastUser(requireContext())
        mViewModel.diaryApi.observeValue(requireActivity(), mDiaryObserver)
        mBinding.apply {
            marksRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                mMarksRecyclerViewAdapter = MarksAdapter()
                adapter = mMarksRecyclerViewAdapter
                mMarksRecyclerViewSkeleton = applyCustomSkeleton({layoutInflater, parent ->
                    MarkElementLoadingBinding.inflate(layoutInflater, parent, false)
                }, { holder ->
                    holder.binding.apply {
                        skeleton1.showSkeleton()
                        markSubject.createSkeleton().showSkeleton()
                    }
                })
                mMarksRecyclerViewSkeleton?.showSkeleton()
            }
            feedRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                mPostsRecyclerViewAdapter = FeedPostAdapter()
                adapter = mPostsRecyclerViewAdapter
                mPostsRecyclerViewSkeleton = applyCustomSkeleton({layoutInflater, parent ->
                    FeedPostLoadingBinding.inflate(layoutInflater, parent, false)
                }, { holder -> holder.binding.apply {
                    skeleton1.showSkeleton()
                    skeleton2.showSkeleton()
                    skeleton3.showSkeleton()
                } })
                mPostsRecyclerViewSkeleton?.showSkeleton()
            }
            appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                val offset = 1 - min(abs((verticalOffset.toFloat() / (appBarLayout.totalScrollRange))), 1F)
                collapsingContent.alpha = offset
                toolbarAvatar.x = -((offset) * toolbarAvatar.width)
                toolbarTitle.x = -((offset) * toolbarAvatar.width) + toolbarAvatar.width + requireContext().getDp(10)
            }
        }
    }

    private inner class RecentMarksObserver : ResourceObserver<RecentMarksResp>() {
        override fun onReady(v: RecentMarksResp) {
            if (mBinding.marksRecyclerView.adapter == null) return
            mMarksRecyclerViewAdapter.replaceData(*v.extendedMarks.toTypedArray())
            mMarksRecyclerViewSkeleton?.showOriginal()
        }
    }

    private inner class FeedPostsObserver : ResourceObserver<UserFeedResponse>() {
        override fun onReady(v: UserFeedResponse) {
            if (mBinding.marksRecyclerView.adapter == null) return
            try {
                mPostsRecyclerViewAdapter.replaceData(*v.news.orEmpty().toTypedArray())
                mPostsRecyclerViewSkeleton?.showOriginal()
            } catch (e: Exception) {
                context?.let {  showErrDialog(it, e.message)  }
            }
            return
        }
    }

    override fun onResume() {
        super.onResume()
        if (isNotAdded) return
    }

    private fun getDiaryObserver(activity: ComponentActivity) : ResourceObserver<DiaryApi> = DiaryApiResourceListener(activity) { api, user ->
        if (isNotAdded) return@DiaryApiResourceListener

        mBinding.toolbarTitle.text = api.user.fullName ?: ""
        if (user.photo != null) {
            mBinding.toolbarAvatar.loadImage(user.photo, onComplete = {
                mBinding.toolbarAvatar.apply {
                    avatarInitials = null
                    avatarInitialsBackgroundColor =
                        requireContext().getColor(R.color.transparent)

                }
            })
        } else {
            mBinding.toolbarAvatar.avatarInitials = requireNotNull(api.user.initials)
        }
        mViewModel.apply {
            val eduGroup = user.eduGroup
            val person = user.personId
            launchToLiveData(recentMarks) {
                return@launchToLiveData api.getRecentMarks(person, eduGroup)
            }
            launchToLiveData(feed) {
                return@launchToLiveData api.getUserFeed(person, eduGroup)
            }
            feed.observeValue(requireActivity(), FeedPostsObserver())
            recentMarks.observeValue(requireActivity(), RecentMarksObserver())
        }


    }
}