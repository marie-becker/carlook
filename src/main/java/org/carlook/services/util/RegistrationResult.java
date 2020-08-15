package org.carlook.services.util;

import java.util.regex.Pattern;

public class RegistrationResult {

    public enum FailureType{
        VORNAME_MISSING, NACHNAME_MISSING,  EMAIL_MISSING, EMAIL_INVALID, EMAIL_EXISTS, PW_MISSING,
        PW_TOO_SHORT, PWS_DONT_MATCH, NO_ROLE}

    private FailureType failureType;
    private boolean result;

    public void setReason(FailureType ft){
        this.failureType = ft;
    }
    public void setResult(boolean res){
        this.result = res;
    }

    public String error(){
        if(!result){
            switch (failureType){
                case VORNAME_MISSING:
                    return "Bitte geben sie Ihren Vornamen ein.";
                case NACHNAME_MISSING:
                    return "Bitte geben sie Ihren Nachnamen ein.";
                case EMAIL_MISSING:
                    return "Bitte geben sie Ihre Email-Adresse ein.";
                case EMAIL_EXISTS:
                    return "Diese Email wird schon verwendet.";
                case PW_MISSING:
                    return "Bitte geben sie ein Passwort ein.";
                case EMAIL_INVALID:
                    return "Ungültige Email-Adresse!";
                case PW_TOO_SHORT:
                    return "Passwort zu kurz! Mind. 8 Zeichen notwendig!";
                case PWS_DONT_MATCH:
                    return "Die eingegebenen Passwörter stimmen nicht überein!";
                case NO_ROLE:
                    return "Wählen sie eine Profilart aus!";
                default:
                    return null;
            }
        }
        return null;
    }

    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public FailureType getFailureType(){
        return this.failureType;
    }

    public boolean getResult(){
        return this.result;
    }

}
