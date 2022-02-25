package com.easyvax.service.service;

import com.easyvax.dto.ProvinciaDTO;

import java.util.List;

public interface ProvinciaService {

    List<ProvinciaDTO> findAll();
    List<ProvinciaDTO> findByNome(String nome);
    List<ProvinciaDTO> findByRegione(String regione);
    ProvinciaDTO findByCap(String cap);

    ProvinciaDTO insertProvincia(ProvinciaDTO provinciaDTO);
    List<ProvinciaDTO> updateProvincia(ProvinciaDTO provinciaDTO);
    List<ProvinciaDTO> deleteProvincia(Long id);
}
