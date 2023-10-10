package ru.ulstu.is.sbapp.rate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.ulstu.is.sbapp.rate.model.Stydent;

import java.util.List;

public interface StydentRepository extends JpaRepository<Stydent, Long> {
    @Query("SELECT st FROM Stydent st WHERE st.firstName LIKE %:strSelect% or st.lastName LIKE %:strSelect%")
    List<Stydent> findByNameContaining(@Param("strSelect")String strSelect);

    @Query("SELECT st FROM Stydent st WHERE st.groupe.id = :groupeId")
    List<Stydent> getStudentsInGroup(@Param("groupeId")Long groupeId);
}
