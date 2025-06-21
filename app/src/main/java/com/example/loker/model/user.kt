// Lokasi File: app/src/main/java/com/example/loker/model/User.kt
package com.example.loker.model // PASTIKAN BARIS INI ADA DAN BENAR

/**
 * Data class untuk merepresentasikan data pengguna.
 * Diberi nilai default agar kompatibel dengan Firestore.
 */
data class User(
    val name: String = "",
    val email: String = "",
    val address: String = "",
    val phone: String = "",
    val photoUrl: String = "",
    val bio: String = "",
    val bookmarkedJobIds: List<String> = emptyList()
)