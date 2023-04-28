package compose.calendar.example.ui

import android.graphics.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.NativePaint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import java.lang.Float.max
import kotlin.math.min

internal inline fun onNativeCanvas(crossinline onDraw: Canvas.()->Unit): DrawScope.()->Unit {
    return { drawContext.canvas.nativeCanvas.onDraw() }
}

inline fun DrawScope.onNativeCanvas(onDraw: (Canvas)->Unit){
    onDraw(drawContext.canvas.nativeCanvas)
}

val DrawScope.nativeCanvas get() = this.drawContext.canvas.nativeCanvas

internal fun centerByStart(contentDimen: Float, containerDimen: Float): Float {
    when(true) {
        (contentDimen < 0) -> throw IllegalArgumentException("content size is below zero")
        (containerDimen < 0) -> throw IllegalArgumentException("container size is below zero")
        else -> {}
    }
    return (containerDimen - contentDimen)/2f
}

internal fun centerByStart(contentDimen: Int, containerDimen: Int): Int {
    when(true) {
        (contentDimen < 0) -> throw IllegalArgumentException("content size is below zero")
        (containerDimen < 0) -> throw IllegalArgumentException("container size is below zero")
        else -> {}
    }
    return (containerDimen - contentDimen)/2
}

private val ReusableRect = android.graphics.Rect()

internal fun NativePaint.textHeight(text:String = "A", isUppercase: Boolean = true): Int
= synchronized(ReusableRect) {
    this.getTextBounds(text, 0, text.length, ReusableRect)
    return ReusableRect.height()
}

internal fun NativePaint.textWidth(text:String, isUppercase: Boolean = true): Int
= synchronized(ReusableRect) {
    this.getTextBounds(text, 0, text.length, ReusableRect)
    return ReusableRect.width()
}



private val _minusInfinityOffset = Offset(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY)
internal val Offset.Companion.MinusInfinity: Offset
    get() = _minusInfinityOffset


fun tightMeasurePath(path: Path, accuracy: Int): androidx.compose.ui.geometry.Rect {
    //Idea is to find absolute minimum and maximum coordinates of points
    //which represents bounds of path. In order to do so we use PathMeasure class
    //Accuracy = number of parts we divide our curve on

    val pathMeasure = android.graphics.PathMeasure(path.asAndroidPath(), false)
    //obviously represents a point
    val cache = FloatArray(2)

    //calculates point coordinates with a given length of the curve
    pathMeasure.getPosTan(0f, cache, null)

    var xMin: Float = cache[0]
    var xMax: Float = xMin

    var yMin: Float = cache[1]
    var yMax: Float = yMin

    do {
        //length of current curve
        val length = pathMeasure.length

        for (i in 0..accuracy) {
            val pos = length * i / accuracy
            pathMeasure.getPosTan(pos, cache, null)
            xMin = min(xMin, cache[0])
            xMax = max(xMax, cache[0])

            yMin = min(yMin, cache[1])
            yMax = max(yMax, cache[1])
        }

    } while (pathMeasure.nextContour()) //Path may contain many curves, so we calculate min and max
    //for all of them

    return Rect(Offset(xMin, yMin), Offset(xMax, yMax))
}


