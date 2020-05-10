package be.ugent.myfestival


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class FirebaseUnitTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun FirebaseIsConfiguredCorrectly() {
        val db = FirebaseDatabase.getInstance()
        Assert.assertNotNull(db)
        Assert.assertEquals(FirebaseApp.getInstance().options.databaseUrl, db.reference.toString())
    }

}