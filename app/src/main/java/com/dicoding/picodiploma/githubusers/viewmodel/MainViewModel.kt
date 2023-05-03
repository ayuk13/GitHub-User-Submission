package com.dicoding.picodiploma.githubusers.viewmodel

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.*
import com.dicoding.picodiploma.githubusers.SettingPreferences
import com.dicoding.picodiploma.githubusers.api.APIConfig
import com.dicoding.picodiploma.githubusers.data.SearchUserResponse
import com.dicoding.picodiploma.githubusers.data.UserResponse
import com.dicoding.picodiploma.githubusers.local.UsersDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var _users = MutableLiveData<List<UserResponse>>()
    val users : LiveData<List<UserResponse>> = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    private val dao = UsersDatabase(getApplication()).usersDao()
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val pref = SettingPreferences.getInstance(application.dataStore)

    fun getUsers() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO){
            APIConfig.getApiService().listUsers().enqueue(object : Callback<List<UserResponse>> {
                override fun onResponse(
                    call: Call<List<UserResponse>>,
                    response: Response<List<UserResponse>>
                ) {
                    _isLoading.value = false
                    if(response.isSuccessful){
                        _isError.value = false
                        viewModelScope.launch{
                            val users = dao.getUser()
                            val addUsers = response.body()?.map{
                                val user = dao.getUsername(it.login)
                                if(users.contains(user)) it.usersFav = true
                                it
                            }
                            _users.value = addUsers!!
                        }

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

    fun getUsers(query:String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO){
            APIConfig.getApiService().getSearchUser(query).enqueue(object : Callback<SearchUserResponse> {
                override fun onResponse(
                    call: Call<SearchUserResponse>,
                    response: Response<SearchUserResponse>
                ) {
                    _isLoading.value = false
                    if(response.isSuccessful){
                        _isError.value = false
                        if(response.body()!!.items!!.isEmpty()){
                            _isEmpty.value = true
                        }else{
                            _isEmpty.value = false
                            viewModelScope.launch{
                                val users = dao.getUser()
                                val addUsers = response.body()?.items?.map{
                                    val user = dao.getUsername(it.login)
                                    if(users.contains(user)) it.usersFav = true
                                    it
                                }
                                _users.value = addUsers!!
                            }
                        }
                    }
                    else{
                        _isError.value = true
                    }
                }

                override fun onFailure(call: Call<SearchUserResponse>, t: Throwable) {
                    _isLoading.value = false
                    _isError.value = true
                }

            })
        }

    }

    fun getToFavorite(users: UserResponse, query: String = ""){
        viewModelScope.launch {
            dao.insertUserFavorite(users)
            if(query.isNotEmpty()) getUsers(query)
            else getUsers()
        }
    }

    fun removeFromFavorite(username: String, query:String = ""){
        viewModelScope.launch{
            dao.deleteUsers(username)
            if(query.isNotEmpty())getUsers(query)
            else getUsers()
        }
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
}