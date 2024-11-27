package org.example.core.services;

import org.example.infrastructure.concurrency.CocinaMonitor;
import org.example.infrastructure.concurrency.MesaMonitor;

public class MeseroService implements Runnable {
    private final int id;
    private final MesaMonitor mesaMonitor;
    private final CocinaMonitor cocinaMonitor;

    public MeseroService(int id, MesaMonitor mesaMonitor, CocinaMonitor cocinaMonitor) {
        this.id = id;
        this.mesaMonitor = mesaMonitor;
        this.cocinaMonitor = cocinaMonitor;
    }

    @Override
    public void run() {
        try {
            while (true) {
                int mesaId = mesaMonitor.getMesaOcupada(); // Verifica mesas ocupadas
                System.out.println("Mesero " + id + " toma pedido de la mesa " + mesaId);

                cocinaMonitor.nuevoPedido(mesaId); // Enviar pedido a la cocina

                Thread.sleep(1000); // Simula tiempo de atención
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
