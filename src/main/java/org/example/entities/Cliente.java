package org.example.entities;

import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;
import org.example.core.controllers.MesaController;
import org.example.core.utils.EntityManager;
import org.example.infrastructure.concurrency.MesaMonitor;
import org.example.ui.entities.Utilidades;

public class Cliente implements Runnable {
    private final int id;
    private final MesaMonitor mesaMonitor;
    private final Recepcionista recepcionista; // Cambiado a Recepcionista en lugar de RecepcionistaService
    private int mesaId = -1; // Inicialmente sin mesa asignada
    private boolean pedidoEntregado = false;
    private final MesaController mesaController;
    private final org.example.ui.entities.Cliente clienteView;

    // Entidad visual asociada
    private Entity clientEntity;

    public Cliente(int id, MesaMonitor mesaMonitor, Recepcionista recepcionista, MesaController mesaController, org.example.ui.entities.Cliente clienteView) {
        this.id = id;
        this.mesaMonitor = mesaMonitor;
        this.recepcionista = recepcionista;
        this.mesaController = mesaController;
        this.clienteView = clienteView;
    }

    public int getMesaId() {
        System.out.println("idMesa: " + mesaId);
        return mesaId;
    }

    public synchronized void setMesaId(int mesaId) {
        this.mesaId = mesaId;
        System.out.println("Mesa asignada: " + mesaId + " para cliente " + id);
        notifyAll();
    }


    public synchronized void esperarMesa() {
        while (mesaId == -1) {
            try {
                System.out.println("Cliente " + id + " esperando asignación de mesa...");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }


    public synchronized void recibirPedido() {
        this.pedidoEntregado = true;
        notify(); // Notificar que el pedido fue entregado
    }

    public int getId() {
        return id;
    }

    public void setClientEntity(Entity entity) {
        this.clientEntity = entity;
    }

    public Entity getClientEntity() {
        return clientEntity;
    }

    @Override
    public void run() {
        try {
            recepcionista.registrarCliente(this);
            synchronized (this) {
                System.out.println("Cliente " + id + " registrado con el recepcionista.");
            }

            this.esperarMesa();
            move();
            System.out.println("Cliente " + id + " se sienta en la mesa " + mesaId);
            synchronized (this) {
                System.out.println("Cliente " + id + " esperando a ser atendido.");
                while (!pedidoEntregado) {
                    wait();
                }
            }
            System.out.println("Cliente " + id + " recibió su pedido");
            mesaController.cambiarEstadoMesa(mesaId, "comiendo");
            Thread.sleep(2000);
            // Liberar la mesa después de terminar
            mesaMonitor.liberarMesaCliente(mesaId, this, mesaController);
            mesaController.cambiarEstadoMesa(mesaId, "sucia");


        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    private void move() {
        Point2D destino = obtenerDestinoMesa();
        if (destino != null) {
            System.out.println("Movimiento de destino del cliente: " + destino);
            Utilidades.moverEntidad(clienteView.getEntity(), destino, 3.0, () -> {
                System.out.println("Movimiento del mesero completado.");
                mesaController.cambiarEstadoMesa(mesaId, "ocupada");
                EntityManager.deleteEntity(clienteView.getEntity());
            });
        } else {
            System.err.println("Error: No se pudo obtener el destino de la mesa para el cliente.");
        }
    }

    private Point2D obtenerDestinoMesa() {
        if (this.getMesaId() >= 0) {
            return mesaController.obtenerCoordenadasMesa(this.getMesaId());
        }
        return null;
    }
}
