package be.ugent.myfestival.models

import com.google.firebase.storage.StorageReference
import java.time.LocalDateTime

class NewsfeedItem(val id: String, var time: LocalDateTime, var image: StorageReference?, var message: String, var title: String) {
}