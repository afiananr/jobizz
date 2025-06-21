// Lokasi: app/src/main/java/com/example/loker/ui/SharedComponents.kt
package com.example.loker.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

/**
 * Komponen Bottom Navigation Bar yang bisa digunakan di banyak layar.
 * Sudah diperbarui dengan ikon yang benar dan gaya aktif/non-aktif.
 */
@Composable
fun BottomNavigationBar(navController: NavController) {
    // Mendefinisikan item-item navigasi dengan ikon untuk dua keadaan: terpilih dan tidak
    val items = listOf(
        Screen("dashboard", "Home", Icons.Filled.Home, Icons.Outlined.Home),
        Screen("bookmark", "Bookmark", Icons.Filled.Bookmark, Icons.Outlined.BookmarkBorder),
        Screen("community", "Komunitas", Icons.Filled.Forum, Icons.Outlined.Forum),
        Screen("profile", "Profile", Icons.Filled.Person, Icons.Outlined.PersonOutline)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        items.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            NavigationBarItem(
                icon = {
                    // Pilih ikon berdasarkan status 'isSelected'
                    Icon(
                        imageVector = if (isSelected) screen.filledIcon else screen.outlinedIcon,
                        contentDescription = screen.label
                    )
                },
                label = { Text(screen.label) },
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    }
}

// Data class kecil untuk membantu mengorganisir item navigasi
private data class Screen(
    val route: String,
    val label: String,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector
)