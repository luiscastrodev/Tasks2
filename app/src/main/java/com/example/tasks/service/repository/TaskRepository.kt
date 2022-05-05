package com.example.tasks.service.repository

import android.content.Context
import com.example.tasks.R
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.model.HeaderModel
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.local.TaskDatabase
import com.example.tasks.service.repository.remote.PriorityService
import com.example.tasks.service.repository.remote.RetrofitClient
import com.example.tasks.service.repository.remote.TaskService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository(val context: Context) {

    private val mRemote = RetrofitClient.createService(TaskService::class.java)

    fun create(task: TaskModel, listener: APIListener<Boolean>) {
        val call: Call<Boolean> =
            mRemote.create(task.priorityId, task.description, task.dueDate, task.complete)
        call.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onError(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
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
        })
    }

    fun update(task: TaskModel, listener: APIListener<Boolean>) {
        val call: Call<Boolean> =
            mRemote.update(task.id, task.priorityId, task.description, task.dueDate, task.complete)
        call.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onError(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
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
        })
    }

    fun undo(id:Int, listener: APIListener<Boolean>){
        val call: Call<Boolean> =
            mRemote.undo(id)
        call.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onError(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
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
        })
    }

    fun complete(id:Int, listener: APIListener<Boolean>){
        val call: Call<Boolean> =
            mRemote.complete(id)
        call.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onError(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
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
        })
    }

    fun delete(id:Int, listener: APIListener<Boolean>){
        val call: Call<Boolean> =
            mRemote.delete(id)
        call.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onError(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
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
        })
    }
    fun load(taskId: Int, listener: APIListener<TaskModel>) {
        val call: Call<TaskModel> = mRemote.load(taskId)
        call.enqueue(object : Callback<TaskModel> {
            override fun onFailure(call: Call<TaskModel>, t: Throwable) {
                listener.onError(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<TaskModel>, response: Response<TaskModel>) {
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
        })
    }

    fun all(listener: APIListener<List<TaskModel>>) {
        val call: Call<List<TaskModel>> = mRemote.all()
        list(call, listener)
    }

    fun nextWeek(listener: APIListener<List<TaskModel>>) {
        val call: Call<List<TaskModel>> = mRemote.nextWeek()
        list(call, listener)
    }

    fun overDue(listener: APIListener<List<TaskModel>>) {
        val call: Call<List<TaskModel>> = mRemote.overdue()
        list(call, listener)
    }

    private fun list(call: Call<List<TaskModel>>, listener: APIListener<List<TaskModel>>) {
        call.enqueue(object : Callback<List<TaskModel>> {
            override fun onResponse(
                call: Call<List<TaskModel>>,
                response: Response<List<TaskModel>>
            ) {
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

            override fun onFailure(call: Call<List<TaskModel>>, t: Throwable) {
                listener.onError(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })
    }


}