package org.example.ui.spaceScene;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class DiningRoom {
    public void createDiningRoom(double x, double y, double width, double height) {
        Entity diningRoom = FXGL.entityBuilder()
                .at(x, y)
                .view(new Rectangle(width, height, Color.TRANSPARENT)) // Comedor transparente
                .build();
        FXGL.getGameWorld().addEntity(diningRoom);
    }
}
