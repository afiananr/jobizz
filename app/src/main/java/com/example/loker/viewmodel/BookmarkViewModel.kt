// Lokasi: app/src/main/java/com/example/loker/viewmodel/BookmarkViewModel.kt
package com.example.loker.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loker.model.Job
import com.example.loker.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FieldPath // <-- TAMBAHKAN IMPORT INI
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// State untuk halaman Bookmark
sealed interface BookmarkUiState {
    object Loading : BookmarkUiState
    data class Success(val bookmarkedJobs: List<Job>) : BookmarkUiState
    data class Error(val message: String) : BookmarkUiState
}

class BookmarkViewModel : ViewModel() {

    private val auth = Firebase.auth
    private val db = Firebase.firestore

    private val _uiState = MutableStateFlow<BookmarkUiState>(BookmarkUiState.Loading)
    val uiState: StateFlow<BookmarkUiState> = _uiState

    /**
     * Mengambil daftar lowongan yang telah di-bookmark oleh pengguna saat ini.
     */
    fun fetchBookmarkedJobs() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            _uiState.value = BookmarkUiState.Error("Silakan login untuk melihat bookmark.")
            return
        }

        _uiState.value = BookmarkUiState.Loading
        viewModelScope.launch {
            db.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val user = document.toObject(User::class.java)
                        val bookmarkedIds = user?.bookmarkedJobIds ?: emptyList()

                        if (bookmarkedIds.isEmpty()) {
                            _uiState.value = BookmarkUiState.Success(emptyList())
                        } else {
                            fetchJobsByIds(bookmarkedIds)
                        }
                    } else {
                        _uiState.value = BookmarkUiState.Error("Data pengguna tidak ditemukan.")
                    }
                }
                .addOnFailureListener { exception ->
                    _uiState.value = BookmarkUiState.Error(exception.localizedMessage ?: "Error")
                }
        }
    }

    private fun fetchJobsByIds(ids: List<String>) {
        // [PERUBAHAN UTAMA DI SINI]
        // Kita mencari berdasarkan ID dokumen, bukan field "id"
        db.collection("jobs").whereIn(FieldPath.documentId(), ids).get()
            .addOnSuccessListener { result ->
                // Mengambil ID dokumen dan memasukkannya ke dalam objek Job
                val jobs = result.documents.mapNotNull { doc ->
                    doc.toObject(Job::class.java)?.apply { id = doc.id }
                }
                _uiState.value = BookmarkUiState.Success(jobs)
            }
            .addOnFailureListener { exception ->
                _uiState.value = BookmarkUiState.Error(exception.localizedMessage ?: "Error")
            }
    }

    fun toggleBookmark(jobId: String, isCurrentlyBookmarked: Boolean) {
        val currentUser = auth.currentUser ?: return
        val userDocRef = db.collection("users").document(currentUser.uid)

        val updateValue = if (isCurrentlyBookmarked) {
            FieldValue.arrayRemove(jobId)
        } else {
            FieldValue.arrayUnion(jobId)
        }

        userDocRef.update("bookmarkedJobIds", updateValue)
            .addOnSuccessListener {
                Log.d("BookmarkViewModel", "Bookmark toggled successfully.")
                // Muat ulang data setelah berhasil mengubah bookmark
                fetchBookmarkedJobs()
            }
            .addOnFailureListener { e ->
                Log.w("BookmarkViewModel", "Error toggling bookmark", e)
            }
    }
}