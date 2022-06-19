package com.easyvax.controller;

import com.easyvax.dto.SomministrazioneDTO;
import com.easyvax.dto.UtenteDTO;
import com.easyvax.model.Somministrazione;
import com.easyvax.model.Utente;
import com.easyvax.service.impl.SomministrazioneServiceImpl;
import com.easyvax.service.impl.UtenteServiceImpl;
import com.easyvax.service.service.SomministrazioneService;
import com.easyvax.service.service.UtenteService;
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
@RequestMapping("/api/somministrazione")
@CrossOrigin("*")

/**
 * -Nella classe SomministrazioneController vengono gestiti e organizzati tutti gli endpoint relativi alle somministrazioni.
 * -I path delle api, ovvero delle attività che si possono svolgere iniziano con:
 * "http://localhost:8080/api/somministrazioni/...".
 * -Nei metodi presenti in questa classe vengono semplicemente richiamati i metodi dela classe SomministrazioneService
 * per il controllo e la validità dei dati in input delle request dal front-end, nonchè lo svoglimento dell'algoritmo implementato nel
 * service.
 * -Infine tutte le response ricevute dal livello "service" verranno inviate al front-end.
 */

public class SomministrazioneController {

    private final SomministrazioneService somministrazioneService;
   // private final SomministrazioneServiceImpl somministrazioneServiceImpl;
    private final UtenteServiceImpl utenteServiceImpl;

    /**
     * Ritorna tutte le somministrazioni
     * @return List<SomministrazioneDTO>
     */
    @GetMapping("/findAll")
    public List<SomministrazioneDTO> findAll() {
        return somministrazioneService.findAll();
    }

    /**
     * Ritorna il numero di somministrazioni
     * per la giornata che devono essere effettuate.
     * Prende come parametro l'id del personale.
     * @param id
     * @return int
     */
    @GetMapping("/preno_odierne")
    public int SomministrazioniOdierne(@Valid @RequestParam Long id) {
        return somministrazioneService.somministrazioniOdierne(id);
    }

    /**
     * Ritorna i dettagli di una prenotazione.
     * Prende come parametro l'id della somministrazione.
     * @param id
     * @return SomministrazioneDTO
     */
    @GetMapping("/getDetails")
    public SomministrazioneDTO getDetails(@Valid @NotNull() @RequestParam Long id) {
        return somministrazioneService.getDetails(id);
    }


    /**
     * Ritorna tutte le somministrazioni in base all'utente.
     * Prende come parametro il cf.
     * @param cf
     * @return List<SomministrazioneDTO>
     */
    @GetMapping("/findByUtente")
    public List<SomministrazioneDTO> findByUtente(@Valid @NotNull() @RequestParam String cf) {
        return somministrazioneService.findByUtente(cf);
    }

    /**
     * Ritorna una somministrazione cercata in base al codice.
     * @param cod
     * @return SomministrazioneDTO
     */
    @GetMapping("/findByCod")
    public SomministrazioneDTO findByCod(@Valid @NotNull() @RequestParam String cod) {
        return somministrazioneService.findByCod(cod);
    }

    /**
     * Inserisce una nuova somministrazione.
     *
     * N.B
     * L'email è stata disattivata in quanto Google dal 29/05 non supporta più
     * l'accesso con app meno sicure (developer)
     * @param somministrazioneDTO
     * @return
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    @PostMapping("/insertSomministrazione")
    public SomministrazioneDTO insertSomministrazione(@NonNull SomministrazioneDTO somministrazioneDTO) throws MessagingException, UnsupportedEncodingException {
        UtenteDTO utenteDTO = utenteServiceImpl.getDetails(somministrazioneDTO.getIdUtente());
        SomministrazioneDTO somministrazioneDTOGenerated = somministrazioneService.insertSomministrazione(somministrazioneDTO);
        //somministrazioneServiceImpl.sendEmail(somministrazioneDTOGenerated.getCode(),utenteDTO.getEmail());
        return somministrazioneDTOGenerated;
    }

    /**
     * Elimina una sommministrazione in base all'id
     * @param id
     * @return boolean
     */
    @DeleteMapping("/deleteSomministrazione")
    public boolean deleteSomministrazione(@Valid @NotNull(message = "Il campo non deve essere vuoto") @RequestParam Long id) {
        return somministrazioneService.deletePrenotazione(id);
    }

    /**
     * Modifica una somministrazione esistente
     * @param code
     * @param somministrazioneDTO
     * @return SomministrazioneDTO
     */
    @PutMapping("/updateSomministrazione")
    public SomministrazioneDTO updateSomministrazione(@Valid @RequestParam String code, @RequestBody SomministrazioneDTO somministrazioneDTO) {
        return somministrazioneService.updateSomministrazione(code, somministrazioneDTO);
    }
}
