package store.andefi.service

import com.auth0.android.jwt.JWT
import store.andefi.data.local.dao.AccountDao
import store.andefi.data.local.entity.Account
import store.andefi.data.remote.api.AccountApi
import store.andefi.data.remote.dto.AccountSignInRequestDto
import store.andefi.data.remote.dto.AccountSignUpRequestDto
import store.andefi.data.remote.dto.ResendAccountVerificationEmailRequestDto
import store.andefi.exception.AuthenticationFailedException
import javax.inject.Inject

class AccountService @Inject constructor(
    private val accountDao: AccountDao,
    private val accountApi: AccountApi
) {
    suspend fun signIn(accountSignInRequestDto: AccountSignInRequestDto): Result<Unit> {
        try {
            val response = accountApi.signIn(accountSignInRequestDto)

            return when (response.code()) {
                200 -> {
                    val rawJwt = requireNotNull(response.body())
                    val jwt = JWT(rawJwt)

                    accountDao.insertAccountInformation(
                        Account(
                            jwt.subject.toString(),
                            jwt.getClaim("email").asString()!!,
                            jwt.getClaim("preferred_username").asString()!!,
                            rawJwt
                        )
                    )

                    Result.success(Unit)
                }

                else -> Result.failure(AuthenticationFailedException())
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun signUp(accountSignUpRequestDto: AccountSignUpRequestDto): Result<Unit> {
        try {
            val response = accountApi.signUp(accountSignUpRequestDto)
            return when (response.code()) {
                201 -> Result.success(Unit)
                else -> Result.failure(RuntimeException())
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun resendAccountVerificationEmail(resendAccountVerificationEmailRequestDto: ResendAccountVerificationEmailRequestDto): Result<Unit> {
        try {
            val response =
                accountApi.resendAccountVerificationEmail(resendAccountVerificationEmailRequestDto)
            return when (response.code()) {
                200 -> Result.success(Unit)
                else -> Result.failure(RuntimeException())
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        try {
            val response =
                accountApi.forgotPassword(mapOf("email" to email))
            return when (response.code()) {
                200 -> Result.success(Unit)
                else -> Result.failure(RuntimeException())
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun resetPassword(token: String, password: String): Result<Unit> {
        try {
            val response =
                accountApi.resetPassword("Bearer $token", mapOf("password" to password))
            return when (response.code()) {
                200 -> Result.success(Unit)
                else -> Result.failure(RuntimeException())
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}
