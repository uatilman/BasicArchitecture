package ru.otus.basicarchitecture.model

data class Interest (
    val id: Int,
    val title: String,
)

val interests = setOf(
    Interest(1, "IT"),
    Interest(2, "Sport"),
    Interest(3, "Music"),
    Interest(4, "Reading"),
    Interest(5, "Travel"),
    Interest(6, "Photography"),
    Interest(7, "Cooking"),
    Interest(8, "Dancing"),
    Interest(9, "Art"),
    Interest(10, "Gaming"),
    Interest(11, "Movies"),
    Interest(12, "Fitness"),
    Interest(13, "Yoga"),
    Interest(14, "Fashion"),
    Interest(15, "Programming"),
    Interest(16, "Science"),
    Interest(17, "Nature"),
    Interest(18, "Cars"),
    Interest(19, "History"),
    Interest(20, "Languages"),
    Interest(21, "Writing")
)
