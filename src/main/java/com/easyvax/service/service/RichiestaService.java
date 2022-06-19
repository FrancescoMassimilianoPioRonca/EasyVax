package com.easyvax.service.service;

import com.easyvax.dto.RichiestaDTO;

import java.util.List;

public interface RichiestaService {

    List<RichiestaDTO> getRichiesteOperatore(Long idOperatore);

    List<RichiestaDTO> findAll();

    List<RichiestaDTO> getRichiesteUtente(Long idUtente);

    boolean accettaRichiesta(Long idR,Long idO);

    boolean rifiutaRichiesta(Long idR, Long idO);

    List<RichiestaDTO> deleteRichiesta(Long idUtente);

    RichiestaDTO insertRichiesta(RichiestaDTO richiestaDTO);

}
