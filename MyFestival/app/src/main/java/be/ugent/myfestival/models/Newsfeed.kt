package be.ugent.myfestival.models

import com.google.firebase.storage.StorageReference
import java.time.LocalDateTime

class NewsfeedItem(val id: String, val time: LocalDateTime, val image: StorageReference?, val message: String, val title: String) {
}