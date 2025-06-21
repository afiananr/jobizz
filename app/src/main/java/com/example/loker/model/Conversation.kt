// Lokasi: app/src/main/java/com/example/loker/model/Conversation.kt
package com.example.loker.model

import com.google.firebase.Timestamp

data class Conversation(
    val id: String = "",
    val participantIds: List<String> = emptyList(),
    val participantNames: Map<String, String> = emptyMap(),
    val participantPhotos: Map<String, String> = emptyMap(),
    val lastMessage: String = "",
    val lastMessageTimestamp: Timestamp? = null
)