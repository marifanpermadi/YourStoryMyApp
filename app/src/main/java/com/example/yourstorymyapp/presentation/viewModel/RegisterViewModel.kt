package com.example.yourstorymyapp.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yourstorymyapp.data.api.ApiConfig
import com.example.yourstorymyapp.data.model.RegisterResponse
import com.example.yourstorymyapp.presentation.utils.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    private val _register = MutableLiveData<Result<RegisterResponse>>()
    val register: LiveData<Result<RegisterResponse>> = _register

    fun register(name: String, email: String, password: String) {
        val client = ApiConfig.getApiService().register(name, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    _register.value = Result.Success(response.body()!!)
                } else {
                    _register.value = Result.Failure(response.message().toString())
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _register.value = Result.Failure(t.message)
            }
        })
    }
}