package org.example.core.controllers;

import javafx.application.Platform;
import org.example.ui.entities.Recepcionista;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecepcionistaController {
    private final org.example.entities.Recepcionista recepcionistaService; // Clase Recepcionista que implementa Runnable
    private final Recepcionista recepcionista;
    private final ExecutorService executor; // Pool de un solo hilo para el recepcionista

    public RecepcionistaController(org.example.entities.Recepcionista recepcionistaService, Recepcionista recepcionista) {
        this.recepcionistaService = recepcionistaService;
        this.recepcionista = recepcionista;
        this.executor = Executors.newSingleThreadExecutor(); // Inicializa el executor para manejar el hilo del recepcionista
    }

    /**
     * Inicia el hilo del recepcionista y actualiza su vista periódicamente.
     */
    public void initRecepcionista() {
        // Ejecuta el método `run` de `Recepcionista` en un hilo separado
        executor.submit(() -> {
            try {
                Thread recepcionistaThread = new Thread(recepcionistaService::run);
                recepcionistaThread.start(); // Inicia el hilo que ejecutará `run`

                // Actualiza la vista del recepcionista periódicamente
//                while (!Thread.currentThread().isInterrupted()) {
//                    Platform.runLater(this::actualizarVistaRecepcionista); // Actualiza la interfaz gráfica
//                    Thread.sleep(100); // Controla la frecuencia de actualización
//                }

                recepcionistaThread.join(); // Espera a que el hilo del recepcionista termine
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("RecepcionistaController interrumpido.");
            }
        });
    }

    /**
     * Actualiza la vista del recepcionista (puedes personalizar este método).
     */
    private void actualizarVistaRecepcionista() {
        // Aquí puedes actualizar la posición del recepcionista o realizar animaciones
        double nuevaX = recepcionista.getEntity().getX() + 100; // Movimiento ficticio
        recepcionista.getEntity().setPosition(nuevaX, recepcionista.getEntity().getY());
    }

    /**
     * Detiene el hilo del recepcionista.
     */
    public void detener() {
        executor.shutdownNow(); // Detiene el executor
        System.out.println("RecepcionistaController detenido.");
    }
}
