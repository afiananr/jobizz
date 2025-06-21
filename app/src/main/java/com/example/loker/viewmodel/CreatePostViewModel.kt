// Lokasi: app/src/main/java/com/example/loker/viewmodel/CreatePostViewModel.kt
package com.example.loker.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loker.model.CommunityPost
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

sealed interface CreatePostUiState {
    object Idle : CreatePostUiState
    object Loading : CreatePostUiState
    object Success : CreatePostUiState
    data class Error(val message: String) : CreatePostUiState
}

class CreatePostViewModel : ViewModel() {

    private val auth = Firebase.auth
    private val db = Firebase.firestore

    private val _postContent = mutableStateOf("")
    val postContent: State<String> = _postContent

    private val _uiState = mutableStateOf<CreatePostUiState>(CreatePostUiState.Idle)
    val uiState: State<CreatePostUiState> = _uiState

    fun onContentChange(newContent: String) {
        _postContent.value = newContent
    }

    fun createPost() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            _uiState.value = CreatePostUiState.Error("Anda harus login untuk membuat postingan.")
            return
        }
        if (postContent.value.isBlank()) {
            _uiState.value = CreatePostUiState.Error("Postingan tidak boleh kosong.")
            return
        }

        _uiState.value = CreatePostUiState.Loading
        viewModelScope.launch {
            val newPost = CommunityPost(
                authorId = currentUser.uid,
                authorName = currentUser.displayName ?: "Pengguna Anonim",
                authorPhotoUrl = currentUser.photoUrl?.toString() ?: "",
                content = postContent.value,
                likes = 0,
                // Timestamp akan diisi oleh server
            )

            db.collection("community_posts").add(newPost)
                .addOnSuccessListener {
                    _uiState.value = CreatePostUiState.Success
                }
                .addOnFailureListener { e ->
                    _uiState.value = CreatePostUiState.Error(e.localizedMessage ?: "Gagal membuat postingan")
                }
        }
    }
}