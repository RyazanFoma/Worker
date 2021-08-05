package com.pushe.worker.operations

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pushe.worker.operations.model.Operation
import retrofit2.HttpException
import java.io.IOException

class OperationDataSource (
    private val backend: OperationApiService,
    private val userId: String,
    private val dateOperations: String
): PagingSource<Int, Operation>() {
    override fun getRefreshKey(state: PagingState<Int, Operation>): Int? {
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

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Operation> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1
            val response = backend.getOperations(userId, dateOperations,
                (nextPageNumber-1)*params.loadSize, params.loadSize)
            return LoadResult.Page(
                data = response.results,
                prevKey = null, // Only paging forward.
                nextKey = if (response.results.isEmpty()) null else nextPageNumber+1
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