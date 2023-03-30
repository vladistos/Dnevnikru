package ru.vladik.opendiary.func

import android.content.Context
import androidx.appcompat.app.AlertDialog
import ru.vladik.opendiary.R
import ru.vladik.opendiary.ext.firstOrEmpty

fun getInitials(firstName: String?, lastName: String?) =
    "${firstName.firstOrEmpty()}. ${lastName.firstOrEmpty()}."



fun showErrDialog(context: Context, error: String?) = AlertDialog.Builder(context)
    .setTitle(R.string.error)
    .setMessage("${context.getString(R.string.error_dialog_message)} ${error}")
    .setPositiveButton(R.string.ok, { _, _ -> })
    .create()
    .show()