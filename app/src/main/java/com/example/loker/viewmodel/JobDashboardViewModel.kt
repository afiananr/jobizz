// Lokasi: app/src/main/java/com/example/loker/viewmodel/JobDashboardViewModel.kt
package com.example.loker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loker.model.Job
import com.example.loker.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Ubah Success state untuk menyertakan daftar ID bookmark
data class DashboardUiSuccessState(
    val suggestedJobs: List<Job> = emptyList(),
    val recentJobs: List<Job> = emptyList(),
    val bookmarkedJobIds: List<String> = emptyList()
)


sealed interface DashboardUiState {
    object Loading : DashboardUiState
    data class Success(val data: DashboardUiSuccessState) : DashboardUiState
    data class Error(val message: String) : DashboardUiState
}

class JobDashboardViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val auth = Firebase.auth

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory = _selectedCategory.asStateFlow()

    init {
        fetchAllData()
    }

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }

    fun onCategoryChange(category: String) {
        _selectedCategory.value = category
    }
    private fun fetchAllData() {
        _uiState.value = DashboardUiState.Loading

        // Ambil data lowongan
        db.collection("jobs").limit(10).get()
            .addOnSuccessListener { jobSnapshot ->
                val jobs = jobSnapshot.documents.mapNotNull { doc ->
                    doc.toObject(Job::class.java)?.apply { id = doc.id }
                }
                // Setelah lowongan didapat, ambil data bookmark pengguna
                fetchUserBookmarks(jobs)
            }
            .addOnFailureListener { e ->
                _uiState.value = DashboardUiState.Error(e.localizedMessage ?: "Error")
            }
    }

    private fun fetchUserBookmarks(jobs: List<Job>) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            // Jika tidak ada user, tampilkan lowongan tanpa info bookmark
            _uiState.value = DashboardUiState.Success(DashboardUiSuccessState(suggestedJobs = jobs, recentJobs = jobs))
            return
        }

        db.collection("users").document(userId).get()
            .addOnSuccessListener { userSnapshot ->
                val bookmarkedIds = userSnapshot.toObject(User::class.java)?.bookmarkedJobIds ?: emptyList()
                _uiState.value = DashboardUiState.Success(
                    DashboardUiSuccessState(
                        suggestedJobs = jobs,
                        recentJobs = jobs,
                        bookmarkedJobIds = bookmarkedIds
                    )
                )
            }
            .addOnFailureListener {
                // Jika gagal ambil data user, tetap tampilkan lowongan
                _uiState.value = DashboardUiState.Success(DashboardUiSuccessState(suggestedJobs = jobs, recentJobs = jobs))
            }
    }

    fun toggleBookmark(jobId: String) {
        val userId = auth.currentUser?.uid ?: return
        val userDocRef = db.collection("users").document(userId)

        // Ambil state saat ini untuk mengetahui apakah sudah di-bookmark atau belum
        val currentState = (uiState.value as? DashboardUiState.Success)?.data ?: return
        val isCurrentlyBookmarked = currentState.bookmarkedJobIds.contains(jobId)

        val updateValue = if (isCurrentlyBookmarked) FieldValue.arrayRemove(jobId) else FieldValue.arrayUnion(jobId)

        userDocRef.update("bookmarkedJobIds", updateValue)
            .addOnSuccessListener {
                // Perbarui UI secara optimis
                val updatedIds = if (isCurrentlyBookmarked) {
                    currentState.bookmarkedJobIds - jobId
                } else {
                    currentState.bookmarkedJobIds + jobId
                }
                _uiState.value = DashboardUiState.Success(currentState.copy(bookmarkedJobIds = updatedIds))
            }
    }
}