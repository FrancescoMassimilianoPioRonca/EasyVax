package com.easyvax.dto;

import com.easyvax.model.Provincia;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ProvinciaDTO {

    public Long id;
    public String nome;
    public String cap;
    public Long idRegione;

    public ProvinciaDTO(Provincia provincia) {
        this.id = provincia.getId();
        this.nome = provincia.getNome();
        this.cap = provincia.getCap();
        this.idRegione = provincia.getRegione().getId();
    }
}
