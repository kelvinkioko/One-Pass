package xml.one.pass.domain.model

data class PasswordModel(
    val id: Int = 0,
    val siteName: String = "",
    val url: String = "",
    val userName: String = "",
    val email: String,
    val password: String,
    val phoneNumber: String = "",
    val securityQuestions: String = "",
    val timeCreated: String,
    val timeUpdated: String
)
