package ru.vladik.dnevnikru.ext

fun Any?.isNotNull(): Boolean {return this != null}

fun Any?.isNull(): Boolean {return this == null}