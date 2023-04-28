package compose.calendar.example.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import compose.calendar.example.R


object Fonts {
    val BebasNeue = FontFamily(Font(R.font.bebas_neue_regular))
    val Circe = FontFamily(Font(R.font.circe_regular))
    val Inter = FontFamily(
        Font(R.font.inter_bold, weight = FontWeight.Bold),
        Font(R.font.inter_regular, weight = FontWeight.Normal),
        Font(R.font.inter_medium, weight = FontWeight.Medium)
    )
    val ClickerScript = FontFamily(Font(R.font.clicker_script_regular))
}

