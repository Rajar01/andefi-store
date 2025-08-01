package store.andefi.ui.account.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import store.andefi.ui.account.viewmodel.ResetPasswordFormViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ResetPasswordFormScreen(
    resetPasswordFormViewModel: ResetPasswordFormViewModel = hiltViewModel(),
    navigateToSignInForm: () -> Unit,
    navigateToResetPasswordSuccess: () -> Unit,
    token: String
) {
    val resetPasswordFormUiState by resetPasswordFormViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Collect Snackbar events from the ViewModel's Flow
    LaunchedEffect(key1 = Unit) {
        resetPasswordFormViewModel.snackbarEvent.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    if (resetPasswordFormUiState.isResetPasswordSuccessful) {
        navigateToResetPasswordSuccess()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets.safeContent
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding() + 16.dp,
                    bottom = paddingValues.calculateBottomPadding() + 16.dp,
                    start = 16.dp,
                    end = 16.dp,
                )
                .fillMaxWidth()
                .verticalScroll(rememberScrollState(0))
                .imeNestedScroll(),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Kata sandi baru",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Kata sandi baru Anda harus berbeda dari kata sandi yang Anda gunakan sebelumnya.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = resetPasswordFormUiState.password,
                    label = { Text("Kata sandi") },
                    placeholder = { Text("Masukkan setidaknya 8 karakter") },
                    onValueChange = resetPasswordFormViewModel::onPasswordValueChanged,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation(),
                    enabled = !resetPasswordFormUiState.isLoading,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = resetPasswordFormUiState.passwordConfirmation,
                    label = { Text("Konfirmasi kata sandi") },
                    placeholder = { Text("Masukkan setidaknya 8 karakter") },
                    onValueChange = resetPasswordFormViewModel::onPasswordConfirmationValueChanged,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation(),
                    enabled = !resetPasswordFormUiState.isLoading,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { resetPasswordFormViewModel.resetPassword(token) },
                    shape = MaterialTheme.shapes.medium,
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Atur ulang kata sandi") }
                Text(
                    "Ingat kata sandi Anda? Masuk",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.clickable { navigateToSignInForm() }
                )
            }
        }
    }
}