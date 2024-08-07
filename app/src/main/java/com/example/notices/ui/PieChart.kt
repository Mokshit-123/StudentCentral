package com.example.notices.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notices.data.attendanceData.Attendance
import kotlin.math.atan2
import kotlin.math.sqrt

@Composable
fun DonutPieChart(
    attendanceList: List<Attendance>,
    animDuration: Int = 1000
) {
    val totalClasses = attendanceList.sumOf { it.totalClasses }
    val totalAttendedClass = attendanceList.sumOf{ it.attendedClasses}
    val totalSubjects = attendanceList.size
    val totalArcSize = 360f - ((totalSubjects + 1) * 12)

    val colors = listOf(
        Color(0xFFE57373), Color(0xFFBA68C8), Color(0xFF64B5F6), Color(0xFF4DB6AC),
        Color(0xFFFFD54F), Color(0xFFFF8A65), Color(0xFF9575CD), Color(0xFF81C784),
        Color(0xFF7986CB), Color(0xFFAED581), Color(0xFF4FC3F7), Color(0xFFFFB74D),
        Color(0xFFDCE775), Color(0xFF4DD0E1), Color(0xFFFF8A80), Color(0xFF90A4AE),
        Color(0xFFA1887F), Color(0xFF80CBC4), Color(0xFFFFF176), Color(0xFFD1C4E9)
    )

    val arcForEachSubject = attendanceList.map { totalArcSize * it.attendedClasses.toFloat() / totalClasses }
    val size = 200.dp
    val strokeWidth = 16.dp

    var selectedArc by remember { mutableStateOf<Attendance?>(null) }
    var displayText by remember { mutableStateOf("Overall Attendance: ${String.format("%.2f", 100.0 * totalAttendedClass / totalClasses)} %") }
    Box(
        modifier = Modifier
            .size(size)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val center = Offset(size.toPx() / 2, size.toPx() / 2)
                    val outerRadius = size.toPx() / 2
                    val innerRadius = outerRadius - strokeWidth.toPx() - 20
                    val distance = offset.distanceTo(center)

                    if (distance <= innerRadius) {
                        // Center clicked
                        selectedArc = null
                        displayText = "Overall Attendance: ${100*totalAttendedClass/totalClasses} %"
                    } else if (distance > innerRadius && distance <= outerRadius) {
                        // Arc clicked
                        val touchAngle = offset.angleTo(center)
                        var startAngle = 0f
                        var found = false

                        attendanceList.forEachIndexed { index, attendance ->
                            val arcStartAngle = startAngle
                            val arcEndAngle = arcStartAngle + arcForEachSubject[index]

                            if (touchAngle in arcStartAngle..arcEndAngle) {
                                selectedArc = attendance
                                displayText =
                                    "${attendance.subjectName}: ${100*attendance.attendedClasses/attendance.totalClasses}%"
                                found = true
                            }
                            startAngle = arcEndAngle + 12 // Add gap
                        }

                        if (!found) {
                            selectedArc = null
                            displayText = "Absent: ${100*(totalClasses-totalAttendedClass)/totalClasses}%"
                        }
                    } else {
                        selectedArc = null
                        displayText = "Overall Attendance: ${100*totalAttendedClass/totalClasses} %"
                    }
                }
            }
    ) {
        Canvas(
            modifier = Modifier
                .align(Alignment.Center)
                .size(size)
                .padding(16.dp)
        ) {
            var startAngle = 0f

            attendanceList.forEachIndexed { index, attendance ->
                drawArc(
                    color = colors[index],
                    startAngle = startAngle,
                    sweepAngle = arcForEachSubject[index],
                    useCenter = false,
                    style = Stroke(
                        width = strokeWidth.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
                startAngle += arcForEachSubject[index] + 12 // Add gap
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(50.dp)
        ) {
            BasicText(
                text = displayText,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

private fun Offset.distanceTo(other: Offset): Float {
    val dx = x - other.x
    val dy = y - other.y
    return sqrt(dx * dx + dy * dy)
}

private fun Offset.angleTo(center: Offset): Float {
    val angle = atan2(y - center.y, x - center.x) * (180 / Math.PI).toFloat()
    return if (angle < 0) angle + 360 else angle
}

@Preview
@Composable
private fun PreviewDrawArcTutorial() {
    DonutPieChart(
        attendanceList = listOf(
            Attendance("Math", "Mr. A", 50, 45),
            Attendance("Science", "Ms. B", 50, 40),
            Attendance("History", "Mr. C", 50, 35),
            Attendance("English", "Ms. D", 50, 30),
            Attendance("Geography", "Mr. E", 50, 25),
            Attendance("Physics", "Mr. F", 50, 45),
            Attendance("Chemistry", "Ms. G", 50, 40),
        )
    )
}
