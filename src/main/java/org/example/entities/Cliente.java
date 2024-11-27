package org.example.entities;

import org.example.core.services.RecepcionistaService;
import org.example.infrastructure.concurrency.MesaMonitor;

public class Cliente implements Runnable {
    private final int id;
    private final MesaMonitor mesaMonitor;
    private final RecepcionistaService recepcionista;
    private int mesaId;

    public Cliente(int id, MesaMonitor mesaMonitor, RecepcionistaService recepcionista) {
        this.id = id;
        this.recepcionista = recepcionista;
        this.mesaMonitor = mesaMonitor;

    }
    public synchronized void setMesaId(int mesaId) {
        this.mesaId = mesaId;
    }

    @Override
    public void run() {
        try {
            synchronized (this) {
                System.out.println("Cliente " + id + " esperando al recepcionista.");
                recepcionista.registrarCliente(this); // Se registra con el recepcionista
                wait(); // Espera a que el recepcionista le asigne una mesa
            }
            System.out.println("Cliente " + id + " se sienta en la mesa " + mesaId);

            synchronized (this) {
                wait(); // Espera su comida
            }

            System.out.println("Cliente " + id + " recibió su comida y se va del restaurante.");
            mesaMonitor.liberarMesa(mesaId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int getId() {
        return id;
    }
}
