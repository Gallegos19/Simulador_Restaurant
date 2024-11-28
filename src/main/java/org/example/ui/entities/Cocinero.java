package org.example.ui.entities;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.dsl.FXGL;

public class Cocinero {
    private Entity entity;

    public Cocinero(double x, double y) {
        // Cocinero en su posición inicial
        entity = FXGL.entityBuilder()
                .at(x, y)
                .view(FXGL.getAssetLoader().loadTexture("cocinero.png", 100, 100))
                .build();
        FXGL.getGameWorld().addEntity(entity);
    }

    public Entity getEntity() {
        return entity;
    }
}
