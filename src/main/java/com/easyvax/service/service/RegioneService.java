package com.easyvax.service.service;

import com.easyvax.dto.RegioneDTO;

import java.util.List;

public interface RegioneService {

    List<RegioneDTO> findAll();

    RegioneDTO findByNome(String nome);

    List<RegioneDTO> findByProvincia(String provincia);

    RegioneDTO insertRegione(RegioneDTO regioneDTO);

    List<RegioneDTO> updateRegione(RegioneDTO regioneDTO);

    boolean deleteRegione(Long id);
}
