package com.dicoding.picodiploma.githubusers.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.githubusers.api.APIConfig
import com.dicoding.picodiploma.githubusers.data.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FolUserVModel: ViewModel() {
    private val _userFollowers = MutableLiveData<List<UserResponse>>()
    val userFollowers : LiveData<List<UserResponse>> = _userFollowers

    private val _userFollowing = MutableLiveData<List<UserResponse>>()
    val userFollowing : LiveData<List<UserResponse>> = _userFollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    fun followersUser(username: String){
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO){
            APIConfig.getApiService().followersUser(username).enqueue(object: Callback<List<UserResponse>> {
            override fun onResponse(
                call: Call<List<UserResponse>>,
                response: Response<List<UserResponse>>
            ){
                _isLoading.value = false
                if(response.isSuccessful){
                    _userFollowers.value = response.body()
                    _isError.value = false
                }
                else{
                    _isError.value = true
                }
            }

                override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                    _isLoading.value = false
                    _isError.value = true
                }

            })
        }

    }

    fun followingUser(username: String){
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO){
            APIConfig.getApiService().followingUser(username).enqueue(object : Callback<List<UserResponse>> {
                override fun onResponse(
                call: Call<List<UserResponse>>,
                response: Response<List<UserResponse>>
                ) {
                    _isLoading.value = false
                    if(response.isSuccessful){
                        _userFollowing.value = response.body()
                        _isError.value = false
                    }
                    else{
                        _isError.value = true
                    }
                }

                override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                    _isLoading.value = false
                    _isError.value = true
                }
            })
        }

    }

}