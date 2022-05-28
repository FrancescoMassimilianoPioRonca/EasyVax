package com.easyvax.service.service;

import com.easyvax.dto.VaccinoDTO;

import java.util.List;

public interface VaccinoService {

    List<VaccinoDTO> findAll();

    List<VaccinoDTO> findByCasaFarmaceutica(String casaFarmaceutica);

    VaccinoDTO findByNome(String nome);

    VaccinoDTO insertVaccino(VaccinoDTO vaccinoDTO);

    boolean deleteVaccino(Long id);

    List<VaccinoDTO> updateVaccino(VaccinoDTO vaccino);

}
