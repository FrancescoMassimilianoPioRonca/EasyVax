package com.easyvax.model;


import com.easyvax.dto.RichiestaDTO;
import com.easyvax.dto.SomministrazioneDTO;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Richiesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_somministrazione")
    private Somministrazione somministrazione;


    private LocalDate newData;


    private Long newCentro;


    @ManyToOne
    @JoinColumn(name = "old_CentroVacc")
    private CentroVaccinale oldCentroVacc;

    private Boolean approvedOp1;
    private Long idOp1;
    private Boolean approvedOp2;
    private Long idOp2;

    /**
     * Attributo che indica il responso finale
     */
    private Boolean approved;


    public Richiesta(RichiestaDTO richiestaDTO) {
        this.id = richiestaDTO.getId();
        this.newData = richiestaDTO.getData();
        this.approved = richiestaDTO.getApproved();
        this.newCentro=richiestaDTO.getIdNewcentro();
        this.approvedOp1 = richiestaDTO.getApprovedOp1();
        this.idOp1=richiestaDTO.getIdOp1();
        this.idOp2=richiestaDTO.getIdOp2();
        this.approvedOp2 = richiestaDTO.getApprovedOp2();
    }

}
