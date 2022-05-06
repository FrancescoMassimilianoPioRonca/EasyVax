package com.easyvax.exception.enums;

public enum ProvinciaEnum {

    PROVINCIA_NOT_FOUND("P_NF", "La provincia che stai cercando non esiste"),
    PROVINCIA_ALREADY_EXISTS("P_AE", "La provincia che vuoi inserire è già presente nel sistema"),
    CAP_NOT_FOUND("C_NF", "Il cap inserito non esiste"),
    PROVINCE_NOT_EXISTS("PE_NE", "Nel db non sono presenti province"),
    PROVINCIA_EMPTY_FIELD("P_EF", "Alcuni campi sono vuoti"),
    PROVINCIA_REGIONE_NOT_EXISTS("PR_NE", "Non ci sono province associate alla regione inserita");

    private final String messageCode;
    private final String message;

    ProvinciaEnum(final String messageCode, final String message) {
        this.messageCode = messageCode;
        this.message = message;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public String getMessage() {
        return message;
    }

    public static ProvinciaEnum getProvinciaByMessageCode(final String messageCode) {
        for (final ProvinciaEnum provinciaEnum : ProvinciaEnum.values()) {
            if (provinciaEnum.getMessageCode().equals(messageCode)) {
                return provinciaEnum;
            }
        }
        return null;
    }
}
