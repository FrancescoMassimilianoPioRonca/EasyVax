package com.easyvax.service.service;

import com.easyvax.dto.OperatoreDTO;
import com.easyvax.dto.PersonaleDTO;

import java.util.List;

public interface OperatoreService {

    OperatoreDTO insertOperatore(OperatoreDTO operatoreDTO);

    List<OperatoreDTO> findAll();

    List<OperatoreDTO> findByCentroVaccinale(Long id);

    OperatoreDTO findByCodFiscale(String cf);

    List<OperatoreDTO> deleteOperatore(Long id);
    List<OperatoreDTO> updateOperatore(OperatoreDTO operatoreDTO);
}
