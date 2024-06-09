package org.astu.feature.auth.impl

import org.astu.feature.auth.AuthServiceConfig
import org.astu.infrastructure.IAccountRepository
import org.astu.feature.auth.client.apis.DefaultApi
import org.astu.feature.auth.client.models.UserInfoDTO
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.UserInfo

class AstuAccountRepository : IAccountRepository {
    private val config by GlobalDIContext.inject<AuthServiceConfig>()

    private var userInfoCache: MutableMap<String, UserInfo> = mutableMapOf()
    private var myUserInfo: UserInfo? = null

    private val api: DefaultApi
        get() = DefaultApi(config.url)

    override suspend fun getUserInfo(): UserInfo {
        if(myUserInfo == null)
            myUserInfo = api.userInfoGet().toUserInfo()
        return myUserInfo!!
    }

    override suspend fun getAllUserInfo(): List<UserInfo> {
        val usersInfo = api.userInfoGetAll().map { it.toUserInfo() }
        userInfoCache = usersInfo.associateBy { it.userId }.toMutableMap()
        return usersInfo
    }

    override suspend fun getUserInfo(userId: String): UserInfo {
        if (userInfoCache.containsKey(userId))
            return userInfoCache.getValue(userId)
        getAllUserInfo()
        if (userInfoCache.containsKey(userId))
            return userInfoCache.getValue(userId)
        TODO("Undefined key")
    }

    override suspend fun getAllUserInfoByName(name: String): List<UserInfo> =
        userInfoCache.values.filter { it.name.contains(name) }

    private fun UserInfoDTO.toUserInfo() = UserInfo(this.userId, this.name)
}