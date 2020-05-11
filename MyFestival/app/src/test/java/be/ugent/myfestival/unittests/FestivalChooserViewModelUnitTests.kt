package be.ugent.myfestival.unittests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import be.ugent.myfestival.data.FestivalRepository
import be.ugent.myfestival.models.FestivalChooser
import be.ugent.myfestival.viewmodels.FestivalChooserViewModel
import com.google.firebase.storage.StorageReference
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FestivalChooserViewModelUnitTests {
    @get:Rule
    @Suppress("unused")
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    val logoRef : StorageReference = mock()
    val festival1 = FestivalChooser("f1", "Festival1", logoRef)
    val festival2 = FestivalChooser("f2", "Festival2", logoRef)
    val festival3 = FestivalChooser("f3", "AnotherName", logoRef)

    val festivals: List<FestivalChooser> = listOf(festival1,festival2,festival3)

    private lateinit var repo: FestivalRepository
    private lateinit var festivalChooserViewModel: FestivalChooserViewModel


    @Before
    fun setup() {
        repo = mock()
        whenever(repo.getFestivals()).thenReturn(MutableLiveData((festivals)))
        festivalChooserViewModel = FestivalChooserViewModel(repo)
    }
    @Test
    fun testNoSearch(){
        val mockObserver = mock<Observer<List<FestivalChooser>>>()
        festivalChooserViewModel.getFestivals().observeForever(mockObserver)
        festivalChooserViewModel.changeSearchValue("")
        verify(mockObserver).onChanged(festivals)
    }
    @Test
    fun testMultipleWithLookAlikeNames(){
        val mockObserver = mock<Observer<List<FestivalChooser>>>()
        festivalChooserViewModel.getFestivals().observeForever(mockObserver)
        festivalChooserViewModel.changeSearchValue("Festival")
        verify(mockObserver).onChanged(listOf(festival1,festival2))
    }
    @Test
    fun testCorrectName(){
        val mockObserver = mock<Observer<List<FestivalChooser>>>()
        festivalChooserViewModel.getFestivals().observeForever(mockObserver)
        festivalChooserViewModel.changeSearchValue("AnotherName")
        verify(mockObserver).onChanged(listOf(festival3))
    }
    @Test
    fun testNameAllCaps(){
        val mockObserver = mock<Observer<List<FestivalChooser>>>()
        festivalChooserViewModel.getFestivals().observeForever(mockObserver)
        festivalChooserViewModel.changeSearchValue("ANOTHERNAME")
        verify(mockObserver).onChanged(listOf(festival3))
    }
    @Test
    fun testWrongName(){
        val mockObserver = mock<Observer<List<FestivalChooser>>>()
        festivalChooserViewModel.getFestivals().observeForever(mockObserver)
        festivalChooserViewModel.changeSearchValue("sdjkfhsdjkfhjksdfh")
        verify(mockObserver).onChanged(listOf())
    }
}