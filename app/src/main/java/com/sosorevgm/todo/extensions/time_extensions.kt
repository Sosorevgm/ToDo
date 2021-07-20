package com.sosorevgm.todo.extensions

import android.text.format.DateFormat
import java.util.*

fun getTimestamp(year: Int, month: Int, day: Int): Long {
    val calendar = GregorianCalendar()
    calendar.set(year, month, day)
    return calendar.timeInMillis / 1000
}

fun getCurrentTimestamp(): Long = System.currentTimeMillis() / 1000

fun getTaskDate(time: Long): String {
    return DateFormat.format("dd MMMM yyyy", time * 1000).toString()
}

fun isSameDay(timeOne: Long, timeTwo: Long): Boolean {
    val calendarOne = GregorianCalendar()
    val calendarTwo = GregorianCalendar()
    calendarOne.timeInMillis = timeOne * 1000
    calendarTwo.timeInMillis = timeTwo * 1000
    return calendarOne.get(Calendar.DAY_OF_YEAR) == calendarTwo.get(Calendar.DAY_OF_YEAR) && calendarOne.get(
        Calendar.YEAR
    ) == calendarTwo.get(Calendar.YEAR)
}