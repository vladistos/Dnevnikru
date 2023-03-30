package ru.vladik.dnevnikru.ext

import ru.vladik.dnevnikru.dnevnikapi.models.Entity

fun <T : Entity> Iterable<T>.toIdValueMap(): HashMap<Long, T> {
    val a = HashMap<Long, T>()
    for (ent in this) {
        if (ent.id == null) {
            continue
        }
        a[ent.id!!] = ent
    }
    return a
}