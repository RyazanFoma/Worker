package com.pushe.worker.totals

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pushe.worker.data.TotalsDataSource
import com.pushe.worker.data.model.Total
import com.pushe.worker.utils.Bar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TotalsViewModel(private val totalsDataSource: TotalsDataSource) : ViewModel() {

    /**
     * Date format for parsing from ERP
     */
    private companion object {
        const val DATE_FORMAT = "yyyy-MM-dd"
    }

    /**
     * Data analysis by period of TIME or TYPE of work
     */
    enum class Analytics {TIME, TYPE}
    private var analytics: Analytics? = null

    /**
     * Data period for totals
     */
    val period = Period(PeriodSize.MONTH, Date())

    /**
     * Data by TYPE (вид) of work or DAYs (день) of the month, DAYs of the week, or MONTHs (месяц) of the year
     */
    enum class AnalyticsData(val value: String) {TYPE("type"), DAY("day"), MONTH("month")}

    /**
     * Model view status
     */
    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }
    var status by mutableStateOf(Status.LOADING)
        private set

    /**
     * headline stating the period of dates and the total amount
     */
    var title by mutableStateOf("")
        private set

    /**
     * Message of load error
     */
    var error : String = ""
        private set

    /**
     * Data of bar chart
     */
    var bars : List<Bar> by mutableStateOf(listOf())
        private set

    /**
     * Loading summary data into a bar chart
     */
    fun loadTotals() {
        if (analytics == null) throw ExceptionInInitializerError("Analytics type not initialized")
        this.viewModelScope.launch {
            title = period.toString
            status = Status.LOADING
            try {
                val response = totalsDataSource.load(
                    startDay = period.firstDay(),
                    endDay = period.lastDay(),
                    analytics = analytics!!.convert(periodSize = period.periodSize)
                )
//                delay(2000)
                if (response.isSuccessful) {
                    bars = response.body()!!.parse(analytics!!, period.periodSize)
                    title = period.toString + " - " + bars.total()
                    status = Status.SUCCESS
                } else {
                    error = response.message()
                    status = Status.ERROR
                }
            } catch (e: IOException) { // IOException for network failures.
                error = e.message ?: ""
                status = Status.ERROR
            } catch (e: HttpException) { // HttpException for any non-2xx HTTP status codes.
                error = e.message ?: ""
                status = Status.ERROR
            }
        }
    }

    /**
     * Change analytics
     * @param analyticsNew - TYPE or TIME
     */
    fun setAnalytics(analyticsNew: Analytics) {
        if (analytics != analyticsNew) {
            analytics = analyticsNew
            loadTotals()
        }
    }

    /**
     * Change period size
     * @param ordinal = 0 - week, 1 - month, 2 - year
     */
    fun changePeriodSize(ordinal: Int) {
        when(ordinal) {
            in 0..2 -> period.changeSize(PeriodSize.values()[ordinal], Date())
            else -> throw IllegalArgumentException("Invalid value of period Id")
        }
        loadTotals()
    }

    /**
     * Move to the previous period from the current one
     */
    fun previousPeriod()  {
        period.nextPeriod(step = -1)
        loadTotals()
    }

    /**
     * Move to the next period from the current one
     */
    fun nextPeriod()  {
        period.nextPeriod(step = 1)
        loadTotals()
    }

    /**
     * Analysis of results with current parameters:
     * @param analytics - current analytics by TIME period or TYPE of work
     * @param periodSize - size of the current period per WEEK and MONTH by days, or per YEAR by months
     */
    private fun List<Total>.parse(analytics: Analytics, periodSize: PeriodSize) : List<Bar> {
        return when(analytics) {
            Analytics.TYPE -> this.parseType()
            else -> //Analytics.TIME
                when(periodSize) {
                    PeriodSize.WEEK -> this.parseWeek()
                    PeriodSize.YEAR -> this.parseYear()
                    else -> //PeriodSize.MONTH
                        this.parseMonth()
                }
        }
    }

    /**
     * Converting PeriodSize to AnalyticsData:
     * WEEK and MONTH -> DAY, YEAR -> MONTH
     */
    private fun PeriodSize.convert() = when(this) {
        PeriodSize.YEAR -> AnalyticsData.MONTH.value
        else -> // PeriodSize.WEEK, PeriodSize.MONTH
            AnalyticsData.DAY.value
    }

    /**
     * Converting Analytics to AnalyticsData:
     * TYPE -> by TYPE, WEEK and MONTH -> by DAY, YEAR -> by MONTH
     */
    private fun Analytics.convert(periodSize : PeriodSize) = when(this) {
        Analytics.TYPE -> AnalyticsData.TYPE.value
        else -> //Analytics.TIME
            periodSize.convert()
    }

    /**
     * Easy conversion of Total to Bar - name -> label and value -> value
     */
    private fun Total.toBar() = Bar(label = this.name ?: "", value = this.value ?: 0f)

    /**
     * Analysis of results by type of work
     */
    private fun List<Total>.parseType() : List<Bar> {
        val result = mutableListOf<Bar>()
        val sorted = this.sortedByDescending { it.value }
        if (sorted.size > 30) {
            for (i in 0..30)
                result.add(sorted[i].toBar())
            var other = 0f
            for (i in 31..sorted.size)
                other += sorted[i].value ?: 0f
            if (other > 0) result.add(Bar(label = "Прочее", other))
        }
        else {
            sorted.forEach {result.add(it.toBar())}
        }
        return result
    }

    /**
     * Analysis of results by days of week
     */
    private fun List<Total>.parseWeek() : List<Bar> {
        val result = listOf(
            Bar(label = "Пн"),
            Bar(label = "Вт"),
            Bar(label = "Ср"),
            Bar(label = "Чт"),
            Bar(label = "Пт"),
            Bar(label = "Сб"),
            Bar(label = "Вс")
        )
        this.forEach { result[it.indexOfWeekDay()].value = it.value ?: 0f }
        return result
    }

    /**
     * Analysis of results by months of year
     */
    private fun List<Total>.parseYear() : List<Bar> {
        val result = listOf(
            Bar(label = "Янв"),
            Bar(label = "Фев"),
            Bar(label = "Мар"),
            Bar(label = "Апр"),
            Bar(label = "Май"),
            Bar(label = "Июн"),
            Bar(label = "Июл"),
            Bar(label = "Авг"),
            Bar(label = "Сен"),
            Bar(label = "Окт"),
            Bar(label = "Ноя"),
            Bar(label = "Дек")
        )
        this.forEach { result[it.indexOfMonth()].value = it.value ?: 0f }
        return result
    }

    /**
     * Analysis of results by days of month
     */
    private fun List<Total>.parseMonth() : List<Bar> {
        val result = mutableListOf<Bar>()
        if (this.isNotEmpty()) {
            for (i in 1..this[0].maxActualDay())
                result.add(Bar(label = i.toString()))
            this.forEach { result[it.indexOfMonthDay()].value = it.value ?: 0f }
        }
        return result
    }

    /**
     * Determining the number of days in a month of totals
     */
    private fun Total.maxActualDay() : Int {
        this.name?.let{ s ->
            if (s.length > 9) {
                try {
                    val date = SimpleDateFormat(DATE_FORMAT, Locale.US).parse(s.substring(0, 10))
                    date?.let {
                        val calendar = Calendar.getInstance()
                        calendar.time = it
                        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                    }
                } catch (e: ParseException) {}
            }
        }
        throw IllegalArgumentException("The input argument does not contain a date string $DATE_FORMAT")
    }

    /**
     * Extract index [in 0..30] of day of month from Total.name
     */
    private fun Total.indexOfMonthDay() : Int {
        this.name?.let {
            if (it.length > 9) {
                try {
                    val day = it.substring(8, 10).toInt()
                    if (day in 1..31) return day - 1
                } catch (e: NumberFormatException) {}
            }
        }
        throw IllegalArgumentException("The input argument does not contain a date string $DATE_FORMAT")
    }

    /**
     * Extract the index [in 0..6] days of the week from Total.name.
     * Where Monday == 0 and Sunday == 6
     */
    private fun Total.indexOfWeekDay() : Int {
        this.name?.let { s ->
            if(s.length > 9) {
                try {
                    val date = SimpleDateFormat(DATE_FORMAT, Locale.US).parse(s.substring(0, 10))
                    date?.let{
                        val calendar = Calendar.getInstance()
                        calendar.time = it
                        val week = calendar.get(Calendar.DAY_OF_WEEK)
                        if (week in 1..7) return if (week > 1) week - 2 else 6
                    }
                } catch (e: ParseException) {}
            }
        }
        throw IllegalArgumentException("The input argument does not contain a date string $DATE_FORMAT")
    }

    /**
     * Extract index [in 0..11] of month of year from Total.name
     */
    private fun Total.indexOfMonth() : Int {
        this.name?.let {
            if (it.length > 6) {
                val month = it.substring(5, 7).toInt()
                if (month in 1..12) return month - 1
            }
        }
        throw IllegalArgumentException("The input argument does not contain a date string $DATE_FORMAT")
    }

    /**
     * Make a headline stating the period of dates and the total amount
     */
    private fun List<Bar>.total() : String {
        var total = 0f
        this.forEach {total += it.value}
        val rub = total.toInt()
        val cop = ((total - rub) * 100).toInt()
        return "%d".format(rub) + "," + "%02d".format(cop) +" ₽"
    }
}