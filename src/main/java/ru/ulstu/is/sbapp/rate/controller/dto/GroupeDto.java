package ru.ulstu.is.sbapp.rate.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.ulstu.is.sbapp.rate.model.Groupe;

import java.util.List;

public class GroupeDto {
    private Long id;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String name;

    public GroupeDto() {
    }

    public GroupeDto(Groupe groupe) {
        this.id = groupe.getId();
        this.name = String.format("%s", groupe.getGroupName());
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }
}
