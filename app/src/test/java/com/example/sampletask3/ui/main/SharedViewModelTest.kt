package com.example.sampletask3.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.sampletask3.R
import com.example.sampletask3.data.remote.response.*
import com.example.sampletask3.data.repository.ProductRepository
import com.example.sampletask3.rx.TestSchedulerProvider
import com.example.sampletask3.utils.common.Resource
import com.example.sampletask3.utils.network.NetworkHelper
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.TestScheduler
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SharedViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var productRepository: ProductRepository

    @Mock
    private lateinit var networkHelper: NetworkHelper

    @Mock
    private lateinit var loadingObserver: Observer<Boolean>

    @Mock
    private lateinit var messageStringObserver: Observer<Resource<String>>

    @Mock
    private lateinit var messageStringIdObserver: Observer<Resource<Int>>

    @Mock
    private lateinit var groupListObserver: Observer<List<VariantGroupsItem>>

    private lateinit var compositeDisposable: CompositeDisposable

    private lateinit var testScheduler: TestScheduler

    private lateinit var viewModel: SharedViewModel

    @Before
    fun setUp() {
        compositeDisposable = CompositeDisposable()
        testScheduler = TestScheduler()
        val schedulerProvider = TestSchedulerProvider(testScheduler)
        viewModel = SharedViewModel(schedulerProvider, compositeDisposable, productRepository, networkHelper)
        viewModel.getIsLoading().observeForever(loadingObserver)
        viewModel.getMessageString().observeForever(messageStringObserver)
        viewModel.getMessageStringId().observeForever(messageStringIdObserver)
        viewModel.getGroupList().observeForever(groupListObserver)
    }

    @Test
    fun givenScreenShown_whenAppLaunched_shouldMakeProductDetailApiCallOnce() {
        val variantResponse = VariantResponse()

        doReturn(Single.just(variantResponse))
            .`when`(productRepository)
            .getProductDetail()

        doReturn(true)
            .`when`(networkHelper)
            .isNetworkConnected()


        viewModel.onViewCreated()
        testScheduler.triggerActions()

        verify(productRepository, times(1)).getProductDetail()
    }

    @Test
    fun givenNoInternet_whenAppLaunched_shouldShowNetworkError() {
        doReturn(false)
            .`when`(networkHelper)
            .isNetworkConnected()

        viewModel.onViewCreated()

        assert(viewModel.getMessageStringId().value == Resource.error(R.string.network_connection_error))
        verify(messageStringIdObserver).onChanged(Resource.error(R.string.network_connection_error))
    }

    @Test
    fun givenServerResponse200_whenProductDetailApiCall_shouldUpdateDataSet() {
        doReturn(true)
            .`when`(networkHelper)
            .isNetworkConnected()

        val variantResponse = VariantResponse(
            Variants(
                listOf(
                    VariantGroupsItem(variations = listOf(VariationsItem(), VariationsItem(), VariationsItem())),
                    VariantGroupsItem(variations = listOf(VariationsItem(), VariationsItem(), VariationsItem())),
                    VariantGroupsItem(variations = listOf(VariationsItem(), VariationsItem(), VariationsItem()))
                ),
                listOf(
                    listOf(ExcludeListItemItem(), ExcludeListItemItem()),
                    listOf(ExcludeListItemItem(), ExcludeListItemItem())
                )
            )
        )

        doReturn(Single.just(variantResponse))
            .`when`(productRepository)
            .getProductDetail()

        viewModel.onViewCreated()
        testScheduler.triggerActions()

        assert(viewModel.getIsLoading().value == false)
        assert(viewModel.getGroupList().value == variantResponse.variants?.variantGroups)

        verify(productRepository, times(1)).getProductDetail()
        verify(loadingObserver).onChanged(true)
        verify(loadingObserver).onChanged(false)
        verify(groupListObserver).onChanged(variantResponse.variants?.variantGroups)
    }

    @Test
    fun givenList_whenFormatDataSetCalled_shouldReturnCorrespondingPairList() {
        val inputList1 = listOf(
            listOf(
                ExcludeListItemItem("1", "3"),
                ExcludeListItemItem("2", "2")
            ),
            listOf(
                ExcludeListItemItem("1", "1"),
                ExcludeListItemItem("2", "3"),
                ExcludeListItemItem("3", "1")
            )
        )

        val inputList2 = listOf(
            listOf(
                ExcludeListItemItem("1", "3"),
                ExcludeListItemItem("1", "5"),
                ExcludeListItemItem("2", "3"),
                ExcludeListItemItem("4", "6")
            ),
            listOf(
                ExcludeListItemItem("1", "1"),
                ExcludeListItemItem("2", "3"),
                ExcludeListItemItem("2", "5"),
                ExcludeListItemItem("4", "1")
            ),
            listOf(
                ExcludeListItemItem("5", "1"),
                ExcludeListItemItem("5", "3")
            )
        )

        val outputList1 = listOf(
            listOf(
                Pair("1", "3"),
                Pair("2", "2")
            ),
            listOf(
                Pair("1", "1"),
                Pair("2", "3"),
                Pair("3", "1")
            )
        )

        val outputList2 = listOf(
            listOf(
                Pair("1", "3"),
                Pair("1", "5"),
                Pair("2", "3"),
                Pair("4", "6")
            ),
            listOf(
                Pair("1", "1"),
                Pair("2", "3"),
                Pair("2", "5"),
                Pair("4", "1")
            ),
            listOf(
                Pair("5", "1"),
                Pair("5", "3")
            )
        )

        val returnedList1 = viewModel.formatDataSet(inputList1)
        val returnedList2 = viewModel.formatDataSet(inputList2)

        assert(returnedList1 == outputList1)
        assert(returnedList2 == outputList2)
    }

    @Test
    fun givenEmptyList_whenFormatDataSetCalled_shouldReturnEmptyPairList() {
        val inputList = listOf<List<ExcludeListItemItem>>(
            listOf(),
            listOf()
        )

        val returnedList = viewModel.formatDataSet(inputList)
        assert(returnedList.isEmpty())
    }

    @Test
    fun givenList_whenConvertListToHashMapCalled_shouldReturnValidHashMap() {
        val inputList1 = mutableListOf(
            mutableListOf(
                Pair("1", "3"),
                Pair("2", "2")
            ),
            mutableListOf(
                Pair("1", "1"),
                Pair("2", "2"),
                Pair("3", "1")
            )
        )

        val inputList2 = mutableListOf(
            mutableListOf(
                Pair("1", "3"),
                Pair("1", "5"),
                Pair("2", "3"),
                Pair("4", "6")
            ),
            mutableListOf(
                Pair("1", "1"),
                Pair("2", "3"),
                Pair("2", "5"),
                Pair("4", "1")
            ),
            mutableListOf(
                Pair("5", "1"),
                Pair("5", "3")
            )
        )

        val outputMap1 = mapOf(
            Pair("1", "3") to listOf(Pair("2", "2")),
            Pair("2", "2") to listOf(Pair("1", "3"), Pair("1", "1"), Pair("3", "1")),
            Pair("1", "1") to listOf(Pair("2", "2"), Pair("3", "1")),
            Pair("3", "1") to listOf(Pair("1", "1"), Pair("2", "2"))
        )

        val outputMap2 = mapOf(
            Pair("1", "3") to listOf(Pair("1", "5"), Pair("2", "3"), Pair("4", "6")),
            Pair("1", "5") to listOf(Pair("1", "3"), Pair("2", "3"), Pair("4", "6")),
            Pair("2", "3") to listOf(
                Pair("1", "3"),
                Pair("1", "5"),
                Pair("4", "6"),
                Pair("1", "1"),
                Pair("2", "5"),
                Pair("4", "1")
            ),
            Pair("4", "6") to listOf(Pair("1", "3"), Pair("1", "5"), Pair("2", "3")),
            Pair("1", "1") to listOf(Pair("2", "3"), Pair("2", "5"), Pair("4", "1")),
            Pair("2", "5") to listOf(Pair("1", "1"), Pair("2", "3"), Pair("4", "1")),
            Pair("4", "1") to listOf(Pair("1", "1"), Pair("2", "3"), Pair("2", "5")),
            Pair("5", "1") to listOf(Pair("5", "3")),
            Pair("5", "3") to listOf(Pair("5", "1"))
        )

        val returnedMap1 = viewModel.convertListToHashMap(inputList1)
        val returnedMap2 = viewModel.convertListToHashMap(inputList2)
        assert(returnedMap1 == outputMap1)
        assert(returnedMap2 == outputMap2)
    }

    @Test
    fun givenEmptyList_whenConvertListToHashMapCalled_shouldReturnEmptyMap() {
        val inputList = mutableListOf<MutableList<Pair<String, String>>>(
            mutableListOf(),
            mutableListOf()
        )

        val returnedMap = viewModel.convertListToHashMap(inputList)
        assert(returnedMap.isEmpty())
    }

    @After
    fun tearDown() {
        viewModel.getMessageStringId().removeObserver(messageStringIdObserver)
        viewModel.getMessageString().removeObserver(messageStringObserver)
        viewModel.getGroupList().removeObserver(groupListObserver)
        viewModel.getIsLoading().removeObserver(loadingObserver)
    }
}