package com.example.atkex

class LeaderboardsModel {
    var name: String? = null
    var points: Int? = 0


    /** Return the name of the landmark */
    fun getNames(): String {
        return name.toString()
    }

    /** Set the name of the landmark */
    fun setNames(name: String) {
        this.name = name
    }

    /** Return the distance of the landmark */
    fun getPoints(): String {
        return points.toString()
    }

    /** Set the distance of the landmark */
    fun setPoints(distance: Int) {
        this.points = distance
    }
}