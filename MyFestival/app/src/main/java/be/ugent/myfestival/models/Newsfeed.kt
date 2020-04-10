package be.ugent.myfestival.models

import com.google.firebase.storage.StorageReference

class NewsfeedItem(val time: String, val image: StorageReference?, val message: String, val title: String) {
}