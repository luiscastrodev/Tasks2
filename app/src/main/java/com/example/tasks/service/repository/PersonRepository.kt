package com.example.tasks.service.repository

import android.content.Context
import com.example.tasks.R
import com.example.tasks.service.model.HeaderModel
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.repository.remote.PersonService
import com.example.tasks.service.repository.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonRepository(val context: Context) : BaseRepository(context) {

    private val remote = RetrofitClient.createService(PersonService::class.java)

    fun login(email: String, password: String, listener: APIListener<HeaderModel>) {
        if(!isConnectionAvailable(context))
        {
            listener.onError(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        //remote login
        val call: Call<HeaderModel> = remote.login(email, password)
        call.enqueue(object : Callback<HeaderModel> {
            override fun onResponse(call: Call<HeaderModel>, response: Response<HeaderModel>) {

                if (response.code() != TaskConstants.HTTP.SUCCESS) {
                    val validation =
                        Gson().fromJson(response.errorBody()?.string(), String::class.java)
                    listener.onError(validation)
                } else {
                    response.body()?.let {
                        listener.onSucess(response = it)
                    }
                }
            }

            override fun onFailure(call: Call<HeaderModel>, t: Throwable) {
                listener.onError(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })

    }

    fun create(email: String, password: String, name: String, listener: APIListener<HeaderModel>) {
        if(!isConnectionAvailable(context))
        {
            listener.onError(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        //remote
        val call: Call<HeaderModel> = remote.create(email, password, name,true)
        call.enqueue(object : Callback<HeaderModel> {
            override fun onResponse(call: Call<HeaderModel>, response: Response<HeaderModel>) {

                if (response.code() != TaskConstants.HTTP.SUCCESS) {
                    val validation = Gson().fromJson(response.errorBody()?.string(), String::class.java)
                    listener.onError(validation)
                } else {
                    response.body()?.let {
                        listener.onSucess(response = it)
                    }
                }
            }

            override fun onFailure(call: Call<HeaderModel>, t: Throwable) {
                listener.onError(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })

    }
}