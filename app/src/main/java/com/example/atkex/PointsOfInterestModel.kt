package com.example.atkex

/**
 * Model for points of interest
 */
class PointsOfInterestModel {
    var name: String? = null
    var info: String? = null
    var long: String? = null
    var lat: String? = null
    var distance: Int? = 0
    var img: String? = null

    /** Return the name of the landmark */
    fun getNames(): String {
        return name.toString()
    }

    /** Set the name of the landmark */
    fun setNames(name: String) {
        this.name = name
    }

    /** Get the info of the landmark */
    fun getInfos(): String {
        return info.toString()
    }

    /** Set the info of the landmark */
    fun setInfos(info: String) {
        this.info = info
    }

    /** Return the distance of the landmark */
    fun getDistances(): String {
        return distance.toString()
    }

    /** Set the distance of the landmark */
    fun setDistances(distance: Int) {
        this.distance = distance
    }

    /** Return the image of the landmark */
    fun getImages(): String {
        return img.toString()
    }

    /** Set the image of the landmark */
    fun setImages(image: String) {
        this.img = image
    }
}