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
    private lateinit var childReference:DatabaseReference

    private val testId = "123abc"


    @Before
    fun setup() {
        database = mock()
        storage = mock()
        reference = mock()
        childReference = mock()

        whenever(database.getReference(any())).thenReturn(reference)
        whenever(database.reference).thenReturn(reference)
        whenever(reference.child(any())).thenReturn(childReference)
        whenever(childReference.orderByKey()).thenReturn(childReference)

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
    }

    @Test
    fun gettersCallDatabase_whenFieldsAreNotSet() {
        repository.getFestivalName()
        repository.getFestivalLogo()
        repository.getFestivals()
        repository.getFoodstandList()
        repository.getLineup()
        repository.getNewsfeedItems()

        verify(database, times(6)).getReference(testId)
        verify(database).reference
    }

    @Test
    fun gettersDontCallDatabase_whenFieldsAreSet() {
        fillRepository(repository)
        repository.getFestivalName()
        repository.getFestivalLogo()
        repository.getFestivals()
        repository.getFoodstandList()
        repository.getLineup()
        repository.getNewsfeedItems()

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
    }

    @Test
    fun getFestivalNameCallsDatabase_AfterResetOfValues() {
        repository.reset("")

        //moet 4x opgeroepen worden voor foodstands, linup, logo en kaart
        verify(database, times(4)).getReference(testId)
    }


}

