package com.example.atkex

class PointsOfInterestModel {
    var name: String? = null
    var info: String? = null
    var long: String? = null
    var lat: String? = null
    var distance: Int? = 0
    private var modelImage: Int = 0


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
    fun getImages(): Int {
        return modelImage
    }

    /** Set the image of the landmark */
    fun setImages(image_drawable: Int) {
        this.modelImage = image_drawable
    }
}