package org.example.entities;

public class Recepcionista {
    private int id;
    private int id_lugar;
    private int id_cliente;

    public Recepcionista(int id, String nombre, int id_cliente) {
        this.id = id;
        this.id_cliente = id_cliente;
    }

    public void buscar_lugar() {
        System.out.println("Recepcionista " + id + " está buscando lugar para el cliente " + id_cliente);
    }
    public void asignar_lugar(int id_lugar) {
        this.id_lugar = id_lugar;
        System.out.printf("Recepcionista" + id + " esta asignando lugar" + this.id_lugar + "para el cliente " + id_cliente );

    }
}
