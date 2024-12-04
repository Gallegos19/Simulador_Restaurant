package org.example.ui.entities;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.dsl.FXGL;

public class Mesero {
    private Entity entity;

    public Mesero(double x, double y) {
        // Mesero con estado inicial
        entity = FXGL.entityBuilder()
                .at(x, y)
                .view(FXGL.getAssetLoader().loadTexture("mesero-comida.png", 80, 80))
                .build();
        FXGL.getGameWorld().addEntity(entity);
    }

    public void cambiarEstado(String estado) {
        entity.getViewComponent().clearChildren();

        switch (estado) {
            case "normal":
                entity.getViewComponent().addChild(FXGL.getAssetLoader().loadTexture("mesero.png", 80, 80));
                break;
            case "comida":
                entity.getViewComponent().addChild(FXGL.getAssetLoader().loadTexture("mesero-comida.png", 80, 80));
                break;
        }
    }


    public Entity getEntity() {
        return entity;
    }
}

