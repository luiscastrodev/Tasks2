package com.example.tasks.service.repository

import com.example.tasks.service.HeaderModel
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.repository.remote.PersonService
import com.example.tasks.service.repository.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonRepository {

    private val remote = RetrofitClient.createService(PersonService::class.java)

    fun login(email: String, password: String, listener: APIListener<HeaderModel>) {

        //remote login
        val call: Call<HeaderModel> = remote.login(email, password)
        call.enqueue(object : Callback<HeaderModel> {
            override fun onResponse(call: Call<HeaderModel>, response: Response<HeaderModel>) {

                if(response.code() != TaskConstants.HTTP.SUCCESS)
                {
                    val validation = Gson().fromJson(response.errorBody()!!.string(), String::class.java)
                    listener.onError(validation)
                }else{
                    response.body()?.let {
                        listener.onSucess(response = it)
                    }
                }
            }

            override fun onFailure(call: Call<HeaderModel>, t: Throwable) {
                listener.onError(t.message.toString())
            }
        })
    }
}