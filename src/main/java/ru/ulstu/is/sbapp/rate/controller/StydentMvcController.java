package ru.ulstu.is.sbapp.rate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.is.sbapp.rate.controller.dto.GroupeDto;
import ru.ulstu.is.sbapp.rate.controller.dto.StydentDto;
import ru.ulstu.is.sbapp.rate.controller.dto.SubjectDto;
import ru.ulstu.is.sbapp.rate.model.Groupe;
import ru.ulstu.is.sbapp.rate.model.Subject;
import ru.ulstu.is.sbapp.rate.repository.GroupeRepository;
import ru.ulstu.is.sbapp.rate.repository.SubjectRepository;
import ru.ulstu.is.sbapp.rate.service.StydentService;
import ru.ulstu.is.sbapp.rate.service.exception.GroupeNotFoundException;
import ru.ulstu.is.sbapp.rate.service.exception.SubjectNotFoundException;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/stydent")
public class StydentMvcController {
    private final StydentService studentService;
    private final SubjectRepository subjectRepository;
    private final GroupeRepository groupeRepository;

    public StydentMvcController(StydentService studentService, SubjectRepository subjectRepository, GroupeRepository groupeRepository) {
        this.studentService = studentService;
        this.subjectRepository = subjectRepository;
        this.groupeRepository = groupeRepository;
    }

    @GetMapping
    public String getStudents(Model model) {
        model.addAttribute("stydents",
                studentService.findAllStudents().stream()
                        .map(StydentDto::new)
                        .toList());
        return "stydent";
    }

    @GetMapping(value = {"/edit", "/edit/{id}"})
    public String editStudent(@PathVariable(required = false) Long id, Model model) {
        model.addAttribute("groups", groupeRepository.findAll().stream()
                .map(GroupeDto::new)
                .toList());
        model.addAttribute("subjects", subjectRepository.findAll().stream()
                .map(SubjectDto::new)
                .toList());
        if (id == null || id <= 0) {
            model.addAttribute("stydentDto", new StydentDto());
        } else {
            model.addAttribute("stydentId", id);
            model.addAttribute("stydentDto", new StydentDto(studentService.findStudent(id)));
        }
        return "stydent-edit";
    }

    @PostMapping(value = {"", "/{id}"})
    public String saveStudent(@PathVariable(required = false) Long id,
                              @ModelAttribute @Valid StydentDto stydentDto,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "stydent-edit";
        }

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

        if (id == null || id <= 0) {
            studentService.addStudent(stydentDto.getFirstName(), stydentDto.getLastName(),
                    stydentDto.getHostelStatus(), subjects, groupe);
        } else {
            studentService.updateStudent(id, stydentDto.getFirstName(), stydentDto.getLastName(),
                    stydentDto.getHostelStatus(), subjects, groupe);
        }
        return "redirect:/stydent";
    }

    @PostMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return "redirect:/stydent";
    }
}
