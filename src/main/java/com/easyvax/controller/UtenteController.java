package com.easyvax.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.easyvax.dto.UtenteDTO;
import com.easyvax.service.service.UtenteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.*;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AllArgsConstructor
@RestController
@RequestMapping("/utente")
@CrossOrigin("*")
public class UtenteController {

    private final UtenteService utenteService;

    @GetMapping("/findAll")
    public List<UtenteDTO> findAll(){
        return utenteService.findAll();
    }

    @GetMapping("/getDetails")
    public UtenteDTO getDetails(@Valid @NotNull() @RequestParam Long id){
        return utenteService.getDetails(id);
    }

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
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10*60*1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRuolo().toString())
                        .sign(algorithm);
                Map<String,String> tokens = new HashMap<>();
                tokens.put("access-token",access_token);
                tokens.put("refresh-token",refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);

                new ObjectMapper().writeValue(response.getOutputStream(),tokens);
            } catch (Exception exception) {
                response.setHeader("error",exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String,String> error = new HashMap<>();
                error.put("error_message",exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

    @PostMapping("/insertUtente")
    public UtenteDTO insertUtente(@NonNull @RequestBody UtenteDTO utenteDTO){
        return utenteService.insertUtente(utenteDTO);
    }

    @PostMapping("/insertAdmin")
    public UtenteDTO insertAdmin(@NonNull @RequestBody UtenteDTO utenteDTO){
        return utenteService.insertAdminUtente(utenteDTO);
    }

    @DeleteMapping("/deleteUtente")
    public List<UtenteDTO> deleteUtente(@Valid @NotNull(message = "Il campo non deve essere vuoto") @RequestParam Long id){
        return utenteService.deleteUtente(id);
    }

    @PutMapping("/updateUtente")
    public List<UtenteDTO> updateAnagrafica(@Valid @RequestBody UtenteDTO utenteDTO){
        return utenteService.updateAnagrafica(utenteDTO);
    }
}
