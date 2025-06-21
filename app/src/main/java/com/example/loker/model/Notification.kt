// Lokasi: app/src/main/java/com/example/loker/model/Notification.kt
package com.example.loker.model

import com.google.firebase.Timestamp

data class Notification(
    val id: String = "",
    val userId: String = "",
    val message: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val isRead: Boolean = false
)