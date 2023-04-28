package compose.calendar.example.ui.theme

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.ContentDrawScope

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)


private val NoIndicationInstance = object : IndicationInstance {
    override fun ContentDrawScope.drawIndication() {
        drawContent()
    }

}

private val NoIndication: Indication = object : Indication {
    @Composable
    override fun rememberUpdatedInstance(interactionSource: InteractionSource): IndicationInstance {
        return NoIndicationInstance
    }

}

val DisableIndication = LocalIndication provides NoIndication

@Composable
fun PixelToolsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    CompositionLocalProvider(
        DisableIndication
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Palette.Background)
        ) {
            content()
        }
    }

    /*MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )*/
}