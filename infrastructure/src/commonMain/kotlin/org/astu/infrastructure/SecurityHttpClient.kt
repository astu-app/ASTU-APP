package org.astu.infrastructure

import io.ktor.client.*

interface  SecurityHttpClient{
    val instance: HttpClient
}