package xml.one.pass.data.repository

import xml.one.pass.data.local.dao.PasswordDao
import xml.one.pass.data.local.entity.PasswordEntity
import xml.one.pass.domain.repository.PasswordRepository

class PasswordRepositoryImpl(
    private val passwordDao: PasswordDao
) : PasswordRepository {
    override suspend fun insertPassword(accountEntity: PasswordEntity) {
        passwordDao.insertPassword(accountEntity = accountEntity)
    }

    override suspend fun updatePasswordDetails(
        id: Int,
        siteName: String,
        url: String,
        userName: String,
        email: String,
        password: String,
        phoneNumber: String,
        securityQuestions: String,
        timeUpdated: String
    ): Int {
        return passwordDao.updatePasswordDetails(
            id = id,
            siteName = siteName,
            url = url,
            userName = userName,
            email = email,
            password = password,
            phoneNumber = phoneNumber,
            securityQuestions = securityQuestions,
            timeUpdated = timeUpdated
        )
    }

    override suspend fun loadPassword(): List<PasswordEntity> {
        return passwordDao.loadPassword()
    }

    override suspend fun deletePasswordByID(id: String) {
        passwordDao.deletePasswordByID(id = id)
    }

    override suspend fun deletePassword() {
        passwordDao.deletePassword()
    }
}
