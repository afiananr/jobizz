// Lokasi: app/src/main/java/com/example/loker/model/ChatMessage.kt
package com.example.loker.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class ChatMessage(
    val id: String = "",
    val senderId: String = "",
    val text: String = "",
    @ServerTimestamp
    val timestamp: Timestamp? = null
)