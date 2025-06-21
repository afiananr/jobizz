
package com.example.loker.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // Fungsi ini akan terpanggil saat aplikasi sedang berjalan (foreground)
        // dan menerima notifikasi.

        Log.d("FCM", "From: ${remoteMessage.from}")

        // Cek jika pesan berisi data payload
        remoteMessage.data.isNotEmpty().let {
            Log.d("FCM", "Message data payload: " + remoteMessage.data)
        }

        // Cek jika pesan berisi notification payload
        remoteMessage.notification?.let {
            Log.d("FCM", "Message Notification Body: ${it.body}")
            // Di sini Anda bisa membuat notifikasi custom jika diperlukan
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Fungsi ini terpanggil saat token baru dibuat.
        // Kita perlu menyimpan token ini ke Firestore agar server tahu
        // alamat HP ini.
        Log.d("FCM", "Refreshed token: $token")
        // Kirim token ini ke server atau simpan di Firestore
        // sendRegistrationToServer(token)
    }
}