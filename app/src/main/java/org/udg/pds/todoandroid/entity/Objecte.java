package org.udg.pds.todoandroid.entity;

//Clase a l'Android Studio
public class Objecte {
    private Long id;
    private String nom;
    private String descripcio;
    //IMATGE?

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }
}
