package ru.vladik.dnevnikru.dnevnikapi.models.v2

import ru.vladik.dnevnikru.dnevnikapi.models.Entity
import ru.vladik.dnevnikru.dnevnikapi.models.Mood

data class MarkV2 (
        override val id: Long? = null, val person: Long? = null, val work: Long? = null,
        val lesson: Long? = null, val workType: Long? = null,
        val use_avg_calc: Boolean? = null, val textValue: String? = null, val number: String? = null,
        val  type: String? = null, val mood: Mood? = null
) : Entity