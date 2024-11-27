package org.example.core.services;

import org.example.entities.Chef;
import org.example.infrastructure.concurrency.CocinaMonitor;

public class ChefService implements Runnable {
    private final Chef chef;
    private final CocinaMonitor cocinaMonitor;

    public ChefService(Chef chef, CocinaMonitor cocinaMonitor) {
        this.chef = chef;
        this.cocinaMonitor = cocinaMonitor;
    }

    @Override
    public void run() {
        try {
            while (true) {
                int idMesa = cocinaMonitor.tomarPedido(); // Toma un pedido para prepararlo
                chef.prepararPedido(idMesa); // Simula la preparación del pedido
                Thread.sleep(500); // Simula el tiempo de preparación
                cocinaMonitor.pedidoListo(idMesa); // Notifica que el pedido está listo
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
