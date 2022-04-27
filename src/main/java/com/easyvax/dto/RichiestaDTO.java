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
    public Long IdCentroVaccinale;
    public Long idSomministrazione;
    public Boolean approved;
    public Boolean approvedOp1;
    public Boolean approvedOp2;

    public RichiestaDTO(Richiesta richiesta) {
        this.id = richiesta.getId();
        this.data = richiesta.getNewData();
        this.idSomministrazione = richiesta.getSomministrazione().getId();
        this.IdCentroVaccinale=richiesta.getIdCentroVacc();
        this.approvedOp1=richiesta.getApprovedOp1();
        this.approvedOp2=richiesta.getApprovedOp2();
        this.approved = richiesta.getApproved();
    }
}
