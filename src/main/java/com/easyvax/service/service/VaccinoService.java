package com.easyvax.service.service;

import com.easyvax.dto.VaccinoDTO;

import java.util.List;

public interface VaccinoService {

    List<VaccinoDTO> findAll();
    List<VaccinoDTO> findByCasaFarmaceutica(String casaFarmaceutica);
    VaccinoDTO findByNome(String nome);

    /**
     * Riservato agli admin
     * @param
     * @return
     */
    VaccinoDTO insertVaccino(VaccinoDTO vaccinoDTO);
    List<VaccinoDTO> deleteVaccino(Long id);
    List<VaccinoDTO> updateVaccino(VaccinoDTO vaccino);

}
