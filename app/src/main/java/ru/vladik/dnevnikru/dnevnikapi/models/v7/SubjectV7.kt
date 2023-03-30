package ru.vladik.dnevnikru.dnevnikapi.models.v7

data class SubjectV7(
    val id: Long = 0,
    val knowledgeArea: String? = null,
    val metadata: MetaData? = null,
    val name: String? = null,
    val subjectMood: String? = null
)