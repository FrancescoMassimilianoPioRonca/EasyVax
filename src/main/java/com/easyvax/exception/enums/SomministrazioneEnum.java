package com.easyvax.exception.enums;

public enum SomministrazioneEnum {

    SOMMINISTRAZIONE_NOT_FOUND("SOMM_NF", "La somministrazione che stai cercando non è stata trovata oppure puo' essere nello stato di approvazione"),
    SOMMINISTRAZIONE_INSERT_ERROR("SOMM_IE", "Errore durante l'inserimento della richiesta. Controllare i parametri relativi al centro vaccinale, al vaccino,alla data o al personale"),
    SOMMINISTRAZIONE_ALREADY_EXISTS("SOMM_AE", "La somministrazione che vuoi inserire è già presente nel sistema"),
    SOMMINISTRAZIONE_DELETE_ERROR("SOMM_DLE", "Errore durante l'eliminazione della somministrazione."),
    SOMMINISTRAZIONE_DATA_ERROR("SOMM_DE", "Errore nell'aggiornare la somministrazione. Verificare la validità della data inserita"),
    SOMMINISTRAZIONE_ID_NOT_EXIST("SOMM_IDNE", "Nel database non sono presenti somministrazioni"),
    SOMMINISTRAZIONE_DATE_NOT_COMPATIBLE("SOMM_DNC", "Le date della somministrazione non sono compatibili"),
    DATE_ERROR("SOMM_DE", "Date inserite non corrette"),
    UTENTE_ERROR("SOMM_UA", "L'utente non è ablitato per ricevere il vaccino"),
    SOMMINISTRAZIONE_LIMITE_UPDATE("SOMM_LUP", "Impossibile modificare la somministrazione per i limiti");

    private final String messageCode;
    private final String message;

    SomministrazioneEnum(final String messageCode, final String message) {
        this.messageCode = messageCode;
        this.message = message;
    }


    public static SomministrazioneEnum getSomministrazioneEnumByMessageCode(final String messageCode) {
        for (final SomministrazioneEnum somministrazioneEnum : SomministrazioneEnum.values()) {
            if (somministrazioneEnum.getMessageCode().equals(messageCode)) {
                return somministrazioneEnum;
            }
        }
        return null;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public String getMessage() {
        return message;
    }
}
