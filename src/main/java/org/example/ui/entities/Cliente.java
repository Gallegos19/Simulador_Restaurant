package org.example.ui.entities;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.dsl.FXGL;

public class Cliente {
    private Entity entity;

    public Cliente(double x, double y) {
        // Inicialmente, el cliente está de pie
        entity = FXGL.entityBuilder()
                .at(x, y)
                .view(FXGL.getAssetLoader().loadTexture("cliente.png", 80, 80))
                .build();
        FXGL.getGameWorld().addEntity(entity);
    }

    public Entity getEntity() {
        return entity;
    }
}
