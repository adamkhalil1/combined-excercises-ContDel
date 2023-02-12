package com.docdate

import android.net.Uri

class Upload {
    var name: String? = null
    var imageUrl: String? = null

    constructor() {
        //empty constructor needed
    }

    constructor(name: String, imageUrl: Uri?) {
        var name = name
        if (name.trim { it <= ' ' } == "") {
            name = "No Name"
        }
        this.name = name
        this.imageUrl = imageUrl.toString()
    }
}