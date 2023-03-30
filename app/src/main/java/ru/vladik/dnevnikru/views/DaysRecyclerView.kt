package ru.vladik.dnevnikru.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.R
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs


class DaysRecyclerView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : RecyclerView(context, attrs, defStyleAttr) {

    var motionEventListener: OnTouchListener? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(e: MotionEvent): Boolean {
        val preventDefault = motionEventListener?.onTouch(this, e) ?: true
        val s = if (preventDefault)super.onTouchEvent(e) else true
        return s
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs,
        R.attr.recyclerViewStyle
    )


}