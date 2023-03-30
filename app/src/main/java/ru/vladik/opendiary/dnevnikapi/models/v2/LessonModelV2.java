package ru.vladik.opendiary.dnevnikapi.models.v2;

import androidx.annotation.Nullable;

import java.util.List;

import ru.vladik.opendiary.dnevnikapi.models.Entity;

public abstract class LessonModelV2 implements Entity {
    @Nullable
    @Override
    public Long getId() {
        return id;
    }

    public Long getGroup() {
        return group;
    }

    public Long getResultPlaceId() {
        return resultPlaceId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public List<Long> getTeachers() {
        return teachers;
    }

    public Integer getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public SubjectV2 getSubject() {
        return subject;
    }

    private Long id, group, resultPlaceId, subjectId;
    private List<Long> teachers;
    private Integer number;
    private String title, date, status;
    private SubjectV2 subject;
}
