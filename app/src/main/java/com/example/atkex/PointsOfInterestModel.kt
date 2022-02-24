package com.example.atkex

class PointsOfInterestModel {
    var modelName: String? = null
    var distance: Int? = 0
    private var modelImage: Int = 0


    /** Return the name of the landmark */
    fun getNames(): String {
        return modelName.toString()
    }

    /** Set the name of the landmark */
    fun setNames(name: String) {
        this.modelName = name
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