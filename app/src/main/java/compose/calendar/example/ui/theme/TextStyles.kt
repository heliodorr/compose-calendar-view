package compose.calendar.example.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp

object TextStyles {

    val BebasNeue18RegularBlack = TextStyle(
        fontFamily = Fonts.BebasNeue,
        fontSize = 18.sp,
        color = Palette.Black,
    )

    val BebasNeue64RegularBlack = TextStyle(
        fontSize = 64.sp,
        fontFamily = Fonts.BebasNeue,
        color = Palette.Black
    )

    val BebasNeue22RegularBlack = BebasNeue64RegularBlack.copy(fontSize = 22.sp)

    val BebasNeue36RegularBlack = BebasNeue64RegularBlack.copy(fontSize = 36.sp)


    private val Cirece12Regular = TextStyle(
        fontFamily = Fonts.Circe,
        fontSize = 12.sp
    )


    val Circe10RegularBlack = Cirece12Regular.copy(color = Palette.Black, fontSize = 10.sp)

    val Circe12RegularGray = Cirece12Regular.copy(color = Palette.Gray)

    val Circe12RegularBlack = Cirece12Regular.copy(color = Palette.Black)

    val CirceSmallRed = Cirece12Regular.copy(color = Palette.Red)

    val Circe12RegularRedUnderlined = CirceSmallRed.copy(textDecoration = TextDecoration.Underline)

    val Circe14RegularBlack = Circe12RegularBlack.copy(fontSize = 14.sp)

    val Circe14RegularGray = Circe14RegularBlack.copy(color = Palette.Gray)

    val Circe16RegularBlack = Circe12RegularBlack.copy(fontSize = 16.sp)

    val InterRegular = TextStyle(fontFamily = Fonts.Inter, fontWeight = FontWeight.Normal)

    val Inter12RegularGray = InterRegular.copy(color = Palette.Gray, fontSize = 12.sp)

    val Inter12RegularBlack = Inter12RegularGray.copy(color = Palette.Black)

    val Inter14RegularBlack = Inter12RegularBlack.copy(fontSize = 14.sp)

    val Inter12RegularWhite = Inter12RegularGray.copy(color = Palette.White)

    val Inter24RegularBlack = Inter12RegularBlack.copy(fontSize = 24.sp)

    val Inter18RegularBlack = Inter12RegularBlack.copy(fontSize = 18.sp)


    val ClickerScript = TextStyle(fontFamily = Fonts.ClickerScript)

    val ClickerScript14RegularGray = ClickerScript.copy(color = Palette.Gray, fontSize = 14.sp)

}