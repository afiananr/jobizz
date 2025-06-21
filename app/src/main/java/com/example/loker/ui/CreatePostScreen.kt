// Lokasi: app/src/main/java/com/example/loker/ui/CreatePostScreen.kt
package com.example.loker.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.loker.viewmodel.CreatePostUiState
import com.example.loker.viewmodel.CreatePostViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    navController: NavController,
    viewModel: CreatePostViewModel = viewModel()
) {
    val context = LocalContext.current
    val postContent by viewModel.postContent
    val uiState by viewModel.uiState

    // LaunchedEffect untuk menangani navigasi setelah post berhasil dibuat
    LaunchedEffect(uiState) {
        if (uiState is CreatePostUiState.Success) {
            Toast.makeText(context, "Postingan berhasil dibuat!", Toast.LENGTH_SHORT).show()
            navController.popBackStack() // Kembali ke layar komunitas
        } else if (uiState is CreatePostUiState.Error) {
            Toast.makeText(context, (uiState as CreatePostUiState.Error).message, Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buat Postingan Baru") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Batal")
                    }
                },
                actions = {
                    TextButton(
                        onClick = { viewModel.createPost() },
                        // Tombol non-aktif jika sedang loading atau teks kosong
                        enabled = uiState !is CreatePostUiState.Loading && postContent.isNotBlank()
                    ) {
                        Text("Post")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            OutlinedTextField(
                value = postContent,
                onValueChange = viewModel::onContentChange,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                placeholder = { Text("Apa yang Anda pikirkan?") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }
    }
}