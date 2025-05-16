package com.hackmobile.hackmobile.presentation.control

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.hotelhackapp.presentation.MainListViewModel
import com.example.hotelhackapp.utils.AppColors
import org.koin.androidx.compose.koinViewModel

@Composable
fun ControlScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Управление номером", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))
        Text("Температура: 22°C")
        Text("Освещение: включено")

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Button(onClick = { /* включить/выключить свет */ }) {
                Text("Переключить свет")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { /* управление температурой */ }) {
                Text("Настроить климат")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun RoomControlScreen() {

    val viewModel: MainListViewModel = koinViewModel()
    val doorState by viewModel.doorState.collectAsState()
    val commandResult by viewModel.commandResult.collectAsState()
    val permissionRequired by viewModel.permissionRequired.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Управление дверью",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Дверь", style = MaterialTheme.typography.bodyLarge)
            Switch(
                checked = doorState,
                onCheckedChange = { isOpen ->
                    viewModel.toggleDoor(isOpen)
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        commandResult?.let { success ->
            val message = if (success) "Команда отправлена успешно" else "Ошибка отправки команды"
            Text(
                text = message,
                color = if (success) Color.Green else Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }


    }
}


@Composable
fun DoorControlCard(isOpen: Boolean, onToggle: (Boolean) -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
            .background(AppColors.Card)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "Дверь", style = MaterialTheme.typography.bodyLarge)
            }

            Switch(
                checked = isOpen,
                onCheckedChange = { onToggle(it) }
            )
        }
    }
}


@Composable
fun ControlCard(label: String) {
    var isOn by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
            .background(AppColors.Card)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = label, style = MaterialTheme.typography.bodyLarge)
            }

            Switch(
                checked = isOn,
                onCheckedChange = { isOn = it }
            )
        }
    }
}
