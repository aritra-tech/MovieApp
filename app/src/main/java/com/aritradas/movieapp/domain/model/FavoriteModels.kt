package com.aritradas.movieapp.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccountDetails(
    @SerialName("id") val id: Int,
    @SerialName("username") val username: String? = null
)

@Serializable
data class FavoriteRequest(
    @SerialName("media_type") val mediaType: String = "movie",
    @SerialName("media_id") val mediaId: Int,
    @SerialName("favorite") val favorite: Boolean
)

@Serializable
data class FavoriteResponse(
    @SerialName("status_code") val statusCode: Int,
    @SerialName("status_message") val statusMessage: String
)

@Serializable
data class MovieAccountState(
    @SerialName("id") val id: Int,
    @SerialName("favorite") val favorite: Boolean
)
