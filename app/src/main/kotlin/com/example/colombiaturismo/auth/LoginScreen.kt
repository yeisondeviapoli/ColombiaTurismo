package com.example.colombiaturismo.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.colombiaturismo.R
import com.example.colombiaturismo.ui.theme.ColomViaBlue
import com.example.colombiaturismo.ui.theme.ColomViaNavy

@Composable
fun LoginRoute(
    viewModel: LoginViewModel,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    LoginScreen(
        uiState = viewModel.uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLoginClick = viewModel::login,
        onForgotPasswordClick = onForgotPasswordClick,
        onRegisterClick = onRegisterClick,
        onInfoMessageShown = viewModel::onInfoMessageShown
    )
}

/** Pantalla 1 del mockup: Login. */
@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onInfoMessageShown: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val infoMessage = uiState.infoMessage?.let { stringResource(it) }

    LaunchedEffect(infoMessage) {
        if (infoMessage != null) {
            snackbarHostState.showSnackbar(infoMessage)
            onInfoMessageShown()
        }
    }

    val submit = {
        focusManager.clearFocus()
        onLoginClick()
    }

    AuthScaffold(snackbarHostState = snackbarHostState) {
        Spacer(Modifier.height(56.dp))
        ColomViaLogo()
        Spacer(Modifier.height(56.dp))

        AuthTextField(
            label = stringResource(R.string.label_correo),
            value = uiState.email,
            onValueChange = onEmailChange,
            placeholder = stringResource(R.string.hint_correo),
            leadingIcon = Icons.Outlined.Email,
            contentType = ContentType.Username,
            errorRes = uiState.emailError,
            enabled = !uiState.isLoading,
            keyboardType = KeyboardType.Email
        )
        Spacer(Modifier.height(18.dp))
        AuthTextField(
            label = stringResource(R.string.label_contrasena),
            value = uiState.password,
            onValueChange = onPasswordChange,
            placeholder = stringResource(R.string.hint_contrasena),
            leadingIcon = Icons.Outlined.Lock,
            contentType = ContentType.Password,
            errorRes = uiState.passwordError,
            enabled = !uiState.isLoading,
            imeAction = ImeAction.Done,
            keyboardActions = KeyboardActions(onDone = { submit() }),
            isPassword = true
        )
        uiState.generalError?.let { AuthErrorText(it) }

        TextButton(
            onClick = onForgotPasswordClick,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
        ) {
            Text(
                stringResource(R.string.login_olvidaste),
                color = ColomViaBlue,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(Modifier.height(28.dp))
        AuthPrimaryButton(
            text = stringResource(R.string.login_boton),
            onClick = submit,
            containerColor = ColomViaNavy,
            loading = uiState.isLoading
        )
        Spacer(Modifier.height(36.dp))
        AuthFooterLink(
            question = stringResource(R.string.login_sin_cuenta),
            action = stringResource(R.string.login_registrate),
            onClick = onRegisterClick
        )
        Spacer(Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun LoginScreenPreview() {
    LoginScreen(
        uiState = LoginUiState(),
        onEmailChange = {},
        onPasswordChange = {},
        onLoginClick = {},
        onForgotPasswordClick = {},
        onRegisterClick = {},
        onInfoMessageShown = {}
    )
}
