package com.easyvax.dto;

import com.easyvax.model.Vaccino;
import lombok.*;

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
