package org.example.entities;

public class Chef {
    private int id;
    private String nombre;
    private int id_orden;

    public Chef(int id, String nombre, int id_orden) {
        this.id = id;
        this.nombre = nombre;
        this.id_orden = id_orden;
    }

    public void cocinar() {
        System.out.println("Chef " + id + " está cocinando.");
    }
    public void notificar() {
        System.out.println("Chef " + id + " Ya termino el pedido" + id_orden);
    }
}