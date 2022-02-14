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
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Vaccino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String nome;

    @NonNull
    private String casaFarmaceutica;

    @NonNull
    private LocalDate dataApprovazioneVaccino;

    @OneToMany(mappedBy="vaccino" , cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Somministrazione> somministrazioni = new ArrayList<>();
}
