package com.easyvax.exception.enums;

public enum SomministrazioneEnum {

    SOMMINISTRAZIONE_NOT_FOUND("SOMM_NF", "La somministrazione che stai cercando non è stata trovata"),
    SOMMINISTRAZIONE_ALREADY_EXISTS("SOMM_AE", "La somministrazione che vuoi inserire è già presente nel sistema"),
    SOMMINISTRAZIONE_DELETE_ERROR("SOMM_DLE", "Errore durante l'eliminazione della somministrazione"),
    SOMMINISTRAZIONE_ID_NOT_EXIST("SOMM_IDNE", "La somministrazione che stai cercando non esiste"),
    SOMMINISTRAZIONE_DATE_NOT_COMPATIBLE("SOMM_DNC", "Le date della somministrazione non sono compatibili"),
    DATE_ERROR("SOMM_DE", "Date inserite non corrette"),
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
