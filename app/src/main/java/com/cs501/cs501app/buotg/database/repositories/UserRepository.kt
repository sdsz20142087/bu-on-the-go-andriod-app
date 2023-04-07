package com.cs501.cs501app.buotg.database.repositories

import com.cs501.cs501app.buotg.connection.API
import com.cs501.cs501app.buotg.connection.LoginResponse
import com.cs501.cs501app.buotg.connection.SafeAPIRequest
import com.cs501.cs501app.buotg.connection.SignupResponse
import com.cs501.cs501app.buotg.database.AppDatabase
import com.cs501.cs501app.buotg.database.entities.KVEntry
import com.cs501.cs501app.buotg.database.entities.USER_TOKEN_KEY
class UserRepository(
    db: AppDatabase
) : SafeAPIRequest() {
    private val api: API = API.getClient()
    private val userDao = db.userDao()
    private val kvDao = db.kvDao()

    suspend fun userLogin(email: String, password: String): LoginResponse {
        val res = apiRequest { api.userLogin(email, password) }
        kvDao.put(KVEntry(USER_TOKEN_KEY, res.token))

        return res
    }

    suspend fun userSignup(
        name: String,
        email: String,
        password: String,
        user_type: String
    ): SignupResponse {
        val res = apiRequest { api.userSignup(name, email, password, user_type) }
        userDao.upsert(res.user)
        return res
    }

    suspend fun getCurrentUser() = userDao.getCurrentUser()

    suspend fun logout() = userDao.logout()
}