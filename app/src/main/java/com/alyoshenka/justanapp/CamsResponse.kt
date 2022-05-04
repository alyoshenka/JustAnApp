package com.alyoshenka.justanapp

import com.google.gson.annotations.SerializedName


data class CamsResponse (

    @SerializedName("Features" ) var Features : ArrayList<Features> = arrayListOf()

)

data class Cameras (

    @SerializedName("Id"          ) var Id          : String? = null,
    @SerializedName("Description" ) var Description : String? = null,
    @SerializedName("ImageUrl"    ) var ImageUrl    : String? = null,
    @SerializedName("Type"        ) var Type        : String? = null

)

data class Features (

    @SerializedName("PointCoordinate" ) var PointCoordinate : ArrayList<Double>  = arrayListOf(),
    @SerializedName("Cameras"         ) var Cameras         : ArrayList<Cameras> = arrayListOf()

)

