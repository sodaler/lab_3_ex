package ru.ulstu.is.sbapp.rate.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.is.sbapp.rate.controller.dto.StydentDto;
import ru.ulstu.is.sbapp.rate.controller.dto.SubjectDto;
import ru.ulstu.is.sbapp.rate.model.Stydent;
import ru.ulstu.is.sbapp.rate.repository.StydentRepository;
import ru.ulstu.is.sbapp.rate.service.SubjectService;
import ru.ulstu.is.sbapp.rate.service.exception.StydentNotFoundException;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/subject")
public class SubjectMvcController {
    private final SubjectService subjectService;
    private final StydentRepository stydentRepository;

    public SubjectMvcController(SubjectService subjectService, StydentRepository stydentRepository) {
        this.subjectService = subjectService;
        this.stydentRepository = stydentRepository;
    }

    @GetMapping
    public String getSubjects(Model model) {
        model.addAttribute("subjects",
                subjectService.findAllSubjects().stream()
                        .map(SubjectDto::new)
                        .toList());
        return "subject";
    }

    @GetMapping(value = {"/edit", "/edit/{id}"})
    public String editSubject(@PathVariable(required = false) Long id, Model model) {
        model.addAttribute("students", stydentRepository.findAll().stream()
                .map(StydentDto::new)
                .toList());
        if (id == null || id <= 0) {
            model.addAttribute("subjectDto", new SubjectDto());
        } else {
            model.addAttribute("subjectId", id);
            model.addAttribute("subjectDto", new SubjectDto(subjectService.findSubject(id)));
        }
        return "subject-edit";
    }

    @PostMapping(value = {"", "/{id}"})
    public String saveSubject(@PathVariable(required = false) Long id,
                            @ModelAttribute @Valid SubjectDto subjectDto,
                            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "subject-edit";
        }

        List<Stydent> students = Collections.emptyList();
        if(subjectDto.getStudents()!=null){
            students = subjectDto.getStudents().stream()
                    .map(stydentId -> stydentRepository.findById(stydentId)
                            .orElseThrow(()-> new StydentNotFoundException(stydentId))).toList();
        }

        if (id == null || id <= 0) {
            subjectService.addSubject(subjectDto.getName(), students);
        } else {
            subjectService.updateSubject(id, subjectDto.getName(), students);
        }
        return "redirect:/subject";
    }

    @PostMapping("/delete/{id}")
    public String deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return "redirect:/subject";
    }
}
