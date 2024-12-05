package org.example.ui.spaceScene;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Kitchen {
    public void createKitchen(double startX, double startY) {
        int tileSize = 50; // Tamaño de los azulejos
        int rows = 4; // Número de filas de azulejos
        int cols = 6; // Número de columnas de azulejos

        // Crear patrón de azulejos
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Alternar colores para simular un patrón de cocina
                Color color = (i + j) % 2 == 0 ? Color.BEIGE : Color.DIMGRAY;
                FXGL.getGameWorld().addEntity(createTile(startX + j * tileSize, startY + i * tileSize, tileSize, color));
            }
        }

        // Crear un "mesón" en la cocina
        createCounter(startX, startY + rows * tileSize, cols * tileSize, 20);
    }

    private Entity createTile(double x, double y, int size, Color color) {
        return FXGL.entityBuilder()
                .at(x, y)
                .view(new Rectangle(size, size, color))
                .build();
    }

    private void createCounter(double x, double y, double width, double height) {
        // Crear una entidad que represente un mesón
        FXGL.entityBuilder()
                .at(x, y)
                .view(new Rectangle(width, height, Color.SADDLEBROWN)) // Color marrón para el mesón
                .buildAndAttach();
    }
}

