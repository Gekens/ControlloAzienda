package com.example.giacomo.controllomagazzino;

/**
 * Created by Giacomo on 24/06/2016.
 */
public class Cherries {

    String code = null;
    String name = null;
    String surname = null;
    String mail = null;
    boolean selected = false;

    public Cherries(String code, String name, String surname, String mail, boolean selected) {
        super();
        this.code = code;
        this.name = name;
        this.surname = surname;
        this.mail = mail;
        this.selected = selected;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getConferma() {
        return name;
    }
    public String getConfermaSur() {
        return surname;
    }
    public String getConfermaMail() {
        return mail;
    }

    public String getName() {
        return name.concat(" ").concat(surname);
    }
    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
