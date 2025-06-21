// Lokasi File: app/src/main/java/com/example/loker/MainActivity.kt
package com.example.loker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.loker.ui.BookmarkScreen
import com.example.loker.ui.CommunityScreen
import com.example.loker.ui.CreatePostScreen
import com.example.loker.ui.JobDashboardScreen
import com.example.loker.ui.JobDetailScreen
import com.example.loker.ui.LoginScreen
import com.example.loker.ui.MessagesScreen
import com.example.loker.ui.PostDetailScreen
import com.example.loker.ui.ProfileScreen
import com.example.loker.ui.RegisterScreen
import com.example.loker.ui.theme.LokerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            LokerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    // NavHost adalah "panggung" yang menampilkan layar yang sesuai.
                    // startDestination adalah rute/grup pertama yang ditampilkan.
                    NavHost(navController = navController, startDestination = "auth_graph") {

                        // Graf Otentikasi
                        navigation(startDestination = "login", route = "auth_graph") {
                            composable("login") { LoginScreen(navController = navController) }
                            composable("register") { RegisterScreen(navController = navController) }
                        }

                        // Graf Utama
                        composable("dashboard") { JobDashboardScreen(navController = navController) }
                        composable("bookmark") { BookmarkScreen(navController = navController) }
                        composable("community") { CommunityScreen(navController = navController) }
                        composable("create_post") {
                            CreatePostScreen(navController = navController)
                        }
                        composable("profile") { ProfileScreen(navController = navController) }
                        composable("messages") { MessagesScreen(navController = navController) }

                        // Rute untuk Detail Lowongan
                        composable(
                            route = "detail/{jobId}",
                            arguments = listOf(navArgument("jobId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val jobId = backStackEntry.arguments?.getString("jobId")
                            JobDetailScreen(navController = navController, jobId = jobId)
                        }

                        // [BARU] Rute untuk Detail Postingan Komunitas
                        composable(
                            route = "post_detail/{postId}",
                            arguments = listOf(navArgument("postId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val postId = backStackEntry.arguments?.getString("postId")
                            PostDetailScreen(navController = navController, postId = postId)
                        }}
                    }
                }
            }
        }
    }