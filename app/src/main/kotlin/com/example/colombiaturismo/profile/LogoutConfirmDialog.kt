package com.example.colombiaturismo.profile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.colombiaturismo.R
import com.example.colombiaturismo.ui.theme.ColomViaError
import com.example.colombiaturismo.ui.theme.ColomViaNavy
import com.example.colombiaturismo.ui.theme.ColomViaTextMuted

/** Confirmación antes de cerrar sesión (desde el menú lateral o desde Perfil). */
@Composable
fun LogoutConfirmDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.AutoMirrored.Outlined.Logout, contentDescription = null, tint = ColomViaError) },
        title = { Text(stringResource(R.string.cerrar_sesion), color = ColomViaNavy) },
        text = { Text(stringResource(R.string.cerrar_sesion_mensaje), color = ColomViaTextMuted) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.cerrar_sesion), color = ColomViaError, fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.accion_cancelar), color = ColomViaTextMuted)
            }
        },
        containerColor = Color.White
    )
}
