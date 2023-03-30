package ru.vladik.dnevnikru.dnevnikapi.db.dao

import androidx.room.*
import ru.vladik.dnevnikru.dnevnikapi.db.models.ApiUserImpl

@Dao
interface UsersDao {

    @Query("SELECT * FROM user WHERE person_id in (:userIds)")
    fun getByIds(userIds: Array<Long>): List<ApiUserImpl>

    @Query("SELECT * FROM user WHERE person_id = (:userId)")
    fun getById(userId: Long): ApiUserImpl?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg users: ApiUserImpl)

    @Delete
    fun delete(user: ApiUserImpl)
}