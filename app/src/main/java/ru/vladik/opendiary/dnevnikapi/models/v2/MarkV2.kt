package ru.vladik.opendiary.dnevnikapi.models.v2

import ru.vladik.opendiary.dnevnikapi.models.Entity
import ru.vladik.opendiary.dnevnikapi.models.Mood

data class MarkV2 (
        override val id: Long? = null, val person: Long? = null, val work: Long? = null,
        val lesson: Long? = null, val workType: Long? = null,
        val use_avg_calc: Boolean? = null, val textValue: String? = null, val number: String? = null,
        val  type: String? = null, val mood: Mood? = null
) : Entity