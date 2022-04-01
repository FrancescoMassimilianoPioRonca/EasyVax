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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="id_somministrazione")
    private Somministrazione somministrazione;

    @ManyToOne
    @JoinColumn(name="id_personale")
    private Personale personale;

    @NonNull
    private LocalDate newData;


    private Boolean approved;


    public Richiesta(RichiestaDTO richiestaDTO)
    {
        this.id = richiestaDTO.getId();
        this.newData = richiestaDTO.getData();
        this.approved = richiestaDTO.getApproved();
    }

}
