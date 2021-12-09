package com.pushe.worker.operations.model

import com.pushe.worker.totals.Period
import com.pushe.worker.totals.PeriodSize
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import java.util.*

internal class PeriodTest {

    private val calendar = Calendar.getInstance()

    init {
        calendar.set(2021, 11, 15)
    }

    private val period = Period(PeriodSize.MONTH, calendar.time )

    @Test
    fun changeSize() {
        assertArrayEquals(period.firstDay.toCharArray(), "2021-12-01".toCharArray(), "Первый день месяца")
        assertArrayEquals(period.lastDay.toCharArray(), "2021-12-31".toCharArray(), "Последний день месяца")
        assertArrayEquals(period.toString.toCharArray(), "Декабрь 2021 г.".toCharArray(), "Месяц текстом")
        period.changeSize(PeriodSize.WEEK, calendar.time)
        assertArrayEquals(period.firstDay.toCharArray(), "2021-12-13".toCharArray(), "Первый день недели")
        assertArrayEquals(period.lastDay.toCharArray(), "2021-12-19".toCharArray(), "Последний день недели")
        assertArrayEquals(period.toString.toCharArray(), "13.12.2021 - 19.12.2021".toCharArray(), "Неделя текстом")
        period.changeSize(PeriodSize.YEAR, calendar.time)
        assertArrayEquals(period.firstDay.toCharArray(), "2021-01-01".toCharArray(), "Первый день года")
        assertArrayEquals(period.lastDay.toCharArray(), "2021-12-31".toCharArray(), "Последний день года")
        assertArrayEquals(period.toString.toCharArray(), "2021 г.".toCharArray(), "Год текстом")
    }

    @Test
    fun nextPeriod() {
        period.changeSize(PeriodSize.WEEK, calendar.time)
        period.nextPeriod(2)
        period.nextPeriod(-1)
        assertArrayEquals(period.firstDay.toCharArray(), "2021-12-20".toCharArray(), "Первый день следующей недели")
        assertArrayEquals(period.lastDay.toCharArray(), "2021-12-26".toCharArray(), "Последний день следующей недели")
        period.changeSize(PeriodSize.MONTH, calendar.time)
        period.nextPeriod(3)
        period.nextPeriod(-1)
        assertArrayEquals(period.firstDay.toCharArray(), "2022-02-01".toCharArray(), "Первый день февраля 2022")
        assertArrayEquals(period.lastDay.toCharArray(), "2022-02-28".toCharArray(), "Последний день февраля 2022")
        period.changeSize(PeriodSize.YEAR, calendar.time)
        period.nextPeriod(2)
        period.nextPeriod(-1)
        assertArrayEquals(period.firstDay.toCharArray(), "2022-01-01".toCharArray(), "Первый день 2022 года")
        assertArrayEquals(period.lastDay.toCharArray(), "2022-12-31".toCharArray(), "Последний день 2022 года")
    }
}