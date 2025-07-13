# 📱 Jobizz - Aplikasi Pencarian Lowongan Kerja

Jobizz adalah aplikasi android yang dirancang untuk membantu pengguna mencari dan menemukan lowongan pekerjaan. Aplikasi ini dikembangkan menggunakan Kotlin dengan Jetpack Compose untuk antarmuka pengguna, dan menggunakan integrasikan layanan Firebase untuk autentikasi dan databasenya.

## ✨ Fitur Utama

* Pencarian Pekerjaan
* List Daftar Pekerjaan
* Bookmark Pekerjaan
* Autentikasi Pengguna
* Notifikasi
* Fitur Komunitas/Forum
* Manajemen Profil

## 🛠️ Teknologi yang Digunakan

* Kotlin : Bahasa pemrograman utama.
* Jetpack Compose : Untuk membangun antar muka penggyna.
* Android Architecture Components : `ViewModel`, `Lifecycle`, `Navigation Compose`.
* Firebase : Menyimpan data

## 🔧 Struktur Proyek

Proyek ini terbagi menjadi beberapa modul utama:

* `app/`: Berisi kode sumber aplikasi Android (Kotlin/Compose).
    * `ui/`: Komponen UI Compose dan layar-layar aplikasi.
    * `viewmodel/`: ViewModel untuk mengelola state UI dan berinteraksi dengan data.
    * `model/`: Kelas-kelas model data (Job, User, CommunityPost, dll.).
    * `service/`: Layanan seperti Firebase Messaging Service.
    * `data/`: (Mungkin akan ada di masa depan untuk repository data).
* `functions/`: Berisi kode sumber Firebase Functions (TypeScript).

## Tampilan Aplikasi
