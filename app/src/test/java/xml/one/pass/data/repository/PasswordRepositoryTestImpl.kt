package xml.one.pass.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import xml.one.pass.domain.model.PasswordModel
import xml.one.pass.domain.repository.PasswordRepository
import xml.one.pass.util.Resource
import java.util.function.Predicate

class PasswordRepositoryTestImpl : PasswordRepository {

    private var passwords = mutableListOf<PasswordModel>()

    override suspend fun insertPassword(passwordModel: PasswordModel):
        Flow<Resource<Boolean>> = flow {
        if (doesPasswordExist(
                passwordModel.siteName,
                passwordModel.userName,
                passwordModel.email,
                passwordModel.phoneNumber,
                passwordModel.password
            )
        )
            emit(Resource.Error(message = "Password exists!"))
        else {
            passwords.add(passwordModel)

            emit(
                Resource.Success(
                    data = doesPasswordExist(
                        siteName = passwordModel.siteName,
                        userName = passwordModel.userName,
                        email = passwordModel.email,
                        phoneNumber = passwordModel.phoneNumber,
                        password = passwordModel.password
                    )
                )
            )
        }
    }

    override suspend fun updatePasswordDetails(passwordModel: PasswordModel):
        Flow<Resource<Boolean>> = flow {
        val passwordExists = passwords.any { it.id == passwordModel.id }

        if (passwordExists) {
            passwords.also { passwordsModel ->
                passwordsModel.find { it.id == passwordModel.id }?.let {
                    it.id = passwordModel.id
                    it.siteName = passwordModel.siteName
                    it.url = passwordModel.url
                    it.userName = passwordModel.userName
                    it.email = passwordModel.email
                    it.password = passwordModel.password
                    it.phoneNumber = passwordModel.phoneNumber
                    it.securityQuestions = passwordModel.securityQuestions
                    it.timeUpdated = passwordModel.timeUpdated
                }
            }

            emit(
                Resource.Success(
                    data = doesPasswordExist(
                        siteName = passwordModel.siteName,
                        userName = passwordModel.userName,
                        email = passwordModel.email,
                        phoneNumber = passwordModel.phoneNumber,
                        password = passwordModel.password
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
    ): Boolean = passwords.any {
        it.siteName == siteName &&
            (it.userName == userName || it.email == email || it.phoneNumber == phoneNumber) &&
            it.password == password
    }

    override suspend fun deletePasswordByID(id: Int): Boolean {
        val condition = Predicate<PasswordModel> { it.id == id }
        passwords.also { it.removeIf(condition) }

        return passwords.any { it.id == id }
    }

    override suspend fun deletePassword() {
        passwords.clear()
    }
}
