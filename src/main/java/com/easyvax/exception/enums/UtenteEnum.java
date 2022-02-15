package com.easyvax.exception.enums;

public enum UtenteEnum {

    UTENTE_NOT_FOUND("UTE_NF", "l'utente che stai cercando non è stato trovato"),
    UTENTE_ALREADY_EXISTS("UTE_AE", "l'utente che vuoi inserire è già presente nel sistema"),
    UTENTE_DELETE_ERROR("UTE_DLE", "Errore durante l'eliminazione dell'utente"),
    UTENTE_ID_NOT_EXIST("UTE_IDNE", "l'utente che stai cercando non esiste"),
    UTENTE_EMPTY_FIELD("UTE_EF", "Alcuni campi sono vuoti");

    private final String messageCode;
    private final String message;

    UtenteEnum(final String messageCode, final String message) {
        this.messageCode = messageCode;
        this.message = message;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public String getMessage() {
        return message;
    }

    public static UtenteEnum getUtenteEnumByMessageCode(final String messageCode) {
        for (final UtenteEnum utenteEnum : UtenteEnum.values()) {
            if (utenteEnum.getMessageCode().equals(messageCode)) {
                return utenteEnum;
            }
        }
        return null;
    }
}