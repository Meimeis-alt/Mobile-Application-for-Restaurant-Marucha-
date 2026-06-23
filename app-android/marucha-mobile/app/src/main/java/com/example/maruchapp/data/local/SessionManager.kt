package com.example.maruchapp.data.local

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("marucha_session", Context.MODE_PRIVATE)

    fun saveLoginSession(userId: Int) {
        prefs.edit()
            .putBoolean("is_logged_in", true)
            .putInt("user_id", userId)
            .apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean("is_logged_in", false)
    }

    fun getUserId(): Int {
        return prefs.getInt("user_id", 1)
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}