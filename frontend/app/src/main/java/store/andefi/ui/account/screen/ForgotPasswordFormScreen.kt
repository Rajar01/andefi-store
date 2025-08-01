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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import store.andefi.ui.account.viewmodel.ForgotPasswordFormViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ForgotPasswordFormScreen(
    forgotPasswordFormViewModel: ForgotPasswordFormViewModel = hiltViewModel(),
    navigateToSignInForm: () -> Unit,
    navigateToForgotPasswordEmailConfirmation: (email: String) -> Unit,
) {
    val forgotPasswordFormUiState by forgotPasswordFormViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Collect Snackbar events from the ViewModel's Flow
    LaunchedEffect(key1 = Unit) {
        forgotPasswordFormViewModel.snackbarEvent.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    if (forgotPasswordFormUiState.isSendPasswordResetEmailSuccessful) {
        navigateToForgotPasswordEmailConfirmation(forgotPasswordFormUiState.email)
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
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Atur ulang kata sandi Anda",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Masukkan email Anda untuk menerima tautan pengaturan ulang kata sandi.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                )
            }
            OutlinedTextField(
                value = forgotPasswordFormUiState.email,
                label = { Text("Email") },
                placeholder = { Text("Contoh: email@address.com") },
                onValueChange = forgotPasswordFormViewModel::onEmailValueChanged,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                enabled = !forgotPasswordFormUiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = forgotPasswordFormViewModel::sendPasswordResetEmail,
                    shape = MaterialTheme.shapes.medium,
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                    enabled = !forgotPasswordFormUiState.isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Kirim tautan") }
                Text(
                    "Ingat kata sandi Anda? Masuk",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.clickable {
                        navigateToSignInForm()
                    }
                )
            }
        }
    }
}