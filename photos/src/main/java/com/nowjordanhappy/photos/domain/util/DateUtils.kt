package com.nowjordanhappy.photos.domain.util

import java.lang.NullPointerException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private const val DATE_FORMAT_PHOTO = "MMM dd yyyy"

    fun longToDate(long: Long): Date {
        return Date(long)
    }

    fun dateToLong(date: Date): Long {
        return date.time / 1000 // return seconds
    }

    // Ex: November 4, 2021
    fun dateToString(date: Date): String{
        val sdf = SimpleDateFormat(DATE_FORMAT_PHOTO, Locale.getDefault())
        return sdf.format(date)
    }

    fun stringToDate(string: String): Date {
        val sdf = SimpleDateFormat(DATE_FORMAT_PHOTO, Locale.getDefault())
        return sdf.parse(string) ?:throw NullPointerException("Could not convert date string to Date object.")
    }

    fun createTimestamp(): Date{
        return Date()
    }
}