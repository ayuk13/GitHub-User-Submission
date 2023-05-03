package com.dicoding.picodiploma.githubusers.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.picodiploma.githubusers.data.UserResponse

@Dao
interface UsersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserFavorite(users: UserResponse): Long

    @Query("SELECT * FROM userresponse")
    suspend fun getUser(): List<UserResponse>

    @Query("SELECT * FROM userresponse WHERE login= :username")
    suspend fun getUsername(username: String) : UserResponse?

    @Query("DELETE FROM userresponse WHERE login = :username")
    suspend fun deleteUsers(username: String)
}