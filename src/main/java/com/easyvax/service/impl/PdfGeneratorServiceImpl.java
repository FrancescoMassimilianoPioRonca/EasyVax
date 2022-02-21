package com.easyvax.service.impl;

import com.easyvax.exception.enums.SomministrazioneEnum;
import com.easyvax.exception.handler.ApiRequestException;
import com.easyvax.model.CentroVaccinale;
import com.easyvax.model.Provincia;
import com.easyvax.model.Somministrazione;
import com.easyvax.model.Vaccino;
import com.easyvax.repository.CentroVaccinaleRepository;
import com.easyvax.repository.ProvinciaRepository;
import com.easyvax.repository.SomministrazioneRepository;
import com.easyvax.repository.VaccinoRepository;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;

@Service
@Slf4j
@AllArgsConstructor

public class PdfGeneratorServiceImpl {

    private final SomministrazioneRepository somministrazioneRepository;
    private final CentroVaccinaleRepository centroVaccinaleRepository;
    private final ProvinciaRepository provinciaRepository;
    private final VaccinoRepository vaccinoRepository;
    private static SomministrazioneEnum somministrazioneEnum;

    public void export(String cod, HttpServletResponse response) throws IOException {

        if(somministrazioneRepository.existsByCodiceSomm(cod)){

            Somministrazione somministrazione = somministrazioneRepository.findByCodiceSomm(cod);
            CentroVaccinale centroVaccinale = centroVaccinaleRepository.findById(somministrazione.getCentro().getId()).get();
            Vaccino vaccino = vaccinoRepository.findById(somministrazione.getVaccino().getId()).get();
            Provincia provincia = provinciaRepository.findById(centroVaccinale.getProvincia().getId()).get();

            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document,response.getOutputStream());

            document.open();

            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE);
            fontTitle.setSize(20);
            fontTitle.setColor(Color.red);

            Paragraph titolo = new Paragraph("EasyVax cod." + somministrazione.getCodiceSomm() + "\n \n", fontTitle);
            titolo.setAlignment(Element.ALIGN_CENTER);

            Font fontParagraph = FontFactory.getFont(FontFactory.COURIER);
            fontParagraph.setSize(12);

            Paragraph corpo = new Paragraph("Ecco la ricevuta per la tua richiesta di vaccinazione. Di seguito tutti i dettagli: \n Hai scelto il seguente centro vaccinale: " + centroVaccinale.getNome() + " sito in " + centroVaccinale.getIndirizzo() + " nella provincia di " + provincia.getNome() + ". \n Hai scelto il seguente vaccino : " + vaccino.getNome() + "\n Hai scelto le seguenti data e ora : " + somministrazione.getDataSomministrazione() + " alle ore " + somministrazione.getOraSomministrazione() + "\n \n \n Ti ricordiamo che puoi modificare la data e ora fino al giorno antecedenta la vaccinazione. \n\n\n\n Mostra questa ricevuta il giorno della somministrazione.\n\n\n\n Ti ringraziamo per esserti affidato ad EasyVax.", fontParagraph);

            corpo.setAlignment(Element.ALIGN_LEFT);

            document.add(titolo);
            document.add(corpo);

            document.close();
        }
        else
        {
            somministrazioneEnum = SomministrazioneEnum.getSomministrazioneEnumByMessageCode("SOMM_NF");
            throw new ApiRequestException(somministrazioneEnum.getMessage());
        }

    }
}
