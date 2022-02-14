package com.easyvax.repository;

import com.easyvax.model.CentroVaccinale;
import com.easyvax.model.Personale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CentroVaccinaleRepository extends JpaRepository<CentroVaccinale,Long> {
}
