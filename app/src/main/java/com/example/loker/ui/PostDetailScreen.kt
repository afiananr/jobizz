// Lokasi: app/src/main/java/com/example/loker/ui/PostDetailScreen.kt
package com.example.loker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.loker.model.CommunityPost
import com.example.loker.model.PostComment
import com.example.loker.viewmodel.PostDetailViewModel
import com.example.loker.viewmodel.PostDetailUiState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    navController: NavController,
    postId: String?,
    viewModel: PostDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val commentText by viewModel.commentText
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = postId) {
        if (postId != null) {
            viewModel.fetchPostAndComments(postId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Postingan") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        },
        // [PERUBAHAN] Menambahkan bottomBar untuk input komentar
        bottomBar = {
            CommentInputBar(
                text = commentText,
                onTextChange = viewModel::onCommentChange,
                onSendClick = {
                    if (postId != null) {
                        viewModel.addComment(postId)
                    }
                }
            )
        }
    ) { innerPadding ->
        when (val state = uiState) {
            is PostDetailUiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            }
            is PostDetailUiState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(state.message) }
            }
            is PostDetailUiState.Success -> {
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    item {
                        state.post?.let { PostItem(post = it) }
                        Divider(modifier = Modifier.padding(vertical = 16.dp))
                        Text("Komentar", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    items(state.comments) { comment ->
                        CommentItem(comment = comment)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                // Efek untuk auto-scroll ke komentar paling bawah saat ada komentar baru
                LaunchedEffect(state.comments.size) {
                    if (state.comments.isNotEmpty()) {
                        coroutineScope.launch {
                            lazyListState.animateScrollToItem(state.comments.size)
                        }
                    }
                }
            }
        }
    }
}

// [KOMPONEN BARU]
@Composable
fun CommentInputBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Surface(shadowElevation = 8.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                placeholder = { Text("Tulis komentar...") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onSendClick,
                enabled = text.isNotBlank() // Tombol hanya aktif jika ada teks
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Kirim Komentar")
            }
        }
    }
}


// ... Komponen PostItem, CommentItem, dan formatDate tetap sama ...
@Composable
fun PostItem(post: CommunityPost) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = post.authorPhotoUrl,
                contentDescription = "Author Photo",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(post.authorName, fontWeight = FontWeight.Bold)
                // Panggilan PENTING ke formatDate ada di sini
                Text(
                    text = post.timestamp?.toDate()?.let { formatDate(it) } ?: "beberapa saat lalu",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(post.content, style = MaterialTheme.typography.bodyLarge)
    }
}
@Composable
fun CommentItem(comment: PostComment) { /* ... isi tidak berubah ... */ }
fun formatDate(date: java.util.Date): String {
    // Tentukan format yang diinginkan, contoh: "20 Jun 2025, 08:30"
    val format = java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", java.util.Locale("id", "ID"))
    return format.format(date)
}