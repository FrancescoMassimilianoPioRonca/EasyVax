package com.easyvax.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.easyvax.dto.UtenteDTO;
import com.easyvax.model.Utente;
import com.easyvax.service.impl.UtenteServiceImpl;
import com.easyvax.service.service.UtenteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AllArgsConstructor
@RestController
@RequestMapping("/api/utente")
@CrossOrigin("*")

/**
 * -Nella classe UtenteController vengono gestiti e organizzati tutti gli endpoint relativi all'utente.
 * -I path delle api, ovvero delle attività che si possono svolgere iniziano con:
 * "http://localhost:8080/api/utente/...".
 * -Nei metodi presenti in questa classe vengono semplicemente richiamati i metodi dela classe UtenteService
 * per il controllo e la validità dei dati in input delle request dal front-end, nonchè lo svolgimento dell'algoritmo implementato nel service.
 * -Infine tutte le response ricevute dal livello "service" verranno inviare al front-end.
 */
public class UtenteController {

    private final UtenteService utenteService;
    private final UtenteServiceImpl utenteServiceImpl;

    /**
     * Ritorna tutti gli utenti
     * @return List<UtenteDTO>
     */
    @GetMapping("/findAll")
    public List<UtenteDTO> findAll() {
        return utenteService.findAll();
    }


    /**
     * Ritorna i dettagli di un utente cercato in base all'id
      * @param id
     * @return UtenteDTO
     */
    @GetMapping("/getDetails")
    public UtenteDTO getDetails(@Valid @NotNull() @RequestParam Long id) {
        return utenteService.getDetails(id);
    }

    /**
     * Refresha il token
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                UtenteDTO user = utenteService.findByCF(username);

                String access_token = JWT.create()
                        .withSubject(user.getCodFiscale())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRuolo().toString())
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access-token", access_token);
                tokens.put("refresh-token", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);

                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

    /**
     * Si prega di notare che non è presente il @RequestBody perchè le pagine statiche non supportano le post in JSON
     * Un qualsiasi front-end del tipo react o vue supporterebbe le post con axios
     *
     * N.B. dal 30/05 Google ha disattivato l'accesso da app meno sicure (developers)
     *
     * Inserisce un nuovo utente
     * @param utenteDTO
     * @return UtenteDTO
     */
    @PostMapping("/insertUtente")
    public UtenteDTO insertUtente(@NonNull UtenteDTO utenteDTO) throws MessagingException, UnsupportedEncodingException {

        UtenteDTO utenteIns =  utenteService.insertUtente(utenteDTO);
        Utente utente = new Utente(utenteIns);
        //utenteServiceImpl.sendVerificationEmail(utente,"http://localhost:8080");

        return utenteIns;
    }

    /**
     * Si prega di notare che non è presente il @RequestBody perchè le pagine statiche non supportano le post in JSON
     * Un qualsiasi front-end del tipo react o vue supporterebbe le post con axios
     *
     * Inserisce un nuovo admin
     * @param utenteDTO
     * @return UtenteDTO
     */
    @PostMapping("/insertAdmin")
    public UtenteDTO insertAdmin(@NonNull  UtenteDTO utenteDTO) {
        return utenteService.insertAdminUtente(utenteDTO);
    }

    /**
     * Elimina un utente cercato in base all'id
     * @param id
     * @return boolean
     */
    @DeleteMapping("/deleteUtente")
    public Boolean deleteUtente(@Valid @NotNull(message = "Il campo non deve essere vuoto") @RequestParam Long id) {
        return utenteService.deleteUtente(id);
    }

    /**
     * Modifica l'anagrafica di un utente.
     * @param utenteDTO
     * @return List<UtenteDTO>
     */
    @PutMapping("/updateUtente")
    public List<UtenteDTO> updateAnagrafica(@Valid @RequestBody UtenteDTO utenteDTO) {
        return utenteService.updateAnagrafica(utenteDTO);
    }
}
