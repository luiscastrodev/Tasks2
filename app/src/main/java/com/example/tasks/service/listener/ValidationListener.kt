package com.example.tasks.service.listener

class ValidationListener(str: String = "") {
    private var mStatus: Boolean = true
    private var mMensage: String = ""

    init {
        if (str != "") {
            mStatus = false
            mMensage = str
        }
    }

    fun sucess() = mStatus
    fun failure() = mMensage
}