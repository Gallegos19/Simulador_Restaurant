package org.example.core.controllers;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import org.example.core.utils.EntityManager;
import org.example.entities.Cliente;
import org.example.ui.entities.Utilidades;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ClienteController implements Runnable{
    private final Cliente cliente;
    private final org.example.ui.entities.Cliente clienteView;
    private final MesaController mesaController; // Inyección del controlador global de mesas
    private final ExecutorService executor;


    public ClienteController(Cliente cliente, org.example.ui.entities.Cliente clienteView, MesaController mesaController) {
        this.cliente = cliente;
        this.clienteView = clienteView;
        this.mesaController = mesaController;
        this.executor = Executors.newSingleThreadExecutor();


        // Asociar la entidad visual del cliente
        cliente.setClientEntity(clienteView.getEntity());
    }
    @Override
    public void run() {
        executor.submit(() -> {
            try {
                System.out.println("Iniciando cliente " + cliente.getId());
                // Ejecutar la lógica del cliente y su movimiento
                cliente.run();
                //move();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }

    public void move() {
        Point2D destino = obtenerDestinoMesa();
        System.out.println("Movimiento de destino: " + destino);
        if (destino != null) {
            // Mover la entidad visual del cliente hacia su destino
            Utilidades.moverEntidad(clienteView.getEntity(), destino, 3.0, () -> {
                Platform.runLater(() -> {
                    System.out.println("Cliente " + cliente.getId() + " llegó a la mesa " + cliente.getMesaId());

                    // Eliminar la entidad visual específica
                    EntityManager.deleteEntity(clienteView.getEntity());
                    System.out.println("Entidad del cliente " + cliente.getId() + " eliminada correctamente.");

                    // Mostrar entidades activas después de eliminar
//                    System.out.println("Entidades activas después de eliminar: ");
//                    FXGL.getGameWorld().getEntities().forEach(entity -> {
//                        Integer clienteId = entity.getProperties().getValue("clienteId");
//                        System.out.println("Entidad activa con clienteId: " + clienteId);
//                    });
                });
            });
        } else {
            System.err.println("Error: No se pudo obtener el destino de la mesa para el cliente " + cliente.getId());
        }
    }

    private Point2D obtenerDestinoMesa() {
        // Verificar si el cliente tiene asignada una mesa
        if (cliente.getMesaId() >= 0) {
            // Obtener las coordenadas de la mesa desde el controlador de mesas
            return mesaController.obtenerCoordenadasMesa(cliente.getMesaId());
        }
        return null; // Si no tiene mesa asignada, retornar null
    }
}
