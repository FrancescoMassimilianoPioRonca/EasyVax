package com.easyvax.service.service;

import com.easyvax.dto.RichiestaDTO;

import java.util.List;

public interface RichiestaService {

    List<RichiestaDTO> getRichiestePersonale(Long idPersonale);

    List<RichiestaDTO> getRichiesteUtente(Long idUtente);
    void accettaRichiesta(Long id);
    void rifiutaRichiesta(Long id);

   void deleteRichiesta(Long idUtente);

    RichiestaDTO insertRichiesta(RichiestaDTO richiestaDTO);
}
