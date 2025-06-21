// Lokasi: app/src/main/java/com/example/loker/viewmodel/CommunityViewModel.kt
package com.example.loker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loker.model.CommunityPost
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface CommunityUiState {
    object Loading : CommunityUiState
    data class Success(val posts: List<CommunityPost>) : CommunityUiState
    data class Error(val message: String) : CommunityUiState
}

class CommunityViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val _uiState = MutableStateFlow<CommunityUiState>(CommunityUiState.Loading)
    val uiState: StateFlow<CommunityUiState> = _uiState

    init {
        fetchPosts()
    }

    fun fetchPosts() {
        _uiState.value = CommunityUiState.Loading
        viewModelScope.launch {
            db.collection("community_posts")
                .orderBy("timestamp", Query.Direction.DESCENDING) // Tampilkan postingan terbaru di atas
                .get()
                .addOnSuccessListener { result ->
                    val posts = result.documents.mapNotNull { doc ->
                        doc.toObject(CommunityPost::class.java)?.copy(id = doc.id)
                    }
                    _uiState.value = CommunityUiState.Success(posts)
                }
                .addOnFailureListener { e ->
                    _uiState.value = CommunityUiState.Error(e.localizedMessage ?: "Error")
                }
        }
    }
}