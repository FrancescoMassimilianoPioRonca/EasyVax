package com.easyvax.exception.enums;

public enum RegioneEnum {

    REGIONE_NOT_FOUND("R_NF", "La regione che stai cercando non esiste"),
    REGIONI_NOT_EXIST("RI_NE", "Nel db non sono presenti regioni"),
    REGIONE_ALREADY_EXISTS("R_AE", "La regione che vuoi inserire è già presente nel sistema"),
    REGIONE_EMPTY_FIELD("R_EF", "Alcuni campi sono vuoti");

    private final String messageCode;
    private final String message;

    RegioneEnum(final String messageCode, final String message) {
        this.messageCode = messageCode;
        this.message = message;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public String getMessage() {
        return message;
    }

    public static RegioneEnum getRegioneEnumByMessageCode(final String messageCode) {
        for (final RegioneEnum regioneEnum : RegioneEnum.values()) {
            if (regioneEnum.getMessageCode().equals(messageCode)) {
                return regioneEnum;
            }
        }
        return null;
    }

}
