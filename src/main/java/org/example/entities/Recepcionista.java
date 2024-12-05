package org.example.entities;

import org.example.core.controllers.MesaController;
import org.example.infrastructure.concurrency.MesaMonitor;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Recepcionista implements Runnable {
    private final Queue<Cliente> colaClientes; // Cola de clientes
    private final MesaMonitor mesaMonitor;
    private final MesaController mesaController;

    public Recepcionista(MesaMonitor mesaMonitor, MesaController mesaController) {
        this.colaClientes = new LinkedBlockingQueue<>();
        this.mesaMonitor = mesaMonitor;
        this.mesaController = mesaController;
    }

    /**
     * Registrar cliente en la cola del recepcionista.
     */
    public synchronized void registrarCliente(Cliente cliente) {
        colaClientes.add(cliente);
        System.out.println("Recepcionista registra al cliente " + cliente.getId());
        notifyAll();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Cliente cliente;
                synchronized (this) {
                    while (colaClientes.isEmpty()) {
                        System.out.println("Recepcionista esperando clientes...");
                        wait();
                    }
                    cliente = colaClientes.poll();
                }

                if (cliente != null) {
                    procesarCliente(cliente);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Recepcionista interrumpido.");
        }
    }

    private void procesarCliente(Cliente cliente) {
        try {
            System.out.println("Recepcionista procesando al cliente " + cliente.getId());

            // Asignar una mesa al cliente
            synchronized (cliente) {
                int mesaId = mesaMonitor.ocuparMesa(cliente, mesaController);
                cliente.setMesaId(mesaId); // Asigna el ID de la mesa al cliente
                System.out.println("Recepcionista asignó la mesa " + mesaId + " al cliente " + cliente.getId());
                cliente.notifyAll();
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Recepcionista interrumpido al procesar cliente.");
        }
    }
}
