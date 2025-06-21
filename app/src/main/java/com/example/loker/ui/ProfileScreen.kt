// Lokasi: app/src/main/java/com/example/loker/ui/ProfileScreen.kt
package com.example.loker.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.loker.model.User
import com.example.loker.viewmodel.ProfileViewModel
import com.example.loker.viewmodel.ProfileUiState

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel()
) {
    val context = LocalContext.current
    // LaunchedEffect akan memanggil fetchUserProfile HANYA SATU KALI saat layar pertama kali muncul.
    LaunchedEffect(key1 = Unit) {
        viewModel.fetchCurrentUserProfile()
    }

    val uiState by viewModel.uiState.collectAsState()
    val hasLoggedOut by viewModel.logoutState.collectAsState()

    // LaunchedEffect kedua untuk mengamati status logout
    LaunchedEffect(key1 = hasLoggedOut) {
        if (hasLoggedOut) {
            // Jika logout berhasil, kembali ke halaman login dan hapus semua layar sebelumnya
            navController.navigate("auth_graph") {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            }
            Toast.makeText(context, "Anda telah logout.", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        // Kita gunakan kembali BottomNavigationBar yang sudah ada
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                is ProfileUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ProfileUiState.Success -> {
                    val user = state.user
                    if (user != null) {
                        ProfileContent(user = user, onLogoutClick = { viewModel.logout() })
                    } else {
                        Text("Profil tidak ditemukan.", modifier = Modifier.align(Alignment.Center))
                    }
                }
                is ProfileUiState.Error -> {
                    Text(
                        text = "Gagal memuat profil:\n${state.message}",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileContent(user: User, onLogoutClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = user.photoUrl,
            contentDescription = "Foto Profil",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = user.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(text = user.email, style = MaterialTheme.typography.bodyLarge, color = Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))

        // Bio Pengguna
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text("Tentang Saya", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(user.bio, style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.weight(1f)) // Mendorong tombol ke bawah

        Button(
            onClick = onLogoutClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Logout")
        }
    }
}