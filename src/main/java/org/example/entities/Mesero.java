package org.example.entities;

public class Mesero {
    private final int id;

    public Mesero(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void atenderMesa(int mesaId) {
        System.out.println("Mesero " + id + " atendiendo la mesa " + mesaId);
    }
}
