package xml.one.pass.domain.repository

import xml.one.pass.data.local.entity.AccountEntity

interface AccountRepository {
    suspend fun insertAccount(accountEntity: AccountEntity)

    suspend fun updateAccountName(name: String, id: Int): Int

    suspend fun updateAccountPassword(password: String, id: Int): Int

    suspend fun loadAccount(): List<AccountEntity>

    suspend fun deleteAccount()
}
