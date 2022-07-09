package xml.one.pass.data.repository

import xml.one.pass.data.local.dao.AccountDao
import xml.one.pass.data.local.entity.AccountEntity
import xml.one.pass.domain.repository.AccountRepository

class AccountRepositoryImpl(
    private val accountDao: AccountDao
) : AccountRepository {
    override suspend fun insertAccount(accountEntity: AccountEntity) {
        accountDao.insertAccount(accountEntity = accountEntity)
    }

    override suspend fun updateAccountName(name: String, id: Int): Int {
        return accountDao.updateAccountName(name = name, id = id)
    }

    override suspend fun updateAccountPassword(password: String, id: Int): Int {
        return accountDao.updateAccountPassword(password = password, id = id)
    }

    override suspend fun loadAccount(): List<AccountEntity> {
        return accountDao.loadAccount()
    }

    override suspend fun deleteAccount() {
        accountDao.deleteAccount()
    }
}
