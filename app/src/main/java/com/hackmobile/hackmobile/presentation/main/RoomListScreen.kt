package com.hackmobile.hackmobile.presentation.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.hotelhackapp.utils.AppColors
import com.example.hotelhackapp.utils.painter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.hackmobile.hackmobile.R
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun RoomListScreen(
    navController: NavController,
) {
    LazyColumn(modifier = Modifier.padding(top = 16.dp, start = 8.dp, end = 8.dp)) {
        items(3) { index ->
            RoomCard(
                onClick = { navController.navigate("room_detail/${index + 1}") },
                navController
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RoomDetailScreenV2(
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

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            ImageSlider(
                modifier = Modifier.fillMaxWidth().height(240.dp),
                fromRoom = true,
                navController = navController,
            )
        }

        item {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Номер 1488 — 120 BYN/ночь", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text("2 кровати • ванная • Wifi", color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Некоторое описание комнаты она типа 1488 не все поймут и тут 2 кровати типа можно спать вдвоем если они односпальные или вчетвером если двуспальные лол",
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Выберите даты бронирования:", fontSize = 16.sp, fontWeight = FontWeight.Medium)
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
    }
}

@Composable
fun RoomCard(
    onClick: () -> Unit,
    navController: NavController,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column {
            ImageSlider(
                modifier = Modifier.fillMaxWidth().height(200.dp),
                navController = navController,
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text("2-х комнатный номер", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("2 кровати • ванная • деньги • телки • тачки", color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Минимально для максимального комфорта", fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("120 BYN/ночь", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ImageSlider(
    modifier: Modifier = Modifier,
    fromRoom: Boolean = false,
    navController: NavController,
    ) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val imageResIds = listOf(
        R.drawable.hotel,
        R.drawable.hotel,
        R.drawable.hotel,
        R.drawable.hotel,
        R.drawable.hotel,
        R.drawable.hotel,
        R.drawable.hotel,
    )
    val cornerShape = if (fromRoom) RoundedCornerShape(0.dp) else RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    Box(modifier = modifier.clip(cornerShape)) {

        com.google.accompanist.pager.HorizontalPager(
            count = imageResIds.size,
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Image(
                painter = imageResIds[page].painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        if (fromRoom) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .clickable { navController.popBackStack() }
                    .padding(8.dp),
                tint = Color.White

            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp)
                .background(Color.Black.copy(alpha = 0.5f), shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = "${pagerState.currentPage + 1} из ${imageResIds.size}",
                color = Color.White,
                fontSize = 12.sp
            )
        }
    }
}