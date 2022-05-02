package com.example.tasks.service.listener

interface APIListener<T> {
    fun onSucess(response: T)
    fun onError(erro: String)
}