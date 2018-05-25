package com.simple.simplejobfinder.models

data class JobItem(val id: String, val jobTitle: String, val company: String, val salary: String,
                   val location: String, val thumbnail: String) {
    constructor() : this("", "", "", "", "","")
}
