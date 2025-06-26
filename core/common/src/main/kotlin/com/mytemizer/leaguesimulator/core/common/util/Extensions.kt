package com.mytemizer.leaguesimulator.core.common.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/**
 * A generic class that holds a value with its loading status.
 */
sealed interface Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>
    data class Error(val exception: Throwable? = null) : Resource<Nothing>
    data object Loading : Resource<Nothing>
}

/**
 * Extension function to convert Flow<T> to Flow<Resource<T>>
 */
fun <T> Flow<T>.asResource(): Flow<Resource<T>> {
    return this
        .map<T, Resource<T>> { Resource.Success(it) }
        .onStart { emit(Resource.Loading) }
        .catch { emit(Resource.Error(it)) }
}

/**
 * Extension function to safely get data from Resource
 */
fun <T> Resource<T>.getDataOrNull(): T? {
    return when (this) {
        is Resource.Success -> data
        else -> null
    }
}

/**
 * Extension function to check if Resource is successful
 */
fun <T> Resource<T>.isSuccess(): Boolean = this is Resource.Success

/**
 * Extension function to check if Resource is loading
 */
fun <T> Resource<T>.isLoading(): Boolean = this is Resource.Loading

/**
 * Extension function to check if Resource is error
 */
fun <T> Resource<T>.isError(): Boolean = this is Resource.Error
