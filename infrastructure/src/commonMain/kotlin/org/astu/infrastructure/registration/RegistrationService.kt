package org.astu.infrastructure.registration

import com.benasher44.uuid.uuid4
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.GatewayServiceConfig
import org.astu.infrastructure.SecurityHttpClient
import org.astu.infrastructure.gateway.models.JWTLoginDTO

class RegistrationService() {
    private val config by GlobalDIContext.inject<GatewayServiceConfig>()
    private val securityClient by GlobalDIContext.inject<SecurityHttpClient>()
    private val client = securityClient.instance


    suspend fun register(bytes: ByteArray): List<String> {
        val csv = bytes.decodeToString()

        val reader = csvReader { delimiter = ';' }
        val rows: List<Map<String, String>> = reader.readAllWithHeader(csv)
        val newAccounts = rows.map { row ->
            RegistrationDTO(
                account = AddAccountDTO(
                    firstName = row["Имя"] ?: "Дефолтное имя",
                    secondName = row["Фамилия"] ?: "Дефолтная фамилия",
                    patronymic = row["Отчество"],
                    isAdmin = row["Админ"]?.toBoolean() ?: false,
                    departmentId = uuid4().toString(),
                    studentGroupId = uuid4().toString(),
                ),
                auth = JWTLoginDTO(
                    login = row["Логин"] ?: "Дефолтный логин",
                    password = row["Пароль"] ?: "Дефолтный пароль",
                )
            )
        }

        return sendNewAccounts(newAccounts)
    }



    private suspend fun sendNewAccounts(accounts: List<RegistrationDTO>): List<String> =
        accounts.mapNotNull { sendNewAccount(it) }

    private suspend fun sendNewAccount(account: RegistrationDTO): String? {
        val response = client.post("${config.url}api/account-service/account") {
            contentType(ContentType.Application.Json)
            setBody(account)
        }

        if (response.status.isSuccess())
            return null

        return account.account.fullName
    }
}

@Serializable
class RegistrationDTO (
    val account: AddAccountDTO,
    val auth: JWTLoginDTO
)

@Serializable
class AddAccountDTO(
    val firstName: String,
    val secondName: String,
    val patronymic: String?,
    val isAdmin: Boolean,
    val departmentId: String? = null,
    val studentGroupId: String? = null
) {
    val fullName: String
        get() =
            if (patronymic != null) "$secondName $firstName $patronymic"
            else "$secondName $firstName"
}

@Serializable
data class JWTLoginDTO (
    val login: String,
    val password: String
)