// Lokasi: app/src/main/java/com/example/loker/model/PostComment.kt
package com.example.loker.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class PostComment(
    val id: String = "",
    val authorName: String = "",
    val authorPhotoUrl: String = "",
    val authorId: String = "",
    val content: String = "",
    @ServerTimestamp
    val timestamp: Timestamp? = null
)