package com.dicoding.picodiploma.githubusers.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserResponse(
    @PrimaryKey
    val login: String,
    val name: String?,
    val avatar_url: String?,
    val company: String?,
    val followers: Int?,
    val followers_url: String?,
    val following: Int?,
    val following_url: String?,
    val id: Int?,
    val location: String?,
    val public_repos: Int?,
){
    var usersFav = false
}