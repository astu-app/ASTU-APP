package org.astu.infrastructure

interface IAccountRepository {
    suspend fun getUserInfo(): UserInfo
    suspend fun getAllUserInfo(): List<UserInfo>
    suspend fun getUserInfo(userId: String): UserInfo
    suspend fun getAllUserInfoByName(name: String): List<UserInfo>
}