package compose.calendar.example.ui.calendar

import androidx.compose.ui.graphics.Color
import compose.calendar.example.ui.theme.Palette
import java.util.*


internal class MonthData(
    val calendar: Calendar,
    val days: List<Day>
    ) {

    class Day(number: Int,  val fromCurrentMonth: Boolean) {
        val number: String = number.toString()
        var isChosen = false
        val color: Color
            get() = if (fromCurrentMonth && isChosen) {
                Palette.White
            } else if (fromCurrentMonth && !isChosen) {
                Palette.Black
            } else {
                Palette.Gray
            }

        fun invert(): Boolean {
            isChosen = !isChosen
            return isChosen
        }
    }

    val month: Int = calendar.get(Calendar.MONTH)
    val year: Int = calendar.get(Calendar.YEAR)
    val fullMonthName: String

    init {

        val monthNum = calendar.get(Calendar.MONTH)
        val yearNum = calendar.get(Calendar.YEAR)

        val monthString = when(monthNum) {
            0 -> "Jan"
            1 -> "Feb"
            2 -> "Mar"
            3 -> "Apr"
            4 -> "May"
            5 -> "Jun"
            6 -> "Jul"
            7 -> "Aug"
            8 -> "Sep"
            9 -> "Oct"
            10 -> "Nov"
            11 -> "Dec"
            else -> IllegalArgumentException("Incorrect month index")
        }
        fullMonthName = "$monthString $yearNum"
    }

    fun cleanRange() {
         days.forEach { it.isChosen = false }
    }

    fun fillRange(singleRange: Int) {
        cleanRange()
        days[singleRange].isChosen = true
    }

    fun fillRange(range: IntRange) {
        cleanRange()
        for (i in range) {
            days[i].isChosen = true
        }
    }

    fun clone(): MonthData = MonthData(calendar, days)

    override fun equals(other: Any?): Boolean {
        return false
    }

}