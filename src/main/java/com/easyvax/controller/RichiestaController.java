package com.easyvax.controller;


import com.easyvax.dto.RichiestaDTO;
import com.easyvax.dto.SomministrazioneDTO;
import com.easyvax.model.Richiesta;
import com.easyvax.model.Somministrazione;
import com.easyvax.repository.RichiestaRepository;
import com.easyvax.repository.SomministrazioneRepository;
import com.easyvax.service.impl.RichiestaServiceImpl;
import com.easyvax.service.service.RichiestaService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/richiesta")
@CrossOrigin("*")

/**
 * -Nella classe RichiestaController vengono gestiti e organizzati tutti gli endpoint relativi alle richieste.
 * -I path delle api, ovvero delle attività che si possono svolgere relative all'amministratore, iniziano con:
 * "http://localhost:8080/api/amministratore/...".
 * -Nei metodi presenti in questa classe vengono semplicemente richiamati i metodi dela classe RichiestaService
 * per il controllo e la validità dei dati in input delle request dal front-end.
 * -Infine tutte le response ricevute dal livello "service" verranno inviare al front-end.
 */

public class RichiestaController {

    private final RichiestaService richiestaService;
    private final RichiestaServiceImpl richiestaServiceImpl;
    private final RichiestaRepository richiestaRepository;
    private final SomministrazioneRepository somministrazioneRepository;

    @GetMapping("/getRichiesteUtente")
    public List<RichiestaDTO> getDetails(@Valid @NotNull() @RequestParam Long id) {
        return richiestaService.getRichiesteUtente(id);
    }

    @GetMapping("/findAll")
    public List<RichiestaDTO> findAll() {
        return richiestaService.findAll();
    }

    @GetMapping("/getRichiesteOperatore")
    public List<RichiestaDTO> getDetailsOperatore(@Valid @NotNull() @RequestParam Long id) {
        return richiestaService.getRichiesteOperatore(id);
    }

    @DeleteMapping("/deleteRichiesta")
    public List<RichiestaDTO> deleteRichiesta(@Valid @NotNull() @RequestParam Long id) {
        return richiestaService.deleteRichiesta(id);
    }

    @PostMapping("/insertRichiesta")
    public RichiestaDTO insertRichiesta(@NonNull RichiestaDTO richiestaDTO) throws MessagingException, UnsupportedEncodingException {
        Somministrazione somministrazione = somministrazioneRepository.findById(richiestaDTO.idSomministrazione).get();
        richiestaServiceImpl.sendEmail(somministrazione.getCodiceSomm(),somministrazione);

        return richiestaService.insertRichiesta(richiestaDTO);
    }

    @PutMapping("/accettaRichiesta")
    public void accettaRichiesta(@NonNull @RequestParam Long idR,Long idO) throws MessagingException, UnsupportedEncodingException {
        richiestaService.accettaRichiesta(idR,idO);
        Somministrazione somministrazione = richiestaRepository.findById(idR).get().getSomministrazione();

        if(somministrazione.getInAttesa()==false)
            richiestaServiceImpl.acceptEmail(idR,somministrazione);
    }

    @PutMapping("/rejectRichiesta")
    public void rejectichiesta(@NonNull @RequestParam Long id) throws MessagingException, UnsupportedEncodingException {
        richiestaService.rifiutaRichiesta(id);
        Somministrazione somministrazione = richiestaRepository.findById(id).get().getSomministrazione();
        richiestaServiceImpl.rejectEmail(id,somministrazione);
    }
}
