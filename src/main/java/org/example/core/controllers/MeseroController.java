package org.example.core.controllers;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import org.example.core.services.MeseroService;
import org.example.ui.entities.Utilidades;
import com.almasb.fxgl.entity.Entity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MeseroController {
    private final MeseroService mesero;
    private final org.example.ui.entities.Mesero meseroView;
    private final ExecutorService executor;
    private final MesaController mesaController;

    // Entidad visual asociada
    private Entity meseroEntity;

    public MeseroController(MeseroService mesero, org.example.ui.entities.Mesero meseroView, MesaController mesaController) {
        this.mesero = mesero;
        this.meseroView = meseroView;
        this.executor = Executors.newSingleThreadExecutor();
        this.mesaController = mesaController;

        // Asociar la entidad visual del mesero al controlador
        this.meseroEntity = meseroView.getEntity();
    }

    public void attend() {
        executor.execute(() -> {
            try {
                mesero.run(); // Lógica del mesero
            } catch (Exception e) {
                System.err.println("Error durante la ejecución del mesero: " + e.getMessage());
                e.printStackTrace();
            }
        });


    }

    public void detener() {
        executor.shutdownNow();
    }


}
