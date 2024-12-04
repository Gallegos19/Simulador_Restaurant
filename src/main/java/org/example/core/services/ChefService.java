package org.example.core.services;

import org.example.entities.Chef;
import org.example.infrastructure.concurrency.CocinaMonitor;

import java.util.Random;

public class ChefService implements Runnable {
    private final Chef chef;
    private final CocinaMonitor cocinaMonitor;
    private final Random random = new Random();

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

                // Genera un tiempo aleatorio entre 1 y 5 segundos
                int tiempoPreparacion = 1000 + random.nextInt(4000);
                System.out.println("Chef preparando pedido de la mesa " + idMesa + " durante " + tiempoPreparacion + " ms.");

                Thread.sleep(tiempoPreparacion); // Simula el tiempo de preparación

                cocinaMonitor.pedidoListo(idMesa); // Notifica que el pedido está listo
                System.out.println("Chef terminó el pedido de la mesa " + idMesa);
            }
        } catch (InterruptedException e) {
            System.out.println("Chef interrumpido durante la preparación.");
            Thread.currentThread().interrupt();
        }
    }
}
