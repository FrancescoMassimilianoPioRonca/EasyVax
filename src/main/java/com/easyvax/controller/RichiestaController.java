package com.easyvax.controller;


import com.easyvax.dto.RichiestaDTO;
import com.easyvax.dto.SomministrazioneDTO;
import com.easyvax.model.Richiesta;
import com.easyvax.service.service.RichiestaService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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

    @GetMapping("/getRichiesteUtente")
    public List<RichiestaDTO> getDetails(@Valid @NotNull() @RequestParam Long id) {
        return richiestaService.getRichiesteUtente(id);
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
    public RichiestaDTO insertRichiesta(@NonNull @RequestBody RichiestaDTO richiestaDTO) {
        return richiestaService.insertRichiesta(richiestaDTO);
    }

    @PutMapping("/accettaRichiesta")
    public void accettaRichiesta(@NonNull @RequestParam Long id) {
        richiestaService.accettaRichiesta(id);
    }

    @PutMapping("/rejectRichiesta")
    public void rejectichiesta(@NonNull @RequestParam Long id) {
        richiestaService.rifiutaRichiesta(id);
    }
}
