package org.example.ui.spaceScene;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Kitchen {
    public void createKitchen(double x, double y) {
        Entity kitchen = FXGL.entityBuilder()
                .at(x, y)
                .view(new Rectangle(250, 150, Color.WHITE)) // Cocina en blanco
                .build();
        FXGL.getGameWorld().addEntity(kitchen);
    }
}
