package ru.ulstu.is.sbapp.rate.controller;

import org.springframework.web.bind.annotation.*;
import ru.ulstu.is.sbapp.configuration.WebConfiguration;
import ru.ulstu.is.sbapp.rate.controller.dto.GroupeDto;
import ru.ulstu.is.sbapp.rate.service.GroupeService;

import java.util.List;

@RestController
@RequestMapping(WebConfiguration.REST_API + "/groupe")
public class GroupeController {
    private final GroupeService groupeService;

    public GroupeController(GroupeService groupeService) {
        this.groupeService = groupeService;
    }

    @GetMapping("/{id}")
    public GroupeDto getGroup(@PathVariable Long id) {
        return new GroupeDto(groupeService.findGroup(id));
    }

    @GetMapping("/")
    public List<GroupeDto> getGroups() {
        return groupeService.findAllGroups().stream()
                .map(GroupeDto::new)
                .toList();
    }

    @PostMapping("/")
    public GroupeDto createGroup(@RequestParam("groupName") String groupName){
        return new GroupeDto(groupeService.addGroup(groupName));
    }

    @PutMapping("/{id}")
    public GroupeDto updateGroup(@PathVariable Long id,
                                 @RequestParam("groupName") String groupName) {
        return new GroupeDto(groupeService.updateGroup(id, groupName));
    }

    @DeleteMapping("/{id}")
    public GroupeDto deleteGroup(@PathVariable Long id) {
        return new GroupeDto(groupeService.deleteGroup(id));
    }
}
