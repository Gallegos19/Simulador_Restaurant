package org.example.ui.entities;

import com.almasb.fxgl.entity.Entity;
import javafx.application.Platform;
import javafx.geometry.Point2D;

public class Utilidades {
    public static void moverEntidad(Entity entidad, Point2D destino, double duracionSegundos, Runnable callback) {
        Point2D inicio = entidad.getPosition();
        double distanciaX = destino.getX() - inicio.getX();
        double distanciaY = destino.getY() - inicio.getY();
        int pasos = 60 * (int) duracionSegundos;
        double incrementoX = distanciaX / pasos;
        double incrementoY = distanciaY / pasos;

        new Thread(() -> {
            for (int i = 0; i < pasos; i++) {
                double finalIncrementoX = incrementoX * (i + 1);
                double finalIncrementoY = incrementoY * (i + 1);

                Platform.runLater(() -> entidad.setPosition(
                        inicio.getX() + finalIncrementoX,
                        inicio.getY() + finalIncrementoY
                ));

                try {
                    Thread.sleep((long) (1000 / 60.0));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (callback != null) {
                Platform.runLater(callback);
            }
        }).start();
    }
}