package ru.vladik.dnevnikru.dnevnikapi.models.v2

import ru.vladik.dnevnikru.dnevnikapi.models.Entity

data class WorkV2(
    override val id: Long? = null, val workType: Long? = null, val lesson: Long? = null,
    val eduGroup: Long? = null, val subjectId: Long? = null,
    val createdBy: Long? = null, val displayInJournal: Boolean? = null, val isImportant: Boolean? = null,
    val markCount: Int? = null, val periodNumber: Int? = null, val type: WorkType? = null,
    val markType: String? = null, val status: String? = null,
    val text: String? = null, val periodType: String? = null, val targetDate: String? = null,
    val sentDate: String? = null, val tasks: List<Any>? = null,
    val oneDriveLinks: List<Any>? = null, val files: List<Any>? = null
) : Entity {

}


