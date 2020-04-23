package be.ugent.myfestival.models

import java.time.LocalDateTime

class Concert(val id: String, val artist: String, val start : LocalDateTime, val stop : LocalDateTime) {}

class Stage(val id: String, val name: String, val concerts: List<Concert>) {}

