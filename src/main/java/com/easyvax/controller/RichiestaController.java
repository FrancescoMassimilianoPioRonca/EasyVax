package com.easyvax.controller;


import com.easyvax.dto.RichiestaDTO;
import com.easyvax.dto.SomministrazioneDTO;
import com.easyvax.model.Richiesta;
import com.easyvax.model.Somministrazione;
import com.easyvax.repository.RichiestaRepository;
import com.easyvax.repository.SomministrazioneRepository;
import com.easyvax.service.impl.RichiestaServiceImpl;
import com.easyvax.service.service.RichiestaService;
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
@RequestMapping("/api/richiesta")
@CrossOrigin("*")

/**
 * -Nella classe RichiestaController vengono gestiti e organizzati tutti gli endpoint relativi alle richieste.
 * -I path delle api, ovvero delle attività che si possono svolgere iniziano con:
 * "http://localhost:8080/api/amministratore/...".
 * -Nei metodi presenti in questa classe vengono semplicemente richiamati i metodi dela classe RichiestaService
 * per il controllo e la validità dei dati in input delle request dal front-end, nonchè l'algoritmo implementato nel service.
 * -Infine tutte le response ricevute dal livello "service" verranno inviare al front-end.
 */

public class RichiestaController {

    private final RichiestaService richiestaService;
    private final RichiestaServiceImpl richiestaServiceImpl;
    private final RichiestaRepository richiestaRepository;
    private final SomministrazioneRepository somministrazioneRepository;

    /**
     * Ritorna tutte le richieste dell'utente che ancora non sono state smarcate
     * Prende come parametro l'id dell'utente
     * @param id
     * @return List<RichiestaDTO>
     */
    @GetMapping("/getRichiesteUtente")
    public List<RichiestaDTO> getDetails(@Valid @NotNull() @RequestParam Long id) {
        return richiestaService.getRichiesteUtente(id);
    }

    /**
     * Ritorna tutte le richieste
     * @return List<RichiestaDTO>
     */
    @GetMapping("/findAll")
    public List<RichiestaDTO> findAll() {
        return richiestaService.findAll();
    }

    /**
     * Ritorna tutte le richieste che devono ancora essere smarcate
     * relative al centro vaccinale in cui lavora l'operatore.
     * Prende come parametro l'id dell'operatore
     * @param id
     * @return List<RichiestaDTO>
     */
    @GetMapping("/getRichiesteOperatore")
    public List<RichiestaDTO> getDetailsOperatore(@Valid @NotNull() @RequestParam Long id) {
        return richiestaService.getRichiesteOperatore(id);
    }

    /**
     * Elimina una richiesta in base all'id
     * @param id
     * @return List<RichiestaDTO>
     */
    @DeleteMapping("/deleteRichiesta")
    public List<RichiestaDTO> deleteRichiesta(@Valid @NotNull() @RequestParam Long id) {
        return richiestaService.deleteRichiesta(id);
    }

    /**
     * Inserisce una nuova richiesta.
     *
     * N.B.
     * L'email è stata disattivata in quanto Google dal 29/05 non supporta più
     * l'accesso con app meno sicure (developer)
     * @param richiestaDTO
     * @return
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    @PostMapping("/insertRichiesta")
    public RichiestaDTO insertRichiesta(@NonNull RichiestaDTO richiestaDTO) throws MessagingException, UnsupportedEncodingException {
        Somministrazione somministrazione = somministrazioneRepository.findById(richiestaDTO.idSomministrazione).get();
        //richiestaServiceImpl.sendEmail(somministrazione.getCodiceSomm(),somministrazione);

        return richiestaService.insertRichiesta(richiestaDTO);
    }

    /**
     * Accetta una richiesta. Prende come parametro l'id dell'operatore e l'id della richiesta.
     *
     * N.B.
     *  L'email è stata disattivata in quanto Google dal 29/05 non supporta più
     *  l'accesso con app meno sicure (developer)
     * @param idR
     * @param idO
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    @PutMapping("/accettaRichiesta")
    public void accettaRichiesta(@NonNull @RequestParam Long idR,Long idO) throws MessagingException, UnsupportedEncodingException {
        richiestaService.accettaRichiesta(idR,idO);
        Somministrazione somministrazione = richiestaRepository.findById(idR).get().getSomministrazione();

       /* if(somministrazione.getInAttesa()==false)
            richiestaServiceImpl.acceptEmail(idR,somministrazione);*/
    }

    /**
     * Rifiuta una richiesta.Prende come parametro l'id dell'operatore e l'id della richiesta
     * N.B.
     *  L'email è stata disattivata in quanto Google dal 29/05 non supporta più
     *  l'accesso con app meno sicure (developer)
      * @param idR
     * @param idO
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    @PutMapping("/rejectRichiesta")
    public void rejectichiesta(@NonNull @RequestParam Long idR, Long idO) throws MessagingException, UnsupportedEncodingException {
        richiestaService.rifiutaRichiesta(idR,idO);
        Somministrazione somministrazione = richiestaRepository.findById(idR).get().getSomministrazione();


       // richiestaServiceImpl.rejectEmail(idR,somministrazione);
    }
}
