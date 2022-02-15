package com.easyvax.service.service;

import com.easyvax.DTO.VaccinoDTO;
import com.easyvax.model.Vaccino;

import java.time.LocalDate;
import java.util.List;

public interface VaccinoService {

    List<VaccinoDTO> findAll();
    List<VaccinoDTO> findByCasaFarmaceutica(String casaFarmaceutica);

    /**
     * Riservato agli admin
     * @param
     * @return
     */
    VaccinoDTO insertVaccino(VaccinoDTO vaccinoDTO);
    List<VaccinoDTO> deleteVaccino(Long id);
    List<VaccinoDTO> updateVaccino(VaccinoDTO vaccino);

}
