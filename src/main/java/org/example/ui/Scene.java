package org.example.ui;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

public class Scene extends GameApplication {

    private Entity recepcionista;
    private Entity cocinero;
    private List<Entity> mesas;
    private List<Entity> estaciones;
    private Entity cliente;
    private Entity mesero;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1000);
        settings.setHeight(800);
        settings.setTitle("Pizzería Simulación");
    }

    @Override
    protected void initGame() {
        // Crear elementos básicos
        createCheckerboardFloor();
        createOutsideAreas();

        // Áreas del edificio
        createStage(400, 50); // Escenario
        FXGL.getGameWorld().addEntity(createDiningRoom(100, 200, 800, 400)); // Comedor
        FXGL.getGameWorld().addEntity(createKitchen(50, 600)); // Cocina
        FXGL.getGameWorld().addEntity(createEntrance(400, 750)); // Entrada principal
        recepcionista = createRecepcion(390, 650);

        // Crear mesas
        mesas = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                double x = 150 + i * 200;
                double y = 250 + j * 100;
                mesas.add(createTable(x, y));
            }
        }

        // Crear estaciones de cocina
        estaciones = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            estaciones.add(createKitchenStation(60 + i * 60, 700));
        }

        // Crear cocinero
        cocinero = FXGL.entityBuilder()
                .at(estaciones.get(0).getX() - 30, estaciones.get(0).getY() - 100)
                .view(FXGL.getAssetLoader().loadTexture("cocinero.png", 100, 100))
                .build();
        FXGL.getGameWorld().addEntity(cocinero);

        // Crear mesero
        mesero = FXGL.entityBuilder()
                .at(200, 500)
                .view(FXGL.getAssetLoader().loadTexture("mesero.png", 80, 80))
                .build();
        FXGL.getGameWorld().addEntity(mesero);

        // Simular llegada del cliente
        simularFlujoCliente();
    }

    private void moverEntidad(Entity entidad, Point2D destino, double duracionSegundos, Runnable callback) {
        // Obtener la posición inicial y calcular la distancia total
        Point2D inicio = entidad.getPosition();
        double distanciaX = destino.getX() - inicio.getX();
        double distanciaY = destino.getY() - inicio.getY();

        // Dividir el movimiento en pasos pequeños
        int pasos = 60 * (int) duracionSegundos; // 60 FPS por duración en segundos
        double incrementoX = distanciaX / pasos;
        double incrementoY = distanciaY / pasos;

        new Thread(() -> {
            for (int i = 0; i < pasos; i++) {
                double finalIncrementoX = incrementoX * (i + 1);
                double finalIncrementoY = incrementoY * (i + 1);

                // Usar Platform.runLater para actualizar en el hilo de JavaFX
                Platform.runLater(() -> {
                    entidad.setPosition(inicio.getX() + finalIncrementoX, inicio.getY() + finalIncrementoY);
                });

                try {
                    Thread.sleep((long) (1000 / 60.0)); // Esperar el tiempo correspondiente a 60 FPS
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Ejecutar callback al final del movimiento
            if (callback != null) {
                Platform.runLater(callback);
            }
        }).start();
    }

    private void simularFlujoCliente() {
        FXGL.runOnce(() -> {
            // Crear cliente en la entrada
            cliente = FXGL.entityBuilder()
                    .at(400, 850)
                    .view(FXGL.getAssetLoader().loadTexture("cliente.png", 80, 80))
                    .build();
            FXGL.getGameWorld().addEntity(cliente);

            // Mover cliente al recepcionista
            moverEntidad(cliente, new Point2D(recepcionista.getX(), recepcionista.getY()), 3, () -> asignarMesa(cliente));
        }, javafx.util.Duration.seconds(1));
    }

    private void asignarMesa(Entity cliente) {
        for (Entity mesa : mesas) {
            if (mesa.getProperties().getValue("estado").equals("vacia")) {
                // Asignar mesa
                mesa.getProperties().setValue("estado", "ocupada");

                // Mover cliente a la mesa
                moverEntidad(cliente, new Point2D(mesa.getX(), mesa.getY()), 3, () -> {
                    // Cambiar sprite del cliente a "sentado"
                    mesa.getViewComponent().clearChildren();
                    mesa.getViewComponent().addChild(FXGL.getAssetLoader().loadTexture("cliente-sentado.png", 100, 100));

                    // Ocultar al cliente hasta que deba ir a pagar
                    cliente.getViewComponent().setVisible(false);

                    // Mover mesero a la mesa
                    moverMeseroAMesa(mesa);
                });
                return;
            }
        }
    }

    private void moverMeseroAMesa(Entity mesa) {
        moverEntidad(mesero, new Point2D(mesa.getX(), mesa.getY()), 3, () -> llevarPedidoACocinero(mesa));
    }

    private void llevarPedidoACocinero(Entity mesa) {
        // Cambiar sprite del mesero a "mesero-comida"
        mesero.getViewComponent().clearChildren();
        mesero.getViewComponent().addChild(FXGL.getAssetLoader().loadTexture("mesero.png", 80, 80));

        moverEntidad(mesero, new Point2D(cocinero.getX(), cocinero.getY()), 3, () -> entregarPedidoAMesa(mesa));
    }

    private void entregarPedidoAMesa(Entity mesa) {
        // Cambiar sprite del mesero de nuevo a "mesero"
        mesero.getViewComponent().clearChildren();
        mesero.getViewComponent().addChild(FXGL.getAssetLoader().loadTexture("mesero-comida.png", 80, 80));


        moverEntidad(mesero, new Point2D(mesa.getX(), mesa.getY()), 3, () -> {
            // Cambiar mesa a "cliente comiendo"
            mesa.getViewComponent().clearChildren();
            mesa.getViewComponent().addChild(FXGL.getAssetLoader().loadTexture("cliente-comiendo.png", 100, 100));
            mesero.getViewComponent().addChild(FXGL.getAssetLoader().loadTexture("mesero.png", 80, 80));
            // Simular cliente comiendo
            FXGL.runOnce(() -> terminarComida(mesa), javafx.util.Duration.seconds(5));
        });
    }

    private void terminarComida(Entity mesa) {
        // Cambiar la mesa a estado "sucia" pero no esperar para mover al cliente
        mesa.getViewComponent().clearChildren();
        mesa.getViewComponent().addChild(FXGL.getAssetLoader().loadTexture("mesa-sucia.png", 100, 100));
        mesa.getProperties().setValue("estado", "sucia");

        // Mover al cliente a pagar inmediatamente después de terminar de comer
        clientePagaYSeVa();

        // En paralelo, limpiar la mesa después de que el cliente se haya ido
        FXGL.runOnce(() -> limpiarMesa(mesa), javafx.util.Duration.seconds(2));
    }

    private void limpiarMesa(Entity mesa) {
        moverEntidad(mesero, new Point2D(mesa.getX(), mesa.getY()), 2, () -> {
            mesa.getViewComponent().clearChildren();
            mesa.getViewComponent().addChild(FXGL.getAssetLoader().loadTexture("mesa-vacia.png", 100, 100));
            mesa.getProperties().setValue("estado", "vacia");
        });
    }

    private void clientePagaYSeVa() {
        // Hacer visible al cliente y moverlo a la posición de salida
        cliente.getViewComponent().setVisible(true);
        moverEntidad(cliente, new Point2D(400, 850), 3, () -> {
            // Eliminar al cliente del mundo después de pagar
            FXGL.getGameWorld().removeEntity(cliente);
        });
    }

    private Entity createKitchenStation(double x, double y) {
        Entity station = FXGL.entityBuilder()
                .at(x, y)
                .view(new Rectangle(40, 30, Color.RED))
                .build();
        FXGL.getGameWorld().addEntity(station); // Asegúrate de agregarlo al mundo
        return station;
    }

    private Entity createTable(double x, double y) {
        Entity mesa = FXGL.entityBuilder()
                .at(x, y)
                .view(FXGL.getAssetLoader().loadTexture("mesa-vacia.png", 100, 100))
                .build();
        mesa.getProperties().setValue("estado", "vacia");
        FXGL.getGameWorld().addEntity(mesa); // Asegúrate de agregarlo al mundo
        return mesa;
    }

    private void createCheckerboardFloor() {
        int tileSize = 50; // Tamaño de cada cuadro
        for (int i = 0; i < 16; i++) { // Filas
            for (int j = 0; j < 20; j++) { // Columnas
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

    private void createOutsideAreas() {
        FXGL.getGameWorld().addEntity(createOutsideArea(0, 0, 1000, 100)); // Área superior
        FXGL.getGameWorld().addEntity(createOutsideArea(0, 0, 50, 800)); // Área izquierda
        FXGL.getGameWorld().addEntity(createOutsideArea(950, 0, 50, 800)); // Área derecha
        FXGL.getGameWorld().addEntity(createOutsideArea(0, 750, 1000, 50)); // Área inferior
    }

    private Entity createOutsideArea(double x, double y, double width, double height) {
        return FXGL.entityBuilder()
                .at(x, y)
                .view(new Rectangle(width, height, Color.BLACK))
                .build();
    }

    private void createStage(double x, double y) {
        Rectangle stageBase = new Rectangle(200, 100, Color.DARKVIOLET);

        var stageImage = FXGL.getAssetLoader().loadTexture("robots.png");
        stageImage.setFitWidth(180);
        stageImage.setFitHeight(90);

        FXGL.entityBuilder()
                .at(x, y)
                .view(stageBase)
                .view(stageImage)
                .buildAndAttach();
    }

    private Entity createDiningRoom(double x, double y, double width, double height) {
        return FXGL.entityBuilder()
                .at(x, y)
                .view(new Rectangle(width, height, Color.TRANSPARENT)) // Comedor transparente
                .build();
    }

    private Entity createKitchen(double x, double y) {
        return FXGL.entityBuilder()
                .at(x, y)
                .view(new Rectangle(250, 150, Color.WHITE)) // Cocina en blanco
                .build();
    }

    private Entity createEntrance(double x, double y) {
        return FXGL.entityBuilder()
                .at(x, y)
                .view(new Rectangle(200, 20, Color.DARKRED)) // Entrada principal roja
                .build();
    }


    private Entity createRecepcion(double x, double y) {
        Color recepcionColor = Color.web("#d4672e");
        Rectangle recepcion = new Rectangle(10, 100);
        recepcion.setFill(recepcionColor);

        var recepcionistaImage = FXGL.getAssetLoader().loadTexture("recepcionista.png", 100, 100);

        Entity recepcionEntity = FXGL.entityBuilder()
                .at(x, y)
                .view(recepcion)
                .build();

        Entity recepcionistaEntity = FXGL.entityBuilder()
                .at(x - 90, y)
                .view(recepcionistaImage)
                .build();

        FXGL.getGameWorld().addEntity(recepcionEntity);
        FXGL.getGameWorld().addEntity(recepcionistaEntity);

        return recepcionEntity;
    }


    public static void main(String[] args) {
        launch(args);
    }
}