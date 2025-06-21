// Lokasi: app/src/main/java/com/example/loker/ui/LoginScreen.kt
package com.example.loker.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.loker.R
import com.example.loker.viewmodel.LoginViewModel
import com.example.loker.viewmodel.LoginUiState
import com.example.loker.ui.RegisterScreen

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = viewModel()
) {
    val context = LocalContext.current
    val loginState = viewModel.loginUiState.value
    var passwordVisible by remember { mutableStateOf(false) }

    // LaunchedEffect untuk menangani event sekali jalan seperti navigasi atau Toast
    LaunchedEffect(key1 = loginState) {
        if (loginState is LoginUiState.Success) {
            Toast.makeText(context, "Login Berhasil!", Toast.LENGTH_SHORT).show()
            // Pindah ke dashboard dan hapus semua halaman di auth_graph dari back stack
            navController.navigate("dashboard") {
                popUpTo("auth_graph") { inclusive = true }
            }
        } else if (loginState is LoginUiState.Error) {
            Toast.makeText(context, "Error: ${loginState.message}", Toast.LENGTH_LONG).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_bg),
                contentDescription = "Logo Aplikasi",
                modifier = Modifier
                    .size(120.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text("Selamat Datang Kembali", style = MaterialTheme.typography.headlineSmall)
            Text("Login untuk melanjutkan", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
            Spacer(modifier = Modifier.height(32.dp))

            // Input Fields
            OutlinedTextField(
                value = viewModel.email.value,
                onValueChange = viewModel::onEmailChange,
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = viewModel.password.value,
                onValueChange = viewModel::onPasswordChange,
                label = { Text("Password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description = if (passwordVisible) "Sembunyikan password" else "Tampilkan password"
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = description)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.loginUser() },
                modifier = Modifier.fillMaxWidth(),
                enabled = loginState !is LoginUiState.Loading // Tombol disable saat loading
            ) {
                Text("Login")
            }

            TextButton(onClick = { navController.navigate("register") }) {
                Text("Belum punya akun? Daftar di sini")
            }
        }

        // Tampilkan loading indicator di tengah jika sedang loading
        if (loginState is LoginUiState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}