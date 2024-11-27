package org.example.entities;

import org.example.core.services.RecepcionistaService;
import org.example.infrastructure.concurrency.MesaMonitor;

public class Cliente implements Runnable {
    private final int id;
    private final MesaMonitor mesaMonitor;
    private final RecepcionistaService recepcionistaService;
    private int mesaId;
    private boolean pedidoEntregado = false; // Estado del pedido

    public Cliente(int id, MesaMonitor mesaMonitor, RecepcionistaService recepcionistaService) {
        this.id = id;
        this.mesaMonitor = mesaMonitor;
        this.recepcionistaService = recepcionistaService;
    }

    public synchronized void setMesaId(int mesaId) {
        this.mesaId = mesaId;
        notify(); // Notifica que se le asignó una mesa
    }

    public synchronized void recibirPedido() {
        this.pedidoEntregado = true;
        notify(); // Notifica que el pedido fue entregado
    }

    public int getId() {
        return id;
    }

    @Override
    public void run() {
        try {
            // Registrarse con el recepcionista
            recepcionistaService.registrarCliente(this);

            synchronized (this) {
                System.out.println("Cliente " + id + " esperando al recepcionista.");
                wait(); // Espera al recepcionista
            }

            System.out.println("Cliente " + id + " se sienta en la mesa " + mesaId);

            synchronized (this) {
                System.out.println("Cliente " + id + " esperando a ser atendido.");
                while (!pedidoEntregado) {
                    wait(); // Espera a que su pedido sea entregado
                }
            }

            System.out.println("Cliente " + id + " recibió su pedido y se va del restaurante.");
            Thread.sleep(1000); // Simula el tiempo antes de salir
            mesaMonitor.liberarMesa(mesaId);
            System.out.println("Cliente " + id + " deja la mesa " + mesaId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
