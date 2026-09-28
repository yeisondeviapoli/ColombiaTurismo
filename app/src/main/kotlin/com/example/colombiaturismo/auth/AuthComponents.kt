package com.example.colombiaturismo.auth

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.colombiaturismo.R
import com.example.colombiaturismo.ui.theme.ColomViaBlue
import com.example.colombiaturismo.ui.theme.ColomViaError
import com.example.colombiaturismo.ui.theme.ColomViaFieldBorder
import com.example.colombiaturismo.ui.theme.ColomViaHint
import com.example.colombiaturismo.ui.theme.ColomViaNavy
import com.example.colombiaturismo.ui.theme.ColomViaText
import com.example.colombiaturismo.ui.theme.ColomViaTextMuted

/** Contenedor común de las pantallas de cuenta: fondo blanco, scroll y márgenes seguros (teclado incluido). */
@Composable
fun AuthScaffold(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    content: @Composable ColumnScope.() -> Unit
) {
    Scaffold(
        containerColor = Color.White,
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            content = content
        )
    }
}

@Composable
fun ColomViaLogo(
    modifier: Modifier = Modifier,
    logoHeight: Dp = 88.dp,
    textSize: TextUnit = 36.sp
) {
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(R.drawable.ic_colomvia_logo),
            contentDescription = stringResource(R.string.cd_logo),
            modifier = Modifier.height(logoHeight)
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.brand_name),
            color = ColomViaNavy,
            fontSize = textSize,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.5).sp
        )
    }
}

/** Campo con la etiqueta encima, igual que en los mockups de Login y Registro. */
@Composable
fun AuthTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    contentType: ContentType,
    modifier: Modifier = Modifier,
    @StringRes errorRes: Int? = null,
    enabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isPassword: Boolean = false
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(label, color = ColomViaText, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { this.contentType = contentType },
            enabled = enabled,
            singleLine = true,
            placeholder = { Text(placeholder, fontSize = 15.sp) },
            leadingIcon = { Icon(leadingIcon, contentDescription = null, modifier = Modifier.size(22.dp)) },
            trailingIcon = if (isPassword) {
                {
                    // Sin foco de teclado: "Siguiente" debe saltar al próximo campo, no a este botón.
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible },
                        modifier = Modifier.focusProperties { canFocus = false }
                    ) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                            contentDescription = stringResource(
                                if (passwordVisible) R.string.cd_ocultar_contrasena else R.string.cd_mostrar_contrasena
                            )
                        )
                    }
                }
            } else {
                null
            },
            isError = errorRes != null,
            supportingText = errorRes?.let { { Text(stringResource(it)) } },
            visualTransformation = if (isPassword && !passwordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isPassword) KeyboardType.Password else keyboardType,
                imeAction = imeAction,
                autoCorrectEnabled = false
            ),
            keyboardActions = keyboardActions,
            shape = RoundedCornerShape(12.dp),
            textStyle = LocalTextStyle.current.copy(fontSize = 15.sp),
            colors = authTextFieldColors()
        )
    }
}

@Composable
fun authTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = ColomViaText,
    unfocusedTextColor = ColomViaText,
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    disabledContainerColor = Color.White,
    errorContainerColor = Color.White,
    focusedBorderColor = ColomViaBlue,
    unfocusedBorderColor = ColomViaFieldBorder,
    disabledBorderColor = ColomViaFieldBorder,
    errorBorderColor = ColomViaError,
    cursorColor = ColomViaBlue,
    focusedLeadingIconColor = ColomViaTextMuted,
    unfocusedLeadingIconColor = ColomViaHint,
    errorLeadingIconColor = ColomViaHint,
    focusedTrailingIconColor = ColomViaTextMuted,
    unfocusedTrailingIconColor = ColomViaHint,
    errorTrailingIconColor = ColomViaHint,
    focusedPlaceholderColor = ColomViaHint,
    unfocusedPlaceholderColor = ColomViaHint,
    errorPlaceholderColor = ColomViaHint,
    errorSupportingTextColor = ColomViaError
)

@Composable
fun AuthPrimaryButton(
    text: String,
    onClick: () -> Unit,
    containerColor: Color,
    loading: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = !loading,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = Color.White,
            disabledContainerColor = containerColor.copy(alpha = 0.8f),
            disabledContentColor = Color.White
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
    ) {
        if (loading) {
            CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(22.dp))
        } else {
            Text(text, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

/** "¿No tienes cuenta? Regístrate" / "¿Ya tienes cuenta? Inicia sesión". */
@Composable
fun AuthFooterLink(
    question: String,
    action: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(question, color = ColomViaText, fontSize = 14.sp)
        TextButton(onClick = onClick, contentPadding = PaddingValues(horizontal = 6.dp)) {
            Text(action, color = ColomViaBlue, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun AuthErrorText(@StringRes messageRes: Int, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(messageRes),
        color = ColomViaError,
        fontSize = 13.sp,
        modifier = modifier.padding(top = 8.dp)
    )
}
