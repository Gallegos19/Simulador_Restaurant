package org.example.entities;

import org.example.core.services.RecepcionistaService;
import org.example.infrastructure.concurrency.MesaMonitor;
import org.example.patterns.Observador;

public class Cliente implements Observador, Runnable{
    private final int id;
    private final MesaMonitor mesaMonitor;
    private final RecepcionistaService recepcionistaService;
    private int mesaId;
    private boolean pedidoEntregado = false; // Estado del pedido
    private int mesaAsignada = -1;

    public Cliente(int id, MesaMonitor mesaMonitor, RecepcionistaService recepcionistaService) {
        this.id = id;
        this.mesaMonitor = mesaMonitor;
        this.recepcionistaService = recepcionistaService;
        this.mesaMonitor.agregarObservador((Observador) this);
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

//    @Override
//    public void actualizar(String mensaje) {
//        System.out.println("Cliente " + id + " recibió notificación: " + mensaje);
//        if (mensaje.contains("asignada")) {
//            mesaAsignada = Integer.parseInt(mensaje.split(" ")[1]);
//        }
//    }

    @Override
    public void actualizar(String mensaje) {
        synchronized (this) {
            System.out.println("Cliente " + id + " recibió notificación: " + mensaje);
            if (mensaje.contains("asignada")) {
                int mesa = Integer.parseInt(mensaje.split(" ")[1]);
                if (mesaId == -1) { // Solo toma la primera mesa asignada
                    setMesaId(mesa);
                }
            }
        }
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
            System.out.println("Cliente " + id + " recibió su pedido");
            Thread.sleep(3000); // Simula el tiempo antes de salir
            mesaMonitor.liberarMesaCliente(mesaId, this);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
