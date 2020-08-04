package org.carlook.model.objects;

public class Auto {
    private int autoid;
    private String marke;
    private int baujahr;
    private String beschreibung;

    public String getMarke() {
        return marke;
    }

    public void setMarke(String marke) {
        this.marke = marke;
    }

    public int getBaujahr() {
        return baujahr;
    }

    public void setBaujahr(int baujahr) {
        this.baujahr = baujahr;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public int getAutoid() {
        return autoid;
    }

    public void setAutoid(int autoid) {
        this.autoid = autoid;
    }
}
