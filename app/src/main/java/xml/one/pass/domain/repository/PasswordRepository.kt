package xml.one.pass.domain.repository

import xml.one.pass.data.local.entity.PasswordEntity
import xml.one.pass.domain.model.PasswordModel

interface PasswordRepository {
    suspend fun insertPassword(passwordEntity: PasswordEntity)

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

    suspend fun loadPassword(): List<PasswordModel>

    suspend fun deletePasswordByID(id: String)

    suspend fun deletePassword()
}
