package com.easyvax.repository;

import com.easyvax.DTO.VaccinoDTO;
import com.easyvax.model.Somministrazione;
import com.easyvax.model.Vaccino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface VaccinoRepository extends JpaRepository<Vaccino,Long> {

    List<VaccinoDTO>  findVaccinoByCasaFarmaceutica(String casaFarmaceutica);
    List<VaccinoDTO> findByCasaFarmaceutica(String casaFarmaceutica);
}
