// Lokasi: app/src/main/java/com/example/loker/ui/RegisterScreen.kt
package com.example.loker.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.loker.R
import com.example.loker.viewmodel.RegisterUiState
import com.example.loker.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = viewModel()
) {
    val context = LocalContext.current
    val registerState = viewModel.registerUiState.value
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = registerState) {
        if (registerState is RegisterUiState.Success) {
            Toast.makeText(context, "Registrasi Berhasil!", Toast.LENGTH_SHORT).show()
            navController.navigate("login") {
                popUpTo("auth_graph") { inclusive = true }
            }
        } else if (registerState is RegisterUiState.Error) {
            Toast.makeText(context, "Error: ${registerState.message}", Toast.LENGTH_LONG).show()
        }
    }

    // [FIX] Bungkus dengan Scaffold untuk Edge-to-Edge yang konsisten
    Scaffold { innerPadding ->
        // [FIX] Gunakan Box untuk centering saat konten pendek
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                },
            contentAlignment = Alignment.Center // Untuk center konten saat tidak perlu scroll
        ) {
            Column(
                // [FIX] HAPUS .fillMaxSize() dan ganti dengan .fillMaxWidth()
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp) // Padding vertikal bisa diatur di dalam
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Spacer untuk memberi jarak dari atas saat di-scroll
                Spacer(modifier = Modifier.height(24.dp))

                Image(
                    painter = painterResource(id = R.drawable.logo_bg),
                    contentDescription = "Logo Aplikasi",
                    modifier = Modifier.size(120.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text("Buat Akun Baru", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(32.dp))

                // Input Fields
                OutlinedTextField(value = viewModel.name.value, onValueChange = viewModel::onNameChange, label = { Text("Nama Lengkap") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = viewModel.address.value, onValueChange = viewModel::onAddressChange, label = { Text("Alamat") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = viewModel.phone.value, onValueChange = viewModel::onPhoneChange, label = { Text("No. Telepon") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = viewModel.email.value, onValueChange = viewModel::onEmailChange, label = { Text("Email") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = viewModel.password.value, onValueChange = viewModel::onPasswordChange, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.registerUser()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = registerState !is RegisterUiState.Loading
                ) {
                    Text("Daftar")
                }

                TextButton(onClick = { navController.popBackStack() }) {
                    Text("Sudah punya akun? Login")
                }

                // Spacer untuk memberi jarak dari bawah saat di-scroll
                Spacer(modifier = Modifier.height(24.dp))
            }

            if (registerState is RegisterUiState.Loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}