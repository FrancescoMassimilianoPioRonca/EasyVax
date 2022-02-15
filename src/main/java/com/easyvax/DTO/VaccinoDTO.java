package com.easyvax.DTO;

import com.easyvax.model.Utente;
import com.easyvax.model.Vaccino;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VaccinoDTO {


    public Long id;

    @NonNull
    public String nome;

    @NonNull
    public String casaFarmaceutica;

    public VaccinoDTO(Vaccino vaccino) {
        this.id = vaccino.getId();
        this.nome = vaccino.getNome();
        this.casaFarmaceutica = vaccino.getCasaFarmaceutica();
    }
}
