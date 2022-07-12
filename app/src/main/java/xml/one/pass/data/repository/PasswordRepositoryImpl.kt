package xml.one.pass.data.repository

import xml.one.pass.data.local.OnePassDatabase
import xml.one.pass.data.local.entity.PasswordEntity
import xml.one.pass.data.local.mapper.mapToPasswordModel
import xml.one.pass.domain.model.PasswordModel
import xml.one.pass.domain.repository.PasswordRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PasswordRepositoryImpl @Inject constructor(
    onePassDatabase: OnePassDatabase
) : PasswordRepository {

    private val passwordDao = onePassDatabase.passwordDao()

    override suspend fun insertPassword(passwordEntity: PasswordEntity) {
        passwordDao.insertPassword(passwordEntity = passwordEntity)
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

    override suspend fun loadPassword(): List<PasswordModel> {
        return passwordDao.loadPassword().map { it.mapToPasswordModel() }
    }

    override suspend fun deletePasswordByID(id: String) {
        passwordDao.deletePasswordByID(id = id)
    }

    override suspend fun deletePassword() {
        passwordDao.deletePassword()
    }
}
