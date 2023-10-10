package ru.ulstu.is.sbapp.rate.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.ulstu.is.sbapp.rate.model.Stydent;
import ru.ulstu.is.sbapp.rate.model.Subject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StydentDto {
    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String firstName;
    private String lastName;
    private Boolean hostelStatus;
    private String hostelStatusStr;
    private GroupeDto groupe;
    private List<Long> subjects;
    private List<String> subjectsName;

    public StydentDto(){
    }

    public StydentDto(Stydent student) {
        this.id = student.getId();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.hostelStatus = student.getHostelStatus();
        this.hostelStatusStr = student.getHostelStatusDto();
        this.subjectsName = new ArrayList<>();
        for (Subject subject : student.getSubjects()) {
            this.subjectsName.add(subject.getSubjectName());
        }
        if(student.getGroupe() != null){
            this.groupe = new GroupeDto(student.getGroupe());
        }
        this.subjects = new ArrayList<>();
        List<Subject> subjectList = student.getSubjects();
        if(subjectList != null) {
            for (Subject subject : subjectList) {
                this.subjects.add(subject.getId());
            }
        }
        else {
            this.subjects = Collections.emptyList();
        }
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public long getId() {
        return id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Boolean getHostelStatus() {
        return hostelStatus;
    }

    public GroupeDto getGroupe(){
        return groupe;
    }

    public List<Long> getSubjects(){
        return subjects;
    }

    public String getHostelStatusStr() {
        return hostelStatusStr;
    }

    public void setGroupe(GroupeDto groupe) {
        this.groupe = groupe;
    }

    public void setSubjects(List<Long> subjects) {
        this.subjects = subjects;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setHostelStatus(Boolean hostelStatus) {
        this.hostelStatus = hostelStatus;
    }

    public void setHostelStatusStr(String hostelStatusStr) {
        this.hostelStatusStr = hostelStatusStr;
    }
    public List<String> getSubjectsName() {
        return subjectsName;
    }

    public void setSubjectsName(List<String> subjectsName) {
        this.subjectsName = subjectsName;
    }

    public String prettySubjects(){
        return String.join(", ", subjectsName);
    }
}



 /*this.subjects = student.getSubjects().stream()
                .map(SubjectForStydentDto::new)
                .toList();*/