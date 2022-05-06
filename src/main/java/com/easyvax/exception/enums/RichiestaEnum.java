package com.easyvax.exception.enums;

public enum RichiestaEnum {

    RICHIESTE_NE("RS_NE", "Nel db non sono presenti richieste"),
    RICHIESTE_E("RS_E", "Errore nell'inserire la rchiesta. Controllare i parametri"),
    RICHIESTA_NF("RS_NF", "La richiesta non è stata trovata");


    private final String messageCode;
    private final String message;

    RichiestaEnum(final String messageCode, final String message) {
        this.messageCode = messageCode;
        this.message = message;
    }


    public static RichiestaEnum getRichiestEnumByMessageCode(final String messageCode) {
        for (final RichiestaEnum richiestaEnum : RichiestaEnum.values()) {
            if (richiestaEnum.getMessageCode().equals(messageCode)) {
                return richiestaEnum;
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
