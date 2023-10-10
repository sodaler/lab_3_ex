package ru.ulstu.is.sbapp.rate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ulstu.is.sbapp.rate.model.Groupe;

public interface GroupeRepository extends JpaRepository<Groupe, Long> {
}
