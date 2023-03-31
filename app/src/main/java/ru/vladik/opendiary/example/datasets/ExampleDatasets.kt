package ru.vladik.opendiary.example.datasets

import ru.vladik.opendiary.util.NonNullableApiUserImpl
import ru.vladik.opendiary.dnevnikapi.models.Mood
import ru.vladik.opendiary.dnevnikapi.models.extended.ExtendedLesson
import ru.vladik.opendiary.dnevnikapi.models.v2.*
import ru.vladik.opendiary.dnevnikapi.models.v7.*

object ExampleDatasets {

    enum class Subjects(val title: String) {
        MATHS("Математика"),
        RUSSIAN("Русский"),
        IT_TECHNOLOGIES("Информатика"),
        PHYSICAL_CULTURE("Физ-ра"),
        ENGLISH("Английский"),
        HISTORY("История")
    }

    val ExampleMarksData = mutableListOf(
        createExtendedMark(
            markId = 1,
            personId = 1,
            workId = 1,
            lessonId = 1,
            workTypeId = 1,
            textValue = "4",
            mood = Mood.GOOD,
            subjectName = Subjects.MATHS.title
        ),
        createExtendedMark(
            markId = 2,
            personId = 1,
            workId = 2,
            lessonId = 2,
            workTypeId = 1,
            textValue = "3",
            mood = Mood.AVERAGE,
            subjectName = Subjects.RUSSIAN.title
        ),
        createExtendedMark(
            markId = 3,
            personId = 1,
            workId = 3,
            lessonId = 3,
            workTypeId = 2,
            textValue = "2",
            mood = Mood.BAD,
            subjectName = Subjects.IT_TECHNOLOGIES.title
        ),
        createExtendedMark(
            markId = 4,
            personId = 1,
            workId = 4,
            lessonId = 4,
            workTypeId = 2,
            textValue = "4",
            mood = Mood.AVERAGE,
            subjectName = Subjects.PHYSICAL_CULTURE.title
        ),
        createExtendedMark(
            markId = 5,
            personId = 1,
            workId = 5,
            lessonId = 5,
            workTypeId = 1,
            textValue = "4",
            mood = Mood.GOOD,
            subjectName = Subjects.ENGLISH.title
        ),
        createExtendedMark(
            markId = 6,
            personId = 1,
            workId = 6,
            lessonId = 6,
            workTypeId = 2,
            textValue = "3",
            mood = Mood.AVERAGE,
            subjectName = Subjects.HISTORY.title
        ),
        createExtendedMark(
            markId = 7,
            personId = 1,
            workId = 7,
            lessonId = 7,
            workTypeId = 1,
            textValue = "2",
            mood = Mood.BAD,
            subjectName = Subjects.MATHS.title
        ),
        createExtendedMark(
            markId = 8,
            personId = 1,
            workId = 8,
            lessonId = 8,
            workTypeId = 2,
            textValue = "4",
            mood = Mood.AVERAGE,
            subjectName = Subjects.RUSSIAN.title
        ),
        createExtendedMark(
            markId = 9,
            personId = 1,
            workId = 9,
            lessonId = 9,
            workTypeId = 2,
            textValue = "5",
            mood = Mood.GOOD,
            subjectName = Subjects.IT_TECHNOLOGIES.title
        ),
        createExtendedMark(
            markId = 10,
            personId = 1,
            workId = 10,
            lessonId = 10,
            workTypeId = 1,
            textValue = "3",
            mood = Mood.AVERAGE,
            subjectName = Subjects.HISTORY.title
        )
    )

    private fun getRandomString(strings: List<String>): String {
        return strings.random()
    }

    val sentences = listOf(
        "Сегодня у нас был очень продуктивный урок. Ученики проявили большой интерес и задали много вопросов.",
        "Мы провели интересный эксперимент на уроке физики. Все ученики были вовлечены и получили много полезной информации.",
        "На уроке русского языка мы изучали новые слова и правила грамматики. Ученики проявили большой трудолюбие и успешно справились с заданиями."
    )

    fun getRandomText(): String {
        return "${getRandomString(sentences)} ${getRandomString(sentences)}"
    }

    val ExampleApiUser = NonNullableApiUserImpl(
        uId = 1234567890,
        personId = 987654321,
        firstName = "Максим",
        lastName = "Капустин",
        photo = "https://img.freepik.com/free-photo/fun-3d-illustration-american-referee_183364-81231.jpg",
        token = "abc123xyz",
        login = "johndoe",
        password = "pa\$w0rd",
        schoolId = 456,
        eduGroup = 789
    )

    val ExampleFeedPostsData = mutableListOf(
        FeedInfo(
            content = FeedPost(
                id = 1,
                title = "Новый урок по математике",
                authorFirstName = "Иван",
                authorMiddleName = "Петрович",
                authorLastName = "Сидоров",
                authorImageUrl = "https://www.w3schools.com/howto/img_avatar.png",
                text = getRandomText()
            ),
            timeStamp = 2475025566
        ),
        FeedInfo(
            content = FeedPost(
                id = 2,
                title = "Конкурс чтецов",
                authorFirstName = "Елена",
                authorMiddleName = "Владимировна",
                authorLastName = "Иванова",
                authorImageUrl = "https://www.w3schools.com/howto/img_avatar.png",
                text = getRandomText()
            ),
            timeStamp = 2475025566
        ),
        FeedInfo(
            content = FeedPost(
                id = 3,
                title = "Экскурсия в музей",
                authorFirstName = "Сергей",
                authorMiddleName = "Андреевич",
                authorLastName = "Кузнецов",
                authorImageUrl = "https://www.w3schools.com/howto/img_avatar.png",
                text = getRandomText()
            ),
            timeStamp = 2475025566
        ),

        FeedInfo(
             content = FeedPost(
                id = 1,
                eventKey = "new_announcement",
                topicEventKey = "homework",
                topicLogoUrl = "https://example.com/homework_logo.png",
                eventUrl = "https://example.com/homeworks/1",
                eventSign = "Добавлено новое задание",
                title = "Домашнее задание по математике",
                subtitle = "Класс 7Б",
                text = "Дорогие ученики! Ваше домашнее задание на сегодня - решить задачи 1-5 на странице 42 в учебнике по математике. Удачи вам!",
                createdDateTime = "2022-03-28T10:00:00+03:00",
                commentsCount = 0,
                authorImageUrl = "https://example.com/teacher_avatars/john_smith.png",
                authorFirstName = "Иван",
                authorMiddleName = "Петрович",
                authorLastName = "Сидоров",
                authorName = "Сидоров Иван Петрович",
                previewUrl = null,
                attachmentFiles = emptyList(),
                isNew = true,
                unsubscribeIsPossible = true,
                publicClubId = null,
                messengerEntryPoint = null
            ),
            timeStamp = 2475025566
        )


    )

    val ExampleSchedule = mutableListOf(
        ExtendedLesson(
            DaybookLessonNoPayment(
                id = 1,
                date = 1649798400000,
                number = 1,
                theme = "Введение в программирование",
                subject = SubjectV7(
                    id = 101,
                    knowledgeArea = "Программирование",
                    name = "Основы программирования"
                ),
                teacher = LessonTeacher(
                    firstName = "Иван",
                    lastName = "Иванов",
                    middleName = "Иванович"
                )
            ),
            marks = listOf(
                MarkV2(
                    id = 1,
                    textValue = "2",
                    mood = Mood.BAD
                ),
                MarkV2(
                    id = 2,
                    textValue = "3",
                    mood = Mood.AVERAGE
                )
            )
        ),
        ExtendedLesson(
            DaybookLessonNoPayment(
                id = 2,
                date = 1649884800000,
                number = 2,
                theme = "Циклы",
                subject = SubjectV7(
                    id = 101,
                    knowledgeArea = "Программирование",
                    name = "Основы программирования"
                ),
                teacher = LessonTeacher(
                    firstName = "Петр",
                    lastName = "Петров",
                    middleName = "Петрович"
                )
            ),
            marks = listOf(
                MarkV2(
                    id = 3,
                    textValue = "4",
                    mood = Mood.GOOD
                ),
                MarkV2(
                    id = 4,
                    textValue = "5",
                    mood = Mood.GOOD
                )
            )
        ),
        ExtendedLesson(
            DaybookLessonNoPayment(
                id = 3,
                date = 1650144000000,
                number = 3,
                theme = "Циклы",
                subject = SubjectV7(
                    id = 202,
                    knowledgeArea = "Математика",
                    name = "Алгебра"
                ),
                teacher = LessonTeacher(
                    firstName = "Алексей",
                    lastName = "Алексеев",
                    middleName = "Алексеевич"
                )
            ),
            marks = listOf(
                MarkV2(
                    id = 5,
                    textValue = "5",
                    mood = Mood.GOOD
                ),
                MarkV2(
                    id = 6,
                    textValue = "5",
                    mood = Mood.GOOD
                )
            )
        ),
        ExtendedLesson(
            DaybookLessonNoPayment(
                id = 4,
                date = 1650230400000,
                number = 4,
                theme = "Геометрия",
                subject = SubjectV7(
                    id = 202,
                    knowledgeArea = "Математика",
                    name = "Алгебра"
                ),
                teacher = LessonTeacher(
                    firstName = "Сергей",
                    lastName = "Сергеев",
                    middleName = "Сергеевич"
                )
            ),
            marks = listOf(
                MarkV2(
                    id = 7,
                    textValue = "4",
                    mood = Mood.GOOD
                ),
                MarkV2(
                    id = 8,
                    textValue = "5",
                    mood = Mood.GOOD
                )
            ),
            lessonV2 = LessonV2(works = arrayListOf(
                WorkV2(id = 999, type = WorkType.HOMEWORK, text = "Приготовить обед и ужин до конца недели")
            ))
        )
    )

    private fun createExtendedMark(
        markId: Long? = null,
        personId: Long? = null,
        workId: Long? = null,
        lessonId: Long? = null,
        workTypeId: Long? = null,
        useAvgCalc: Boolean? = null,
        textValue: String? = null,
        number: String? = null,
        type: String? = null,
        mood: Mood? = null,
        subjectName: String? = null,
        knowledgeArea: String? = null,
        subjectId: Long? = null,
        fgosSubjectId: Long? = null,
        workCreatedBy: Long? = null,
        displayInJournal: Boolean? = null,
        isImportant: Boolean? = null,
        markCount: Int? = null,
        periodNumber: Int? = null,
        workType: WorkType? = null,
        markType: String? = null,
        status: String? = null,
        text: String? = null,
        periodType: String? = null,
        targetDate: String? = null,
        sentDate: String? = null,
        tasks: List<Any>? = null,
        oneDriveLinks: List<Any>? = null,
        files: List<Any>? = null
    ): RecentMarksResp.ExtendedMark {
        val mark = MarkV2(markId, personId, workId, lessonId, workTypeId, useAvgCalc, textValue, number, type, mood)
        val subject = SubjectV2(subjectName, knowledgeArea, subjectId, fgosSubjectId)
        val lesson = LessonNotFullV2(null)
        val work = WorkV2(null, workTypeId, lessonId, null, subjectId, workCreatedBy, displayInJournal,
            isImportant, markCount, periodNumber, workType, markType, status, text,
            periodType, targetDate, sentDate, tasks, oneDriveLinks, files)

        return RecentMarksResp.ExtendedMark(mark, subject, lesson, work)
    }

}