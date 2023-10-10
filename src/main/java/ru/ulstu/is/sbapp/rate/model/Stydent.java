package ru.ulstu.is.sbapp.rate.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
public class Stydent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    @NotBlank(message = "First Name can't be null or empty")
    private String firstName;
    @Column(nullable = false)
    @NotBlank(message = "Last Name can't be null or empty")
    private String lastName;
    @Column(nullable = false)
    private boolean hostelStatus;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "students_subjects",
            joinColumns = @JoinColumn(name = "student_fk"),
            inverseJoinColumns = @JoinColumn(name = "subject_fk"))
    //@JsonManagedReference
    private List<Subject> subjects = new ArrayList<>();
    @ManyToOne
    private Groupe groupe;

    public Stydent() {
    }

    public Stydent(String firstName, String lastName, boolean hostelStatus, List<Subject> subjects, Groupe groupe) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.hostelStatus = hostelStatus;
        this.subjects = subjects;
        this.groupe = groupe;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getHostelStatus() {
        return hostelStatus;
    }

    public String getHostelStatusDto() {
        if (hostelStatus) {
            return "Проживает";
        } else return "Не проживает";
    }

    public void setHostelStatus(boolean hostelStatus) {
        this.hostelStatus = hostelStatus;
    }

    public void addSubject(Subject subject) {
        if (!this.subjects.contains(subject)) {
            this.subjects.add(subject);
        }
        if (subject.getStudents().contains(this)) {
            subject.getStudents().add(this);
        }
    }
    public List<Subject> getSubjects() {
        return this.subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
    }
    public Groupe getGroupe() {
        return groupe;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stydent student = (Stydent) o;
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", hostelStatus='" + this.getHostelStatusDto() + '\'' +
                ", group='" + groupe + '\'' +
                ", subjects='" + subjects.stream()
                                            .map(String::valueOf)
                                            .collect(Collectors.joining("\n")) + '\'' +
                '}';
    }
}
