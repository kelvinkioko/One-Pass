package xml.one.pass.data.local.mapper

import xml.one.pass.data.local.entity.PasswordEntity
import xml.one.pass.domain.model.PasswordModel

fun PasswordEntity.mapToPasswordModel(): PasswordModel {
    return PasswordModel(
        id = id,
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
}
