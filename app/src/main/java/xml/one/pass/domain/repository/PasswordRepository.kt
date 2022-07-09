package xml.one.pass.domain.repository

import xml.one.pass.data.local.entity.PasswordEntity

interface PasswordRepository {
    suspend fun insertPassword(accountEntity: PasswordEntity)

    suspend fun updatePasswordDetails(
        id: Int,
        siteName: String,
        url: String = "",
        userName: String = "",
        email: String,
        password: String,
        phoneNumber: String = "",
        securityQuestions: String = "",
        timeUpdated: String
    ): Int

    suspend fun loadPassword(): List<PasswordEntity>

    suspend fun deletePasswordByID(id: String)

    suspend fun deletePassword()
}
