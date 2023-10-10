package ru.ulstu.is.sbapp.rate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.is.sbapp.rate.controller.dto.GroupeDto;
import ru.ulstu.is.sbapp.rate.controller.dto.StydentDto;
import ru.ulstu.is.sbapp.rate.model.Stydent;
import ru.ulstu.is.sbapp.rate.service.GroupeService;
import ru.ulstu.is.sbapp.rate.service.StydentService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/groupe")
public class GroupeMvcController {
    private final GroupeService groupeService;
    private final StydentService studentService;

    public GroupeMvcController(GroupeService groupeService, StydentService studentService) {
        this.groupeService = groupeService;
        this.studentService = studentService;
    }

    @GetMapping
    public String getGroups(Model model) {
        model.addAttribute("groups",
                groupeService.findAllGroups().stream()
                        .map(GroupeDto::new)
                        .toList());
        return "groupe";
    }

    @GetMapping(value = {"/edit", "/edit/{id}"})
    public String editGroup(@PathVariable(required = false) Long id, Model model) {
        model.addAttribute("stydents", studentService.findStydentsInGroupe(id));
        if (id == null || id <= 0) {
            model.addAttribute("groupeDto", new GroupeDto());
        } else {
            model.addAttribute("groupeId", id);
            model.addAttribute("groupeDto", new GroupeDto(groupeService.findGroup(id)));
        }
        return "groupe-edit";
    }

    @PostMapping(value = {"", "/{id}"})
    public String saveGroup(@PathVariable(required = false) Long id,
                            @ModelAttribute @Valid GroupeDto groupeDto,
                            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "groupe-edit";
        }
        if (id == null || id <= 0) {
            groupeService.addGroup(groupeDto.getName());
        } else {
            groupeService.updateGroup(id, groupeDto.getName());
        }
        return "redirect:/groupe";
    }

    @PostMapping("/delete/{id}")
    public String deleteGroup(@PathVariable Long id) {
        groupeService.deleteGroup(id);
        return "redirect:/groupe";
    }
}