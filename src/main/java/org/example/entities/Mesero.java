package org.example.entities;

public class Mesero {
    private int id;
    private String nombre;
    private int id_mesa;

    public Mesero(int id, String nombre, int id_mesa) {
        this.id = id;
        this.nombre = nombre;
        this.id_mesa = id_mesa;
    }

    public void atender() {
        System.out.println("Mesero " + id + " está atendiendo la mesa" + id_mesa);
    }
    public void limpiar_mesa() {
        System.out.println("Mesero " + id + " está limpiando la mesa" + id_mesa);    }
}
