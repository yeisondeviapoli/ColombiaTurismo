package com.example.colombiaturismo.auth

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.colombiaturismo.R
import com.example.colombiaturismo.ui.theme.ColomViaBlue
import com.example.colombiaturismo.ui.theme.ColomViaNavy
import com.example.colombiaturismo.ui.theme.ColomViaText
import com.example.colombiaturismo.ui.theme.ColomViaTextMuted

@Composable
fun ForgotPasswordRoute(
    onBack: () -> Unit,
    onPasswordReset: (email: String) -> Unit,
    viewModel: ForgotPasswordViewModel = viewModel(factory = ForgotPasswordViewModel.Factory)
) {
    val uiState = viewModel.uiState
    val back = {
        viewModel.onResetHandled()
        onBack()
    }

    BackHandler(onBack = back)

    LaunchedEffect(uiState.resetEmail) {
        uiState.resetEmail?.let { email ->
            onPasswordReset(email)
            viewModel.onResetHandled()
        }
    }

    ForgotPasswordScreen(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onNameChange = viewModel::onNameChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmationChange = viewModel::onConfirmationChange,
        onResetClick = viewModel::resetPassword,
        onBack = back
    )
}

/** Se abre desde "¿Olvidaste tu contraseña?" en el Login. */
@Composable
fun ForgotPasswordScreen(
    uiState: ForgotPasswordUiState,
    onEmailChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmationChange: (String) -> Unit,
    onResetClick: () -> Unit,
    onBack: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val submit = {
        focusManager.clearFocus()
        onResetClick()
    }
    val enabled = !uiState.isLoading

    AuthScaffold {
        Spacer(Modifier.height(8.dp))
        IconButton(onClick = onBack, modifier = Modifier.offset(x = (-12).dp)) {
            Icon(Icons.AutoMirrored.Outlined.ArrowBack, stringResource(R.string.cd_volver), tint = ColomViaText)
        }
        Spacer(Modifier.height(8.dp))
        ColomViaLogo(logoHeight = 72.dp, textSize = 30.sp)
        Spacer(Modifier.height(28.dp))

        Text(
            stringResource(R.string.recuperar_titulo),
            color = ColomViaNavy,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(6.dp))
        Text(
            stringResource(R.string.recuperar_descripcion),
            color = ColomViaTextMuted,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
        Spacer(Modifier.height(24.dp))

        AuthTextField(
            label = stringResource(R.string.label_correo),
            value = uiState.email,
            onValueChange = onEmailChange,
            placeholder = stringResource(R.string.hint_correo),
            leadingIcon = Icons.Outlined.Email,
            contentType = ContentType.Username,
            errorRes = uiState.emailError,
            enabled = enabled,
            keyboardType = KeyboardType.Email
        )
        Spacer(Modifier.height(14.dp))
        AuthTextField(
            label = stringResource(R.string.label_nombre),
            value = uiState.name,
            onValueChange = onNameChange,
            placeholder = stringResource(R.string.hint_nombre),
            leadingIcon = Icons.Outlined.Person,
            contentType = ContentType.PersonFullName,
            errorRes = uiState.nameError,
            enabled = enabled
        )
        Spacer(Modifier.height(14.dp))
        AuthTextField(
            label = stringResource(R.string.label_nueva_contrasena),
            value = uiState.password,
            onValueChange = onPasswordChange,
            placeholder = stringResource(R.string.hint_contrasena),
            leadingIcon = Icons.Outlined.Lock,
            contentType = ContentType.NewPassword,
            errorRes = uiState.passwordError,
            enabled = enabled,
            isPassword = true
        )
        Spacer(Modifier.height(14.dp))
        AuthTextField(
            label = stringResource(R.string.label_confirmar_contrasena),
            value = uiState.confirmation,
            onValueChange = onConfirmationChange,
            placeholder = stringResource(R.string.hint_contrasena),
            leadingIcon = Icons.Outlined.Lock,
            contentType = ContentType.NewPassword,
            errorRes = uiState.confirmationError,
            enabled = enabled,
            imeAction = ImeAction.Done,
            keyboardActions = KeyboardActions(onDone = { submit() }),
            isPassword = true
        )
        uiState.generalError?.let { AuthErrorText(it) }

        Spacer(Modifier.height(32.dp))
        AuthPrimaryButton(
            text = stringResource(R.string.recuperar_boton),
            onClick = submit,
            containerColor = ColomViaBlue,
            loading = uiState.isLoading
        )
        Spacer(Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun ForgotPasswordScreenPreview() {
    ForgotPasswordScreen(
        uiState = ForgotPasswordUiState(),
        onEmailChange = {},
        onNameChange = {},
        onPasswordChange = {},
        onConfirmationChange = {},
        onResetClick = {},
        onBack = {}
    )
}
