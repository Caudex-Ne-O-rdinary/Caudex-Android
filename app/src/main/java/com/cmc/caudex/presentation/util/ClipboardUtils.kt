package com.cmc.caudex.presentation.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast

fun Context.copyTextToClipboard(
    label: String,
    text: String,
    toastMessage: String? = null,
) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
    toastMessage?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
}
