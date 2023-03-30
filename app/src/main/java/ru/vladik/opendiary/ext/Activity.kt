package ru.vladik.opendiary.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.TypedValue

/**
 * ###Метод для более удобного запуска одной [Activity] из другой
 *
 * @param activity [Activity], которую требуется запустить
 * @param finish [Boolean] флаг, отвечающий за то, стоит ли заканчивать текущую [Activity].
 */
fun Activity.startActivity(activity: Class<out Activity>, finish: Boolean) {
    Intent(this, activity).apply {
        startActivity(this)
    }
    if (finish) this.finish()
}

/**
 * ### Метод, для более быстрого преобразования dp в px
 *
 * @param dp
 */
fun Context.getDp(dp: Int) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), this.resources.displayMetrics)
