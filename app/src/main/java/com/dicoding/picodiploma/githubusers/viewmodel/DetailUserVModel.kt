package com.dicoding.picodiploma.githubusers.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.dicoding.picodiploma.githubusers.api.APIConfig
import com.dicoding.picodiploma.githubusers.data.UserResponse
import com.dicoding.picodiploma.githubusers.local.UsersDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserVModel(application: Application) : AndroidViewModel(application) {
    private val dao = UsersDatabase(getApplication()).usersDao()

    private val _users = MutableLiveData<UserResponse>()
    val users: LiveData<UserResponse> = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _isFav = MutableLiveData<Boolean>()
    val isFav = _isFav

    fun detailUser(username: String){
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO){
            APIConfig.getApiService().detailUser(username).enqueue(object: Callback<UserResponse>{
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    _isLoading.value = false
                    if(response.isSuccessful){
                        _users.value = response.body()
                        loadFav(username)
                        _isError.value = false
                    }
                    else{
                        _isError.value = true
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    _isLoading.value = false
                    _isError.value = true
                }
            })
        }

    }

    private fun loadFav(username: String){
        viewModelScope.launch{
            val users: UserResponse? = dao.getUsername(username)
            _isFav.value = users?.usersFav
        }
    }

    fun addFavorite(users: UserResponse){
        viewModelScope.launch{
            val userOnDatabase: UserResponse? = dao.getUsername(users.login)
            if(userOnDatabase == null){
                dao.insertUserFavorite(users)
            }else{
                dao.deleteUsers(users.login)
            }
            detailUser(users.login)
        }
    }
}