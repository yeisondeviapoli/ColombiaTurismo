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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.colombiaturismo.R
import com.example.colombiaturismo.ui.theme.ColomViaBlue
import com.example.colombiaturismo.ui.theme.ColomViaText

@Composable
fun RegisterRoute(
    onBack: () -> Unit,
    viewModel: RegisterViewModel = viewModel(factory = RegisterViewModel.Factory)
) {
    BackHandler(onBack = onBack)

    RegisterScreen(
        uiState = viewModel.uiState,
        onNameChange = viewModel::onNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmationChange = viewModel::onConfirmationChange,
        onRegisterClick = viewModel::register,
        onBack = onBack
    )
}

/** Pantalla 2 del mockup: Registro. */
@Composable
fun RegisterScreen(
    uiState: RegisterUiState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmationChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onBack: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val submit = {
        focusManager.clearFocus()
        onRegisterClick()
    }
    val enabled = !uiState.isLoading

    AuthScaffold {
        Spacer(Modifier.height(8.dp))
        IconButton(onClick = onBack, modifier = Modifier.offset(x = (-12).dp)) {
            Icon(Icons.AutoMirrored.Outlined.ArrowBack, stringResource(R.string.cd_volver), tint = ColomViaText)
        }
        Spacer(Modifier.height(8.dp))
        ColomViaLogo()
        Spacer(Modifier.height(36.dp))

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
            label = stringResource(R.string.label_correo),
            value = uiState.email,
            onValueChange = onEmailChange,
            placeholder = stringResource(R.string.hint_correo),
            leadingIcon = Icons.Outlined.Email,
            contentType = ContentType.NewUsername,
            errorRes = uiState.emailError,
            enabled = enabled,
            keyboardType = KeyboardType.Email
        )
        Spacer(Modifier.height(14.dp))
        AuthTextField(
            label = stringResource(R.string.label_contrasena),
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

        Spacer(Modifier.height(32.dp))
        AuthPrimaryButton(
            text = stringResource(R.string.registro_boton),
            onClick = submit,
            containerColor = ColomViaBlue,
            loading = uiState.isLoading
        )
        Spacer(Modifier.height(28.dp))
        AuthFooterLink(
            question = stringResource(R.string.registro_con_cuenta),
            action = stringResource(R.string.registro_inicia_sesion),
            onClick = onBack
        )
        Spacer(Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun RegisterScreenPreview() {
    RegisterScreen(
        uiState = RegisterUiState(),
        onNameChange = {},
        onEmailChange = {},
        onPasswordChange = {},
        onConfirmationChange = {},
        onRegisterClick = {},
        onBack = {}
    )
}
