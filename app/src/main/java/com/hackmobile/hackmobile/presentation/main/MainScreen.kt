package com.hackmobile.hackmobile.presentation.main

import androidx.compose.runtime.Composable
import com.example.hotelhackapp.presentation.MainListViewModel
import org.koin.androidx.compose.koinViewModel

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.hotelhackapp.core.navigation.NavRoutes
import com.example.hotelhackapp.utils.AppColors
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.hackmobile.hackmobile.R
import kotlinx.coroutines.delay

@Composable
fun MainScreen(
    viewModel: MainListViewModel = koinViewModel(),
    navController: NavHostController = NavHostController(LocalContext.current),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Заголовок
        Text(
            text = "Главная",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
        )

        AdvancedImageSlider()

        Text(
            text = "Номера",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Дата заезда", style = MaterialTheme.typography.bodyMedium)
                Text("10 апреля", style = MaterialTheme.typography.bodyLarge)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Дата выезда", style = MaterialTheme.typography.bodyMedium)
                Text("12 апреля", style = MaterialTheme.typography.bodyLarge)
            }
        }


        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        RoomItem(
            title = "2-комнатная Комната 101",
            price = "от 120 BYN/ночь",
            onClick = {
                navController.navigate(NavRoutes.RoomDetail.createRoute("Комната 101"))
            },
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        RoomItem(
            title = "3-комнатная Комната 202",
            price = "от 150 BYN/ночь",
            onClick = {
                navController.navigate(NavRoutes.RoomDetail.createRoute("Комната 202"))
            },
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        RoomItem(
            title = "4-комнатная Комната 303",
            price = "от 180 BYN/ночь",
            onClick = {
                navController.navigate(NavRoutes.RoomDetail.createRoute("Комната 303"))
            },
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Text(
            text = "VIP",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
    }
}

@Composable
fun RoomItem(
    title: String,
    price: String,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.Card
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = price,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Navigate",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }

        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AdvancedImageSlider() {
    val images = listOf(R.drawable.hotel, R.drawable.hotel, R.drawable.hotel)
    val pagerState = rememberPagerState()
    val autoScrollEnabled = remember { mutableStateOf(true) }

    LaunchedEffect(pagerState, autoScrollEnabled.value) {
        if (!autoScrollEnabled.value) {
            delay(5000)
            autoScrollEnabled.value = true
        }

        while (autoScrollEnabled.value && images.isNotEmpty()) {
            delay(3000)
            if (autoScrollEnabled.value) {
                pagerState.animateScrollToPage(
                    page = (pagerState.currentPage + 1) % images.size
                )
            }
        }
    }

    Column {
        HorizontalPager(
            count = images.size,
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            userScrollEnabled = true
        ) { page ->
            Image(
                painter = painterResource(images[page]),
                contentDescription = "Hotel image ${page + 1}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .clickable {
                        autoScrollEnabled.value = false

                    }
            )
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),
            activeColor = MaterialTheme.colorScheme.primary,
            inactiveColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MaterialTheme {
        MainScreen()
    }
}