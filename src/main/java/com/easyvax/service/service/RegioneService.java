package com.easyvax.service.service;

import com.easyvax.DTO.RegioneDTO;
import com.easyvax.model.Regione;

import java.util.List;

public interface RegioneService {

    List<RegioneDTO> findAll();
    RegioneDTO findByNome(String nome);
    RegioneDTO findByProvincia(String provincia);

    RegioneDTO insertRegione(RegioneDTO regioneDTO);
    List<RegioneDTO> updateRegione(RegioneDTO regioneDTO);
    List<RegioneDTO> deleteRegione(Long id);
}
