package compose.calendar.example.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@JvmInline
value class WidthAttribute internal constructor(val modifier: Modifier) {

    companion object {
        val FillMax = WidthAttribute(Modifier.fillMaxWidth())
        val WrapContent = WidthAttribute(Modifier.fillMaxWidth())
        fun exact(width: Dp) = WidthAttribute(Modifier.width(width = width))
    }
}

@JvmInline
value class HeightAttribute internal constructor(val modifier: Modifier) {
    companion object {
        val FillMax = WidthAttribute(Modifier.fillMaxWidth())
        val WrapContent = WidthAttribute(Modifier.fillMaxWidth())
        fun exact(height: Dp) = WidthAttribute(Modifier.height(height = height))
    }
}

