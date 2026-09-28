package com.example.colombiaturismo.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.colombiaturismo.R
import com.example.colombiaturismo.auth.AuthTextField
import com.example.colombiaturismo.data.Usuario
import com.example.colombiaturismo.ui.theme.ColomViaBlue
import com.example.colombiaturismo.ui.theme.ColomViaBlueSoft
import com.example.colombiaturismo.ui.theme.ColomViaError
import com.example.colombiaturismo.ui.theme.ColomViaHint
import com.example.colombiaturismo.ui.theme.ColomViaNavy
import com.example.colombiaturismo.ui.theme.ColomViaSurface
import com.example.colombiaturismo.ui.theme.ColomViaText
import com.example.colombiaturismo.ui.theme.ColomViaTextMuted
import java.text.DateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProfileRoute(
    onLogoutClick: () -> Unit,
    viewModel: ProfileViewModel = viewModel(factory = ProfileViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val message = viewModel.message?.let { stringResource(it) }

    LaunchedEffect(message) {
        if (message != null) {
            snackbarHostState.showSnackbar(message)
            viewModel.onMessageShown()
        }
    }

    ProfileScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onEditNameClick = viewModel::onEditNameClick,
        onChangePasswordClick = viewModel::onChangePasswordClick,
        onLogoutClick = onLogoutClick
    )

    viewModel.editNameState?.let { state ->
        EditNameDialog(
            state = state,
            onNameChange = viewModel::onNameChange,
            onSave = viewModel::saveName,
            onDismiss = viewModel::onEditNameDismiss
        )
    }
    viewModel.changePasswordState?.let { state ->
        ChangePasswordDialog(
            state = state,
            onCurrentPasswordChange = viewModel::onCurrentPasswordChange,
            onNewPasswordChange = viewModel::onNewPasswordChange,
            onConfirmationChange = viewModel::onConfirmationChange,
            onSave = viewModel::savePassword,
            onDismiss = viewModel::onChangePasswordDismiss
        )
    }
}

/** Pestaña "Perfil" de la barra inferior. Sigue el estilo de la pantalla Guardados del mockup. */
@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    snackbarHostState: SnackbarHostState,
    onEditNameClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val usuario = uiState.usuario

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ColomViaBlue)
                    .padding(vertical = 18.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(R.string.perfil_titulo),
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(28.dp))
                Avatar(name = usuario?.nombre.orEmpty())
                Spacer(Modifier.height(14.dp))
                Text(
                    usuario?.nombre.orEmpty(),
                    color = ColomViaNavy,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(usuario?.correo.orEmpty(), color = ColomViaTextMuted, fontSize = 14.sp)
                if (usuario != null) {
                    val memberSince = remember(usuario.fechaRegistro) { formatDate(usuario.fechaRegistro) }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        stringResource(R.string.perfil_miembro_desde, memberSince),
                        color = ColomViaHint,
                        fontSize = 12.sp
                    )
                }

                Spacer(Modifier.height(28.dp))
                SavedPlacesCard(count = uiState.savedPlacesCount)

                Spacer(Modifier.height(24.dp))
                Text(
                    stringResource(R.string.perfil_seccion_cuenta),
                    color = ColomViaTextMuted,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, bottom = 8.dp)
                )
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(ColomViaSurface)
                ) {
                    ProfileOption(Icons.Outlined.Edit, stringResource(R.string.perfil_editar_nombre), onEditNameClick)
                    HorizontalDivider(color = Color(0xFFE9EBEF), modifier = Modifier.padding(horizontal = 16.dp))
                    ProfileOption(Icons.Outlined.Lock, stringResource(R.string.perfil_cambiar_contrasena), onChangePasswordClick)
                }

                Spacer(Modifier.height(28.dp))
                OutlinedButton(
                    onClick = onLogoutClick,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, ColomViaError.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Icon(Icons.AutoMirrored.Outlined.Logout, contentDescription = null, tint = ColomViaError)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        stringResource(R.string.cerrar_sesion),
                        color = ColomViaError,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(Modifier.height(24.dp))
            }
        }

        SnackbarHost(snackbarHostState, Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun Avatar(name: String) {
    val initials = initialsOf(name)
    Box(
        modifier = Modifier
            .size(96.dp)
            .clip(CircleShape)
            .background(ColomViaBlueSoft),
        contentAlignment = Alignment.Center
    ) {
        if (initials.isEmpty()) {
            Icon(Icons.Outlined.Person, contentDescription = null, tint = ColomViaBlue, modifier = Modifier.size(48.dp))
        } else {
            Text(initials, color = ColomViaBlue, fontSize = 34.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SavedPlacesCard(count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(ColomViaSurface)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(ColomViaBlueSoft),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.Bookmark, contentDescription = null, tint = ColomViaBlue)
        }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(
                stringResource(R.string.perfil_lugares_guardados),
                color = ColomViaText,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(stringResource(R.string.perfil_lugares_guardados_desc), color = ColomViaTextMuted, fontSize = 12.sp)
        }
        Text(count.toString(), color = ColomViaNavy, fontSize = 22.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ProfileOption(icon: ImageVector, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = ColomViaNavy, modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(14.dp))
        Text(text, color = ColomViaText, fontSize = 15.sp, modifier = Modifier.weight(1f))
        Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, contentDescription = null, tint = ColomViaHint)
    }
}

@Composable
private fun EditNameDialog(
    state: EditNameState,
    onNameChange: (String) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {
    ProfileFormDialog(
        title = stringResource(R.string.perfil_editar_nombre),
        isSaving = state.isSaving,
        onSave = onSave,
        onDismiss = onDismiss
    ) {
        AuthTextField(
            label = stringResource(R.string.label_nombre),
            value = state.name,
            onValueChange = onNameChange,
            placeholder = stringResource(R.string.hint_nombre),
            leadingIcon = Icons.Outlined.Person,
            contentType = ContentType.PersonFullName,
            errorRes = state.error,
            enabled = !state.isSaving,
            imeAction = ImeAction.Done,
            keyboardActions = KeyboardActions(onDone = { onSave() })
        )
    }
}

@Composable
private fun ChangePasswordDialog(
    state: ChangePasswordState,
    onCurrentPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmationChange: (String) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {
    val hint = stringResource(R.string.hint_contrasena)
    ProfileFormDialog(
        title = stringResource(R.string.perfil_cambiar_contrasena),
        isSaving = state.isSaving,
        onSave = onSave,
        onDismiss = onDismiss
    ) {
        AuthTextField(
            label = stringResource(R.string.label_contrasena_actual),
            value = state.currentPassword,
            onValueChange = onCurrentPasswordChange,
            placeholder = hint,
            leadingIcon = Icons.Outlined.Lock,
            contentType = ContentType.Password,
            errorRes = state.currentPasswordError,
            enabled = !state.isSaving,
            isPassword = true
        )
        Spacer(Modifier.height(12.dp))
        AuthTextField(
            label = stringResource(R.string.label_nueva_contrasena),
            value = state.newPassword,
            onValueChange = onNewPasswordChange,
            placeholder = hint,
            leadingIcon = Icons.Outlined.Lock,
            contentType = ContentType.NewPassword,
            errorRes = state.newPasswordError,
            enabled = !state.isSaving,
            isPassword = true
        )
        Spacer(Modifier.height(12.dp))
        AuthTextField(
            label = stringResource(R.string.label_confirmar_contrasena),
            value = state.confirmation,
            onValueChange = onConfirmationChange,
            placeholder = hint,
            leadingIcon = Icons.Outlined.Lock,
            contentType = ContentType.NewPassword,
            errorRes = state.confirmationError,
            enabled = !state.isSaving,
            imeAction = ImeAction.Done,
            keyboardActions = KeyboardActions(onDone = { onSave() }),
            isPassword = true
        )
    }
}

@Composable
private fun ProfileFormDialog(
    title: String,
    isSaving: Boolean,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, color = ColomViaNavy, fontWeight = FontWeight.Bold) },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState())) { content() }
        },
        confirmButton = {
            TextButton(onClick = onSave, enabled = !isSaving) {
                if (isSaving) {
                    CircularProgressIndicator(color = ColomViaBlue, strokeWidth = 2.dp, modifier = Modifier.size(18.dp))
                } else {
                    Text(stringResource(R.string.accion_guardar), color = ColomViaBlue, fontWeight = FontWeight.SemiBold)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isSaving) {
                Text(stringResource(R.string.accion_cancelar), color = ColomViaTextMuted)
            }
        },
        containerColor = Color.White
    )
}

private val WHITESPACE = Regex("\\s+")

private fun initialsOf(name: String): String =
    name.trim()
        .split(WHITESPACE)
        .filter { it.isNotEmpty() }
        .take(2)
        .joinToString("") { it.first().uppercase() }

private fun formatDate(epochMillis: Long): String =
    DateFormat.getDateInstance(DateFormat.LONG, Locale.forLanguageTag("es-CO")).format(Date(epochMillis))

@Preview(showBackground = true, widthDp = 390, heightDp = 780)
@Composable
private fun ProfileScreenPreview() {
    ProfileScreen(
        uiState = ProfileUiState(
            usuario = Usuario("1", "Sebastián Mira", "sebastian@correo.com", "", System.currentTimeMillis()),
            savedPlacesCount = 3
        ),
        snackbarHostState = remember { SnackbarHostState() },
        onEditNameClick = {},
        onChangePasswordClick = {},
        onLogoutClick = {}
    )
}
