// Lokasi: app/src/main/java/com/example/loker/viewmodel/ProfileViewModel.kt
package com.example.loker.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loker.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface ProfileUiState {
    object Loading : ProfileUiState
    data class Success(val user: User?) : ProfileUiState
    data class Error(val message: String) : ProfileUiState
}

class ProfileViewModel : ViewModel() {

    private val auth = Firebase.auth
    private val db = Firebase.firestore

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState

    private val _logoutState = MutableStateFlow(false)
    val logoutState: StateFlow<Boolean> = _logoutState

    /**
     * Mengambil data profil dari pengguna yang SEDANG LOGIN.
     */
    fun fetchCurrentUserProfile() {
        _uiState.value = ProfileUiState.Loading

        // 1. Dapatkan pengguna yang sedang login dari Firebase Auth
        val currentUser = auth.currentUser
        if (currentUser == null) {
            // Jika tidak ada yang login, langsung kirim state error
            _uiState.value = ProfileUiState.Error("Tidak ada pengguna yang login.")
            return
        }

        // 2. Gunakan UID pengguna tersebut untuk mengambil datanya di Firestore
        val userId = currentUser.uid
        viewModelScope.launch {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val user = document.toObject(User::class.java)
                        _uiState.value = ProfileUiState.Success(user)
                    } else {
                        _uiState.value = ProfileUiState.Error("Data profil tidak ditemukan.")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("ProfileViewModel", "get failed with ", exception)
                    _uiState.value = ProfileUiState.Error(exception.localizedMessage ?: "Error tidak diketahui")
                }
        }
    }

    /**
     * Melakukan proses logout dari Firebase Authentication.
     */
    fun logout() {
        auth.signOut()
        // Kirim sinyal bahwa logout telah selesai
        _logoutState.value = true
        Log.d("ProfileViewModel", "User signed out.")
    }
}