package ru.ulstu.is.sbapp.rate.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.ulstu.is.sbapp.rate.model.Stydent;
import ru.ulstu.is.sbapp.rate.model.Subject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SubjectDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String name;
    private List<Long> students;
    private List<String> studentsName;

    public SubjectDto(){

    }

    public SubjectDto(Subject subject) {
        this.id = subject.getId();
        this.name = String.format("%s", subject.getSubjectName());
        this.students = new ArrayList<>();
        this.studentsName = new ArrayList<>();
        for (Stydent stydent : subject.getStudents()) {
            this.studentsName.add(String.format("%s %s", stydent.getFirstName(), stydent.getLastName()));
        }
        List<Stydent> stydentList = subject.getStudents();
        if(stydentList != null) {
            for (Stydent stydent : stydentList) {
                this.students.add(stydent.getId());
            }
        }
        else {
            this.students = Collections.emptyList();
        }
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Long> getStudents() {
        return students;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStudents(List<Long> students) {
        this.students = students;
    }

    public List<String> getStudentsName() {
        return studentsName;
    }

    public void setStudentsName(List<String> studentsName) {
        this.studentsName = studentsName;
    }

    public String prettyStudents(){
        return String.join(", ", studentsName);
    }
}
