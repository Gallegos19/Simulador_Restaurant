package org.example.core.services;

import org.example.infrastructure.concurrency.CocinaMonitor;

public class ChefService implements Runnable {
    private final int id;
    private final CocinaMonitor cocinaMonitor;

    public ChefService(int id, CocinaMonitor cocinaMonitor) {
        this.id = id;
        this.cocinaMonitor = cocinaMonitor;
    }

    @Override
    public void run() {
        try {
            while (true) {
                int mesaId = cocinaMonitor.tomarPedido(); // Toma un pedido de la cola
                System.out.println("Chef " + id + " está preparando el pedido de la mesa " + mesaId);

                Thread.sleep(3000); // Simula tiempo de preparación
                System.out.println("Chef " + id + " terminó el pedido de la mesa " + mesaId);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
