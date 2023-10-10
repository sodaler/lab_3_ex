package ru.ulstu.is.sbapp.rate.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.is.sbapp.rate.controller.dto.StydentDto;
import ru.ulstu.is.sbapp.rate.model.Groupe;
import ru.ulstu.is.sbapp.rate.model.Stydent;
import ru.ulstu.is.sbapp.rate.model.Subject;
import ru.ulstu.is.sbapp.rate.repository.GroupeRepository;
import ru.ulstu.is.sbapp.rate.repository.StydentRepository;
import ru.ulstu.is.sbapp.rate.repository.SubjectRepository;
import ru.ulstu.is.sbapp.rate.service.exception.GroupeNotFoundException;
import ru.ulstu.is.sbapp.rate.service.exception.StydentNotFoundException;
import ru.ulstu.is.sbapp.rate.service.exception.SubjectNotFoundException;
import ru.ulstu.is.sbapp.util.validation.ValidatorUtil;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StydentService {
    private final StydentRepository stydentRepository;
    private final SubjectRepository subjectRepository;
    private final GroupeRepository groupeRepository;
    private final ValidatorUtil validatorUtil;

    public StydentService(StydentRepository stydentRepository, SubjectRepository subjectRepository, GroupeRepository groupeRepository, ValidatorUtil validatorUtil) {
        this.stydentRepository = stydentRepository;
        this.subjectRepository = subjectRepository;
        this.groupeRepository = groupeRepository;
        this.validatorUtil = validatorUtil;
    }

    @Transactional
    public Stydent addStudent(String firstName, String lastName, boolean hostelStatus, List<Subject> subjects, Groupe groupe) {
        final Stydent student = new Stydent(firstName, lastName, hostelStatus, subjects, groupe);
        validatorUtil.validate(student);
        return stydentRepository.save(student);
    }
    @Transactional
    public StydentDto addStudent(StydentDto stydentDto) {
        String firstName = stydentDto.getFirstName();
        String lastName = stydentDto.getLastName();
        Boolean hostelStatus = stydentDto.getHostelStatus();
        List<Subject> subjects = Collections.emptyList();
        Groupe groupe = null;
        if(stydentDto.getSubjects()!=null){
            subjects = stydentDto.getSubjects().stream()
                    .map(subjectId -> subjectRepository.findById(subjectId)
                            .orElseThrow(()-> new SubjectNotFoundException(subjectId))).toList();
        }
        if(stydentDto.getGroupe()!=null){
            groupe = groupeRepository.findById(stydentDto.getGroupe().getId())
                    .orElseThrow(()-> new GroupeNotFoundException(stydentDto.getGroupe().getId()));
        }
        return new StydentDto(addStudent(firstName, lastName, hostelStatus, subjects, groupe));
    }
    @Transactional(readOnly = true)
    public Stydent findStudent(Long id) {
        final Optional<Stydent> student = stydentRepository.findById(id);
        return student.orElseThrow(() -> new StydentNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Stydent> findAllStudents() {
        return stydentRepository.findAll();
    }

    @Transactional
    public Stydent updateStudent(Long id, String firstName, String lastName, boolean hostelStatus, List<Subject> subjects, Groupe groupe) {
        final Stydent currentStudent = findStudent(id);
        currentStudent.setFirstName(firstName);
        currentStudent.setLastName(lastName);
        currentStudent.setHostelStatus(hostelStatus);
        currentStudent.setGroupe(groupe);
        for (Subject subject : currentStudent.getSubjects()) {
            subject.getStudents().remove(id);
        }
        currentStudent.getSubjects().clear();
        for (Subject subject : subjects) {
            currentStudent.addSubject(subject);
        }
        validatorUtil.validate(currentStudent);
        return stydentRepository.save(currentStudent);
    }

    @Transactional
    public StydentDto updateStudent(Long id, StydentDto stydentDto) {
        List<Subject> subjects = Collections.emptyList();
        Groupe groupe = null;
        String firstName = null;
        String lastName = null;
        Boolean hostelStatus;

        if (stydentDto.getSubjects() != null) {
            subjects = stydentDto.getSubjects().stream()
                    .map(subjectId -> subjectRepository.findById(subjectId)
                            .orElseThrow(() -> new SubjectNotFoundException(subjectId))).toList();
        }

        if (stydentDto.getGroupe() != null) {
            groupe = groupeRepository.findById(stydentDto.getGroupe().getId())
                    .orElseThrow(() -> new GroupeNotFoundException(stydentDto.getGroupe().getId()));
        }

        if (stydentDto.getFirstName() == null) {
            firstName = findStudent(id).getFirstName();
        } else {
            firstName = stydentDto.getFirstName();
        }

        if (stydentDto.getLastName() == null) {
            lastName = findStudent(id).getLastName();
        } else {
            lastName = stydentDto.getLastName();
        }

        if (stydentDto.getHostelStatus() == null) {
            hostelStatus = findStudent(id).getHostelStatus();
        } else {
            hostelStatus = stydentDto.getHostelStatus();
        }
        return new StydentDto(updateStudent(id, firstName, lastName, hostelStatus, subjects, groupe));
    }

    @Transactional
    public Stydent deleteStudent(Long id) {
        final Stydent currentStudent = findStudent(id);
        stydentRepository.delete(currentStudent);
        return currentStudent;
    }

    @Transactional
    public List<StydentDto> findStydentsInGroupe(Long groupeId){
        List<Stydent> st = stydentRepository.getStudentsInGroup(groupeId);
        return stydentRepository.getStudentsInGroup(groupeId).stream()
                .map(StydentDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<StydentDto> findStydentsByNameContaining(String strSelect){
        return stydentRepository.findByNameContaining(strSelect).stream()
                .map(StydentDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteAllStudents() {
        stydentRepository.deleteAll();
    }
}
