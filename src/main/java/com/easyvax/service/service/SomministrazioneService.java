package com.easyvax.service.service;

import com.easyvax.DTO.SomministrazioneDTO;
import com.easyvax.model.Somministrazione;

import java.util.List;

public interface SomministrazioneService {

    SomministrazioneDTO insertSomministrazione(SomministrazioneDTO somministrazione);
    SomministrazioneDTO updateSomministrazione(SomministrazioneDTO somministrazione);
    SomministrazioneDTO getDetails(SomministrazioneDTO somministrazione);

    /**
     * Riservate all'admin
     * @param somministrazione
     * @return
     */
    List<SomministrazioneDTO> deletePrenotazione(SomministrazioneDTO somministrazione);
    SomministrazioneDTO updateSomministrazioneAdmin(SomministrazioneDTO somministrazione);


}
