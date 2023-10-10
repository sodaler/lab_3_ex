package ru.ulstu.is.sbapp.rate.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.is.sbapp.rate.controller.dto.SubjectDto;
import ru.ulstu.is.sbapp.rate.model.Stydent;
import ru.ulstu.is.sbapp.rate.model.Subject;
import ru.ulstu.is.sbapp.rate.repository.StydentRepository;
import ru.ulstu.is.sbapp.rate.repository.SubjectRepository;
import ru.ulstu.is.sbapp.rate.service.exception.StydentNotFoundException;
import ru.ulstu.is.sbapp.rate.service.exception.SubjectNotFoundException;
import ru.ulstu.is.sbapp.util.validation.ValidatorUtil;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final StydentService stydentService;
    private final StydentRepository stydentRepository;
    private final ValidatorUtil validatorUtil;

    public SubjectService(SubjectRepository subjectRepository, StydentRepository stydentRepository, StydentService stydentService, ValidatorUtil validatorUtil){
        this.subjectRepository = subjectRepository;
        this.stydentRepository = stydentRepository;
        this.stydentService = stydentService;
        this.validatorUtil = validatorUtil;
    }

    @Transactional
    public Subject addSubject(String subjectName, List<Stydent> stydents) {
        final Subject subject = new Subject(subjectName, stydents);
        validatorUtil.validate(subject);
        return subjectRepository.save(subject);
    }

    @Transactional
    public SubjectDto addSubject(SubjectDto subjectDto) {
        List <Stydent> students = Collections.emptyList();
        String subjectName;
        if(subjectDto.getStudents()!=null){
            students = subjectDto.getStudents().stream()
                    .map(stydentId -> stydentRepository.findById(stydentId)
                            .orElseThrow(()-> new StydentNotFoundException(stydentId))).toList();
        }
        if(subjectDto.getName() == null){
            subjectName = findSubject(subjectDto.getId()).getSubjectName();
        }
        else {
            subjectName = subjectDto.getName().split(";")[0];
        }
        return new SubjectDto(addSubject(subjectName, students));
    }

    @Transactional(readOnly = true)
    public Subject findSubject(Long id) {
        final Optional<Subject> subject = subjectRepository.findById(id);
        return subject.orElseThrow(() -> new SubjectNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Subject> findAllSubjects() {
        return subjectRepository.findAll();
    }

    @Transactional
    public Subject updateSubject(Long id, String subjectName, List<Stydent> stydents) {
        final Subject currentSubject = findSubject(id);
        currentSubject.setSubjectName(subjectName);
        for (Stydent stydent : currentSubject.getStudents()) {
            stydent.getSubjects().remove(id);
        }
        currentSubject.getStudents().clear();
        for (Stydent stydent : stydents) {
            currentSubject.addStudent(stydent);
        }
        validatorUtil.validate(currentSubject);
        return subjectRepository.save(currentSubject);
    }

    @Transactional
    public SubjectDto updateSubject(Long id, SubjectDto subjectDto) {
        List <Stydent> students = Collections.emptyList();
        String subjectName;
        if(subjectDto.getStudents()!=null){
            students = subjectDto.getStudents().stream()
                    .map(stydentId -> stydentRepository.findById(stydentId)
                            .orElseThrow(()-> new StydentNotFoundException(stydentId))).toList();
        }
        if(subjectDto.getName() == null){
            subjectName = findSubject(id).getSubjectName();
        }
        else {
            subjectName = subjectDto.getName().split(";")[0];
        }
        return new SubjectDto(updateSubject(id, subjectName, students));
    }

    @Transactional
    public Subject deleteSubject(Long id) {
        final Subject currentSubject = findSubject(id);
        subjectRepository.delete(currentSubject);
        return currentSubject;
    }

    @Transactional
    public void addSubjectStudent(Long subjectId,Long studentId){
        var subject = findSubject(subjectId);
        validatorUtil.validate(subject);
        var student = stydentService.findStudent(studentId);
        validatorUtil.validate(student);
        subject.addStudent(student);
        stydentRepository.save(student);
    }

    @Transactional
    public void deleteAllSubjects() {
        subjectRepository.deleteAll();
    }
}
