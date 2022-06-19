package com.easyvax.model;

import com.easyvax.dto.OperatoreDTO;
import com.easyvax.dto.PersonaleDTO;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Operatore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_centroVaccinale")
    private CentroVaccinale centroVaccinale;

    @ManyToOne
    @JoinColumn(name = "id_utente")
    private Utente utente;


    public Operatore(OperatoreDTO operatoreDTO) {
        this.id = operatoreDTO.getId();
    }
}
