// Lokasi: app/src/main/java/com/example/loker/ui/JobDashboardScreen.kt
package com.example.loker.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.loker.R
import com.example.loker.model.Job
import com.example.loker.viewmodel.DashboardUiState
import com.example.loker.viewmodel.DashboardUiSuccessState
import com.example.loker.viewmodel.JobDashboardViewModel
import com.google.android.material.bottomappbar.BottomAppBar.FabAlignmentMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDashboardScreen(
    navController: NavController,
    viewModel: JobDashboardViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopBar(navController = navController) },
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                is DashboardUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is DashboardUiState.Success -> {
                    DashboardContent(
                        navController = navController,
                        successState = state.data,
                        onBookmarkToggle = { jobId -> viewModel.toggleBookmark(jobId) }
                    )
                }
                is DashboardUiState.Error -> {
                    Text(
                        text = "Gagal memuat data:\n${state.message}",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center).padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun DashboardContent(
    navController: NavController,
    successState: DashboardUiSuccessState,
    onBookmarkToggle: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(16.dp))
        SearchBar(
            query = query,
            onQueryChange = viewModel::onQueryChange
        )
        Spacer(Modifier.height(24.dp))
        SuggestedJobsSection(
            navController = navController,
            jobs = successState.suggestedJobs,
            bookmarkedJobIds = successState.bookmarkedJobIds,
            onBookmarkToggle = onBookmarkToggle
        )
        Spacer(Modifier.height(24.dp))
        RecentJobsSection(
            navController = navController,
            jobs = successState.recentJobs,
            bookmarkedJobIds = successState.bookmarkedJobIds,
            onBookmarkToggle = onBookmarkToggle
        )
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun SuggestedJobsSection(
    navController: NavController,
    jobs: List<Job>,
    bookmarkedJobIds: List<String>,
    onBookmarkToggle: (String) -> Unit
) {
    Column {
        SectionHeader(title = "Suggested Jobs", onSeeAllClick = { })
        Spacer(Modifier.height(8.dp))
        if (jobs.isEmpty()) {
            Text("Tidak ada lowongan yang disarankan.", color = Color.Gray)
        } else {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(jobs) { job ->
                    val isBookmarked = job.id in bookmarkedJobIds
                    SuggestedJobCard(
                        navController = navController,
                        job = job,
                        isBookmarked = isBookmarked,
                        onBookmarkClick = { onBookmarkToggle(job.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun RecentJobsSection(
    navController: NavController,
    jobs: List<Job>,
    bookmarkedJobIds: List<String>,
    onBookmarkToggle: (String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf("All") }
    Column {
        SectionHeader(title = "Recent Jobs", onSeeAllClick = { })
        Spacer(Modifier.height(16.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(Job.JOB_CATEGORIES) { category ->
                FilterChip(selected = category == selectedCategory, onClick = { selectedCategory = category }, label = { Text(category) })
            }
        }
        Spacer(Modifier.height(16.dp))
        if (jobs.isEmpty()) {
            Text("Tidak ada lowongan terbaru.", color = Color.Gray)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                jobs.forEach { job ->
                    val isBookmarked = job.id in bookmarkedJobIds
                    RecentJobCard(
                        navController = navController,
                        job = job,
                        isBookmarked = isBookmarked,
                        onBookmarkClick = { onBookmarkToggle(job.id) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SuggestedJobCard(
    navController: NavController,
    job: Job,
    isBookmarked: Boolean,
    onBookmarkClick: () -> Unit
) {
    Card(
        onClick = { navController.navigate("detail/${job.id}") },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF5F5BFF)),
        modifier = Modifier.width(280.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(model = job.companyLogoUrl, contentDescription = null, modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.White))
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = job.title,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1, // <-- Batasi teks hanya dalam 1 baris
                        overflow = TextOverflow.Ellipsis)
                    Text(job.companyName, fontSize = 14.sp, color = Color.LightGray)
                }
                IconButton(onClick = onBookmarkClick) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = Color.White
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            Text(job.salaryRange, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.White)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                JobTag(text = job.jobType, isSuggested = true)
                JobTag(text = job.workModel, isSuggested = true)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentJobCard(
    navController: NavController,
    job: Job,
    isBookmarked: Boolean,
    onBookmarkClick: () -> Unit
) {
    Card(
        onClick = { navController.navigate("detail/${job.id}") },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(model = job.companyLogoUrl, contentDescription = null, modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(Color.LightGray))
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(job.title, fontWeight = FontWeight.Bold)
                Text(job.companyName, fontSize = 14.sp, color = Color.Gray)
                Spacer(Modifier.height(4.dp))
                Text(job.salaryRange, fontWeight = FontWeight.SemiBold, color = Color(0xFF5F5BFF))
            }
            IconButton(onClick = onBookmarkClick) {
                Icon(
                    imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                    contentDescription = "Bookmark",
                    tint = if (isBookmarked) MaterialTheme.colorScheme.primary else Color.Gray
                )
            }
        }
    }
}

@Composable
private fun TopBar(navController: NavController) {
    Row(modifier = Modifier.fillMaxWidth().padding(6.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.logo_nama),
                contentDescription = "Logo Aplikasi",
                // 2. UKURAN GAMBAR DIPERKECIL drastis agar pas di header
                modifier = Modifier.height(70.dp) // Cukup atur tingginya
            )
        }
        // [PERUBAHAN] Ikon ini sekarang mengarah ke rute "messages"
        IconButton(onClick = { navController.navigate("messages") }) {
            Image(
                painter = painterResource(id = R.drawable.ic_notif), // <-- Logo notifikasi Anda
                contentDescription = "Notifikasi", // Deskripsi yang lebih sesuai
                // SARAN: Gunakan .size() untuk ikon agar proporsional, 24.dp adalah standar
                modifier = Modifier.size(70.dp)
            )
        }
    }
}

@Composable
fun SearchBar(
    query: String, // 'query' adalah nama yang lebih deskriptif untuk teks pencarian
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier // Best practice: selalu terima modifier
) {
    OutlinedTextField(
        value = query, // Gunakan nilai dari parameter
        onValueChange = onQueryChange, // Gunakan fungsi dari parameter
        placeholder = { Text("Cari pekerjaan...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true // Tambahan: agar tidak jadi multiline saat teks panjang
    )
}

@Composable
private fun SectionHeader(title: String, onSeeAllClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        TextButton(onClick = onSeeAllClick) { Text("See all", color = Color.Gray) }
    }
}

@Composable
private fun JobTag(text: String, isSuggested: Boolean = false) {
    val backgroundColor = if (isSuggested) Color.White.copy(alpha = 0.2f) else Color(0xFFE3E1FF)
    val textColor = if (isSuggested) Color.White else Color(0xFF5F5BFF)
    Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(backgroundColor).padding(horizontal = 12.dp, vertical = 6.dp)) {
        Text(text, color = textColor, fontSize = 12.sp)
    }
}