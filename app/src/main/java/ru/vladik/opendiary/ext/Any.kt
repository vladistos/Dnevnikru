package ru.vladik.opendiary.ext

fun Any?.isNotNull(): Boolean {return this != null}

fun Any?.isNull(): Boolean {return this == null}