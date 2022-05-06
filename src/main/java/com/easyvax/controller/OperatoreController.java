package com.easyvax.controller;

import com.easyvax.dto.OperatoreDTO;
import com.easyvax.dto.PersonaleDTO;
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
@RequestMapping("/api/operatore")
@CrossOrigin("*")

/**
 * -Nella classe OpertoreController vengono gestiti e organizzati tutti gli endpoint relativi all'operatore.
 * -I path delle api, ovvero delle attività che si possono svolgere relative all'amministratore, iniziano con:
 * "http://localhost:8080/api/operatore/...".
 * -Nei metodi presenti in questa classe vengono semplicemente richiamati i metodi dela classe OperatoreService
 * per il controllo e la validità dei dati in input delle request dal front-end.
 * -Infine tutte le response ricevute dal livello "service" verranno inviare al front-end.
 */
public class OperatoreController {

    private final OperatoreService operatoreService;

    /**
     * Restituisce tutti gli operatori
     *
     * @return List<OperatoriDTO>
     */
    @GetMapping("/findAll")
    public List<OperatoreDTO> findAll() {
        return operatoreService.findAll();
    }

    /**
     * Restituisce tutti gli operatori che sono in servizio nel centroVaccinale cercato
     *
     * @return List<OperatoreDTO>
     */
    @GetMapping("/findByCentroVaccinale")
    public List<OperatoreDTO> findByCentroVaccinale(@Valid @NotNull() @RequestParam Long id) {
        return operatoreService.findByCentroVaccinale(id);
    }

    /**
     * Restituisce l'operatore cercato in base al codicefiscale
     *
     * @return OperatoreDTO
     */
    @GetMapping("/findByCodFiscale")
    public OperatoreDTO findByCodFiscale(@Valid @NotNull() @RequestParam String cf) {
        return operatoreService.findByCodFiscale(cf);
    }

    /**
     * Registra un operatore ad un centrovaccinale
     */
    @PostMapping("/insertOperatore")
    public OperatoreDTO insertOperatore(@NonNull @RequestBody OperatoreDTO operatoreDTO) {
        return operatoreService.insertOperatore(operatoreDTO);
    }

    /**
     * Elimina un operatore
     */
    @DeleteMapping("/deleteOperatore")
    public List<OperatoreDTO> deleteOperatore(@Valid @NotNull(message = "Il campo non deve essere vuoto") @RequestParam Long id) {
        return operatoreService.deleteOperatore(id);
    }

    /**
     * Aggiorna un operatore
     */
    @PutMapping("/updateOperatore")
    public List<OperatoreDTO> updateOperatore(@Valid @RequestBody OperatoreDTO operatoreDTO) {
        return operatoreService.updateOperatore(operatoreDTO);
    }


}
