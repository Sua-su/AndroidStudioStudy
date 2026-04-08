package com.example.contacts.data

data class Contact (
    val id:Int,
    val name: String,
    val personalPhoneNumber: String,
    val officePhoneNumber: String,
    val email: String
)

val members = listOf(
    Contact(1, "Erin Lopez","010-1111-1111", "02-1234-1234","erin.lopez@example.com"),
    Contact(2, "Oscar Thomas", "010-2222-2222", "053-111-2222", "oscar.thomas@example.com"),
    Contact(3, "Isaiah Perry", "010-3333-3333", "02-4321-4321","isaiah.perry@example.com"),
    Contact(4, "Aubree Elliott", "010-4444-4444", "054-123-4567","aubree.elliott@example.com"),
    Contact(5, "Billy Terry", "010-5555-5555", "053-0987-0987", "billy.terry@example.com"),
    Contact(6, "Josephine Barrett", "010-6666-6666", "02-9876-9876", "josephine.barrett@example.com"),
    Contact(7, "Connor Kelly","010-7777-7777","031-1234-1234", "connor.kelly@example.com")
)

fun getContacts() = members