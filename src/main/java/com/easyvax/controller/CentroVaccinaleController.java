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

    @GetMapping("/findAll")
    public List<CentroVaccinaleDTO> findAll(){
        return centroVaccinaleService.findAll();
    }

    @GetMapping("/findByName")
    public List<CentroVaccinaleDTO> findByName(@NonNull @RequestParam String nome){
        return centroVaccinaleService.findbyName(nome);
    }

    @GetMapping("/findByCap")
    public List<CentroVaccinaleDTO> findByCap(@NonNull @RequestParam String cap){
        return centroVaccinaleService.findByCap(cap);
    }

    @GetMapping("/findByVaccino")
    public List<CentroVaccinaleDTO> findByVaccino(@NonNull @RequestParam Long id){
        return centroVaccinaleService.findByVaccino(id);
    }

    @GetMapping("/findByProvincia")
    public List<CentroVaccinaleDTO> findByProvincia(@NonNull @RequestParam Long id){
        return centroVaccinaleService.findByProvincia(id);
    }

    @GetMapping("/findByRegione")
    public List<CentroVaccinaleDTO> findByRegione(@NonNull @RequestParam String regione){
        return centroVaccinaleService.findByRegione(regione);
    }

    @PostMapping("/insertCentro")
    public CentroVaccinaleDTO insertCentro(@NonNull @RequestBody CentroVaccinaleDTO centroVaccinaleDTO){
        return centroVaccinaleService.insertCentro(centroVaccinaleDTO);
    }

    @DeleteMapping("/deleteCentroVaccinale")
    public List<CentroVaccinaleDTO> deleteCentro(@Valid @NotNull(message = "Il campo non deve essere vuoto") @RequestParam Long id){
        return centroVaccinaleService.deleteCentro(id);
    }

    @PutMapping("/updateCentroVaccinale")
    public List<CentroVaccinaleDTO> updateCentro(@Valid @RequestBody CentroVaccinaleDTO centroVaccinaleDTO){
        return centroVaccinaleService.updateCentro(centroVaccinaleDTO);
    }
}
