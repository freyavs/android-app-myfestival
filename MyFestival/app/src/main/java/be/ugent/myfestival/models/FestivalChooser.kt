package be.ugent.myfestival.models

import com.google.firebase.storage.StorageReference

data class FestivalChooser(val id: String, val name: String, val logoRef: StorageReference)