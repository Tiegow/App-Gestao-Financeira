package br.ufrn.gerfin.app.utils

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSTimeZone
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.timeZoneForSecondsFromGMT

actual fun formatMillisToDateString(millis: Long): String {
    val date = NSDate.dateWithTimeIntervalSince1970(millis / 1000.0)
    val formatter = NSDateFormatter()
    formatter.dateFormat = "dd/MM/yyyy"
    formatter.timeZone = NSTimeZone.timeZoneForSecondsFromGMT(0)
    return formatter.stringFromDate(date)
}
