// Lokasi: app/src/main/java/com/example/loker/ui/JobDetailScreen.kt
package com.example.loker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.loker.model.Job
import com.example.loker.viewmodel.JobDetailUiState
import com.example.loker.viewmodel.JobDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDetailScreen(
    navController: NavController,
    jobId: String?,
    viewModel: JobDetailViewModel = viewModel()
) {
    // LaunchedEffect akan memanggil fetchJobById HANYA SATU KALI
    // saat layar ini pertama kali muncul.
    LaunchedEffect(key1 = jobId) {
        if (jobId != null) {
            viewModel.fetchJobById(jobId)
        }
    }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Lowongan") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        },
        bottomBar = {
            // Menampilkan tombol Apply hanya jika data berhasil dimuat
            if (uiState is JobDetailUiState.Success) {
                Box(modifier = Modifier.padding(16.dp)) {
                    Button(
                        onClick = { /* TODO: Implement apply logic */ },
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        Text("Apply for Job")
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                is JobDetailUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is JobDetailUiState.Success -> {
                    val job = state.job
                    if (job != null) {
                        JobDetailContent(job = job)
                    } else {
                        Text("Lowongan tidak ditemukan.", modifier = Modifier.align(Alignment.Center))
                    }
                }
                is JobDetailUiState.Error -> {
                    Text(
                        text = "Gagal memuat detail:\n${state.message}",
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
fun JobDetailContent(job: Job) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = job.companyLogoUrl,
            contentDescription = "${job.companyName} Logo",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = job.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(text = job.companyName, style = MaterialTheme.typography.titleMedium, color = Color.Gray)
        Text(text = job.salaryRange, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 8.dp))

        Spacer(modifier = Modifier.height(24.dp))

        // Deskripsi Pekerjaan (Contoh)
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text("About this job", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "We are searching for a talented and motivated this job to join our growing team. In this role, you will be responsible for this job and will be responsible for this job.",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Job Description", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "We are searching for a talented and motivated this job to join our growing team. In this role, you will be responsible for this job and will be responsible for this job. You will work with other developers and stakeholders to create high-quality software solutions.",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(80.dp)) // Memberi ruang untuk Bottom Bar
    }
}