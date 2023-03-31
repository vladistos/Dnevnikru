package ru.vladik.opendiary.example.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.getstream.avatarview.coil.loadImage
import ru.vladik.opendiary.R
import ru.vladik.opendiary.adapters.recyclerview.FeedPostAdapter
import ru.vladik.opendiary.adapters.recyclerview.MarksAdapter
import ru.vladik.opendiary.databinding.FragmentMainBinding
import ru.vladik.opendiary.example.datasets.ExampleDatasets
import ru.vladik.opendiary.ext.getDp
import ru.vladik.opendiary.ext.loadImageWithInitialsWrapper
import ru.vladik.opendiary.viewmodels.DiaryGetViewModel.DiaryApiSingleton.api
import kotlin.math.abs
import kotlin.math.min

class MainFragment : Fragment(R.layout.fragment_main) {

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = FragmentMainBinding.bind(view).run {
        marksRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        marksRecyclerView.adapter = MarksAdapter(ExampleDatasets.ExampleMarksData)

        feedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        feedRecyclerView.adapter = FeedPostAdapter(ExampleDatasets.ExampleFeedPostsData)

        appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val offset = 1 - min(abs((verticalOffset.toFloat() / (appBarLayout.totalScrollRange))), 1F)
            collapsingContent.alpha = offset
            toolbarAvatar.x = -((offset) * toolbarAvatar.width)
            toolbarTitle.x = -((offset) * toolbarAvatar.width) + toolbarAvatar.width + requireContext().getDp(10)
        }

        val user = ExampleDatasets.ExampleApiUser
        toolbarTitle.text = "${user.firstName} ${user.lastName}"
        toolbarAvatar.loadImageWithInitialsWrapper(user.photo, initials = "КМ")

    }

}