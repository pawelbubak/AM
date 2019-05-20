package com.example.lab7.model

import lombok.ToString

@ToString
class User(
    var id: Int = 0,
    var name: String? = "",
    var username: String? = "",
    var password: String? = "",
    var points: Int = 0
) {
    override fun toString(): String {
        return "User(id=$id, name=$name, username=$username, password=$password)"
    }
}