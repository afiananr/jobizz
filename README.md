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

## 📸 Tampilan Aplikasi

Berikut adalah beberapa tangkapan layar dari aplikasi Jobizz:

### Login & Register

<p align="center">
  <img src="screenshots/Screenshot 2025-07-13 075916.png" alt="Layar Login" width="230"/>
  &nbsp; &nbsp; &nbsp; &nbsp; <img src="screenshots/Screenshot 2025-07-13 075944.png" alt="Layar Register" width="230"/>
</p>

### Beranda & Pencarian

<p align="center">
  <img src="screenshots/Screenshot 2025-07-13 080039.png" alt="Dashboard Utama" width="230"/>
  &nbsp; &nbsp; &nbsp; &nbsp; <img src="screenshots/Screenshot 2025-07-13 080104.png" alt="Dashboard Scroll" width="230"/>
</p>
<p align="center">
  <img src="screenshots/Screenshot 2025-07-13 080504.png" alt="Pencarian Hasil Kosong" width="230"/>
  &nbsp; &nbsp; &nbsp; &nbsp; <img src="screenshots/Screenshot 2025-07-13 080531.png" alt="Pencarian dengan Hasil" width="230"/>
</p>

### Detail Pekerjaan dan Notifikasi

<p align="center">
  <img src="screenshots/Screenshot 2025-07-13 080334.png" alt="Detail Pekerjaan 1" width="230"/>
  &nbsp; &nbsp; &nbsp; &nbsp; <img src="screenshots/Screenshot 2025-07-13 080348.png" alt="Detail Pekerjaan 2" width="230"/>
  &nbsp; &nbsp; &nbsp; &nbsp; <img src="screenshots/Screenshot 2025-07-13 080407.png" alt="Layar Notifikasi" width="230"/>
</p>

### Bookmark

<p align="center">
  <img src="screenshots/Screenshot 2025-07-13 080318.png" alt="Daftar Bookmark" width="230"/>
  &nbsp; &nbsp; &nbsp; &nbsp; <img src="screenshots/Screenshot 2025-07-13 080429.png" alt="Bookmark Kosong" width="230"/>
</p>
