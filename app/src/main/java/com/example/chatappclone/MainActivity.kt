package com.example.chatappclone

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatappclone.ui.LoginScreen
import com.example.chatappclone.ui.SignupScreen
import com.example.chatappclone.ui.SingleChatScreen
import com.example.chatappclone.ui.StatusListScreen
import com.example.chatappclone.ui.StatusScreen
import com.example.chatappclone.ui.theme.ProfileScreen
import com.leboncointest.android.ui.theme.LeBonCoinTheme
import dagger.hilt.android.AndroidEntryPoint

sealed class DestinationScreen(val route: String) {
    object Signup : DestinationScreen("signup")
    object Login : DestinationScreen("login")
    object Profile : DestinationScreen("profile")
    object ChatList : DestinationScreen("chatList")
    object SingleChat : DestinationScreen("singleChat/{chatId}") {
        fun createRoute(id: String) = "singleChat/$id"
    }
    object StatusList : DestinationScreen("statusList")
    object Status : DestinationScreen("status/{userId}") {
        fun createRoute(userId: String?) = "status/$userId"
    }
}


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LeBonCoinTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatAppNavigation()
                }
            }
        }
    }

    @Composable
    fun ChatAppNavigation() {
        val navController = rememberNavController()
        val vm = hiltViewModel<CAViewModel>()

        NotificationMessage(vm = vm)

        NavHost(navController = navController, startDestination = DestinationScreen.Signup.route) {
            composable(DestinationScreen.Signup.route) {
                SignupScreen(navController, vm)
            }
            composable(DestinationScreen.Login.route) {
                LoginScreen(navController, vm)
            }
            composable(DestinationScreen.Profile.route) {
                ProfileScreen(navController, vm)
            }
            composable(DestinationScreen.StatusList.route) {
                StatusListScreen(navController, vm)
            }
            composable(DestinationScreen.ChatList.route) {
                ChatListScreen(navController, vm)
            }
            composable(DestinationScreen.SingleChat.route) {
                val chatId = it.arguments?.getString("chatId")
                chatId?.let {
                    SingleChatScreen(navController = navController, vm = vm, chatId = it)
                }
            }
            composable(DestinationScreen.Status.route) {
                val userId = it.arguments?.getString("userId")
                userId?.let {
                    StatusScreen(navController = navController, vm = vm, userId = it)
                }
            }
        }

    }
}