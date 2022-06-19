package com.easyvax.controller;

import com.easyvax.dto.VaccinoDTO;
import com.easyvax.service.service.VaccinoService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/vaccino")
@CrossOrigin("*")

/**
 * -Nella classe VaccinoController vengono gestiti e organizzati tutti gli endpoint relativi ai vaccini.
 * -I path delle api, ovvero delle attività che si possono svolgere iniziano con:
 * "http://localhost:8080/api/vaccino/...".
 * -Nei metodi presenti in questa classe vengono semplicemente richiamati i metodi dela classe VaccinoService
 * per il controllo e la validità dei dati in input delle request dal front-end.
 * -Infine tutte le response ricevute dal livello "service" verranno inviare al front-end.
 */

public class VaccinoController {

    public final VaccinoService vaccinoService;

    /**
     * Ritorna tutti i vaccini
     * @return List<VaccinoDTO>
     */
    @GetMapping("/findAll")
    public List<VaccinoDTO> findAll() {
        return vaccinoService.findAll();
    }

    /**
     * Ritorna tutti i vaccini cercati in base alla casa farmaceutica.
     * @param casaFarmaceutica
     * @return List<VaccinoDTO>
     */
    @GetMapping("/findByCasaFarmaceutica")
    public List<VaccinoDTO> findByCasaFarmaceutica(@Valid @NotNull() @RequestParam String casaFarmaceutica) {
        return vaccinoService.findByCasaFarmaceutica(casaFarmaceutica);
    }

    /**
     * Ritorna il vaccino cercato in base al nome
     * @param nome
     * @return VaccinoDTO
     */
    @GetMapping("/findByNome")
    public VaccinoDTO findByNome(@Valid @NotNull() @RequestParam String nome) {
        return vaccinoService.findByNome(nome);
    }

    /**
     * Inserisce un nuovo vaccino
     * @param vaccinoDTO
     * @return VaccinoDTO
     */
    @PostMapping("/insertVaccino")
    public VaccinoDTO insertVaccino(@NonNull @RequestBody VaccinoDTO vaccinoDTO) {
        return vaccinoService.insertVaccino(vaccinoDTO);
    }

    /**
     * Elimina un vaccino in base all'id
     * @param id
     * @return boolean
     */
    @DeleteMapping("/deleteVaccino")
    public boolean deleteVaccino(@Valid @NotNull(message = "Il campo non deve essere vuoto") @RequestParam Long id) {
        return vaccinoService.deleteVaccino(id);
    }

    /**
     * Modifica un vaccino esistente
     * @param vaccinoDTO
     * @return List<VaccinoDTO>
     */
    @PutMapping("/updateVaccino")
    public List<VaccinoDTO> updateVaccino(@Valid @RequestBody VaccinoDTO vaccinoDTO) {
        return vaccinoService.updateVaccino(vaccinoDTO);
    }

}
