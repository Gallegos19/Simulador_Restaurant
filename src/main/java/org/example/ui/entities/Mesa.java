package org.example.ui.entities;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import org.example.config.Constants;
import org.example.infrastructure.concurrency.MesaMonitor;
import org.example.config.Constants;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private List<Entity> tables; // Lista de mesas gráficas
    private Constants config = new Constants();
    private MesaMonitor mesaMonitor;

    public Table() {
        this.tables = new ArrayList<>();
        this.mesaMonitor = new MesaMonitor(config.getNumMesas()); // Conectar con la lógica de mesas
    }

    public void createTables(int rows, int cols) {
        Constants config = new Constants();
        int maxTables = config.getNumMesas(); // Número máximo de mesas desde configuración
        int tableCount = 0;

        for (int i = 0; i < rows && tableCount < maxTables; i++) {
            for (int j = 0; j < cols && tableCount < maxTables; j++) {
                double x = 150 + i * 200;
                double y = 250 + j * 100;

                // Crear la mesa gráfica
                Entity table = createTable(x, y, tableCount);

                // Pasar la posición lógica al monitor
                mesaMonitor.getPosicion(); // Actualizar posición en MesaMonitor si aplica.

                tables.add(table);
                tableCount++;
            }
        }
    }

    private Entity createTable(double x, double y, int idMesa) {
        Entity table = FXGL.entityBuilder()
                .at(x, y)
                .view(FXGL.getAssetLoader().loadTexture("mesa-vacia.png", 100, 100)) // Usa la textura adecuada
                .build();

        // Define el estado inicial de la mesa como "vacía"
        table.getProperties().setValue("estado", "vacia");
        table.getProperties().setValue("idMesa", idMesa); // Asocia el ID lógico

        FXGL.getGameWorld().addEntity(table); // Agrega la mesa al mundo del juego
        return table;
    }

    public List<Entity> getTables() {
        return tables;
    }

    private void updateTableState(int idMesa, String estado) {
        Entity table = tables.get(idMesa); // Obtiene la mesa por su ID

        table.getProperties().setValue("estado", estado); // Actualiza el estado en las propiedades

        // Elimina la vista existente y añade una nueva según el estado
        table.getViewComponent().clearChildren();

        if (estado.equals("ocupada")) {
            table.getViewComponent().addChild(FXGL.getAssetLoader().loadTexture("mesa-ocupada.png", 100, 100));
        } else {
            table.getViewComponent().addChild(FXGL.getAssetLoader().loadTexture("mesa-vacia.png", 100, 100));
        }
    }

}
