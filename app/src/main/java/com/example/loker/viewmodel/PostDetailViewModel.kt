// Lokasi: app/src/main/java/com/example/loker/viewmodel/PostDetailViewModel.kt
package com.example.loker.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.loker.model.CommunityPost
import com.example.loker.model.PostComment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed interface PostDetailUiState {
    object Loading : PostDetailUiState
    data class Success(val post: CommunityPost?, val comments: List<PostComment>) : PostDetailUiState
    data class Error(val message: String) : PostDetailUiState
}

class PostDetailViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val auth = Firebase.auth

    private val _uiState = MutableStateFlow<PostDetailUiState>(PostDetailUiState.Loading)
    val uiState: StateFlow<PostDetailUiState> = _uiState

    // [BARU] State untuk menampung teks di kolom komentar
    private val _commentText = mutableStateOf("")
    val commentText: State<String> = _commentText

    // [BARU] Fungsi untuk di-trigger dari UI saat teks berubah
    fun onCommentChange(text: String) {
        _commentText.value = text
    }

    fun fetchPostAndComments(postId: String) {
        // ... (Fungsi ini tetap sama, tidak perlu diubah) ...
        if (postId.isBlank()) {
            _uiState.value = PostDetailUiState.Error("Post ID tidak valid.")
            return
        }
        _uiState.value = PostDetailUiState.Loading
        db.collection("community_posts").document(postId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val post = document.toObject(CommunityPost::class.java)?.copy(id = document.id)
                    listenForComments(postId, post)
                } else {
                    _uiState.value = PostDetailUiState.Error("Postingan tidak ditemukan.")
                }
            }
            .addOnFailureListener { e ->
                _uiState.value = PostDetailUiState.Error(e.localizedMessage ?: "Error")
            }
    }

    private fun listenForComments(postId: String, post: CommunityPost?) {
        // ... (Fungsi ini tetap sama, tidak perlu diubah) ...
        db.collection("community_posts").document(postId)
            .collection("comments")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    _uiState.value = PostDetailUiState.Error(error.localizedMessage ?: "Error")
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val comments = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(PostComment::class.java)?.copy(id = doc.id)
                    }
                    _uiState.value = PostDetailUiState.Success(post, comments)
                }
            }
    }

    /**
     * [FUNGSI BARU] Untuk mengirim komentar ke Firestore.
     */
    fun addComment(postId: String) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            // Handle jika pengguna tidak login (seharusnya tidak terjadi di halaman ini)
            return
        }
        if (commentText.value.isBlank()) {
            // Jangan kirim komentar kosong
            return
        }

        val comment = PostComment(
            authorId = currentUser.uid,
            authorName = currentUser.displayName ?: "Pengguna Anonim",
            authorPhotoUrl = currentUser.photoUrl?.toString() ?: "",
            content = commentText.value
            // Timestamp akan diisi otomatis oleh server
        )

        db.collection("community_posts").document(postId)
            .collection("comments") // Akses sub-collection 'comments'
            .add(comment) // Tambahkan dokumen komentar baru
            .addOnSuccessListener {
                // Jika berhasil, kosongkan kembali kolom input
                _commentText.value = ""
            }
            .addOnFailureListener { e ->
                // Handle jika gagal mengirim komentar
                // (misalnya tampilkan Toast error)
            }
    }
}