package com.example.listpractice.data

data class Member (
    val name:String,
    val email:String,
    val photo:String,
)
val members = listOf<Member>(
    Member("Erin Lopez","erin.lopez@example.com","https://randomuser.me/api/portraits/women/40.jpg"),
    Member("Oscar Thomas", "oscar.thomas@example.com", "https://randomuser.me/api/portraits/men/72.jpg"),
    Member("Isaiah Perry", "isaiah.perry@example.com", "https://randomuser.me/api/portraits/men/14.jpg"),
    Member("Aubree Elliott", "aubree.elliott@example.com", "https://randomuser.me/api/portraits/women/33.jpg"),
    Member("Billy Terry", "billy.terry@example.com", "https://randomuser.me/api/portraits/men/31.jpg"),
    Member("Josephine Barrett", "josephine.barrett@example.com", "https://randomuser.me/api/portraits/women/54.jpg"),
    Member("Connor Kelly", "connor.kelly@example.com", "https://randomuser.me/api/portraits/men/89.jpg")
)