package org.example.entities;

public class Mesero {
    private final int id;
    private int mesaid;

    public Mesero(int id) {
        this.id = id;
    }

    public int getMesaid() {
        return mesaid;
    }

    public void setMesaid(int mesaid) {
        this.mesaid = mesaid;
    }

    public int getId() {
        return id;
    }

    public void atenderMesa(int mesaId) {
        System.out.println("Mesero " + id + " atendiendo la mesa " + mesaId);
    }
}
