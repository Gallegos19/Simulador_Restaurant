package org.example.ui.spaceScene;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class OutsideAreas {
    public void createAreas() {
        FXGL.getGameWorld().addEntity(createArea(0, 0, 1000, 100)); // Área superior
        FXGL.getGameWorld().addEntity(createArea(0, 0, 50, 800)); // Área izquierda
        FXGL.getGameWorld().addEntity(createArea(950, 0, 50, 800)); // Área derecha
        FXGL.getGameWorld().addEntity(createArea(0, 750, 1000, 50)); // Área inferior
    }

    private Entity createArea(double x, double y, double width, double height) {
        return FXGL.entityBuilder()
                .at(x, y)
                .view(new Rectangle(width, height, Color.BLACK))
                .build();
    }
}
