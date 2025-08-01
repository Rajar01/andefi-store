package store.andefi.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import store.andefi.data.remote.dto.AccountSignInRequestDto
import store.andefi.data.remote.dto.AccountSignUpRequestDto
import store.andefi.data.remote.dto.ResendAccountVerificationEmailRequestDto

interface AccountApi {
    @POST("/api/accounts/sign-in")
    suspend fun signIn(@Body accountSignInRequestDto: AccountSignInRequestDto): Response<String>

    @POST("/api/accounts/sign-up")
    suspend fun signUp(@Body accountSignUpRequestDto: AccountSignUpRequestDto): Response<Unit>

    @POST("/api/accounts/resend-verification-email")
    suspend fun resendAccountVerificationEmail(@Body resendAccountVerificationEmailRequestDto: ResendAccountVerificationEmailRequestDto): Response<Unit>

    @POST("/api/accounts/forgot-password")
    @Headers("Content-Type: application/json")
    suspend fun forgotPassword(@Body payload: Map<String, String>): Response<Unit>

    @POST("/api/accounts/reset-password")
    @Headers("Content-Type: application/json")
    suspend fun resetPassword(
        @Header("Authorization") token: String,
        @Body payload: Map<String, String>
    ): Response<Unit>
}