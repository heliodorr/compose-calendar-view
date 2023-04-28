package compose.calendar.example.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier

object Modifiers {

    val BackgroundWhiteBottomRoundedRect = Modifier
        .background(color = Palette.White, shape = Shapes.BottomRoundedRect)

    val BackgroundWhiteRoundedRect = Modifier
        .background(color = Palette.White, shape = Shapes.RegularRoundedRect)


    val BackgroundWhiteBorderYellow = BackgroundWhiteRoundedRect
        .border(
            width = Constants.RegularBorderWidth,
            color = Palette.Yellow,
            shape = Shapes.RegularRoundedRect
        )


    val MainPaddings = Modifier.padding(Constants.MainPaddings)
    val HalfPaddings = Modifier.padding(Constants.HalfPaddings)
    val HorizontalMainPaddings = Modifier.padding(horizontal = Constants.MainPaddings)
    val HorizontalHalfPaddings = Modifier.padding(horizontal = Constants.HalfPaddings)
    val VerticalMainPaddings = Modifier.padding(vertical = Constants.MainPaddings)
}