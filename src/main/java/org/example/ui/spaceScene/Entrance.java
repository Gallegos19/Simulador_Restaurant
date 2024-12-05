package org.example.ui.spaceScene;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Entrance {
    public void createEntrance(double x, double y) {
        Entity entrance = FXGL.entityBuilder()
                .at(x, y)
                .view(new Rectangle(200, 20, Color.DARKRED)) // Entrada principal roja
                .build();
        FXGL.getGameWorld().addEntity(entrance);
    }
}
