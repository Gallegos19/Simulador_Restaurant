package org.example.core.services;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import org.example.core.controllers.MesaController;
import org.example.entities.Mesero;
import org.example.infrastructure.concurrency.CocinaMonitor;
import org.example.infrastructure.concurrency.MesaMonitor;
import org.example.ui.entities.Utilidades;

public class MeseroService implements Runnable {
    private final Mesero mesero;
    private final MesaMonitor mesaMonitor;
    private final CocinaMonitor cocinaMonitor;
    private final MesaController mesaController;
    private final org.example.ui.entities.Mesero meseroEntity;
    private volatile boolean running = true; // Flag para controlar la ejecución del hilo
    private int mesaId = -1; // Inicialmente, el mesero no tiene mesa asignada
    private final Object lock = new Object(); // Objeto para sincronización

    public MeseroService(Mesero mesero, MesaMonitor mesaMonitor, CocinaMonitor cocinaMonitor, MesaController mesaController, org.example.ui.entities.Mesero meseroEntity) {
        this.mesero = mesero;
        this.mesaMonitor = mesaMonitor;
        this.cocinaMonitor = cocinaMonitor;
        this.mesaController = mesaController;
        this.meseroEntity = meseroEntity;
    }

    public synchronized int getMesaId() {
        return mesaId;
    }

    public synchronized void volverACocina() {
        mesaId = -1; // Indica que el mesero está disponible
        Platform.runLater(() -> {
            if (!running) return; // Verifica si la aplicación sigue activa
            meseroEntity.cambiarEstado("normal");
        });
    }

    public void stopService() {
        running = false; // Detiene el servicio del mesero
        synchronized (lock) {
            lock.notifyAll(); // Despierta al hilo si está esperando
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("Mesero " + mesero.getId() + " está listo.");
            while (running) {
                // Paso 1: Espera por una mesa ocupada
                synchronized (mesero) {
                    mesaId = mesaMonitor.getMesaOcupada(mesero); // Espera hasta que haya una mesa ocupada
                    mesero.setMesaid(mesaId);
                }

                // Paso 2: Mover hacia la mesa para atenderla
                move(obtenerDestinoMesa(), "comida");
                esperarAnimacion();

                System.out.println("Mesero " + mesero.getId() + " atendiendo la mesa " + mesaId);

                // Simula el tiempo de atención en la mesa
                mesero.atenderMesa(mesaId);

                // Paso 3: Enviar el pedido a la cocina
                cocinaMonitor.nuevoPedido(mesaId);
                System.out.println("Mesero " + mesero.getId() + " envió el pedido de la mesa " + mesaId + " a la cocina.");

                // Paso 4: Mover a la cocina
                moveCocina();
                esperarAnimacion();

                // Paso 5: Esperar hasta que el pedido esté listo
                Integer pedido;
                synchronized (cocinaMonitor) {
                    while ((pedido = cocinaMonitor.retirarPedido(mesero)) == null) {
                        System.out.println("Mesero " + mesero.getId() + " esperando un pedido...");
                        cocinaMonitor.wait(); // Espera hasta que haya un pedido disponible
                        if (!running) return; // Salida segura si el servicio se detiene
                    }
                }

                // Paso 6: Entregar el pedido a la mesa
                moveEntrega(obtenerDestinoMesa(), "comida");
                esperarAnimacion();

                System.out.println("Mesero " + mesero.getId() + " entregó el pedido a la mesa " + mesaId);

                // Paso 7: Liberar la mesa
                synchronized (mesaMonitor) {
                    mesaMonitor.liberarMesa(mesaId, mesaController);
                }

                // Paso 8: Volver a la cocina y esperar nuevas tareas
                volverACocina();
                moveCocina();
                esperarAnimacion();
            }
        } catch (InterruptedException e) {
            if (running) {
                System.err.println("Mesero " + mesero.getId() + " interrumpido inesperadamente.");
            } else {
                System.out.println("Mesero " + mesero.getId() + " detenido.");
            }
        }
    }

    private void move(Point2D destino, String estadoVisual) {
        if (destino == null) {
            System.err.println("Error: No se pudo obtener el destino.");
            return;
        }

        Utilidades.moverEntidad(meseroEntity.getEntity(), destino, 3.0, () -> {
            synchronized (lock) {
                Platform.runLater(() -> {
                    if (!running) return; // Verifica si la aplicación sigue activa
                    meseroEntity.cambiarEstado(estadoVisual);
                });
                lock.notifyAll(); // Notifica que la animación ha terminado
            }
            System.out.println("Mesero " + mesero.getId() + " llegó al destino.");
        });
    }

    private void moveEntrega(Point2D destino, String estadoVisual) {
        move(destino, estadoVisual); // Reutiliza la lógica del método `move`
    }

    private void moveCocina() {
        System.out.println("Mesero " + mesero.getId() + " moviéndose a la cocina.");
        Utilidades.moverEntidad(meseroEntity.getEntity(), new Point2D(50, 650), 3.0, () -> {
            synchronized (lock) {
                Platform.runLater(() -> {
                    if (!running) return; // Verifica si la aplicación sigue activa
                    meseroEntity.cambiarEstado("comida");
                });
                lock.notifyAll(); // Notifica que la animación ha terminado
            }
            System.out.println("Mesero " + mesero.getId() + " llegó a la cocina.");
        });
    }

    private Point2D obtenerDestinoMesa() {
        if (this.getMesaId() >= 0) {
            return mesaController.obtenerCoordenadasMesa(this.getMesaId());
        }
        return null;
    }

    private void esperarAnimacion() throws InterruptedException {
        synchronized (lock) {
            lock.wait(); // Espera hasta que se notifique que la animación ha terminado
        }
    }
}
