package com.easyvax.exception.enums;

public enum VaccinoEnum {

    VACCINO_NOT_FOUND("VACC_NF", "Il vaccino che stai cercando non esiste"),
    VACCINO_ALREADY_EXISTS("VACC_AE", "Il vaccino che vuoi inserire è già presente nel sistema"),
    VACCINO_DELETE_ERROR("VACC_DLE", "Errore durante l'eliminazione del vaccino"),
    VACCINO_ID_NOT_EXIST("VACC_IDNE", "Il vaccino che stai cercando non esiste");

    private final String messageCode;
    private final String message;

    VaccinoEnum(final String messageCode, final String message) {
        this.messageCode = messageCode;
        this.message = message;
    }


    public static VaccinoEnum getVaccinoEnumByMessageCode(final String messageCode) {
        for (final VaccinoEnum vaccinoEnum : VaccinoEnum.values()) {
            if (vaccinoEnum.getMessageCode().equals(messageCode)) {
                return vaccinoEnum;
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
