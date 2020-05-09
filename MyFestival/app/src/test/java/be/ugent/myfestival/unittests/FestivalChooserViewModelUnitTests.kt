package be.ugent.myfestival.unittests

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import be.ugent.myfestival.data.FestivalRepository
import be.ugent.myfestival.models.FestivalChooser
import be.ugent.myfestival.viewmodels.FestivalChooserViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.storage.StorageReference
import com.nhaarman.mockitokotlin2.any
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
    fun testNameFestival(){
        val mockObserver = mock<Observer<List<FestivalChooser>>>()
        festivalChooserViewModel.getFestivals().observeForever(mockObserver)
        festivalChooserViewModel.changeSearchValue("Festival")
        verify(mockObserver).onChanged(listOf(festival1,festival2))
    }
    @Test
    fun testFestival1(){
        val mockObserver = mock<Observer<List<FestivalChooser>>>()
        festivalChooserViewModel.getFestivals().observeForever(mockObserver)
        festivalChooserViewModel.changeSearchValue("Festival1")
        verify(mockObserver).onChanged(listOf(festival1))
    }
    @Test
    fun testFestival2(){
        val mockObserver = mock<Observer<List<FestivalChooser>>>()
        festivalChooserViewModel.getFestivals().observeForever(mockObserver)
        festivalChooserViewModel.changeSearchValue("Festival2")
        verify(mockObserver).onChanged(listOf(festival2))
    }
    @Test
    fun testAnotherName(){
        val mockObserver = mock<Observer<List<FestivalChooser>>>()
        festivalChooserViewModel.getFestivals().observeForever(mockObserver)
        festivalChooserViewModel.changeSearchValue("AnotherName")
        verify(mockObserver).onChanged(listOf(festival3))
    }
}