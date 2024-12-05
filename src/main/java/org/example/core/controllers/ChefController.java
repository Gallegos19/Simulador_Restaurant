package org.example.core.controllers;

import javafx.application.Platform;
import javafx.util.Duration;
import org.example.core.services.ChefService;
import org.example.ui.entities.Cocinero;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.almasb.fxgl.dsl.FXGLForKtKt.runOnce;

public class ChefController {
    private final ChefService chefService;
    private final Cocinero cocinero;
    private final ExecutorService executor;

    public ChefController(ChefService chefService, Cocinero cocinero) {
        this.chefService = chefService;
        this.cocinero = cocinero;
        this.executor = Executors.newSingleThreadExecutor(); // Ejecuta la lógica en un hilo separado
    }

    public void cook() {
        // Inicia el servicio del chef en un hilo aparte
        executor.execute(() -> {
            chefService.run();
        });

        // Actualiza la vista del cocinero periódicamente (simulando movimiento o animaciones)
        runOnce(() -> {
            Platform.runLater(this::actualizarVistaCocinero);
            return null; // Explicitamente retorna null, que FXGL interpreta como Unit
        }, Duration.seconds(1));
    }

    private void actualizarVistaCocinero() {
        // Aquí puedes actualizar la posición del cocinero, por ejemplo:
        double nuevaX = cocinero.getEntity().getX() + 10; // Movimiento ficticio
        cocinero.getEntity().setPosition(nuevaX, cocinero.getEntity().getY());
    }

    public void detener() {
        // Cierra el executor de manera limpia cuando sea necesario detener el chef
        executor.shutdownNow();
    }
}
