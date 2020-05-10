package be.ugent.myfestival.integrationtests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import be.ugent.myfestival.data.FestivalRepository
import be.ugent.myfestival.models.FestivalChooser
import be.ugent.myfestival.viewmodels.FestivalChooserViewModel
import be.ugent.myfestival.viewmodels.FestivalViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.invocation.InvocationOnMock

class FestivalViewModelTests {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database : FirebaseDatabase
    private lateinit var databaseRef : DatabaseReference
    private lateinit var storage : StorageReference

    private lateinit var repository : FestivalRepository
    private lateinit var viewmodel : FestivalViewModel

    private lateinit var  logoRef : StorageReference

    val message = "message"

    @Before
    fun setup(){
        database = mock()
        storage = mock()
        databaseRef = mock()
        logoRef = mock()
        repository = FestivalRepository(database,storage)
        viewmodel = FestivalViewModel(repository)

        whenever(database.getReference(any())).thenReturn(databaseRef)
        whenever(databaseRef.child(any())).thenReturn(databaseRef)

        //newsfeed methode mock
        whenever(databaseRef.addChildEventListener(any()))
            .doAnswer { invocation: InvocationOnMock ->
                if (repository.newsfeedLoaded) {
                    repository.newMessageTitle.apply { postValue(message) }
                }
                null
            }

        //logo methode mock
        whenever(databaseRef.addValueEventListener(any()))
            .doAnswer { invocation: InvocationOnMock ->
                repository.logo.apply{postValue(logoRef)}
                null
            }
    }

    @Test
    fun getNewMessageIsTriggered_WhenInitalNewsfeedHasLoaded() {
        val messageObserver = mock<Observer<String>>()
        viewmodel.getNewMessageTitle().observeForever(messageObserver)

        //initiele newsfeed is geladen
        repository.newsfeedLoaded = true

        repository.getNewsfeedItems()
        verify(messageObserver).onChanged(message)
    }

    @Test
    fun getNewMessageIsNotTriggered_WhenInitalNewsfeedHasNotLoaded() {
        val messageObserver = mock<Observer<String>>()
        viewmodel.getNewMessageTitle().observeForever(messageObserver)

        //initiele newsfeed is nog niet geladen
        repository.newsfeedLoaded = false

        repository.getNewsfeedItems()
        verify(messageObserver,never()).onChanged(message)
    }


    @Test
    fun getLogoIsTriggeredAndMakesListenerOnFirstTimeCall() {
        val logoObserver = mock<Observer<StorageReference>>()
        viewmodel.getLogo().observeForever(logoObserver)
        verify(logoObserver).onChanged(logoRef)
        Assert.assertNotNull(repository.logoListener)
    }

}