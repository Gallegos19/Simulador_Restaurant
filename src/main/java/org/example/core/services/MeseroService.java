package org.example.core.services;

import org.example.entities.Mesero;
import org.example.infrastructure.concurrency.CocinaMonitor;
import org.example.infrastructure.concurrency.MesaMonitor;

public class MeseroService implements Runnable {
    private final Mesero mesero;
    private final MesaMonitor mesaMonitor;
    private final CocinaMonitor cocinaMonitor;

    public MeseroService(Mesero mesero, MesaMonitor mesaMonitor, CocinaMonitor cocinaMonitor) {
        this.mesero = mesero;
        this.mesaMonitor = mesaMonitor;
        this.cocinaMonitor = cocinaMonitor;
    }

    @Override
    public void run() {
        try {
            while (true) {
                int mesaId = mesaMonitor.getMesaOcupada(mesero); // Verifica mesas ocupadas
                mesero.atenderMesa(mesaId); // Mesero atiende la mesa
                cocinaMonitor.nuevoPedido(mesaId); // Envía el pedido a la cocina
                Thread.sleep(1000); // Simula tiempo de atención
                System.out.println("Mesero " + mesero.getId() + " entregó el pedido a la mesa " + mesaId + ".");
                synchronized (mesaMonitor) {
                    mesaMonitor.liberarMesa(mesaId); // Libera la mesa después de que el cliente termina
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
