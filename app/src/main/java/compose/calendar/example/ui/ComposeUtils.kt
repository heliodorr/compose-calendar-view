package compose.calendar.example.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun <T> MutableState<T>.refresh() {
    this.value = this.value
}

@Composable
inline fun <T> rememberAsState(initialState: T): MutableState<T> = remember { mutableStateOf(initialState) }

inline fun Dp(density: Float, pixels: Float): Dp {
    return (pixels/density).dp
}

inline fun Dp(density: Float, pixels: Int): Dp {
    return (pixels/density).dp
}