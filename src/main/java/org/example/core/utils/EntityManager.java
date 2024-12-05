package org.example.core.utils;


import com.almasb.fxgl.entity.Entity;

public class EntityManager {
    public static void deleteEntity(Entity entity) {
        if (entity != null) {
            entity.removeFromWorld(); // Elimina la entidad del mundo
            System.out.println("Entidad eliminada correctamente: " + entity);
        } else {
            System.err.println("Error: Entidad nula, no se pudo eliminar.");
        }
    }
}

