package com.easyvax.controller;


import com.easyvax.dto.PersonaleDTO;
import com.easyvax.service.service.PersonleService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/personale")
@CrossOrigin("*")

/**
 * -Nella classe AmministratoreController vengono gestiti e organizzati tutti gli endpoint relativi al personale.
 * -I path delle api, ovvero delle attività che si possono svolgere relative all'amministratore, iniziano con:
 * "http://localhost:8080/api/personale/...".
 * -Nei metodi presenti in questa classe vengono semplicemente richiamati i metodi dela classe PersonaleService
 * per il controllo e la validità dei dati in input delle request dal front-end.
 * -Infine tutte le response ricevute dal livello "service" verranno inviare al front-end.
 */
public class PersonaleController {

    private final PersonleService personaleService;

    @GetMapping("/findAll")
    public List<PersonaleDTO> findAll(){
        return personaleService.findAll();
    }

    @GetMapping("/findByCentroVaccinale")
    public List<PersonaleDTO> findByCentroVaccinale(@Valid @NotNull() @RequestParam Long id){
        return personaleService.findByCentroVaccinale(id);
    }

    @GetMapping("/findByCognome")
    public List<PersonaleDTO> findByCognome(@Valid @NotNull() @RequestParam String cognome){
        return personaleService.findByCognome(cognome);
    }

    @GetMapping("/findByCodFiscale")
    public PersonaleDTO findByCodFiscale(@Valid @NotNull() @RequestParam String cf){
        return personaleService.findByCodFiscale(cf);
    }

   /* @GetMapping("/findByRuolo")
    public PersonaleDTO findByRuolo(@Valid @NotNull() @RequestParam String cf){
        return personaleService.findByCodFiscale(cf);
    }*/

    @PostMapping("/insertPersonale")
    public PersonaleDTO insertPersonale(@NonNull @RequestBody PersonaleDTO personaleDTO){
        return personaleService.insertpersonale(personaleDTO);
    }

    @DeleteMapping("/deletePersonale")
    public List<PersonaleDTO> deletePersonale(@Valid @NotNull(message = "Il campo non deve essere vuoto") @RequestParam Long id){
        return personaleService.deletePersonale(id);
    }

    @PutMapping("/updatePersonale")
    public List<PersonaleDTO> updatePersonale(@Valid @RequestBody PersonaleDTO personaleDTO){
        return personaleService.updatePersonale(personaleDTO);
    }


}
