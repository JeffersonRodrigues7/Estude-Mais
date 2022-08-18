package br.edu.ufabc.estude_mais.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class SessionViewModel(application: Application) : AndroidViewModel(application){
    private var sessionId: Long = 0
    var isTeacher: Boolean = false

    fun setSessionId(id: Long){
        sessionId = id
    }

    fun getSessionId(): Long{
        return sessionId
    }

}