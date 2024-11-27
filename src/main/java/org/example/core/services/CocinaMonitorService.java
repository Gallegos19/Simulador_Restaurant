package org.example.core.services;

import org.example.core.contracts.ICocinaMonitor;

import java.util.LinkedList;
import java.util.Queue;

public class CocinaMonitorService implements ICocinaMonitor {
    private final Queue<String> pedidosPendientes = new LinkedList<>();
    private final Queue<String> comidasListas = new LinkedList<>();

    @Override
    public synchronized void agregarPedido(String pedido) {
        pedidosPendientes.add(pedido);
        System.out.println("Pedido agregado: " + pedido);
        notifyAll(); // Notifica a los chefs que hay un pedido pendiente
    }

    @Override
    public synchronized String obtenerPedido() throws InterruptedException {
        while (pedidosPendientes.isEmpty()) {
            wait(); // Espera hasta que haya un pedido
        }
        return pedidosPendientes.poll();
    }

    @Override
    public synchronized void agregarComidaLista(String comida) {
        comidasListas.add(comida);
        System.out.println("Comida lista: " + comida);
    }

    @Override
    public synchronized void prepararOrden() throws InterruptedException {
        System.out.println("Cocinero está preparando una orden...");
        Thread.sleep(2000); // Simula tiempo de preparación
        System.out.println("Orden completada por el cocinero.");
    }
}
