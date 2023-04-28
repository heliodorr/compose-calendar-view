package compose.calendar.example.ui.calendar

import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.collections.ArrayList
import kotlin.math.max
import kotlin.math.min





internal class CalendarData {

    companion object {
        private fun firstDayFromSundayToMonday(day: Int): Int {
            val transformedDay = day - 1
            return if (transformedDay == 0) 7 else transformedDay
        }
        private const val NO_VALUE: Int = -1
        private const val DATA_RANGE = 11
    }

    private val mDeq = ArrayDeque<MonthData>(DATA_RANGE * 2 + 1)
    private val mCalendar = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, 1)
        firstDayOfWeek = Calendar.MONDAY
    }

    init {
        for (v in -DATA_RANGE..DATA_RANGE) {
            mDeq.add(generateMonthData(offsetFromCurrentMonth = v))
        }
    }

    private var mCurrentMonth = DATA_RANGE

    private fun getMonthData(vector: Int) = mDeq.run {
        return@run this[mCurrentMonth + vector]
    }

    val left
        get() = getMonthData(Vector.Left)
    val current
        get() = getMonthData(Vector.Zero)
    val right
        get() = getMonthData(Vector.Right)

    private var mFirstTap: Int = NO_VALUE
    private var mSecondTap: Int = NO_VALUE
    private var mMonthContainedRange = mCurrentMonth

    private val mMonthWithRange get() = mDeq[mMonthContainedRange]

    val canGoLeft get() = mCurrentMonth >= 1
    val canGoRight get() = mCurrentMonth <= mDeq.lastIndex - 1

    val currentRangeMonth get() = mDeq[mMonthContainedRange].month
    val currentRangeYear get() = mDeq[mMonthContainedRange].year

    val currentRange: IntRange get() {
        if (mFirstTap == NO_VALUE) return IntRange.EMPTY
        val start: Int
        val end: Int
        if (mSecondTap == NO_VALUE) {
            start = mFirstTap + 1
            end = start
        } else {
            start = min(mFirstTap, mSecondTap)
            end = max(mFirstTap, mSecondTap)
        }
        return start..end
    }

    fun onClick(index: Int) {
        if (index !in current.days.indices) return
        if (!current.days[index].fromCurrentMonth) return

        if (mCurrentMonth != mMonthContainedRange) {
            mMonthWithRange.cleanRange()
            mFirstTap = NO_VALUE; mSecondTap = NO_VALUE
            mMonthContainedRange = mCurrentMonth
        }

        if (mFirstTap == NO_VALUE) {
            current.fillRange(index)
            mFirstTap = index
        }
        else if (mSecondTap != NO_VALUE) {
            current.fillRange(index)
            mFirstTap = index; mSecondTap = NO_VALUE
        }
        else if (mSecondTap == NO_VALUE && index!=mFirstTap) {
            mSecondTap = index
            val rangeStart = min(mFirstTap, mSecondTap)
            val rangeEnd = max(mFirstTap, mSecondTap)
            current.fillRange(rangeStart..rangeEnd)
        }

    }

    fun move(vector: Int) {
        if (vector == Vector.Left && !canGoLeft) return
        if (vector == Vector.Right && !canGoRight) return
        mCurrentMonth += vector
    }

    private fun generateMonthData(offsetFromCurrentMonth: Int): MonthData {
        mCalendar.add(Calendar.MONTH, offsetFromCurrentMonth)

        val days = ArrayList<MonthData.Day>(7 * 6)
        val data = MonthData(calendar = mCalendar, days = days)
        val daysInCurrentMonth = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        val firstDayOfMonth = firstDayFromSundayToMonday(mCalendar.get(Calendar.DAY_OF_WEEK))
        mCalendar.add(Calendar.MONTH, Vector.Left)

        if (firstDayOfMonth!=1) {
            val startInPreviousMonth =
                mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) - (firstDayOfMonth - 2)

            for (i in 0 until firstDayOfMonth - 1) {
                val day = MonthData.Day(startInPreviousMonth + i, false)
                days.add(day)
            }
        }

        for (i in 1 .. daysInCurrentMonth) {
            val day = MonthData.Day(i, true)
            days.add(day)
        }
        var nextMonthDay = 1;
        while (days.size % 7 != 0) {
            val day = MonthData.Day(nextMonthDay++, false)
            days.add(day)
        }

        mCalendar.add(Calendar.MONTH, -offsetFromCurrentMonth)
        mCalendar.add(Calendar.MONTH, Vector.Right)
        return data
    }


}