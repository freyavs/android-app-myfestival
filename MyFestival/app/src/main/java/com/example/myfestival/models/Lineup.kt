package com.example.myfestival.models

class Concert(val artist: String, val start : String, val stop : String) {}

class Stage(val name: String, val concerts: List<Concert>) {}

class LineupDay(val day: String, val stages: List<Stage>){}


//todo: lineup moet weg moet direct lijst worden van linupdays
class Lineup(val days: List<LineupDay>){}
