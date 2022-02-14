package com.easyvax.model;

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
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private LocalDate giorno;

    @NonNull
    private String inizioTurno;

    @NonNull
    private String fineTurno;

    @ManyToOne
    @JoinColumn(name="id_personale")
    private Personale personale;

    @OneToMany(mappedBy="turno", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Somministrazione> somministrazioni = new ArrayList<>();

}
