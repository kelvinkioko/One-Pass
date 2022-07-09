package xml.one.pass.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import xml.one.pass.data.repository.AccountRepositoryImpl
import xml.one.pass.data.repository.PasswordRepositoryImpl
import xml.one.pass.domain.repository.AccountRepository
import xml.one.pass.domain.repository.PasswordRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAccountRepository(
        accountRepositoryImpl: AccountRepositoryImpl
    ): AccountRepository

    @Binds
    @Singleton
    abstract fun bindPasswordRepository(
        passwordRepositoryImpl: PasswordRepositoryImpl
    ): PasswordRepository
}
