package xml.one.pass.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import xml.one.pass.data.local.OnePassDatabase
import xml.one.pass.data.local.entity.PasswordEntity
import xml.one.pass.data.local.mapper.mapToPasswordModel
import xml.one.pass.domain.model.PasswordModel
import xml.one.pass.domain.repository.PasswordRepository
import xml.one.pass.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PasswordRepositoryImpl @Inject constructor(
    onePassDatabase: OnePassDatabase
) : PasswordRepository {

    private val passwordDao = onePassDatabase.passwordDao()

    override suspend fun insertPassword(
        siteName: String,
        url: String,
        userName: String,
        email: String,
        password: String,
        phoneNumber: String,
        securityQuestions: String,
        timeCreated: String,
        timeUpdated: String
    ): Flow<Resource<Boolean>> = flow {
        val passwordExists = passwordDao.doesPasswordExist(
            siteName = siteName,
            userName = userName,
            email = email,
            phoneNumber = phoneNumber,
            password = password
        ) > 0

        if (passwordExists) {
            emit(Resource.Error(message = "Password exists!"))
        } else {
            passwordDao.insertPassword(
                passwordEntity = PasswordEntity(
                    siteName = siteName,
                    url = url,
                    userName = userName,
                    email = email,
                    password = password,
                    phoneNumber = phoneNumber,
                    securityQuestions = securityQuestions,
                    timeCreated = timeCreated,
                    timeUpdated = timeUpdated
                )
            )

            emit(
                Resource.Success(
                    data = passwordDao.doesPasswordExist(
                        siteName = siteName,
                        userName = userName,
                        email = email,
                        phoneNumber = phoneNumber,
                        password = password
                    ) > 0
                )
            )
        }
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
    ): Boolean {
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
        ) == 1
    }

    override suspend fun loadPassword(): List<PasswordModel> {
        return passwordDao.loadPassword().map { it.mapToPasswordModel() }
    }

    override suspend fun loadPasswordById(passwordId: Int): Flow<Resource<PasswordModel>> = flow {
        try {
            val password = passwordDao.loadPasswordById(passwordId = passwordId)

            password?.let { passwordEntity ->
                emit(Resource.Success(data = passwordEntity.mapToPasswordModel()))
            } ?: kotlin.run {
                emit(Resource.Error(message = "Couldn't find the password details"))
            }
        } catch (exception: Throwable) {
            emit(Resource.Error(message = exception.message ?: "Couldn't find the password details"))
        }
    }

    override suspend fun doesPasswordExist(
        siteName: String,
        userName: String,
        email: String,
        phoneNumber: String,
        password: String
    ): Boolean {
        return passwordDao.doesPasswordExist(
            siteName = siteName,
            userName = userName,
            email = email,
            phoneNumber = phoneNumber,
            password = password
        ) > 1
    }

    override suspend fun deletePasswordByID(id: String) {
        passwordDao.deletePasswordByID(id = id)
    }

    override suspend fun deletePassword() {
        passwordDao.deletePassword()
    }
}
