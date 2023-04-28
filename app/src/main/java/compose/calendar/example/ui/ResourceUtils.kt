package compose.calendar.example.ui

import android.content.Context
import androidx.annotation.RawRes

object ResourceUtils {
    fun rawResToStringOrEmpty(context: Context, @RawRes rawID: Int): String {
        return try {
            context.resources.openRawResource(rawID).bufferedReader().use {
                return@use it.readText()
            }
        } catch (e: Exception) {
            ""
        }
    }
}