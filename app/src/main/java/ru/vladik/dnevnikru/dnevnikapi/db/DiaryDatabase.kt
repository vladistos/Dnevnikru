package ru.vladik.dnevnikru.dnevnikapi.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.vladik.dnevnikru.dnevnikapi.db.dao.UsersDao
import ru.vladik.dnevnikru.dnevnikapi.db.models.ApiUserImpl

@Database(entities = [ApiUserImpl::class], version = 1)
abstract class DiaryDatabase : RoomDatabase() {
    abstract fun userDao() : UsersDao
}