package com.example.myfestival.data

class Concert(val artist: String, val start : String, val stop : String) {}

class Stage(val name: String, val concerts: List<Concert>) {}

class LineupDay(val day: String, val stages: List<Stage>){}

class Lineup(val days: List<LineupDay>){}
