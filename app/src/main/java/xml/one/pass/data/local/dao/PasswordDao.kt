package xml.one.pass.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import xml.one.pass.data.local.entity.PasswordEntity

@Dao
interface PasswordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPassword(passwordEntity: PasswordEntity)

    @Query(
        "UPDATE password SET " +
            "site_name = :siteName, " +
            "uniformResourceLocator = :url, " +
            "user_name = :userName, " +
            "email_address =:email, " +
            "password =:password, " +
            "phone_number =:phoneNumber, " +
            "security_questions =:securityQuestions, " +
            "time_updated =:timeUpdated " +
            "WHERE id =:id"
    ) suspend fun updatePasswordDetails(
        id: Int,
        siteName: String,
        url: String = "",
        userName: String = "",
        email: String,
        password: String,
        phoneNumber: String = "",
        securityQuestions: String = "",
        timeUpdated: String
    ): Int

    @Query("SELECT * FROM password")
    suspend fun loadPassword(): List<PasswordEntity>

    @Query("DELETE FROM password WHERE id =:id")
    suspend fun deletePasswordByID(id: String)

    @Query("DELETE FROM account")
    suspend fun deletePassword()
}
