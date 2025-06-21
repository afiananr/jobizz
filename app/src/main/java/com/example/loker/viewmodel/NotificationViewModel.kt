// Lokasi: app/src/main/java/com/example/loker/viewmodel/NotificationViewModel.kt
package com.example.loker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loker.model.Notification
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class NotificationViewModel : ViewModel() {
    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications = _notifications.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        fetchNotifications()
    }

    private fun fetchNotifications() {
        viewModelScope.launch {
            _isLoading.value = true
            val currentUser = Firebase.auth.currentUser
            if (currentUser != null) {
                try {
                    val snapshot = Firebase.firestore
                        .collection("users")
                        .document(currentUser.uid)
                        .collection("notifications")
                        .orderBy("timestamp", Query.Direction.DESCENDING)
                        .get()
                        .await()

                    _notifications.value = snapshot.toObjects(Notification::class.java)
                } catch (e: Exception) {
                    // Handle error
                }
            }
            _isLoading.value = false
        }
    }
}