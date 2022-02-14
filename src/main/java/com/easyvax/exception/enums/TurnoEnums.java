package com.easyvax.exception.enums;

public enum TurnoEnums {

    TURNO_NOT_FOUND("TUR_NF", "Il turno che stai cercando non è stato trovato"),
    TURNO_ALREADY_EXISTS("TUR_AE", "il turno che vuoi inserire è già presente nel sistema"),
    TURNO_DELETE_ERROR("TUR_DLE", "Errore durante l'eliminazione del turno"),
    TURNO_ID_NOT_EXIST("TUR_IDNE", "Il turno che stai cercando non esiste"),
    TURNO_EMPTY_FIELD("TUR_EF", "Alcuni campi sono vuoti");

    private final String messageCode;
    private final String message;

    TurnoEnums(final String messageCode, final String message) {
        this.messageCode = messageCode;
        this.message = message;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public String getMessage() {
        return message;
    }

    public static TurnoEnums getTurnoEnumByMessageCode(final String messageCode) {
        for (final TurnoEnums turnoEnums : TurnoEnums.values()) {
            if (turnoEnums.getMessageCode().equals(messageCode)) {
                return turnoEnums;
            }
        }
        return null;
    }
}
