package ru.vladik.dnevnikru.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.TypedValue

fun Activity.startActivity(activity: Class<out Activity>, finish: Boolean) {
    Intent(this, activity).apply {
        startActivity(this)
    }
    if (finish) this.finish()
}

fun Context.getDp(dp: Int) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), this.resources.displayMetrics)
