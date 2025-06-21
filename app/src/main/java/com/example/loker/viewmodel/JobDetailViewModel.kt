package com.example.loker.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loker.model.Job
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * State untuk UI Halaman Detail.
 * Hanya akan berisi satu objek Job, bisa null jika tidak ditemukan.
 */
sealed interface JobDetailUiState {
    object Loading : JobDetailUiState
    data class Success(val job: Job?) : JobDetailUiState
    data class Error(val message: String) : JobDetailUiState
}

class JobDetailViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val _uiState = MutableStateFlow<JobDetailUiState>(JobDetailUiState.Loading)
    val uiState: StateFlow<JobDetailUiState> = _uiState

    /**
     * Mengambil data satu dokumen pekerjaan dari Firestore berdasarkan ID-nya.
     */
    fun fetchJobById(jobId: String) {
        if (jobId.isBlank()) {
            _uiState.value = JobDetailUiState.Error("Job ID tidak valid.")
            return
        }

        _uiState.value = JobDetailUiState.Loading
        viewModelScope.launch {
            db.collection("jobs").document(jobId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        // Jika dokumen ditemukan, konversi ke objek Job
                        val job = document.toObject(Job::class.java)
                        // Set ID-nya juga
                        job?.id = document.id
                        _uiState.value = JobDetailUiState.Success(job)
                    } else {
                        // Jika dokumen tidak ditemukan
                        Log.d("ViewModel", "No such document")
                        _uiState.value = JobDetailUiState.Success(null)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("ViewModel", "get failed with ", exception)
                    _uiState.value = JobDetailUiState.Error(exception.localizedMessage ?: "Error tidak diketahui")
                }
        }
    }
}