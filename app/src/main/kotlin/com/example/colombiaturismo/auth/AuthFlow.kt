package com.example.colombiaturismo.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel

private enum class AuthDestination { Login, Register, ForgotPassword }

/** Navegación entre Login, Registro y Recuperar contraseña mientras no hay sesión. */
@Composable
fun AuthFlow() {
    var destination by rememberSaveable { mutableStateOf(AuthDestination.Login) }
    val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory)
    val goToLogin = { destination = AuthDestination.Login }

    AnimatedContent(
        targetState = destination,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "auth"
    ) { current ->
        when (current) {
            AuthDestination.Login -> LoginRoute(
                viewModel = loginViewModel,
                onRegisterClick = { destination = AuthDestination.Register },
                onForgotPasswordClick = { destination = AuthDestination.ForgotPassword }
            )
            AuthDestination.Register -> RegisterRoute(onBack = goToLogin)
            AuthDestination.ForgotPassword -> ForgotPasswordRoute(
                onBack = goToLogin,
                onPasswordReset = { email ->
                    loginViewModel.onPasswordReset(email)
                    goToLogin()
                }
            )
        }
    }
}
