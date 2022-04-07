package com.example.atkex

/**
 * Model class for leaderboards
 */
class LeaderboardsModel {
    var name: String? = null
    var point: Int? = 0


    /** Return the name of the landmark */
    fun getNames(): String {
        return name.toString()
    }

    /** Set the name of the landmark */
    fun setNames(name: String) {
        this.name = name
    }

    /** Return the distance of the landmark */
    fun getPoints(): Int? {
        return point
    }

    /** Set the distance of the landmark */
    fun setPoints(p: Int) {
        this.point = p
    }
}