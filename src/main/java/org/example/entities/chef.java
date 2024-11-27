package org.example.entities;

public class Chef {
    private final int id;

    public Chef(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void prepararPedido(int mesaId) {
        System.out.println("Chef " + id + " está preparando el pedido de la mesa " + mesaId);
    }
}
