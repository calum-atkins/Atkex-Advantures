package com.example.atkex

class ReviewsModel {
    var name: String? = null
    var comment: String? = null


    /** Return the name of the landmark */
    fun getNames(): String {
        return name.toString()
    }

    /** Set the name of the landmark */
    fun setNames(name: String) {
        this.name = name
    }

    /** Return the distance of the landmark */
    fun getComments(): String {
        return comment.toString()
    }

    /** Set the distance of the landmark */
    fun setComments(c: String) {
        this.comment = c
    }
}