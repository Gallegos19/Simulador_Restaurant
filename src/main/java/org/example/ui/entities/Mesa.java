package org.example.ui.entities;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.dsl.FXGL;
import com.sun.source.doctree.EntityTree;
import org.example.ui.spaceScene.DiningRoom;

import javax.sound.midi.MidiMessage;

public class Mesa {
    public enum EntityType {
        MESA, // Puedes agregar otros tipos si es necesario
        MESERO,
        COCINA,
        // Otros tipos...
    }

    private Entity entity;

    public Mesa(double x, double y) {
        // Mesa inicialmente vacía
        entity = FXGL.entityBuilder()
                .at(x, y)
                .view(FXGL.getAssetLoader().loadTexture("mesa-vacia.png", 100, 100))
                .build();
        entity.getProperties().setValue("estado", "vacia");
        FXGL.getGameWorld().addEntity(entity);
    }


    public void cambiarEstado(int idMesa,String estado) {
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
    }

    public Entity getEntity() {
        return entity;
    }
}
