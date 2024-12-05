package org.example.ui.entities;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.dsl.FXGL;

public class Recepcionista {
    private Entity entity;

    public Recepcionista(double x, double y) {
        // Inicialmente, el recepcionista está en su posición fija
        entity = FXGL.entityBuilder()
                .at(x, y)
                .view(FXGL.getAssetLoader().loadTexture("recepcionista.png", 100, 100))
                .build();
        FXGL.getGameWorld().addEntity(entity);
    }

    public Entity getEntity() {
        return entity;
    }

    public void mostrarEstado(String estado) {
        FXGL.getNotificationService().pushNotification("Itadori: " + estado);
    }

    // Método para simular movimiento (opcional)
    public void mover(double x, double y) {
        entity.setPosition(x, y);
    }
}
