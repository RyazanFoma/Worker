package com.pushe.worker.operations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.pushe.worker.data.ERPRestService
import com.pushe.worker.data.OperationsDataSource
import com.pushe.worker.data.model.Operation
import kotlinx.coroutines.flow.Flow

/**
 * A simple [OperationsViewModel] that provides a [Flow]<[PagingData]> of performed operations.
 */
class OperationsViewModel(
    private val apiService: ERPRestService,
    private val userId: String
): ViewModel() {

    /**
     * We use the Kotlin [Flow] property available on [Pager].
     */
    val operationsFlow: Flow<PagingData<Operation>> = Pager(
        config = PagingConfig(
            /**
             * A good page size is a value that fills at least a few screens worth of content on a
             * large device so the User is unlikely to see a null item.
             * You can play with this constant to observe the paging behavior.
             *
             * It's possible to vary this with list device size, but often unnecessary, unless a
             * user scrolling on a large device is expected to scroll through items more quickly
             * than a small device, such as when the large device uses a grid layout of items.
             */
            pageSize = 15,
            /**
             * If placeholders are enabled, PagedList will report the full size but some items might
             * be null in onBind method (PagedListAdapter triggers a rebind when data is loaded).
             *
             * If placeholders are disabled, onBind will never receive null but as more pages are
             * loaded, the scrollbars will jitter as new pages are loaded. You should probably
             * disable scrollbars if you disable placeholders.
             */
            enablePlaceholders = true,
            /**
             * Maximum number of items a PagedList should hold in memory at once.
             *
             * This number triggers the PagedList to start dropping distant pages as more are loaded.
             */
            maxSize = 100
        )
    ) {
        OperationsDataSource(apiService = apiService, userId = userId)
    }.flow.cachedIn(viewModelScope)
}