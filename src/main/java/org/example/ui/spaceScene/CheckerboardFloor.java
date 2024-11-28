package org.example.ui.spaceScene;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CheckerboardFloor {
    public void createFloor() {
        int tileSize = 50;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 20; j++) {
                Color color = (i + j) % 2 == 0 ? Color.LIGHTGRAY : Color.DARKGRAY;
                FXGL.getGameWorld().addEntity(createTile(j * tileSize, i * tileSize, tileSize, color));
            }
        }
    }

    private Entity createTile(double x, double y, int size, Color color) {
        return FXGL.entityBuilder()
                .at(x, y)
                .view(new Rectangle(size, size, color))
                .build();
    }
}
