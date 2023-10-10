package ru.ulstu.is.sbapp.rate.controller;

import org.springframework.web.bind.annotation.*;
import ru.ulstu.is.sbapp.configuration.WebConfiguration;
import ru.ulstu.is.sbapp.rate.controller.dto.SubjectDto;
import ru.ulstu.is.sbapp.rate.service.SubjectService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(WebConfiguration.REST_API + "/subject")
public class SubjectController {
    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping("/{id}")
    public SubjectDto getSubject(@PathVariable Long id) {
        return new SubjectDto(subjectService.findSubject(id));
    }

    @GetMapping("/")
    public List<SubjectDto> getSubjects() {
        return subjectService.findAllSubjects().stream()
                .map(SubjectDto::new)
                .toList();
    }

    @PostMapping("/")
    public SubjectDto createSubject(@RequestBody @Valid SubjectDto subjectDto){
        return subjectService.addSubject(subjectDto);
    }

    @PutMapping("/{id}")
    public SubjectDto updateSubject(@PathVariable Long id, @RequestBody @Valid SubjectDto subjectDto) {
        return subjectService.updateSubject(id, subjectDto);
    }

    @DeleteMapping("/{id}")
    public SubjectDto deleteSubject(@PathVariable Long id) {
        return new SubjectDto(subjectService.deleteSubject(id));
    }
}
