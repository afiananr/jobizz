// Lokasi: app/src/main/java/com/example/loker/ui/theme/Type.kt
package com.example.loker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Mengatur gaya teks default untuk Material 3
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Anda bisa menambahkan override untuk gaya lain seperti titleLarge,
       bodySmall, dll. di sini jika diperlukan.
    */
)