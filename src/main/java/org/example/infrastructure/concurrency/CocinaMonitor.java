package org.example.infrastructure.concurrency;

import java.util.LinkedList;
import java.util.Queue;

public class CocinaMonitor {
    private final Queue<Integer> colaPedidos = new LinkedList<>();

    public synchronized void nuevoPedido(int idMesa) {
        colaPedidos.add(idMesa);
        System.out.println("Nuevo pedido recibido para la mesa " + idMesa);
        notifyAll();
    }

    public synchronized int tomarPedido() throws InterruptedException {
        while (colaPedidos.isEmpty()) {
            wait();
        }
        int idMesa = colaPedidos.poll(); // Saca el pedido de la cola
        System.out.println("Preparando pedido para la mesa " + idMesa);
        return idMesa;
    }
}
