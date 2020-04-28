package be.ugent.myfestival


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import be.ugent.myfestival.data.FestivalRepository
import be.ugent.myfestival.models.FestivalChooser
import be.ugent.myfestival.viewmodels.FestivalViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.notNull
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito



//hoe integration tests met firebase
//https://stackoverflow.com/questions/36014865/how-do-i-write-firebase-android-instrumentation-tests
//https://github.com/firebase/firebase-android-sdk/blob/master/firebase-database/src/androidTest/java/com/google/firebase/database/FirebaseDatabaseTest.java

//@RunWith(AndroidJUnit4::class)
class FestivalViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    private lateinit var database : FirebaseDatabase
    private lateinit var storage : StorageReference

    private lateinit var repository : FestivalRepository
    private lateinit var viewmodel : FestivalViewModel


   /*  @Before
     fun setup(){
     }*/

    /*@Test
    fun testDatabase() {
        val db = FirebaseDatabase.getInstance()
        //Assert.assertNotNull(db)
        //Assert.assertEquals(
        //  FirebaseApp.getInstance().options.databaseUrl, db.reference.toString())
    }*/

    @Test
    fun getFestivalsReadsFestivals() {
        val appContext = getInstrumentation().targetContext
        Firebase.initialize(appContext)
        database = Mockito.spy(Firebase.database)
        storage = Mockito.spy(Firebase.storage.reference)

        repository = FestivalRepository(database,storage)
        viewmodel = FestivalViewModel(repository)
        val festivalsObserver = mock<Observer<List<FestivalChooser>>>()

        verify(festivalsObserver).onChanged(notNull())

    }

}