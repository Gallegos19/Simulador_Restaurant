package org.example.core.services;

import org.example.entities.Cliente;
import org.example.infrastructure.concurrency.MesaMonitor;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class RecepcionistaService implements Runnable {
    private final MesaMonitor mesaMonitor;
    private final Queue<Cliente> colaClientes; // Cola compartida para los clientes

    public RecepcionistaService(MesaMonitor mesaMonitor) {
        this.mesaMonitor = mesaMonitor;
        this.colaClientes = new LinkedBlockingQueue<>();
    }

    // Añadir cliente a la cola
    public synchronized void registrarCliente(Cliente cliente) {
        colaClientes.add(cliente);
        System.out.println("Recepcionista registra al cliente " + cliente.getId());
        notifyAll(); // Notifica que hay un cliente esperando
    }

    @Override
    public void run() {
        try {
            while (true) {
                Cliente cliente;

                synchronized (this) {
                    while (colaClientes.isEmpty()) {
                        wait(); // Espera si no hay clientes en la cola
                    }
                    cliente = colaClientes.poll(); // Toma el siguiente cliente de la cola
                }

                if (cliente != null) {
                    synchronized (cliente) {
                        int mesaId = mesaMonitor.ocuparMesa(); // Asigna una mesa al cliente
                        System.out.println("Recepcionista asigna la mesa " + mesaId + " al cliente " + cliente.getId());
                        cliente.setMesaId(mesaId);
                        cliente.notify(); // Notifica al cliente que ya tiene mesa
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
