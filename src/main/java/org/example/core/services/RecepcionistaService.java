package org.example.core.services;

import org.example.entities.Recepcionista;

public class RecepcionistaService {
    private final Recepcionista recepcionista;
    private Thread thread;

    public RecepcionistaService(Recepcionista recepcionista) {
        this.recepcionista = recepcionista;
    }

    /**
     * Iniciar el hilo del recepcionista.
     */
    public synchronized void iniciarServicio() {
        if (thread == null || !thread.isAlive()) {
            thread = new Thread(recepcionista);
            thread.start();
        } else {
            System.out.println("El servicio del recepcionista ya está iniciado.");
        }
    }

    /**
     * Registrar un cliente en la cola del recepcionista.
     */
//    public void registrarCliente(org.example.entities.Cliente cliente) {
//        recepcionista.registrarCliente(cliente);
//    }

    /**
     * Detener el hilo del recepcionista.
     */
    public synchronized void detenerServicio() {
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
            thread = null; // Limpia la referencia del hilo
        }
    }
}
