package com.easyvax.exception.enums;

public enum CentroVaccinaleEnum {

    CENTROVACCINALE_NOT_FOUND("CV_NF", "Il centro vaccinale che stai cercando non è stato trovato"),
    CENTROVACCINALE_ALREADY_EXISTS("CV_AE", "Il CENTROVACCINALE che vuoi inserire è già presente nel sistema"),
    CENTROVACCINALE_DELETE_ERROR("CV_DLE", "Errore durante l'eliminazione del CENTROVACCINALE"),
    CENTROVACCINALE_ID_NOT_EXIST("CV_IDNE", "Il CENTROVACCINALE che stai cercando non esiste"),
    CENTROVACCINALES_NOT_FOUND("CVS_NF","Non sono presenti centri vaccinali nel db"),
    CENTROVACCINALE_CAP_NOT_EXITS("CVS_CNF","Non sono presenti centri vaccinali per il cap inserito"),
    CENTROVACCINALE_EMPTY_FIELD("CV_EF", "Alcuni campi sono vuoti");

    private final String messageCode;
    private final String message;

    CentroVaccinaleEnum(final String messageCode, final String message) {
        this.messageCode = messageCode;
        this.message = message;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public String getMessage() {
        return message;
    }

    public static CentroVaccinaleEnum getCentroVaccinaleEnumByMessageCode(final String messageCode) {
        for (final CentroVaccinaleEnum centroVaccinaleEnum : CentroVaccinaleEnum.values()) {
            if (centroVaccinaleEnum.getMessageCode().equals(messageCode)) {
                return centroVaccinaleEnum;
            }
        }
        return null;
    }
}
