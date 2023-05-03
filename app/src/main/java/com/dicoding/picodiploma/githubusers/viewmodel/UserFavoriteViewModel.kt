package com.dicoding.picodiploma.githubusers.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.dicoding.picodiploma.githubusers.data.UserResponse
import com.dicoding.picodiploma.githubusers.local.UsersDatabase
import kotlinx.coroutines.launch

class UserFavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = UsersDatabase(getApplication()).usersDao()

    private var _listUsers = MutableLiveData<List<UserResponse>>()
    val listUsers: LiveData<List<UserResponse>> = _listUsers

    private var _empty = MutableLiveData<Boolean>()
    val empty: LiveData<Boolean> = _empty

    fun getUsersFavorite(){
        viewModelScope.launch {
            _listUsers.value = dao.getUser()
            _empty.value = _listUsers.value!!.isEmpty()
        }
    }
    fun removeFromFavorite(users: String){
        viewModelScope.launch {
            dao.deleteUsers(users)
            getUsersFavorite()
        }
    }
}