package compose.calendar.example.ui.theme


import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

object Shapes {

    val UnderlineShape = object : Shape {
        override fun createOutline(
            size: Size,
            layoutDirection: LayoutDirection,
            density: Density
        ): Outline {
            density.run {
                return Outline.Rectangle(
                    Rect(
                        top = size.height - 1.dp.toPx(),
                        left = 0f,
                        right = size.width,
                        bottom = size.height
                    )
                )
            }
        }
    }

    val RegularRoundedRect = RoundedCornerShape(Constants.RegularCornerSize)

    val BottomRoundedRect = RoundedCornerShape(
        bottomStart = Constants.RegularCornerSize,
        bottomEnd = Constants.RegularCornerSize
    )

    val TopRoundedRect = RoundedCornerShape(
        topStart = Constants.RegularCornerSize,
        topEnd = Constants.RegularCornerSize
    )

}