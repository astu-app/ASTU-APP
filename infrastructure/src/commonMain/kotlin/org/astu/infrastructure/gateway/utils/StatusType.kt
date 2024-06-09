package org.astu.infrastructure.gateway.utils

import io.ktor.http.HttpStatusCode


/**
 * Provides an extension to evaluation whether the response is a 1xx code
 */
val HttpStatusCode.isInformational: Boolean get() = this.value in 100..199

/**
 * Provides an extension to evaluation whether the response is a 3xx code
 */
val HttpStatusCode.isRedirect: Boolean get() = this.value in 300..399

/**
 * Provides an extension to evaluation whether the response is a 4xx code
 */
val HttpStatusCode.isClientError: Boolean get() = this.value in 400..499

/**
 * Provides an extension to evaluation whether the response is a 5xx (Standard) through 999 (non-standard) code
 */
val HttpStatusCode.isServerError: Boolean get() = this.value in 500..999