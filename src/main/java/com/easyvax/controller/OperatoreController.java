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
public class OperatoreController {

    private final OperatoreService operatoreService;

    @GetMapping("/findAll")
    public List<OperatoreDTO> findAll(){
        return operatoreService.findAll();
    }

    @GetMapping("/findByCentroVaccinale")
    public List<OperatoreDTO> findByCentroVaccinale(@Valid @NotNull() @RequestParam Long id){
        return operatoreService.findByCentroVaccinale(id);
    }

    @GetMapping("/findByCodFiscale")
    public OperatoreDTO findByCodFiscale(@Valid @NotNull() @RequestParam String cf){
        return operatoreService.findByCodFiscale(cf);
    }


    @PostMapping("/insertOperatore")
    public OperatoreDTO insertOperatore(@NonNull @RequestBody OperatoreDTO operatoreDTO){
        return operatoreService.insertOperatore(operatoreDTO);
    }

    @DeleteMapping("/deleteOperatore")
    public List<OperatoreDTO> deleteOperatore(@Valid @NotNull(message = "Il campo non deve essere vuoto") @RequestParam Long id){
        return operatoreService.deleteOperatore(id);
    }

    @PutMapping("/updateOperatore")
    public List<OperatoreDTO> updateOperatore(@Valid @RequestBody OperatoreDTO operatoreDTO){
        return operatoreService.updateOperatore(operatoreDTO);
    }


}
