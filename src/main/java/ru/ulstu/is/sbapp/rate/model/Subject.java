package ru.ulstu.is.sbapp.rate.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    @NotBlank(message = "Subject Name can't be null or empty")
    private String subjectName;
    @ManyToMany(mappedBy = "subjects", fetch = FetchType.EAGER)
    private List<Stydent> students = new ArrayList<>();

    public Subject() {
    }

    public Subject(String subjectName, List<Stydent> stydents) {
        this.subjectName = subjectName;
        this.students = stydents;
    }

    public Long getId() {
        return id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
       Subject subject = (Subject) o;
        return Objects.equals(id, subject.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public List<Stydent> getStudents() {
        return this.students;
    }

    public void setStudents(List<Stydent> students) {
        this.students = students;
    }

    public void addStudent(Stydent student) {
        if(!this.students.contains(student)){
            this.students.add(student);
        }
        if (!student.getSubjects().contains(this)) {
            student.getSubjects().add(this);
        }
    }
    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", subjectName='" + subjectName + '\'' +
                ", students='" +  students.stream()
                                            .map(String::valueOf)
                                            .collect(Collectors.joining("\n")) + '\'' +
                '}';
    }
}
