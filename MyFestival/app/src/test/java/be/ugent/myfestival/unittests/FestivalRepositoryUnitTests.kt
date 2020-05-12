package be.ugent.myfestival.unittests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import be.ugent.myfestival.data.FestivalRepository
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FestivalRepositoryUnitTests {
    @get:Rule
    @Suppress("unused")
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: FestivalRepository

    private lateinit var database: FirebaseDatabase
    private lateinit var storage: StorageReference

    private lateinit var reference: DatabaseReference

    private val testId = "123abc"


    @Before
    fun setup() {
        database = mock()
        storage = mock()
        reference = mock()

        whenever(database.getReference(any())).thenReturn(reference)
        whenever(database.reference).thenReturn(reference)
        whenever(reference.orderByChild(any())).thenReturn(reference)
        whenever(reference.child(any())).thenReturn(reference)
        whenever(reference.orderByKey()).thenReturn(reference)

        repository = FestivalRepository(database,storage)

        repository.setId(testId)
    }

    fun fillRepository(repo: FestivalRepository){
        repo.name.value =  "fest"
        repo.newsfeed.value = mutableListOf(mock())
        repo.foodstands.value  = mutableListOf(mock())
        repo.lineupstages.value = mutableListOf(mock())
        repo.logo.value = storage
        repo.festivalList.value = mutableListOf(mock())
        repo.coords.value = mock()
        repo.foodstandsCoords.value = mock()
        repo.concertsCoords.value = mock()
    }

    @Test
    fun gettersCallDatabase_whenFieldsAreNotSet() {
        repository.initiateData()

        //6x en in newsfeed wordt er 2x opgeroepen
        verify(database, times(7)).getReference(testId)
    }

    @Test
    fun gettersDontCallDatabase_whenFieldsAreSet() {
        fillRepository(repository)
        repository.initiateData()

        verify(database, never()).getReference(testId)
    }

    @Test
    fun resetSetsAllValuesNull() {
        fillRepository(repository)
        repository.reset("")
        Assert.assertNull(repository.name.value)
        Assert.assertNull(repository.logo.value)
        Assert.assertNull(repository.lineupstages.value)
        Assert.assertNull(repository.foodstands.value)
        Assert.assertTrue(repository.newsfeed.value.isNullOrEmpty())
        Assert.assertNull(repository.concertsCoords.value)
        Assert.assertNull(repository.foodstandsCoords.value)
        Assert.assertNull(repository.coords.value)
    }

    @Test
    fun allGettersAreCalled_AfterReset() {
        repository.reset("")

        //6 getters, newsfeed roept firebase 2 keer op
        verify(database, times(7)).getReference(testId)
    }


}

