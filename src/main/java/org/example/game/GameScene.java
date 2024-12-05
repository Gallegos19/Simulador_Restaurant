package org.example.game;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.example.config.Constants;
import org.example.core.controllers.*;
import org.example.core.services.ChefService;
import org.example.core.services.MeseroService;
import org.example.entities.Chef;
import org.example.entities.Cliente;
import org.example.entities.Recepcionista;
import org.example.infrastructure.concurrency.CocinaMonitor;
import org.example.infrastructure.concurrency.MesaMonitor;
import org.example.ui.entities.Cocinero;
import org.example.ui.spaceScene.CheckerboardFloor;
import org.example.ui.spaceScene.OutsideAreas;
import org.example.ui.spaceScene.DiningRoom;
import org.example.ui.spaceScene.Kitchen;
import org.example.ui.spaceScene.Entrance;
import org.example.ui.entities.Mesa;
import org.example.ui.entities.Mesero;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static com.almasb.fxgl.dsl.FXGLForKtKt.loopBGM;
import static java.util.concurrent.Executors.newFixedThreadPool;

public class GameScene extends GameApplication {
    private CheckerboardFloor checkerboardFloor;
    private OutsideAreas outsideAreas;
    private DiningRoom diningRoom;
    private Kitchen kitchen;
    private Entrance entrance;
    private Mesa table;
    private MesaMonitor mesaMonitor;
    private CocinaMonitor cocinaMonitor;
    private org.example.entities.Mesero mesero;
    private MesaController mesaController;
    private ExecutorService executor = newFixedThreadPool(5);
    private List<Entity> estaciones;


    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1000);
        settings.setHeight(800);
        settings.setTitle("Pizzería Simulación");
    }

    @Override
    protected void initGame() {
        this.startSoundtrack();
        Constants config = new Constants();
        createStage(400, 50); // Escenario
        mesaController = new MesaController();
        mesaMonitor = new MesaMonitor(config.getNumMesas());
        cocinaMonitor = new CocinaMonitor();

        checkerboardFloor = new CheckerboardFloor();
        checkerboardFloor.createFloor();

        outsideAreas = new OutsideAreas();
        outsideAreas.createAreas();

        diningRoom = new DiningRoom();
        diningRoom.createDiningRoom(100, 200, 800, 400);

        kitchen = new Kitchen();
        kitchen.createKitchen(50, 600);

        entrance = new Entrance();
        entrance.createEntrance(400, 750);


    }

    private void crearClientesDinamicamente(Constants config, Recepcionista recepcionista) {
        int numClientes = config.getNumClientes();
        double startX = 300; // Coordenada X inicial para los clientes
        double startY = 700; // Coordenada Y inicial para los clientes
        double spacingX = 50; // Espaciado entre clientes en X

        for (int i = 0; i < numClientes; i++) {
            org.example.ui.entities.Cliente clienteView = new org.example.ui.entities.Cliente(startX + (i * spacingX), startY);
            Cliente cliente = new Cliente(i + 1, mesaMonitor, recepcionista, mesaController, clienteView);
            clienteView.getEntity().getProperties().setValue("clienteId", cliente.getId());
            FXGL.getGameWorld().addEntity(clienteView.getEntity());

            ClienteController clienteController = new ClienteController(cliente, clienteView, mesaController);

            executor.submit(clienteController::run);
        }

        System.out.println(numClientes + " clientes creados e inicializados.");
    }

    private void crearMeserosDinamicamente(Constants config) {
        int numMeseros = config.getNumMeseros();
        double startX = 100; // Coordenada X inicial para los meseros
        double startY = 500; // Coordenada Y inicial para los meseros
        double spacingX = 80; // Espaciado entre meseros en X



        for (int i = 0; i < numMeseros; i++) {
            org.example.entities.Mesero mesero = new org.example.entities.Mesero(i + 1);
            // Crear vista del mesero
            Mesero meseroView = new Mesero(startX + (i * spacingX), startY);
            // Crear servicio del mesero
            MeseroService meseroService = new MeseroService(mesero, mesaMonitor, cocinaMonitor, mesaController, meseroView);

            // Crear y enlazar el controlador del mesero
            System.out.println("Creando mesero " + mesero.getId());
            MeseroController meseroController = new MeseroController(meseroService, meseroView, mesaController);
            executor.submit(meseroController::attend);
        }
    }

    private void crearChefsDinamicamente(Constants config) {
        int numChefs = config.getNumChefs();
        double startX = 100; // Coordenada X inicial para los chefs
        double startY = 650; // Coordenada Y inicial para los chefs
        double spacingX = 100; // Espaciado entre chefs en X

        for (int i = 0; i < numChefs; i++) {
            // Crear lógica del chef
            Chef chef = new Chef(i + 1);
            // Crear servicio del chef
            ChefService chefService = new ChefService(chef, cocinaMonitor);
            // Crear vista del chef
            Cocinero cocineroView = new Cocinero(startX + (i * spacingX), startY);
            // Crear y enlazar el controlador del chef
            ChefController chefController = new ChefController(chefService, cocineroView);
            executor.submit(chefController::cook);
        }
    }

    private void generarMesas(Constants config) {
        int numMesas = config.getNumMesas();
        double startX = 150;
        double startY = 250;
        double spacingX = 120;
        double spacingY = 100;
        int mesasPorFila = 5;

        for (int i = 0; i < numMesas; i++) {
            double x = startX + (i % mesasPorFila) * spacingX;
            double y = startY + (i / mesasPorFila) * spacingY;
            mesaController.generarMesa(x, y, i);
        }

        mesaController.imprimirEstadosDeMesas();
    }

    private void startSoundtrack(){
        loopBGM("lefestin.mp3");
    }
    private void createStage(double x, double y) {
        // Crear la base del escenario
        Rectangle stageBase = new Rectangle(200, 100, Color.DARKVIOLET);
        stageBase.setStroke(Color.BLACK); // Agregar borde para mayor visibilidad

        // Cargar la imagen del escenario
        var stageImage = FXGL.getAssetLoader().loadTexture("robots.png");
        stageImage.setFitWidth(180);
        stageImage.setFitHeight(90);

        // Construir la entidad y agregarla al mundo
        FXGL.entityBuilder()
                .at(x, y)
                .view(stageBase)       // Agregar la base como vista
                .view(stageImage)      // Agregar la imagen encima de la base
                .zIndex(10)            // Establecer un índice Z alto para asegurarse de que esté visible
                .buildAndAttach();     // Construir y adjuntar la entidad al mundo

        System.out.println("Escenario creado en posición: (" + x + ", " + y + ")");
    }

    private Entity createKitchenStation(double x, double y) {
        Entity station = FXGL.entityBuilder()
                .at(x, y)
                .view(new Rectangle(40, 30, Color.RED))
                .build();
        FXGL.getGameWorld().addEntity(station); // Asegúrate de agregarlo al mundo
        return station;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
