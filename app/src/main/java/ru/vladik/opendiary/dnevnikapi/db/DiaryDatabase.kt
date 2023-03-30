package ru.vladik.opendiary.dnevnikapi.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.vladik.opendiary.dnevnikapi.db.dao.UsersDao
import ru.vladik.opendiary.dnevnikapi.db.models.ApiUserImpl

@Database(entities = [ApiUserImpl::class], version = 1)
abstract class DiaryDatabase : RoomDatabase() {
    abstract fun userDao() : UsersDao
}