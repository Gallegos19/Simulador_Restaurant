package org.example.core.services;

import org.example.entities.Cliente;
import org.example.entities.Recepcionista;
import org.example.infrastructure.concurrency.MesaMonitor;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class RecepcionistaService implements Runnable {
    private final Recepcionista recepcionista;
    private final MesaMonitor mesaMonitor;
    private final Queue<Cliente> colaClientes; // Cola compartida para los clientes

    public RecepcionistaService(Recepcionista recepcionista, MesaMonitor mesaMonitor) {
        this.recepcionista = recepcionista;
        this.mesaMonitor = mesaMonitor;
        this.colaClientes = new LinkedBlockingQueue<>();
    }

    /**
     * Registrar cliente en la cola del recepcionista.
     */
    public synchronized void registrarCliente(Cliente cliente) {
        colaClientes.add(cliente);
        System.out.println("Recepcionista registra al cliente " + cliente.getId());
        notifyAll(); // Notifica que hay clientes en la cola
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
                        int mesaId = mesaMonitor.ocuparMesa(cliente); // Asigna una mesa disponible
                        recepcionista.asignarMesa(cliente.getId(), mesaId); // Lógica del recepcionista
                        cliente.setMesaId(mesaId); // Notifica al cliente su mesa
                        cliente.notify(); // Despierta al cliente
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
