package com.easyvax.service.service;

import com.easyvax.DTO.SomministrazioneDTO;
import com.easyvax.model.Somministrazione;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface SomministrazioneService {

    SomministrazioneDTO insertSomministrazione(SomministrazioneDTO somministrazione);
    SomministrazioneDTO updateSomministrazione(String code,SomministrazioneDTO somministrazioneDTO);
    SomministrazioneDTO getDetails(Long id);
    List<SomministrazioneDTO> findAll();
    List<SomministrazioneDTO> findByUtente(String cf);
    SomministrazioneDTO findByCod(String cod);


    List<SomministrazioneDTO> deletePrenotazione(Long id);


}
