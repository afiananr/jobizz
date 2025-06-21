// Lokasi: app/src/main/java/com/example/loker/viewmodel/MessagesViewModel.kt
package com.example.loker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loker.model.Conversation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface MessagesUiState {
    object Loading : MessagesUiState
    data class Success(val conversations: List<Conversation>) : MessagesUiState
    data class Error(val message: String) : MessagesUiState
}

class MessagesViewModel : ViewModel() {

    private val auth = Firebase.auth
    private val db = Firebase.firestore

    private val _uiState = MutableStateFlow<MessagesUiState>(MessagesUiState.Loading)
    val uiState: StateFlow<MessagesUiState> = _uiState

    init {
        fetchConversations()
    }

    fun fetchConversations() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            _uiState.value = MessagesUiState.Error("Anda harus login.")
            return
        }

        _uiState.value = MessagesUiState.Loading
        viewModelScope.launch {
            db.collection("conversations")
                // Ini adalah query penting: ambil semua percakapan di mana ID pengguna ada di dalam array 'participantIds'
                .whereArrayContains("participantIds", userId)
                .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { result ->
                    val conversations = result.documents.mapNotNull { doc ->
                        doc.toObject(Conversation::class.java)?.copy(id = doc.id)
                    }
                    _uiState.value = MessagesUiState.Success(conversations)
                }
                .addOnFailureListener { e ->
                    _uiState.value = MessagesUiState.Error(e.localizedMessage ?: "Error")
                }
        }
    }
}