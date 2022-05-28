package com.easyvax.controller;


import com.easyvax.dto.PersonaleDTO;
import com.easyvax.model.Operatore;
import com.easyvax.repository.OperatoreRepository;
import com.easyvax.service.service.OperatoreService;
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
 * -Nella classe PersonaleController vengono gestiti e organizzati tutti gli endpoint relativi al personale.
 * -I path delle api, ovvero delle attività che si possono svolgere relative all'amministratore, iniziano con:
 * "http://localhost:8080/api/personale/...".
 * -Nei metodi presenti in questa classe vengono semplicemente richiamati i metodi dela classe PersonaleService
 * per il controllo e la validità dei dati in input delle request dal front-end.
 * -Infine tutte le response ricevute dal livello "service" verranno inviare al front-end.
 */
public class PersonaleController {

    private final PersonleService personaleService;
    private final OperatoreService operatoreService;
    private final OperatoreRepository operatoreRepository;

    /**
     * Restituisce tutto il personale
     *
     * @return List<OperatoriDTO>
     */
    @GetMapping("/findAll")
    public List<PersonaleDTO> findAll() {
        return personaleService.findAll();
    }

    /**
     * Restituisce tutti il personale che è in servizio nel centroVaccinale cercato
     *
     * @return List<PersonaleDTO>
     */
    @GetMapping("/findByCentroVaccinale")
    public List<PersonaleDTO> findByCentroVaccinale(@Valid @NotNull() @RequestParam Long id) {
        return personaleService.findByCentroVaccinale(id);
    }

    /**
     * Restituisce  il personale in base al cognome
     *
     * @return List<PersonaleDTO>
     */
    @GetMapping("/findByCognome")
    public List<PersonaleDTO> findByCognome(@Valid @NotNull() @RequestParam String cognome) {
        return personaleService.findByCognome(cognome);
    }

    /**
     * Restituisce il personale cercato in base al codicefiscale
     *
     * @return PersonoaleDTO
     */
    @GetMapping("/findByCodFiscale")
    public PersonaleDTO findByCodFiscale(@Valid @NotNull() @RequestParam String cf) {
        return personaleService.findByCodFiscale(cf);
    }

   /* @GetMapping("/findByRuolo")
    public PersonaleDTO findByRuolo(@Valid @NotNull() @RequestParam String cf){
        return personaleService.findByCodFiscale(cf);
    }*/

    /**
     * Associa un nuovo personale alla struttura
     * Si prega di notare che non è presente il @RequestBody perchè thymeleaf non supporta le post in JSON
     * Un qualsiasi front-end del tipo react o vue supporterebbe le post con axios
     */
    @PostMapping("/insertPersonale")
    public PersonaleDTO insertPersonale(@NonNull PersonaleDTO personaleDTO) {
        if(operatoreRepository.existsByUtente_Id(personaleDTO.getIdUtente())){
            Operatore operatore = operatoreRepository.findByUtente_Id(personaleDTO.getIdUtente());
            operatoreService.deleteOperatore(operatore.getId());
        }
        return personaleService.insertpersonale(personaleDTO);
    }

    /**
     * Elimina il personale dalla struttura
     */
    @DeleteMapping("/deletePersonale")
    public Boolean deletePersonale(@Valid @NotNull(message = "Il campo non deve essere vuoto") @RequestParam Long id) {
        return personaleService.deletePersonale(id);
    }

    /**
     * Modifica il personale
     */
    @PutMapping("/updatePersonale")
    public List<PersonaleDTO> updatePersonale(@Valid @RequestBody PersonaleDTO personaleDTO) {
        return personaleService.updatePersonale(personaleDTO);
    }


}
