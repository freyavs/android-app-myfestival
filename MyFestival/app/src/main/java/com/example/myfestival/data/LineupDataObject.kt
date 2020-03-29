package com.example.myfestival.data

class LineupDataObject {

    fun getData() : Lineup {
        val concertsMain = listOf<Concert>(
            Concert("Json Derulo", "18:30", "20:00"),
            Concert("Zwangere Guy", "20:30", "22:00")
        )
        val concertsForest = listOf<Concert>(
            Concert("Frank Ocean", "18:30", "20:00"),
            Concert("George Bucks", "20:30", "22:00")
        )
        val concertsHerbakker = listOf<Concert>(
            Concert("Eddy de Ketelaeare", "18:30", "22:00")
        )

        val Mainstage = Stage("Mainstage", concertsMain)
        val Forestage = Stage("Foreststage", concertsForest)
        val Herbakkerstage = Stage("Herbakkerstage", concertsHerbakker)

        val stages = listOf<Stage>(Mainstage, Forestage)

        val day1 = LineupDay("Donderdag", stages)
        val day2 = LineupDay("Vrijdag", listOf(Herbakkerstage))

        return Lineup(listOf(day1, day2))
    }
}