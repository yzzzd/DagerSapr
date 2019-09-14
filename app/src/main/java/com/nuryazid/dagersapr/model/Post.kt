package com.nuryazid.dagersapr.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

/**
 * Class which provides a model for post
 * @constructor Sets all properties of the post
 * @property userId the unique identifier of the author of the post
 * @property id the unique identifier of the post
 * @property title the title of the post
 * @property body the content of the post
 */
@RealmClass
open class Post : RealmObject() {
    var userId: Int? = null
    @PrimaryKey
    var id: Int? = null
    var title: String? = null
    var body: String? = null
}