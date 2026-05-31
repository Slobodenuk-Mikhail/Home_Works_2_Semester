package ru.itis.home_works_2_semester.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.*

/**
 * Custom Circular Pie Chart with Legend and Title
 * @param data List of Pair(ID, Percentage)
 * @param labels List of explanations for each sector
 */
@Composable
fun CircularPieChart(
    data: List<Pair<Int, Int>>,
    modifier: Modifier = Modifier,
    labels: List<String>? = null,
    title: String = "Simple text",
    gapDegree: Float = 4f,
    thickness: Float = 120f
) {
    val totalSum = data.sumOf { it.second }
    require(totalSum == 100) { "Sum of percentages must be 100, but was $totalSum" }

    val colors = remember(data.size) {
        generateDistinctColors(data.size)
    }

    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Title
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Chart Area
            Box(
                modifier = Modifier
                    .weight(1.5f)
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                PieChartCanvas(
                    data = data,
                    colors = colors,
                    gapDegree = gapDegree,
                    thickness = thickness,
                    selectedIndex = selectedIndex,
                    onSectorClick = { selectedIndex = it }
                )
            }

            // Legend Area
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                data.forEachIndexed { index, pair ->
                    LegendItem(
                        color = colors[index],
                        label = labels?.getOrNull(index) ?: "Key: ${pair.first}",
                        percentage = pair.second,
                        isSelected = selectedIndex == index
                    )
                    if (index < data.size - 1) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun PieChartCanvas(
    data: List<Pair<Int, Int>>,
    colors: List<Color>,
    gapDegree: Float,
    thickness: Float,
    selectedIndex: Int?,
    onSectorClick: (Int?) -> Unit
) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(data) {
                detectTapGestures { offset ->
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val dx = offset.x - center.x
                    val dy = offset.y - center.y
                    val distance = sqrt(dx * dx + dy * dy)
                    val outerRadius = min(size.width, size.height) / 2f
                    val innerRadius = outerRadius - thickness

                    if (distance in innerRadius..outerRadius) {
                        var angle = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()
                        angle += 90f
                        if (angle < 0) angle += 360f

                        var currentAngle = 0f
                        var found = false
                        for (i in data.indices) {
                            val sweepAngle = (data[i].second / 100f) * 360f
                            if (angle >= currentAngle && angle <= currentAngle + sweepAngle) {
                                onSectorClick(i)
                                found = true
                                break
                            }
                            currentAngle += sweepAngle
                        }
                        if (!found) onSectorClick(null)
                    } else {
                        onSectorClick(null)
                    }
                }
            }
    ) {
        val radius = min(size.width, size.height) / 2f
        val center = Offset(size.width / 2f, size.height / 2f)
        val topLeft = Offset(center.x - radius + thickness / 2, center.y - radius + thickness / 2)
        val arcSize = Size(radius * 2 - thickness, radius * 2 - thickness)

        var startAngle = -90f

        data.forEachIndexed { index, pair ->
            val sweepAngle = (pair.second / 100f) * 360f
            val isSelected = selectedIndex == index
            val baseColor = colors[index]
            val color = if (isSelected) baseColor.copy(alpha = 0.5f) else baseColor

            drawArc(
                color = color,
                startAngle = startAngle + gapDegree / 2f,
                sweepAngle = sweepAngle - gapDegree,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = thickness)
            )

            // Text on Chart
            val midAngle = startAngle + sweepAngle / 2f
            val textRadius = radius - thickness / 2f
            val x = center.x + textRadius * cos(Math.toRadians(midAngle.toDouble())).toFloat()
            val y = center.y + textRadius * sin(Math.toRadians(midAngle.toDouble())).toFloat()

            val paint = android.graphics.Paint().apply {
                this.color = android.graphics.Color.WHITE
                this.textSize = (thickness * 0.35f).coerceIn(20f, 60f)
                this.textAlign = android.graphics.Paint.Align.CENTER
                this.isFakeBoldText = true
                this.isAntiAlias = true
            }

            val textHeight = paint.descent() - paint.ascent()
            val textOffset = (textHeight / 2) - paint.descent()

            drawContext.canvas.nativeCanvas.drawText(
                "${pair.second}%",
                x,
                y + textOffset,
                paint
            )

            startAngle += sweepAngle
        }
    }
}

@Composable
private fun LegendItem(
    color: Color,
    label: String,
    percentage: Int,
    isSelected: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(if (isSelected) color.copy(alpha = 0.1f) else Color.Transparent)
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                maxLines = 1
            )
            Text(
                text = "$percentage%",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun generateDistinctColors(count: Int): List<Color> {
    return List(count) { i ->
        val hue = (i * 360f / count)
        val saturation = 0.7f + (i % 2) * 0.1f
        val lightness = 0.5f + (i % 3) * 0.05f
        Color.hsl(hue, saturation, lightness)
    }
}

@Preview(showBackground = true, widthDp = 600)
@Composable
fun PreviewCircularPieChartWithLegend() {
    MaterialTheme {
        CircularPieChart(
            data = listOf(1 to 25, 2 to 42, 3 to 33),
            labels = listOf("First sector", "Main part", "Rest"),
            modifier = Modifier.fillMaxSize()
        )
    }
}
