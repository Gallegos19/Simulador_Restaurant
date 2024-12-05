package org.example.ui.entities;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import javafx.application.Platform;

public class Mesa {
    private final Entity entity;

    public Mesa(double x, double y, int idMesa) {
        // Mesa inicialmente vacía
        entity = FXGL.entityBuilder()
                .at(x, y)
                .view(FXGL.getAssetLoader().loadTexture("mesa-vacia.png", 100, 100))
                .build();
        entity.getProperties().setValue("estado", "vacia");
        entity.getProperties().setValue("id", idMesa);

        FXGL.getGameWorld().addEntity(entity);
    }

    public void cambiarEstado(String estado) {
        Platform.runLater(() -> { // Modificaciones gráficas en el hilo de JavaFX
            entity.getViewComponent().clearChildren();
            switch (estado) {
                case "vacia":
                    entity.getViewComponent().addChild(FXGL.getAssetLoader().loadTexture("mesa-vacia.png", 100, 100));
                    break;
                case "ocupada":
                    entity.getViewComponent().addChild(FXGL.getAssetLoader().loadTexture("cliente-sentado.png", 100, 100));
                    break;
                case "comiendo":
                    entity.getViewComponent().addChild(FXGL.getAssetLoader().loadTexture("cliente-comiendo.png", 100, 100));
                    break;
                case "sucia":
                    entity.getViewComponent().addChild(FXGL.getAssetLoader().loadTexture("mesa-sucia.png", 100, 100));
                    break;
            }

            entity.getProperties().setValue("estado", estado);
        });
    }

    public double getX() {
        return entity.getX();
    }

    public double getY() {
        return entity.getY();
    }

    public String getEstado() {
        return entity.getProperties().getValue("estado");
    }

    public int getId() {
        return entity.getProperties().getInt("id");
    }

    public Entity getEntity() {
        return entity;
    }
}
