package be.ugent.myfestival

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
//TODO ?

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
        whenever(reference.child(any())).thenReturn(childReference)

        repository = FestivalRepository(database,storage)

        repository.setId(testId)
    }

    @Test
    fun getFestivalNameCallsDatabaseListeners_whenNameIsNotSet() {
        repository.getFestivalName()
        verify(database).getReference(testId)
        verify(reference).child("name")
    }


}

