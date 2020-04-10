package be.ugent.myfestival.models

import java.time.LocalDateTime

class Concert(val artist: String, val start : LocalDateTime, val stop : LocalDateTime) {}

class Stage(val name: String, val concerts: List<Concert>) {}

