package xml.one.pass.domain.repository

import kotlinx.coroutines.flow.Flow
import xml.one.pass.domain.model.PasswordModel
import xml.one.pass.util.Resource

interface PasswordRepository {
    suspend fun insertPassword(
        siteName: String,
        url: String,
        userName: String,
        email: String,
        password: String,
        phoneNumber: String,
        securityQuestions: String,
        timeCreated: String,
        timeUpdated: String
    ): Flow<Resource<Boolean>>

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
    ): Flow<Resource<Boolean>>

    suspend fun loadPassword(): List<PasswordModel>

    suspend fun loadPasswordById(passwordId: Int): Flow<Resource<PasswordModel>>

    suspend fun doesPasswordExist(
        siteName: String,
        userName: String,
        email: String,
        phoneNumber: String,
        password: String
    ): Boolean

    suspend fun deletePasswordByID(id: String)

    suspend fun deletePassword()
}
