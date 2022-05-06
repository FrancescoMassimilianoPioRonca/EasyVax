package com.easyvax.service.service;

import com.easyvax.dto.RichiestaDTO;

import java.util.List;

public interface RichiestaService {

    List<RichiestaDTO> getRichiesteOperatore(Long idOperatore);

    List<RichiestaDTO> getRichiesteUtente(Long idUtente);

    void accettaRichiesta(Long id);

    void rifiutaRichiesta(Long id);

    List<RichiestaDTO> deleteRichiesta(Long idUtente);

    RichiestaDTO insertRichiesta(RichiestaDTO richiestaDTO);

}
