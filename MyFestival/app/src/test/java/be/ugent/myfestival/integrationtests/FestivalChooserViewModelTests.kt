package be.ugent.myfestival

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import be.ugent.myfestival.data.FestivalRepository
import be.ugent.myfestival.models.FestivalChooser
import be.ugent.myfestival.viewmodels.FestivalChooserViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.invocation.InvocationOnMock


class FestivalChooserViewModelTests {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    private lateinit var database : FirebaseDatabase
    private lateinit var databaseRef : DatabaseReference
    private lateinit var storage : StorageReference

    private lateinit var repository : FestivalRepository
    private lateinit var viewmodel : FestivalChooserViewModel
    private lateinit var  logoRef : StorageReference
    private lateinit var  festival1 : FestivalChooser
    private lateinit var  festival2 : FestivalChooser
    private lateinit var festival3 : FestivalChooser

    private lateinit var festivals: List<FestivalChooser>


    @Before
    fun setup(){
        database = mock()
        storage = mock()
        databaseRef = mock()
        repository = FestivalRepository(database,storage)
        viewmodel = FestivalChooserViewModel(repository)

        logoRef = mock()
        festival1 = FestivalChooser("f1", "Festival1", logoRef)
        festival2 = FestivalChooser("f2", "Festival2", logoRef)
        festival3 = FestivalChooser("f3", "Festival3", logoRef)

        festivals = listOf(festival1,festival2,festival3)

        whenever(database.reference).thenReturn(databaseRef)
        whenever(storage.child(any())).thenReturn(logoRef)

        whenever(databaseRef.addValueEventListener(any()))
            .doAnswer { invocation: InvocationOnMock ->
                repository.festivalList.apply{postValue(festivals)}
                null
            }

    }

    //filtering wordt getest in unit tests

    @Test
    fun getFestivalsReadsFestivalsFromFirebaseAndAddsListener() {
        val festivalsObserver = mock<Observer<List<FestivalChooser>>>()
        viewmodel.getFestivals().observeForever(festivalsObserver)
        verify(festivalsObserver).onChanged(festivals)
        verify(databaseRef).addValueEventListener(any())
    }

    @Test
    fun getFestivalsDoesntAddListenerToFirebase_WhenFestivalListisNotNull() {
        repository.festivalList.postValue(festivals)
        val festivalsObserver = mock<Observer<List<FestivalChooser>>>()
        viewmodel.getFestivals().observeForever(festivalsObserver)
        verify(festivalsObserver).onChanged(any())
        verify(databaseRef, never()).addValueEventListener(any())
    }

}