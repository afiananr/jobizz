// Lokasi: app/src/main/java/com/example/loker/ui/BookmarkScreen.kt
package com.example.loker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.loker.viewmodel.BookmarkUiState
import com.example.loker.viewmodel.BookmarkViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(
    navController: NavController,
    viewModel: BookmarkViewModel = viewModel()
) {
    // LaunchedEffect akan memanggil fetchBookmarkedJobs HANYA SATU KALI
    // saat layar ini pertama kali muncul, atau saat login berubah (opsional).
    LaunchedEffect(Unit) {
        viewModel.fetchBookmarkedJobs()
    }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Lowongan Tersimpan") })
        },
        bottomBar = {
            // Menggunakan komponen navigasi bawah yang sudah kita buat bersama
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                is BookmarkUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is BookmarkUiState.Success -> {
                    val bookmarkedJobs = state.bookmarkedJobs
                    if (bookmarkedJobs.isEmpty()) {
                        Text(
                            text = "Anda belum menyimpan lowongan apa pun.",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(bookmarkedJobs) { job ->
                                // Kita gunakan kembali RecentJobCard yang sudah ada
                                RecentJobCard(
                                    navController = navController,
                                    job = job,
                                    // Di halaman bookmark, semua pasti sudah di-bookmark
                                    isBookmarked = true,
                                    onBookmarkClick = {
                                        // Saat ikon bookmark di sini diklik, hapus dari daftar
                                        viewModel.toggleBookmark(job.id, true)
                                    }
                                )
                            }
                        }
                    }
                }
                is BookmarkUiState.Error -> {
                    Text(
                        text = "Gagal memuat bookmark:\n${state.message}",
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