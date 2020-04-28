package be.ugent.myfestival

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import be.ugent.myfestival.data.FestivalRepository
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FestivalChooserViewModelUnitTests {
    @get:Rule
    @Suppress("unused")
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: FestivalRepository

    private lateinit var database: FirebaseDatabase
    private lateinit var storage: StorageReference

    private lateinit var reference: DatabaseReference
    private lateinit var childReference: DatabaseReference

    @Before
    fun setup() {
        database = mock()
        storage = mock()
        reference = mock()
        childReference = mock()

        whenever(database.getReference(any())).thenReturn(reference)
        whenever(database.getReference()).thenReturn(reference)
        whenever(reference.child(any())).thenReturn(childReference)
        whenever(childReference.orderByKey()).thenReturn(childReference)

        repository = FestivalRepository(database,storage)
    }
    fun fillRepository(repo: FestivalRepository){
        repo.festivalList.value = mutableListOf(mock())
    }
    @Test
    fun test(){
        fillRepository(repository)
        println(repository.festivalList.value)
    }
}