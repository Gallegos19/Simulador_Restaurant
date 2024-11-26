package org.example.entities;

public class Order {
    private int id_order;
    private int id_cliente;
    private String estado;

    public Order(int id_order, int id_cliente, String estado) {
        this.id_order = id_order;
        this.id_cliente = id_cliente;
        this.estado = estado;

    }
}
