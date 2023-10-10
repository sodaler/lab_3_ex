package ru.ulstu.is.sbapp.rate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ulstu.is.sbapp.rate.model.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
}
