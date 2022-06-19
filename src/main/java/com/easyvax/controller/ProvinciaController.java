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
 * -I path delle api, ovvero delle attività che si possono svolgere iniziano con:
 * "http://localhost:8080/api/provincia/...".
 * -Nei metodi presenti in questa classe vengono semplicemente richiamati i metodi dela classe ProvinciaService
 * per il controllo e la validità dei dati in input delle request dal front-end, nonchè per lo svolgimento dell'algoritmo
 * implementato nel service.
 * -Infine tutte le response ricevute dal livello "service" verranno inviare al front-end.
 */

public class ProvinciaController {

    public final ProvinciaService provinciaService;

    /**
     * Ritorna tutte le provincie
     * @return List<ProvinciaDTO>
     */
    @GetMapping("/findAll")
    public List<ProvinciaDTO> findAll() {
        return provinciaService.findAll();
    }

    /**
     * Ritorna le provincie cercate in base alla regione
     * @param regione
     * @return List<ProvinciaDTO>
     */
    @GetMapping("/findByRegione")
    public List<ProvinciaDTO> findByRegione(@Valid @NotNull() @RequestParam String regione) {
        return provinciaService.findByRegione(regione);
    }

    /**
     * Cerca le provincie cercate in base al nome
     * @param nome
     * @return List<ProvinciaDTO>
     */
    @GetMapping("/findByNome")
    public List<ProvinciaDTO> findByNome(@Valid @NotNull() @RequestParam String nome) {
        return provinciaService.findByNome(nome);
    }

    /**
     * Cerca la provincia cercata in base al cap
     * @param cap
     * @return ProvinciaDTO
     */
    @GetMapping("/findByCap")
    public ProvinciaDTO findByCap(@Valid @NotNull() @RequestParam String cap) {
        return provinciaService.findByCap(cap);
    }


    /**
     * Inserisce una nuova provincia
     * @param provinciaDTO
     * @return ProvinciaDTO
     */
    @PostMapping("/insertProvincia")
    public ProvinciaDTO insertProvincia(@NonNull @RequestBody ProvinciaDTO provinciaDTO) {
        return provinciaService.insertProvincia(provinciaDTO);
    }

    /**
     * Elimina una provincia in base all'id
     * @param id
     * @return boolean
     */
    @DeleteMapping("/deleteProvincia")
    public boolean deleteProvincia(@Valid @NotNull @RequestParam Long id) {
        return provinciaService.deleteProvincia(id);
    }

    /**
     * Modifica una provincia esistente
     * @param provinciaDTO
     * @return List<ProvinciaDTO>
     */
    @PutMapping("/updateProvincia")
    public List<ProvinciaDTO> updateProvincia(@Valid @RequestBody ProvinciaDTO provinciaDTO) {
        return provinciaService.updateProvincia(provinciaDTO);
    }
}
