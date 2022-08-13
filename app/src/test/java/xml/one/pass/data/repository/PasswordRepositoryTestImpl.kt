package xml.one.pass.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import xml.one.pass.data.local.mapper.dateFormatter
import xml.one.pass.domain.model.PasswordModel
import xml.one.pass.domain.repository.PasswordRepository
import xml.one.pass.extension.getCurrentDate
import xml.one.pass.util.Resource
import java.util.function.Predicate

class PasswordRepositoryTestImpl : PasswordRepository {

    private var passwords = mutableListOf<PasswordModel>()

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

        if (doesPasswordExist(siteName, userName, email, phoneNumber, password))
            emit(Resource.Error(message = "Password exists!"))
        else {
            passwords.add(
                PasswordModel(
                    siteName = siteName,
                    url = url,
                    userName = userName,
                    email = email,
                    password = password,
                    phoneNumber = phoneNumber,
                    securityQuestions = securityQuestions,
                    timeCreated = getCurrentDate().dateFormatter(),
                    timeUpdated = getCurrentDate().dateFormatter()
                )
            )

            emit(
                Resource.Success(
                    data = doesPasswordExist(
                        siteName = siteName,
                        userName = userName,
                        email = email,
                        phoneNumber = phoneNumber,
                        password = password
                    )
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
    ): Flow<Resource<Boolean>> = flow {
        val passwordExists = passwords.any { it.id == id }

        if (passwordExists) {
            passwords.also { passwordsModel ->
                passwordsModel.find { it.id == id }?.let {
                    it.id = id
                    it.siteName = siteName
                    it.url = url
                    it.userName = userName
                    it.email = email
                    it.password = password
                    it.phoneNumber = phoneNumber
                    it.securityQuestions = securityQuestions
                    it.timeUpdated = timeUpdated.dateFormatter()
                }
            }

            emit(
                Resource.Success(
                    data = doesPasswordExist(
                        siteName = siteName,
                        userName = userName,
                        email = email,
                        phoneNumber = phoneNumber,
                        password = password
                    )
                )
            )
        } else {
            emit(Resource.Error(message = "Password doesn't exist!"))
        }
    }

    override suspend fun loadPassword(): List<PasswordModel> {
        return passwords
    }

    override suspend fun loadPasswordById(passwordId: Int): Flow<Resource<PasswordModel>> = flow {
        val password = passwords.find { it.id == passwordId }

        password?.let { passwordModel ->
            emit(Resource.Success(data = passwordModel))
        } ?: kotlin.run {
            emit(Resource.Error(message = "Couldn't find the password details"))
        }
    }

    override suspend fun doesPasswordExist(
        siteName: String,
        userName: String,
        email: String,
        phoneNumber: String,
        password: String
    ): Boolean = passwords.any { it.siteName == siteName && (it.userName == userName || it.email == email || it.phoneNumber == phoneNumber) && it.password == password }

    override suspend fun deletePasswordByID(id: Int): Boolean {
        val condition = Predicate<PasswordModel> { it.id == id }
        passwords.also { it.removeIf(condition) }

        return passwords.any { it.id == id }
    }

    override suspend fun deletePassword() {
        passwords.clear()
    }
}
