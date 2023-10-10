package ru.ulstu.is.sbapp.rate.controller;

import org.springframework.web.bind.annotation.*;
import ru.ulstu.is.sbapp.configuration.WebConfiguration;
import ru.ulstu.is.sbapp.rate.controller.dto.GroupeDto;
import ru.ulstu.is.sbapp.rate.controller.dto.StydentDto;
import ru.ulstu.is.sbapp.rate.service.StydentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(WebConfiguration.REST_API + "/stydent")
public class StydentController {
    private final StydentService studentService;

    public StydentController(StydentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{id}")
    public StydentDto getStudent(@PathVariable Long id) {
        return new StydentDto(studentService.findStudent(id));
    }

    @GetMapping("/")
    public List<StydentDto> getStudents() {
        return studentService.findAllStudents().stream()
                .map(StydentDto::new)
                .toList();
    }

    @PostMapping("/")
    public StydentDto createStudent(@RequestBody @Valid StydentDto stydentDto){
        return studentService.addStudent(stydentDto);
    }

    @PutMapping("/{id}")
    public StydentDto updateStudent(@PathVariable Long id, @RequestBody @Valid StydentDto stydentDto) {
        return studentService.updateStudent(id, stydentDto);
    }

    @DeleteMapping("/{id}")
    public StydentDto deleteStudent(@PathVariable Long id) {
        return new StydentDto(studentService.deleteStudent(id));
    }

    @GetMapping("/findEquals")
    public List<StydentDto> findStudent(@RequestParam("strSelect") String strSelect) {
        return studentService.findStydentsByNameContaining(strSelect);
    }
}
