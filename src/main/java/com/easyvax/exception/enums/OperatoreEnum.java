package com.easyvax.exception.enums;

public enum OperatoreEnum {

    OPERATORE_NOT_FOUND("OP_NF", "Il operatore che stai cercando non è stato trovato"),
    OPERATORE_ALREADY_EXISTS("OP_AE", "Il operatore che vuoi inserire è già presente nel sistema"),
    OPERATORE_DELETE_ERROR("OP_DLE", "Errore durante l'eliminazione del operatore"),
    OPERATORE_UPDATE_ERROR("OP_UE", "Errore durante l'update del operatore"),
    OPERATORE_ID_NOT_EXIST("OP_IDNE", "Il operatore che stai cercando non esiste"),
    OPERATORE_NOT_EXIST("OP_NE","Non c'è alcun operatore inserito nel DB"),
    OPERATORE_EMPTY_FIELD("OP_EF", "Alcuni campi sono vuoti");

    private final String messageCode;
    private final String message;

    OperatoreEnum(final String messageCode, final String message) {
        this.messageCode = messageCode;
        this.message = message;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public String getMessage() {
        return message;
    }

    public static OperatoreEnum getOperatoreEnumByMessageCode(final String messageCode) {
        for (final OperatoreEnum operatoreEnum : OperatoreEnum.values()) {
            if (operatoreEnum.getMessageCode().equals(messageCode)) {
                return operatoreEnum;
            }
        }
        return null;
    }
}
