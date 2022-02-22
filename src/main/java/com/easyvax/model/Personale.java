package com.easyvax.model;

import com.easyvax.DTO.PersonaleDTO;
import com.easyvax.DTO.VaccinoDTO;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Personale {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
