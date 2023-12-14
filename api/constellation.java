package app;

public class Constellation {

    public String name = "";
    public String latinName = "";
    public String abr = ""; // Abréviation, basée sur le nom latin.
    public String origin = ""; // Origine de l'appellation

    public Constellation() { }

    public Constellation(String name, String latinName, String abr, String origin) {
        this.name = name;
        this.latinName = name;
        this.abr = abr;
        this.origin = origin
    }
}