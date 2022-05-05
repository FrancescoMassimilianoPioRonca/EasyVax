package com.easyvax.model;

import com.easyvax.dto.PersonaleDTO;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Personale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_centroVaccinale")
    private CentroVaccinale centroVaccinale;

    @ManyToOne
    @JoinColumn(name = "id_utente")
    private Utente utente;



    public Personale(PersonaleDTO personaleDTO) {
        this.id = personaleDTO.getId();
    }
}
