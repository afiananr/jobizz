// Lokasi File: app/src/main/java/com/example/lokerapp/model/Job.kt
package com.example.loker.model

/**
 * Data class untuk lowongan pekerjaan.
 * PENTING: Setiap properti diberi nilai default (= "") agar kompatibel dengan Firestore.
 */
data class Job(
    var id: String = "",
    val title: String = "",
    val companyName: String = "",
    val companyLogoUrl: String = "",
    val salaryRange: String = "",
    val jobType: String = "",
    val workModel: String = ""
) {
    companion object {
        val JOB_CATEGORIES = listOf("All", "Accountant", "Programmer", "Writer", "Designer")
    }
}