package com.example.tasks.service.repository.remote

import android.util.Log
import com.example.tasks.service.constants.TaskConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {

    companion object {
        private lateinit var retrofit: Retrofit
        private val baseURl = "http://devmasterteam.com/CursoAndroidAPI/"
        private var personKey = ""
        private var tokenKey = ""

        private fun getRetrofitInstance(): Retrofit {
            val httpClient = OkHttpClient.Builder()
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging);  // <-- this is the important line!

            httpClient.addInterceptor { chain ->
                Log.d("RetrofitClient", personKey)
                Log.d("RetrofitClient", tokenKey)

                val request = chain.request().newBuilder()
                    .addHeader(TaskConstants.HEADER.PERSON_KEY, personKey)
                    .addHeader(TaskConstants.HEADER.TOKEN_KEY, tokenKey)
                    .build()
                chain.proceed(request)
            }

            if (!Companion::retrofit.isInitialized) {

                retrofit = Retrofit.Builder()
                    .baseUrl(baseURl)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }

        fun addHeader(token: String, personKey: String) {
            this.personKey = personKey
            this.tokenKey = token
        }

        fun <S> createService(serviceClass: Class<S>): S {
            return getRetrofitInstance().create(serviceClass)
        }
    }
}