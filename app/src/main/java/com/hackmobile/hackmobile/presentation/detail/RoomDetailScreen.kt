package com.hackmobile.hackmobile.presentation.detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.hotelhackapp.utils.AppColors
import com.example.hotelhackapp.utils.painter
import com.hackmobile.hackmobile.R
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RoomDetailScreen(
    roomId: String,
    navController: NavHostController
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = currentMonth.minusMonths(6)
    val endMonth = currentMonth.plusMonths(6)

    val calendarState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = DayOfWeek.MONDAY
    )

    var rangeStart by remember { mutableStateOf<LocalDate?>(null) }
    var rangeEnd by remember { mutableStateOf<LocalDate?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier
                .clickable { navController.popBackStack() }
                .padding(8.dp)
        )

        Text(
            text = roomId,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Image(
            painter = R.drawable.hotel.painter,
            contentDescription = "Room",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        Text(
            text = "2 кровати • ванная • WIFI",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Выберите даты заезда и выезда:",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalCalendar(
            state = calendarState,
            dayContent = { day ->
                val isInRange = rangeStart != null && rangeEnd != null &&
                        !day.date.isBefore(rangeStart) && !day.date.isAfter(rangeEnd)
                val isStart = day.date == rangeStart
                val isEnd = day.date == rangeEnd

                val backgroundColor = when {
                    isStart || isEnd -> Color.DarkGray
                    isInRange -> Color.LightGray
                    else -> Color.Transparent
                }

                val textColor = when {
                    isStart || isEnd -> Color.White
                    isInRange -> Color.Black
                    else -> Color.Black
                }

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(backgroundColor)
                        .clickable {
                            when {
                                rangeStart == null -> rangeStart = day.date
                                rangeStart != null && rangeEnd == null -> {
                                    if (day.date.isAfter(rangeStart)) {
                                        rangeEnd = day.date
                                    } else {
                                        rangeStart = day.date
                                    }
                                }
                                else -> {
                                    rangeStart = day.date
                                    rangeEnd = null
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day.date.dayOfMonth.toString(),
                        color = textColor,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }


        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* Забронировать */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColors.Primary
            ),
            modifier = Modifier.fillMaxWidth().heightIn(50.dp),
            shape = RoundedCornerShape(8.dp),
            enabled = rangeStart != null && rangeEnd != null
        ) {
            Text(
                text ="Забронировать",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }
    }
}