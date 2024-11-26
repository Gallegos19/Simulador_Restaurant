package org.example.entities;

public class Mesa {
    private int id_mesa;
    private String estado;
    private int id_cliente;
    public Mesa(int id_mesa, String estado, int id_cliente) {
        this.id_mesa = id_mesa;
        this.estado = estado;
        this.id_cliente = id_cliente;
    }
    public void ocupar(){
        estado = "ocupado";
        System.out.println("Mesa " + id_mesa + "estado" + estado + "para el cliente" + id_cliente);
    }
    public void liberar(){
        estado = "liberado";
        System.out.println("Mesa " + id_mesa + "estado" + estado);
    }

}
