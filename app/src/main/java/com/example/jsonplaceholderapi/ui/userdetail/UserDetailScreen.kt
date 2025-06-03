package com.example.jsonplaceholderapi.ui.userdetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jsonplaceholderapi.viewmodel.*
import com.example.jsonplaceholderapi.ui.userdetail.components.TodoItem
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    userId: Int,
    viewModel: UserDetailViewModel,
    onBackClick: () -> Unit
) {
    val userState by viewModel.userState.collectAsState()
    val todosState by viewModel.todosState.collectAsState()

    LaunchedEffect(userId) {
        viewModel.loadUserDetails(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil użytkownika") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Wróć")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = userState) {
                is UserUiState.Loading -> LoadingRow()
                is UserUiState.Error -> ErrorRow(state.message) {
                    viewModel.retryUser(userId)
                }
                is UserUiState.Success -> {
                    val user = state.user

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            InfoRow(
                                icon = Icons.Filled.Person,
                                text = user.name,
                                style = MaterialTheme.typography.titleMedium,
                                iconDescription = "Imię i nazwisko"
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            InfoRow(
                                icon = Icons.Filled.AccountCircle,
                                text = user.username,
                                style = MaterialTheme.typography.bodyMedium,
                                iconDescription = "Nazwa użytkownika"
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            InfoRow(
                                icon = Icons.Filled.Email,
                                text = user.email,
                                style = MaterialTheme.typography.bodyMedium,
                                iconDescription = "Email"
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            InfoRow(
                                icon = Icons.Filled.Phone,
                                text = user.phone,
                                style = MaterialTheme.typography.bodyMedium,
                                iconDescription = "Telefon"
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            InfoRow(
                                icon = Icons.Filled.Language,
                                text = user.website,
                                style = MaterialTheme.typography.bodyMedium,
                                iconDescription = "Strona internetowa"
                            )

                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                            Text(
                                "Adres:",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            InfoRow(
                                icon = Icons.Filled.LocationOn,
                                text = "${user.address.street}, ${user.address.suite}",
                                style = MaterialTheme.typography.bodyMedium,
                                iconDescription = "Ulica i numer mieszkania"
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            InfoRow(
                                icon = Icons.Filled.LocationCity,
                                text = "${user.address.city}, ${user.address.zipcode}",
                                style = MaterialTheme.typography.bodyMedium,
                                iconDescription = "Miasto i kod pocztowy"
                            )


                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                            Text(
                                "Firma:",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            InfoRow(
                                icon = Icons.Filled.Business,
                                text = user.company.name,
                                style = MaterialTheme.typography.bodyMedium,
                                iconDescription = "Nazwa firmy"
                            )
                            if (user.company.catchPhrase.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "\"${user.company.catchPhrase}\"",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontStyle = FontStyle.Italic,
                                    modifier = Modifier.padding(start = 28.dp)
                                )
                            }
                            if (user.company.bs.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = user.company.bs,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(start = 28.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    val latLng = LatLng(
                        user.address.geo.lat.toDoubleOrNull() ?: 0.0,
                        user.address.geo.lng.toDoubleOrNull() ?: 0.0
                    )
                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(latLng, 12f)
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .padding(horizontal = 16.dp)
                            .pointerInteropFilter { it.action != android.view.MotionEvent.ACTION_DOWN }
                    ) {
                        GoogleMap(
                            modifier = Modifier.matchParentSize(),
                            cameraPositionState = cameraPositionState,
                            uiSettings = MapUiSettings(
                                scrollGesturesEnabled = true,
                                zoomGesturesEnabled = true,
                                tiltGesturesEnabled = true,
                                compassEnabled = false
                            )
                        ) {
                            Marker(
                                state = MarkerState(position = latLng),
                                title = user.name,
                                snippet = user.address.street
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Zadania:",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                when (val state = todosState) {
                    is TodosUiState.Loading -> item { LoadingRow() }
                    is TodosUiState.Error -> item {
                        ErrorRow(state.message) {
                            viewModel.retryTodos(userId)
                        }
                    }
                    is TodosUiState.Success -> {
                        if (state.todos.isEmpty()) {
                            item {
                                Text(
                                    "Brak zadań do wyświetlenia.",
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        } else {
                            items(state.todos) { todo ->
                                TodoItem(todo = todo)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    style: androidx.compose.ui.text.TextStyle,
    iconDescription: String? = null
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = iconDescription,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = style)
    }
}

@Composable
private fun LoadingRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorRow(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Błąd: $message", color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text("Spróbuj ponownie")
        }
    }
}