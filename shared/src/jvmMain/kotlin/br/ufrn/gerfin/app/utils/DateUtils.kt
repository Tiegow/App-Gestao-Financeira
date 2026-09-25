package br.ufrn.gerfin.app.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone
import java.util.Locale

actual fun formatMillisToDateString(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format(Date(millis))
}
