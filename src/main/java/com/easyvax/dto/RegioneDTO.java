package com.easyvax.dto;


import com.easyvax.model.Regione;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor


public class RegioneDTO {

    public Long id;
    public String nome;

    public RegioneDTO(Regione regione) {
        this.id = regione.getId();
        this.nome = regione.getNome();
    }
}
