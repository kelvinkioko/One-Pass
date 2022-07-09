package xml.one.pass.domain.model

data class AccountModel(
    val id: Int = 0,
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val passphrase: String = ""
)
