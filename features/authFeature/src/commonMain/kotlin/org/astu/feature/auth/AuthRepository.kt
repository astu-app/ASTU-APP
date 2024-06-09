package org.astu.feature.auth

/**
 * Интерфейс репозитории авторизации
 */
interface AuthRepository {
    suspend fun authJWT(login: String, password: String)
    suspend fun authYandex()
    suspend fun authGoogle()
    suspend fun registrationYandex()
    suspend fun registrationGoogle()
}