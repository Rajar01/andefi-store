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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import store.andefi.ui.account.viewmodel.ForgotPasswordEmailConfirmationViewModel
import store.andefi.utility.EmailUtils

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ForgotPasswordEmailConfirmationScreen(
    forgotPasswordEmailConfirmationViewModel: ForgotPasswordEmailConfirmationViewModel = hiltViewModel(),
    navigateToSignInForm: () -> Unit,
    email: String
) {
    val forgotPasswordEmailConfirmationUiState by forgotPasswordEmailConfirmationViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Collect Snackbar events from the ViewModel's Flow
    LaunchedEffect(key1 = Unit) {
        forgotPasswordEmailConfirmationViewModel.snackbarEvent.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
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
                    "Cek email Anda",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Tautan pengaturan ulang kata sandi telah dikirimkan ke email Anda ${
                        EmailUtils.maskEmail(
                            email
                        )
                    }. Harap cek kotak masuk Anda dan klik tautan tersebut untuk mengatur ulang kata sandi Anda.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        forgotPasswordEmailConfirmationViewModel.resendPasswordResetEmail(
                            email
                        )
                    },
                    shape = MaterialTheme.shapes.medium,
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                    enabled = !forgotPasswordEmailConfirmationUiState.isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Kirim lagi") }
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