// Lokasi: app/src/main/java/com/example/loker/viewmodel/RegisterViewModel.kt
package com.example.loker.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loker.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

sealed interface RegisterUiState {
    object Idle : RegisterUiState
    object Loading : RegisterUiState
    object Success : RegisterUiState
    data class Error(val message: String) : RegisterUiState
}

class RegisterViewModel : ViewModel() {

    private val auth = Firebase.auth
    private val firestore = Firebase.firestore

    // State untuk setiap input field di UI
    private val _name = mutableStateOf("")
    val name: State<String> = _name

    private val _address = mutableStateOf("")
    val address: State<String> = _address

    private val _phone = mutableStateOf("")
    val phone: State<String> = _phone

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    // State untuk proses registrasi (Loading, Success, Error)
    private val _registerUiState = mutableStateOf<RegisterUiState>(RegisterUiState.Idle)
    val registerUiState: State<RegisterUiState> = _registerUiState

    // Fungsi untuk di-trigger oleh UI saat input berubah
    fun onNameChange(newValue: String) { _name.value = newValue }
    fun onAddressChange(newValue: String) { _address.value = newValue }
    fun onPhoneChange(newValue: String) { _phone.value = newValue }
    fun onEmailChange(newValue: String) { _email.value = newValue }
    fun onPasswordChange(newValue: String) { _password.value = newValue }

    fun registerUser() {
        if (name.value.isBlank() || email.value.isBlank() || password.value.isBlank()) {
            _registerUiState.value = RegisterUiState.Error("Nama, Email, dan Password tidak boleh kosong.")
            return
        }

        _registerUiState.value = RegisterUiState.Loading
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email.value, password.value)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // 1. Registrasi Auth berhasil, dapatkan user ID (uid)
                        val firebaseUser = auth.currentUser
                        val uid = firebaseUser?.uid
                        if (uid != null) {
                            // 2. Siapkan data profil untuk disimpan di Firestore
                            val userProfile = User(
                                name = name.value,
                                email = email.value,
                                address = address.value,
                                phone = phone.value,
                                // Untuk sementara, foto dan bio kita hardcode
                                photoUrl = "https://i.pravatar.cc/150?u=${uid}",
                                bio = "Pengguna baru Loker App!"
                                // No. telepon bisa ditambahkan jika field-nya ada di data class User
                            )

                            // 3. Simpan data profil ke Firestore dengan ID yang sama dengan Auth
                            firestore.collection("users").document(uid).set(userProfile)
                                .addOnSuccessListener {
                                    Log.d("RegisterViewModel", "User profile created in Firestore.")
                                    _registerUiState.value = RegisterUiState.Success
                                }
                                .addOnFailureListener { e ->
                                    Log.w("RegisterViewModel", "Error adding document", e)
                                    _registerUiState.value = RegisterUiState.Error(e.localizedMessage ?: "Gagal menyimpan profil.")
                                }
                        }
                    } else {
                        // Jika registrasi Auth gagal (misal: email sudah terdaftar)
                        Log.w("RegisterViewModel", "createUserWithEmail:failure", task.exception)
                        _registerUiState.value = RegisterUiState.Error(task.exception?.localizedMessage ?: "Registrasi gagal.")
                    }
                }
        }
    }
}