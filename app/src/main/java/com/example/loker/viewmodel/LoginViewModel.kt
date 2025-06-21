// Lokasi: app/src/main/java/com/example/loker/viewmodel/LoginViewModel.kt
package com.example.loker.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface LoginUiState {
    object Idle : LoginUiState
    object Loading : LoginUiState
    object Success : LoginUiState
    data class Error(val message: String) : LoginUiState
}

class LoginViewModel : ViewModel() {


    private val auth = Firebase.auth

    // State untuk input field email dan password
    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    // State untuk proses login
    private val _loginUiState = mutableStateOf<LoginUiState>(LoginUiState.Idle)
    val loginUiState: State<LoginUiState> = _loginUiState

    // Fungsi untuk di-trigger dari UI
    fun onEmailChange(newValue: String) { _email.value = newValue }
    fun onPasswordChange(newValue: String) { _password.value = newValue }

    fun loginUser() {
        if (email.value.isBlank() || password.value.isBlank()) {
            _loginUiState.value = LoginUiState.Error("Email dan Password tidak boleh kosong.")
            return
        }

        _loginUiState.value = LoginUiState.Loading
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email.value, password.value)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Jika login berhasil
                        Log.d("LoginViewModel", "signInWithEmail:success")
                        _loginUiState.value = LoginUiState.Success
                    } else {
                        // Jika login gagal
                        Log.w("LoginViewModel", "signInWithEmail:failure", task.exception)
                        _loginUiState.value = LoginUiState.Error(task.exception?.localizedMessage ?: "Login Gagal.")
                    }
                }
        }
    }
}