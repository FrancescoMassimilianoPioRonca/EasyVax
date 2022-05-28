package com.easyvax.dto;

import com.easyvax.model.Richiesta;
import com.easyvax.model.Somministrazione;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class RichiestaDTO {

    public Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public LocalDate data;

    public Long idNewcentro;
    public Long idOldCentro;
    public Long idSomministrazione;

    @NonNull
    public Long idOp1;

    public Long idOp2;
    public Boolean approved;
    public Boolean approvedOp1;
    public Boolean approvedOp2;

    public RichiestaDTO(Richiesta richiesta) {
        this.id = richiesta.getId();
        this.data = richiesta.getNewData();
        this.idSomministrazione = richiesta.getSomministrazione().getId();
        this.idNewcentro = richiesta.getNewCentro();
        this.idOldCentro=richiesta.getOldCentroVacc().getId();
        this.approvedOp1 = richiesta.getApprovedOp1();
        this.approvedOp2 = richiesta.getApprovedOp2();
        this.idOp1= richiesta.getIdOp1();
        this.idOp2=richiesta.getIdOp2();
        this.approved = richiesta.getApproved();
    }
}
