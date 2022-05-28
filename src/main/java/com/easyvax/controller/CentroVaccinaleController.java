package com.easyvax.controller;


import com.easyvax.dto.CentroVaccinaleDTO;
import com.easyvax.service.service.CentroVaccinaleService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/centroVaccinale")
@CrossOrigin("*")


/**
 * -Nella classe CentroVaccinaleController vengono gestiti e organizzati tutti gli endpoint relativi al centrovaccinale.
 * -I path delle api, ovvero delle attività che si possono svolgere relative all'amministratore, iniziano con:
 * "http://localhost:8080/api/centroVaccinale/...".
 * -Nei metodi presenti in questa classe vengono semplicemente richiamati i metodi dela classe CentroVaccinaleService
 * per il controllo e la validità dei dati in input delle request dal front-end.
 * -Infine tutte le response ricevute dal livello "service" verranno inviare al front-end.
 */

public class CentroVaccinaleController {

    public final CentroVaccinaleService centroVaccinaleService;

    /**
     * Questo controller restituisce tutti i centri vaccinali
     *
     * @return List<CentroVaccinaleDTO>
     */
    @GetMapping("/findAll")
    public List<CentroVaccinaleDTO> findAll() {
        return centroVaccinaleService.findAll();
    }


    /**
     * Questo controller restituisce il centrovaccinale cercato in base al nome
     *
     * @return CentroVaccinaleDTO
     */
    @GetMapping("/findByName")
    public List<CentroVaccinaleDTO> findByName(@NonNull @RequestParam String nome) {
        return centroVaccinaleService.findbyName(nome);
    }

    /**
     * Questo controller restituisce i CentriVaccinali cercati in base al cap
     *
     * @return List<CentroVaccinaleDTO>
     */
    @GetMapping("/findByCap")
    public List<CentroVaccinaleDTO> findByCap(@NonNull @RequestParam String cap) {
        return centroVaccinaleService.findByCap(cap);
    }


    /**
     * Questo controller restituisce i CentriVaccinali cercati in base alla provincia
     *
     * @return List<CentroVaccinaleDTO>
     */
    @GetMapping("/findByProvincia")
    public List<CentroVaccinaleDTO> findByProvincia(@NonNull @RequestParam Long id) {
        return centroVaccinaleService.findByProvincia(id);
    }

    /**
     * Questo controller restituisce i CentriVaccinali cercati in base alla regione
     *
     * @return List<CentroVaccinaleDTO>
     */
    @GetMapping("/findByRegione")
    public List<CentroVaccinaleDTO> findByRegione(@NonNull @RequestParam String regione) {
        return centroVaccinaleService.findByRegione(regione);
    }

    /**
     * Questo controller registra un nuovo centroVaccinale
     */
    @PostMapping("/insertCentro")
    public CentroVaccinaleDTO insertCentro(@NonNull @RequestBody CentroVaccinaleDTO centroVaccinaleDTO) {
        return centroVaccinaleService.insertCentro(centroVaccinaleDTO);
    }

    /**
     * Questo controller elimina un  centroVaccinale
     */
    @DeleteMapping("/deleteCentroVaccinale")
    public boolean deleteCentro(@Valid @NotNull @RequestParam Long id) {
        return centroVaccinaleService.deleteCentro(id);
    }

    /**
     * Questo controller aggiorna un centroVaccinale esistente
     */
    @PutMapping("/updateCentroVaccinale")
    public List<CentroVaccinaleDTO> updateCentro(@Valid @RequestBody CentroVaccinaleDTO centroVaccinaleDTO) {
        return centroVaccinaleService.updateCentro(centroVaccinaleDTO);
    }
}
