package com.easyvax.controller;


import com.easyvax.dto.ProvinciaDTO;
import com.easyvax.service.service.ProvinciaService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/provincia")
@CrossOrigin("*")

/**
 * -Nella classe ProvinciaController vengono gestiti e organizzati tutti gli endpoint relativi alle provincie.
 * -I path delle api, ovvero delle attività che si possono svolgere relative all'amministratore, iniziano con:
 * "http://localhost:8080/api/provincia/...".
 * -Nei metodi presenti in questa classe vengono semplicemente richiamati i metodi dela classe ProvinciaService
 * per il controllo e la validità dei dati in input delle request dal front-end.
 * -Infine tutte le response ricevute dal livello "service" verranno inviare al front-end.
 */

public class ProvinciaController {

    public final ProvinciaService provinciaService;

    @GetMapping("/findAll")
    public List<ProvinciaDTO> findAll() {
        return provinciaService.findAll();
    }

    @GetMapping("/findByRegione")
    public List<ProvinciaDTO> findByRegione(@Valid @NotNull() @RequestParam String regione) {
        return provinciaService.findByRegione(regione);
    }

    @GetMapping("/findByNome")
    public List<ProvinciaDTO> findByNome(@Valid @NotNull() @RequestParam String nome) {
        return provinciaService.findByNome(nome);
    }

    @GetMapping("/findByCap")
    public ProvinciaDTO findByCap(@Valid @NotNull() @RequestParam String cap) {
        return provinciaService.findByCap(cap);
    }

    @PostMapping("/insertProvincia")
    public ProvinciaDTO insertProvincia(@NonNull @RequestBody ProvinciaDTO provinciaDTO) {
        return provinciaService.insertProvincia(provinciaDTO);
    }

    @DeleteMapping("/deleteProvincia")
    public List<ProvinciaDTO> deleteProvincia(@Valid @NotNull(message = "Il campo non deve essere vuoto") @RequestParam Long id) {
        return provinciaService.deleteProvincia(id);
    }

    @PutMapping("/updateProvincia")
    public List<ProvinciaDTO> updateProvincia(@Valid @RequestBody ProvinciaDTO provinciaDTO) {
        return provinciaService.updateProvincia(provinciaDTO);
    }
}
