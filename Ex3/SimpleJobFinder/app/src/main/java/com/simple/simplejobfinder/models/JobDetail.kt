package com.simple.simplejobfinder.models

data class JobDetail(val id: String, val jobTitle: String, val company: String, val location: String,
                     val experience: String, val majors: String, val salary: String, val expired: String,
                     val benefits: String, val description: String, val requirements: String,
                     val otherInfo: String, val introduce: String, val position: String,
                     val thumbnail: String) {
    constructor() : this("", "", "", "", "", "",
            "", "", "", "", "", "",
            "", "", "")

}