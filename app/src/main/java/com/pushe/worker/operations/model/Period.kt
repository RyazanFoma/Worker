package com.pushe.worker.operations.model

import java.util.*

// Positions in the list (0 - WEEK, 1 - MONTH, 2 - YEAR) cannot be changed!!!
enum class PeriodSize(
    val value: String
) { WEEK("Неделя"), MONTH("Месяц"), YEAR("Год") }

class Period(size: PeriodSize, date: Date) {

    private val _firstDay: Calendar? = Calendar.getInstance()
    val firstDay: String
        get() = _firstDay.yyyymmdd()

    private val _lastDay: Calendar? = Calendar.getInstance()
    val lastDay: String
        get() = _lastDay.yyyymmdd()

    private fun Calendar?.yyyymmdd() : String =
        this?.let { "${get(Calendar.YEAR)}-" +
                "${"%02d".format(get(Calendar.MONTH) + 1)}-" +
                "%02d".format(get(Calendar.DAY_OF_MONTH))} ?: ""

    var toString = ""
        private set

    var periodSize: PeriodSize = size
        private set

    private val months = listOf("Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
        "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь")

    init {
        changeSize(periodSize, date)
    }

    fun changeSize(size: PeriodSize, date: Date) {
        periodSize = size
        _firstDay?.apply {
            time = date
            clear(Calendar.MINUTE)
            clear(Calendar.SECOND)
            clear(Calendar.MILLISECOND)
            when(size) {
                PeriodSize.WEEK -> set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                PeriodSize.YEAR -> {
                    set(Calendar.MONTH, Calendar.JANUARY)
                    set(Calendar.DAY_OF_MONTH, 1)
                }
                else -> set(Calendar.DAY_OF_MONTH, 1) //PeriodSize.MONTH
            }
        }
        setLastDay()
    }

    fun nextPeriod(step: Int) {
        _firstDay?.add(
            when(periodSize) {
                PeriodSize.WEEK -> Calendar.WEEK_OF_YEAR
                PeriodSize.YEAR -> Calendar.YEAR
                else -> Calendar.MONTH
            },
            step
        )
        setLastDay()
    }

    private fun Calendar.mount() : String = months[get(Calendar.MONTH)]

    private fun Calendar.year() : String = "${get(Calendar.YEAR)} г."

    private fun setLastDay() {
        _lastDay?.apply {
            time = _firstDay!!.time
            toString = when(periodSize) {
                PeriodSize.WEEK -> {
                    add(Calendar.DAY_OF_YEAR, 6)
                    "${_firstDay.ddmmyyyy()} - ${ddmmyyyy()}"
                }
                PeriodSize.YEAR -> {
                    set(Calendar.DAY_OF_MONTH, 31)
                    set(Calendar.MONTH, Calendar.DECEMBER)
                    year()
                }
                else -> { //PeriodSize.MONTH
                    set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
                    mount() + " " + year()
                }
            }
        }
    }

    private fun Calendar.ddmmyyyy() : String =
        "%02d".format(get(Calendar.DAY_OF_MONTH)) +
                ".${"%02d".format(get(Calendar.MONTH) + 1)}" +
                ".${get(Calendar.YEAR)}"

}