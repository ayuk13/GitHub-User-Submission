package com.dicoding.picodiploma.githubusers.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.picodiploma.githubusers.data.UserResponse

@Database(entities = [UserResponse::class], version = 1)
abstract class UsersDatabase : RoomDatabase() {
    abstract fun usersDao(): UsersDao

    companion object{
        @Volatile
        private var INSTANCE: UsersDatabase? = null
        private val INKEY = Any()

        operator fun invoke(context:Context) = INSTANCE ?: synchronized(INKEY){
            INSTANCE ?: getDatabase(context).also{
                INSTANCE = it
            }
        }

        private fun getDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            UsersDatabase::class.java,
            "userDatabase"
        ).fallbackToDestructiveMigration().build()
    }
}