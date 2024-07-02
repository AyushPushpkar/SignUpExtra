package com.example.signup

class User {
    var Name: String?=null
    var Password: String?=null
    var id: String?=null

    constructor(){}
    constructor(Name : String?, Password: String?, id: String?) {
        this.Name = Name
        this.Password = Password
        this.id = id
    }
}