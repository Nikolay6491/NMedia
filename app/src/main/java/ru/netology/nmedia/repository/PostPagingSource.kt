package ru.netology.nmedia.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.netology.nmedia.api.PostsApiService
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.error.ApiError
import java.io.IOException

class PostPagingSource(
    private val postsApiService: PostsApiService
) : PagingSource<Long, Post>() {
    override fun getRefreshKey(state: PagingState<Long, Post>): Long? = null

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Post> {
        try {
            val result = when (params) {
                is LoadParams.Refresh -> postsApiService.getLatest(params.loadSize)
                is LoadParams.Prepend -> return LoadResult.Page(
                    data = emptyList(),
                    prevKey = params.key,
                    nextKey = null

                )
                is LoadParams.Append -> postsApiService.getBefore(params.key, params.loadSize)

            }

            if (!result.isSuccessful) {
                throw ApiError(result.code(), result.message())
            }

            val body = result.body() ?: throw ApiError(
                result.code(),
                result.message()
            )

            return LoadResult.Page(
                data = body,
                prevKey = params.key,
                body.lastOrNull()?.id
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        }
    }
}