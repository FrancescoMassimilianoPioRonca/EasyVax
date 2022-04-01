package com.easyvax.dto;

import com.easyvax.model.Richiesta;
import com.easyvax.model.Somministrazione;
import lombok.*;

import java.time.LocalDate;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class RichiestaDTO {

    public Long id;
    public LocalDate data;
    public Long idSomministrazione;
    public Long idPersonale;
    public Boolean approved;

    public RichiestaDTO(Richiesta richiesta) {
        this.id = richiesta.getId();
        this.data = richiesta.getNewData();
        this.idSomministrazione = richiesta.getSomministrazione().getId();
        this.idPersonale = richiesta.getPersonale().getId();
        this.approved = richiesta.getApproved();
    }
}
