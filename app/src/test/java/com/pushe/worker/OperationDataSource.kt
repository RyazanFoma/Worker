package com.pushe.worker

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pushe.worker.operations.model.Operations
import retrofit2.HttpException
import java.io.IOException

class OperationDataSource(
    val backend: ExampleBackendService,
    val query: String
) : PagingSource<Int, Operations.Operation>() {
    override fun getRefreshKey(state: PagingState<Int, Operations.Operation>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Operations.Operation> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1
            val response = backend.searchUsers(query, nextPageNumber)
            return LoadResult.Page(
                data = response.users,
                prevKey = null, // Only paging forward.
                nextKey = response.nextPageNumber
            )
        } catch (e: IOException) {
            // IOException for network failures.
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            return LoadResult.Error(e)
        }
    }

}