package ru.ulstu.is.sbapp.rate.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.is.sbapp.rate.model.Groupe;
import ru.ulstu.is.sbapp.rate.repository.GroupeRepository;
import ru.ulstu.is.sbapp.rate.service.exception.GroupeNotFoundException;
import ru.ulstu.is.sbapp.util.validation.ValidatorUtil;

import java.util.List;
import java.util.Optional;

@Service
public class GroupeService {
    private final GroupeRepository groupeRepository;
    private final ValidatorUtil validatorUtil;

    public GroupeService(GroupeRepository groupeRepository, ValidatorUtil validatorUtil) {
        this.groupeRepository = groupeRepository;
        this.validatorUtil = validatorUtil;
    }

    @Transactional
    public Groupe addGroup(String groupName) {
        final Groupe groupe = new Groupe(groupName);
        validatorUtil.validate(groupe);
        return groupeRepository.save(groupe);
    }

    @Transactional(readOnly = true)
    public Groupe findGroup(Long id) {
        final Optional<Groupe> groupe = groupeRepository.findById(id);
        return groupe.orElseThrow(() -> new GroupeNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Groupe> findAllGroups() {
        return groupeRepository.findAll();
    }

    @Transactional
    public Groupe updateGroup(Long id, String groupName) {
        final Groupe currentGroup = findGroup(id);
        currentGroup.setGroupName(groupName);
        validatorUtil.validate(currentGroup);
        return groupeRepository.save(currentGroup);
    }

    @Transactional
    public Groupe deleteGroup(Long id) {
        final Groupe currentGroup = findGroup(id);
        groupeRepository.delete(currentGroup);
        return currentGroup;
    }

    @Transactional
    public void deleteAllGroups() {
        groupeRepository.deleteAll();
    }
}
