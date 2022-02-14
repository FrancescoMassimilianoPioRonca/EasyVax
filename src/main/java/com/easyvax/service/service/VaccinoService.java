package com.easyvax.service.service;

import com.easyvax.DTO.VaccinoDTO;
import com.easyvax.model.Vaccino;

import java.time.LocalDate;
import java.util.List;

public interface VaccinoService {

    List<VaccinoDTO> findAll();
    List<VaccinoDTO> findByDate(LocalDate date);
    List<VaccinoDTO> findByCasaFarmaceutica(String casaFarmaceutica);

    /**
     * Riservato agli admin
     * @param vaccino
     * @return
     */
    VaccinoDTO insertVaccino(VaccinoDTO vaccino);
    List<VaccinoDTO> deleteVaccino(Long id);
    List<VaccinoDTO> updateVaccino(VaccinoDTO vaccino);

}
