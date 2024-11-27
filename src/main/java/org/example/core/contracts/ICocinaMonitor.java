package org.example.core.contracts;

public interface ICocinaMonitor {
    void agregarPedido(String pedido); // Agregar un pedido a la cola
    String obtenerPedido() throws InterruptedException; // Obtener un pedido para preparar
    void agregarComidaLista(String comida); // Marcar una comida como lista
    void prepararOrden() throws InterruptedException; // Simular la preparación de una orden
}
