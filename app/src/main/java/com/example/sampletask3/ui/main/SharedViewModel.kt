package com.example.sampletask3.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sampletask3.R
import com.example.sampletask3.data.remote.response.ExcludeListItemItem
import com.example.sampletask3.data.remote.response.VariantGroupsItem
import com.example.sampletask3.data.repository.ProductRepository
import com.example.sampletask3.utils.common.Resource
import com.example.sampletask3.utils.network.NetworkHelper
import com.example.sampletask3.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.net.ssl.HttpsURLConnection

class SharedViewModel(
    private val schedulerProvider: SchedulerProvider,
    private val compositeDisposable: CompositeDisposable,
    private val productRepository: ProductRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val groupList: MutableLiveData<List<VariantGroupsItem>> = MutableLiveData()
    private val exclusionMap: MutableLiveData<Map<Pair<String, String>, MutableList<Pair<String, String>>>> =
        MutableLiveData()
    private val messageStringId: MutableLiveData<Resource<Int>> = MutableLiveData()
    private val messageString: MutableLiveData<Resource<String>> = MutableLiveData()
    private val snackbarMessage: MutableLiveData<Resource<Int>> = MutableLiveData()
    private val isLoading: MutableLiveData<Boolean> = MutableLiveData()

    fun getMessageStringId(): LiveData<Resource<Int>> = messageStringId
    fun getMessageString(): LiveData<Resource<String>> = messageString
    fun getSnackbarMessage(): LiveData<Resource<Int>> = snackbarMessage
    fun getIsLoading(): LiveData<Boolean> = isLoading
    fun getGroupList(): LiveData<List<VariantGroupsItem>> = groupList
    fun getExclusionMap(): LiveData<Map<Pair<String, String>, MutableList<Pair<String, String>>>> = exclusionMap

    fun onViewCreated() {
        fetchProductDetail()
    }

    private fun fetchProductDetail() {
        isLoading.postValue(true)
        if (checkInternetConnectionWithMessage())
            compositeDisposable.addAll(
                productRepository.getProductDetail()
                    .subscribeOn(schedulerProvider.io())
                    .subscribe(
                        {
                            it?.variants?.variantGroups?.run { groupList.postValue(this) }
                            it?.variants?.excludeList?.run {
                                val pairList = formatDataSet(this)
                                if (pairList.isNotEmpty()) {
                                    val map = convertListToHashMap(pairList)
                                    exclusionMap.postValue(map)
                                }
                            }
                            isLoading.postValue(false)
                        },
                        {
                            isLoading.postValue(false)
                            handleNetworkError(it)
                        }
                    )
            )
    }


    /*
    * Ideally will have these conversion methods in a common utility class, as they are plainly dependent on
    * collection imports. Also encourages reusability.
    *
    * And also this method is in no ways meant to be public, in order to showcase unit test was made public
    * */
    fun convertListToHashMap(dataSet: MutableList<MutableList<Pair<String, String>>>):
            Map<Pair<String, String>, MutableList<Pair<String, String>>> {
        val excludeMap = mutableMapOf<Pair<String, String>, MutableList<Pair<String, String>>>()

        dataSet.forEach { pairList ->
            if (pairList.size >= 2) {
                pairList.forEach { pair ->
                    if (excludeMap.containsKey(pair)) {
                        val tempList = excludeMap[pair]
                        if (tempList != null) {
                            tempList.addAll(pairList.filter { it.first != pair.first || it.second != pair.second }.toList())
                            excludeMap[pair] = tempList
                        }
                    } else {
                        val tempList = mutableListOf<Pair<String, String>>()
                        tempList.addAll(pairList.filter { it.first != pair.first || it.second != pair.second }.toList())
                        excludeMap[pair] = tempList
                    }
                }
            }
        }
        return excludeMap
    }


    /*
    * Ideally will have these conversion methods in a common utility class, as they are plainly dependent on
    * collection imports. Also encourages reusability.
    *
    * And also this method is in no ways meant to be public, in order to showcase unit test was made public
    * */
    fun formatDataSet(list: List<List<ExcludeListItemItem>?>): MutableList<MutableList<Pair<String, String>>> {
        val finalList = mutableListOf<MutableList<Pair<String, String>>>()

        list.forEach { excludeList ->
            val subList = mutableListOf<Pair<String, String>>()
            excludeList?.forEach {
                it.apply {
                    val pair = Pair(groupId, variationId)
                    subList.add(pair)
                }
            }
            if (subList.size > 0)
                finalList.add(subList)
        }
        return finalList
    }

    protected fun handleNetworkError(err: Throwable?) =
        err?.let {
            networkHelper.castToNetworkError(it).run {
                when (status) {
                    -1 -> messageStringId.postValue(Resource.error(R.string.network_default_error))
                    0 -> messageStringId.postValue(Resource.error(R.string.server_connection_error))
                    HttpsURLConnection.HTTP_INTERNAL_ERROR ->
                        messageStringId.postValue(Resource.error(R.string.network_internal_error))
                    HttpsURLConnection.HTTP_UNAVAILABLE ->
                        messageStringId.postValue(Resource.error(R.string.network_server_not_available))
                    else -> messageString.postValue(Resource.error(message))
                }
            }
        }

    private fun checkInternetConnectionWithMessage(): Boolean =
        if (networkHelper.isNetworkConnected()) {
            true
        } else {
            messageStringId.postValue(Resource.error(R.string.network_connection_error))
            false
        }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}