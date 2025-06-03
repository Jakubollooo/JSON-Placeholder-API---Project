package com.example.jsonplaceholderapi.ui.profile

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.jsonplaceholderapi.viewmodel.DarkThemeViewModel
import com.example.jsonplaceholderapi.viewmodel.PostsUiState
import com.example.jsonplaceholderapi.viewmodel.PostsViewModel
import com.example.jsonplaceholderapi.viewmodel.ProfileViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    profileViewModel: ProfileViewModel = viewModel(),
    postsViewModel: PostsViewModel = viewModel(),
    darkThemeViewModel: DarkThemeViewModel = viewModel()
) {
    val context = LocalContext.current
    val fusedLocationProvider = remember { LocationServices.getFusedLocationProviderClient(context) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var userLocation by remember { mutableStateOf<LatLng?>(null) }

    fun showSnackbar(message: String) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }

    fun fetchLocation() {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            showSnackbar("Brak uprawnień do lokalizacji.")
            return
        }

        fusedLocationProvider.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { loc -> loc?.let { userLocation = LatLng(it.latitude, it.longitude) } }
            .addOnFailureListener {
                fusedLocationProvider.lastLocation
                    .addOnSuccessListener { last ->
                        last?.let { userLocation = LatLng(it.latitude, it.longitude) }
                            ?: showSnackbar("Nie znaleziono lokalizacji.")
                    }.addOnFailureListener {
                        showSnackbar("Błąd lokalizacji.")
                    }
            }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> if (granted) fetchLocation() else showSnackbar("Odmówiono dostępu.") }

    LaunchedEffect(Unit) { fetchLocation() }

    val firstName by profileViewModel.firstName.collectAsState(initial = "")
    val lastName by profileViewModel.lastName.collectAsState(initial = "")
    val photoPath by profileViewModel.photoPath.collectAsState(initial = "")
    var firstNameInput by remember(firstName) { mutableStateOf(firstName) }
    var lastNameInput by remember(lastName) { mutableStateOf(lastName) }
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri ?: return@rememberLauncherForActivityResult
        tempImageUri = uri
        val dest = File(context.filesDir, "profile_${System.currentTimeMillis()}.jpg")
        try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                dest.outputStream().use { output -> input.copyTo(output) }
            }
            profileViewModel.savePhotoPath(dest.absolutePath)
        } catch (e: Exception) {
            showSnackbar("Błąd zapisu zdjęcia: ${e.message}")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mój profil") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Cofnij")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Dane personalne", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = firstNameInput,
                onValueChange = { if (!it.contains("\n")) firstNameInput = it },
                label = { Text("Imię") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            OutlinedTextField(
                value = lastNameInput,
                onValueChange = { if (!it.contains("\n")) lastNameInput = it },
                label = { Text("Nazwisko") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )

            Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        profileViewModel.saveName(firstNameInput.trim(), lastNameInput.trim())
                        showSnackbar("Dane zapisane.")
                    },
                    modifier = Modifier.weight(1f)
                ) { Text("Zapisz imię") }

                Button(
                    onClick = { imagePicker.launch("image/*") },
                    modifier = Modifier.weight(1f)
                ) { Text("Zmień zdjęcie") }
            }

            if (firstName.isNotBlank() || lastName.isNotBlank() || photoPath.isNotEmpty() || tempImageUri != null) {
                Text("Podgląd profilu", style = MaterialTheme.typography.titleMedium)
                Card(
                    Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A3C))
                ) {
                    Row(
                        Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val painter = when {
                            photoPath.isNotEmpty() -> rememberAsyncImagePainter(File(photoPath))
                            tempImageUri != null -> rememberAsyncImagePainter(tempImageUri)
                            else -> null
                        }
                        painter?.let {
                            Image(
                                painter = it,
                                contentDescription = "Zdjęcie profilowe",
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                            )
                        } ?: Box(Modifier.size(60.dp))
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = "${firstName.trim()} ${lastName.trim()}".ifBlank { "Brak danych" },
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                OutlinedButton(
                    onClick = {
                        profileViewModel.clearData()
                        tempImageUri = null
                        firstNameInput = ""
                        lastNameInput = ""
                        File(photoPath).takeIf { it.exists() }?.delete()
                        showSnackbar("Profil usunięty.")
                    },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Usuń profil")
                    Spacer(Modifier.width(4.dp))
                    Text("Usuń wszystkie dane")
                }
            }

            val isDark by darkThemeViewModel.isDarkTheme.collectAsState()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tryb ciemny", style = MaterialTheme.typography.bodyLarge)
                Switch(
                    checked = isDark,
                    onCheckedChange = { darkThemeViewModel.toggleTheme(it) }
                )
            }

            Text("Mapa użytkowników:", style = MaterialTheme.typography.titleMedium)
            val cameraPos = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(userLocation ?: LatLng(52.0, 19.0), 5f)
            }

            LaunchedEffect(userLocation) {
                userLocation?.let {
                    cameraPos.animate(CameraUpdateFactory.newLatLngZoom(it, 6f))
                }
            }

            Button(
                onClick = {
                    val perm = Manifest.permission.ACCESS_FINE_LOCATION
                    if (ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED) {
                        fetchLocation()
                        userLocation?.let {
                            coroutineScope.launch {
                                cameraPos.animate(CameraUpdateFactory.newLatLngZoom(it, 12f))
                            }
                        }
                    } else permissionLauncher.launch(perm)
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Pokaż moją lokalizację") }

            val mapUiState by postsViewModel.uiState.collectAsState()
            val users = (mapUiState as? PostsUiState.Success)
                ?.postsWithUsers?.map { it.user }?.distinctBy { it.id } ?: emptyList()

            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                cameraPositionState = cameraPos,
                properties = MapProperties(
                    isMyLocationEnabled = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ),
                uiSettings = MapUiSettings(compassEnabled = false, myLocationButtonEnabled = false)
            ) {
                userLocation?.let {
                    Marker(state = MarkerState(it), title = "Twoja lokalizacja")
                }
                users.forEach { u ->
                    u.address.geo.let { geo ->
                        val lat = geo.lat.toDoubleOrNull()
                        val lng = geo.lng.toDoubleOrNull()
                        if (lat != null && lng != null) {
                            Marker(
                                state = MarkerState(LatLng(lat, lng)),
                                title = u.name,
                                snippet = u.address.city
                            )
                        }
                    }
                }
            }
        }
    }
}
