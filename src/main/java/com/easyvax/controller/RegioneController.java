package com.easyvax.controller;

import com.easyvax.dto.RegioneDTO;
import com.easyvax.service.service.RegioneService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/regione")
@CrossOrigin("*")

/**
 * -Nella classe RegioneController vengono gestiti e organizzati tutti gli endpoint relativi alle regioni.
 * -I path delle api, ovvero delle attività che si possono svolgere relative all'amministratore, iniziano con:
 * "http://localhost:8080/api/regione/...".
 * -Nei metodi presenti in questa classe vengono semplicemente richiamati i metodi dela classe RegioneService
 * per il controllo e la validità dei dati in input delle request dal front-end.
 * -Infine tutte le response ricevute dal livello "service" verranno inviare al front-end.
 */

public class RegioneController {
    public final RegioneService regioneService;

    @GetMapping("/findAll")
    public List<RegioneDTO> findAll(){
        return regioneService.findAll();
    }

    @GetMapping("/findByProvincia")
    public List<RegioneDTO> findByProvincia(@Valid @NotNull() @RequestParam String provincia){
        return regioneService.findByProvincia(provincia);
    }

    @GetMapping("/findByNome")
    public RegioneDTO findByNome(@Valid @NotNull() @RequestParam String nome){
        return regioneService.findByNome(nome);
    }

    @PostMapping("/insertRegione")
    public RegioneDTO insertRegione(@NonNull @RequestBody RegioneDTO regioneDTO){
        return regioneService.insertRegione(regioneDTO);
    }

    @DeleteMapping("/deleteRegione")
    public List<RegioneDTO> deleteVaccino(@Valid @NotNull(message = "Il campo non deve essere vuoto") @RequestParam Long id){
        return regioneService.deleteRegione(id);
    }

    @PutMapping("/updateRegione")
    public List<RegioneDTO> updateVaccino(@Valid @RequestBody RegioneDTO regioneDTO){
        return regioneService.updateRegione(regioneDTO);
    }
}
