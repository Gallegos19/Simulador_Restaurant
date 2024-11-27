package org.example.entities;

public class Recepcionista {
    private final String nombre;

    public Recepcionista(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void asignarMesa(int clienteId, int mesaId) {
        System.out.println("Recepcionista " + nombre + " asigna la mesa " + mesaId + " al cliente " + clienteId);
    }
}
