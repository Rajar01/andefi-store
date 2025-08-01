package store.andefi.ui.account.screen

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
import store.andefi.ui.account.viewmodel.SignUpEmailConfirmationViewModel
import store.andefi.utility.EmailUtils

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SignUpEmailConfirmationScreen(
    signUpEmailConfirmationViewModel: SignUpEmailConfirmationViewModel = hiltViewModel(),
    email: String
) {
    val signUpEmailConfirmationUiState by signUpEmailConfirmationViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Collect Snackbar events from the ViewModel's Flow
    LaunchedEffect(key1 = Unit) {
        signUpEmailConfirmationViewModel.snackbarEvent.collect { message ->
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
                    "Mohon konfirmasi email Anda",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Tautan konfirmasi telah dikirimkan ke email Anda ${EmailUtils.maskEmail(email)}. Mohon klik tautan tersebut untuk memverifikasi alamat email Anda dan menyelesaikan proses pendaftaran.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal
                )
            }
            OutlinedButton(
                onClick = { signUpEmailConfirmationViewModel.resendAccountVerificationEmail(email) },
                shape = MaterialTheme.shapes.medium,
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                modifier = Modifier.fillMaxWidth(),
                enabled = !signUpEmailConfirmationUiState.isLoading
            ) { Text("Kirim lagi") }
        }
    }
}