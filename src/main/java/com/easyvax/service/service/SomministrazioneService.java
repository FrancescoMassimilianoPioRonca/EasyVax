package com.easyvax.service.service;

import com.easyvax.dto.SomministrazioneDTO;

import java.util.List;

public interface SomministrazioneService {

    SomministrazioneDTO insertSomministrazione(SomministrazioneDTO somministrazione);

    SomministrazioneDTO updateSomministrazione(String code, SomministrazioneDTO somministrazioneDTO);

    SomministrazioneDTO getDetails(Long id);

    List<SomministrazioneDTO> findAll();

    List<SomministrazioneDTO> findByUtente(String cf);

    SomministrazioneDTO findByCod(String cod);


    boolean deletePrenotazione(Long id);


}
