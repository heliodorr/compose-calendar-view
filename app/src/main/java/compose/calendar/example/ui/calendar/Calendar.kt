package compose.calendar.example.ui.calendar

import android.graphics.Rect
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.core.content.res.ResourcesCompat
import compose.calendar.example.R
import compose.calendar.example.ui.PreviewContext
import compose.calendar.example.ui.onNativeCanvas
import compose.calendar.example.ui.theme.Palette
import compose.calendar.example.ui.theme.Shapes
import kotlin.math.min


private object GeneralCalendarConstants{
    const val PADDING_TOP = 15
    const val PADDING_BOTTOM = 25

    const val ROWS = 6
    const val COLUMNS = 7

    const val DAY_TEXT_SIZE = 14f
    const val DAYS_V_PADDING = 0f
    const val DAYS_H_PADDING = 40f
    const val DAY_WIDTH = 30f
    const val DAY_HEIGHT = 30f
    const val FINAL_WIDTH = (2 * DAYS_H_PADDING) + (COLUMNS * DAY_WIDTH)
    const val FINAL_HEIGHT = (2 * DAYS_V_PADDING) + (ROWS * DAY_HEIGHT)
    const val CIRCLE_DIVIDER = 2.5f
}




@Preview
@Composable
fun CalendarPreview() = PreviewContext {
    Calendar()
}

@Composable
fun Calendar() = GeneralCalendarConstants.run {
    val calendar = remember { CalendarData() }
    var motionVector by remember { mutableStateOf(Vector.Zero) }
    var left by remember { mutableStateOf(calendar.current) }
    var right by remember { mutableStateOf(calendar.right) }
    val topText = remember { mutableStateOf(left.fullMonthName) }
    val bottomText = remember { mutableStateOf(right.fullMonthName) }

    val animationSpec = remember { tween<Float>(250) }
    val fractionAnimatable = remember { Animatable(0f) }


    LaunchedEffect(motionVector) {
        calendar.run {
            move(motionVector)
        }
        when(motionVector) {
            Vector.Left -> fractionAnimatable.apply {
                left = calendar.current; right = calendar.right;
                topText.value = left.fullMonthName; bottomText.value = right.fullMonthName
                snapTo(1f)
                animateTo(targetValue = 0f, animationSpec = animationSpec)
                motionVector = Vector.Zero
            }
            Vector.Right -> fractionAnimatable.apply {
                left = calendar.left; right = calendar.current
                topText.value = left.fullMonthName; bottomText.value = right.fullMonthName
                snapTo(0f)
                animateTo(targetValue = 1f, animationSpec = animationSpec)
                motionVector = Vector.Zero
            }
            Vector.Zero -> {}
        }

    }

    Box (
        modifier = Modifier
            .background(color = Palette.White, Shapes.RegularRoundedRect)
            .drawWithContent {
                clipRect {
                    this@drawWithContent.drawContent()
                }
            }
            .wrapContentSize()
    ) {

        Column(
            modifier = Modifier
                .padding(top = PADDING_TOP.dp, bottom = PADDING_BOTTOM.dp)
        ) {

            Header(
                onClick = { if (motionVector== Vector.Zero) motionVector = it },
                vector = motionVector,
                topMonthName = topText,
                bottomMonthName = bottomText,
                animationSpec = animationSpec
            )

            DaysOfWeekNames()

            DrawDays(
                fractionState = fractionAnimatable.asState(),
                left = left,
                right = right
            ) {
                calendar.onClick(it); left = left; right = right
            }
        }
    }

}

@Composable
private fun DrawDays(
    fractionState: State<Float>,
    left: MonthData,
    right: MonthData,
    onClick: (Int)->Unit
) = GeneralCalendarConstants.run {
    val context = LocalContext.current
    val density = LocalDensity.current
    val fraction by fractionState

    val chosenTextPaint = remember {
        Paint().asFrameworkPaint().apply {
            isAntiAlias = true; color = Palette.White.toArgb()
            textAlign = android.graphics.Paint.Align.CENTER
            typeface = ResourcesCompat.getFont(context, R.font.inter_bold)
            textSize = with(density) { DAY_TEXT_SIZE.sp.toPx() }
        }
    }

    val currMonthTextPaint = remember {
        Paint().asFrameworkPaint().apply {
            isAntiAlias = true; color = Palette.Black.toArgb()
            textAlign = android.graphics.Paint.Align.CENTER
            typeface = ResourcesCompat.getFont(context, R.font.inter_regular)
            textSize = with(density) { DAY_TEXT_SIZE.sp.toPx() }
        }
    }

    val anotherMonthPaint = remember {
        Paint().asFrameworkPaint().apply {
            isAntiAlias = true; color = Palette.Gray.toArgb()
            textAlign = android.graphics.Paint.Align.CENTER
            typeface = ResourcesCompat.getFont(context, R.font.inter_regular)
            textSize = with(density) { DAY_TEXT_SIZE.sp.toPx() }
        }
    }

    val circlePaint = remember {
        Paint().asFrameworkPaint()
            .apply { isAntiAlias = true; color = Palette.Red.toArgb() }
    }

    val hPadding = remember { density.run { DAYS_H_PADDING.dp.toPx() } }
    val vPadding = remember { density.run { DAYS_V_PADDING.dp.toPx() } }
    val dayWidth = remember { density.run { DAY_WIDTH.dp.toPx() } }
    val dayHeight = remember { density.run { DAY_HEIGHT.dp.toPx() } }


    val textOffset: Float = remember {
        Rect().run {
            chosenTextPaint.getTextBounds("0", 0, 1, this)
            return@remember (dayHeight - this.height())/2f + this.height()
        }
    }

    val circleSize = remember { min(dayHeight, dayWidth)/ CIRCLE_DIVIDER }

    fun DrawScope.drawDays(days: List<MonthData.Day>) {

        var vStart = vPadding
        val hStart = hPadding + dayWidth/2f

        val circleVertInc = dayHeight/2f
        var idx = 0

        onNativeCanvas { nativeCanvas->
            repeat(ROWS) { row ->
                repeat(COLUMNS) { col ->
                    idx = row * COLUMNS + col
                    if (idx >= days.size) return@onNativeCanvas

                    val day = days[idx]
                    val paint = if (day.isChosen) {
                        chosenTextPaint
                    } else if (day.fromCurrentMonth && !day.isChosen) {
                        currMonthTextPaint
                    } else {
                        anotherMonthPaint
                    }
                    val currentXCenter = hStart + dayWidth * col
                    if (day.isChosen) {
                        nativeCanvas
                            .drawCircle(
                                currentXCenter,
                                vStart + circleVertInc,
                                circleSize,
                                circlePaint
                            )
                    }

                    nativeCanvas
                        .drawText(day.number, currentXCenter, vStart + textOffset, paint)
                }
                vStart += dayHeight
            }
        }
    }

    //if return is < 0 then do nothing
    fun detectIndexFromClick(x: Float, y: Float): Int {
        val gridX: Int = ((x-hPadding) / dayWidth).toInt()
        val gridY: Int = ((y-vPadding) / dayHeight).toInt()
        return if (gridX >= 0 && gridY >= 0) { gridY * COLUMNS + gridX } else -1
    }

    Canvas(
        modifier = Modifier
            .requiredWidth(FINAL_WIDTH.dp)
            .requiredHeight(FINAL_HEIGHT.dp)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val index = detectIndexFromClick(offset.x, offset.y)
                    val animationFraction by fractionState
                    if (index >= 0 && (animationFraction == 0f || animationFraction == 1f)) {
                        onClick(index)
                    }
                }
            }
    ) {
        translate(left = -this.size.width * fraction) { drawDays(left.days) }
        translate(left = this.size.width  * (1-fraction)) { drawDays(right.days) }
    }

}

internal object HeaderConstants {
    const val HEIGHT = 40
}

@Composable
private fun DaysOfWeekNames() = GeneralCalendarConstants.run { HeaderConstants.run {

    val weekDaysStringArray = stringArrayResource(R.array.week_days)

        Row(
            modifier = Modifier
                .width(FINAL_WIDTH.dp)
                .padding(horizontal = DAYS_H_PADDING.dp)
                .requiredHeight(HEIGHT.dp)
        ) {
            repeat(COLUMNS) { col->
                Box(
                    modifier = Modifier
                        .width(DAY_WIDTH.dp)
                        .height(DAY_HEIGHT.dp)
                ) {
                    Text(
                        text = weekDaysStringArray[col].uppercase(),
                        color = Palette.Black,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .align(Alignment.Center),

                        )
                }
            }
        }
    }
}


@Composable
private fun Header(
    onClick: (Int) -> Unit,
    topMonthName: State<String>,
    bottomMonthName: State<String>,
    vector :   Int ,
    animationSpec: TweenSpec<Float>,
) = GeneralCalendarConstants.run { HeaderConstants.run {
    val density = LocalDensity.current
    val scrollState = rememberScrollState()
    val maxPos = remember { with(density) { HEIGHT.dp.roundToPx() } }



    LaunchedEffect(vector) {
        when(vector) {
            Vector.Left -> {
                scrollState.run {
                    scrollTo(maxPos)
                    animateScrollTo(0, animationSpec)
                }
            }
            Vector.Right -> {
                scrollState.run {
                    scrollTo(0)
                    animateScrollTo(maxPos, animationSpec)
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .width(FINAL_WIDTH.dp)
            .padding(horizontal = DAYS_H_PADDING.dp)
            .requiredHeight(HEIGHT.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_calendar_arrow),
            contentDescription = null,
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterStart)
                .clickable { onClick(Vector.Left) }
        )
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .requiredWidth(100.dp)
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            0f to Color.White,
                            .45f to Color.Transparent,
                            .55f to Color.Transparent,
                            1f to Color.White
                        )
                    )
                }
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(state = scrollState, enabled = false)
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .align(Alignment.Center)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(HEIGHT.dp)
                ) {
                    Text(
                        text = topMonthName.value,
                        color = Palette.Black,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.bebas_neue_regular)),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(HEIGHT.dp)
                ) {
                    Text(
                        text = bottomMonthName.value,
                        color = Palette.Black,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.bebas_neue_regular)),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }
        }
        Image(
            painter = painterResource(R.drawable.ic_calendar_arrow),
            contentDescription = null,
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterEnd)
                .rotate(180f)
                .clickable { onClick(Vector.Right) }
        )
    }
    }
}
