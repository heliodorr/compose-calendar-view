package compose.calendar.example.ui

import compose.calendar.example.R


enum class ChangeVector {
    Up, Down
}

object ChangeVectorResources {
    fun small(cv: ChangeVector): Int = when(cv) {
        ChangeVector.Up -> R.drawable.arrow_up
        ChangeVector.Down -> R.drawable.arrow_down
    }
}