package be.ugent.myfestival.integrationtests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import be.ugent.myfestival.data.FestivalRepository
import be.ugent.myfestival.models.FestivalChooser
import be.ugent.myfestival.viewmodels.FestivalChooserViewModel
import be.ugent.myfestival.viewmodels.FestivalViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Rule
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

    @Before
    fun setup(){
        database = mock()
        storage = mock()
        databaseRef = mock()
        repository = FestivalRepository(database,storage)
        viewmodel = FestivalViewModel(repository)

        whenever(database.reference).thenReturn(databaseRef)
        whenever(storage.child(any())).thenReturn(logoRef)

        whenever(databaseRef.addValueEventListener(any()))
            .doAnswer { invocation: InvocationOnMock ->
                repository.festivalList.apply{postValue(null)}
                null
            }

    }

}