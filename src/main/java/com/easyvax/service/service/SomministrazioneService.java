package com.easyvax.service.service;

import com.easyvax.dto.SomministrazioneDTO;

import java.time.LocalDate;
import java.util.List;

public interface SomministrazioneService {

    SomministrazioneDTO insertSomministrazione(SomministrazioneDTO somministrazione);

    SomministrazioneDTO updateSomministrazione(String code, SomministrazioneDTO somministrazioneDTO);

    SomministrazioneDTO getDetails(Long id);

    List<SomministrazioneDTO> findAll();

    List<SomministrazioneDTO> findByUtente(String cf);

    SomministrazioneDTO findByCod(String cod);

    int somministrazioniOdierne(Long id);
    boolean deletePrenotazione(Long id);


}
