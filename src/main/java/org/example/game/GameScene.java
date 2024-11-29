package org.example.game;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import org.example.config.Constants;
import org.example.core.services.ChefService;
import org.example.core.services.MeseroService;
import org.example.core.services.RecepcionistaService;
import org.example.entities.Chef;
import org.example.entities.Cliente;
import org.example.entities.Recepcionista;
import org.example.infrastructure.concurrency.CocinaMonitor;
import org.example.infrastructure.concurrency.MesaMonitor;
import org.example.ui.spaceScene.CheckerboardFloor;
import org.example.ui.spaceScene.OutsideAreas;
import org.example.ui.spaceScene.DiningRoom;
import org.example.ui.spaceScene.Kitchen;
import org.example.ui.spaceScene.Entrance;
import org.example.ui.entities.Mesa;
import org.example.ui.entities.Mesero;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameScene extends GameApplication {
    private CheckerboardFloor checkerboardFloor;
    private OutsideAreas outsideAreas;
    private DiningRoom diningRoom;
    private Kitchen kitchen;
    private Entrance entrance;
    private Mesa table;
    private MesaMonitor mesaMonitor;
    private CocinaMonitor cocinaMonitor;
    private ExecutorService executor = Executors.newCachedThreadPool();


    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1000);
        settings.setHeight(800);
        settings.setTitle("Pizzería Simulación");
    }

    @Override
    protected void initGame() {
        Constants config = new Constants();
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



        // Crear meseros en la interfaz gráfica
        for (int i = 0; i < config.getNumMeseros(); i++) {
            // Usa coordenadas para posicionar gráficamente al mesero
            org.example.ui.entities.Mesero meseroUI = new org.example.ui.entities.Mesero(100 * i, 500);
            FXGL.getGameWorld().addEntity(meseroUI.getEntity());
        }
        for (int i = 0; i < config.getNumMesas(); i++) {
            // Usa coordenadas para posicionar gráficamente al mesero
            org.example.ui.entities.Mesa mesaUI = new org.example.ui.entities.Mesa(120 * i, 400);
            FXGL.getGameWorld().addEntity(mesaUI.getEntity());
        }
        for (int i = 0; i < config.getNumChefs(); i++) {
            // Usa coordenadas para posicionar gráficamente al mesero
            org.example.ui.entities.Cocinero chefUI = new org.example.ui.entities.Cocinero(200 * i, 600);
            FXGL.getGameWorld().addEntity(chefUI.getEntity());
        }
        for (int i = 0; i < config.getNumClientes(); i++) {
            // Usa coordenadas para posicionar gráficamente al mesero
            org.example.ui.entities.Cliente clienteUI = new org.example.ui.entities.Cliente(200 * i, 300);
            FXGL.getGameWorld().addEntity(clienteUI.getEntity());
        }
        // Iniciar lógica de simulación
        startSimulation(config);
    }

    private void startSimulation(Constants config) {
        // Crear pool de hilos

        // Crear y ejecutar recepcionista
//        Recepcionista recepcionista = new Recepcionista("Itadori");
//        RecepcionistaService recepcionistaService = new RecepcionistaService(recepcionista, mesaMonitor, recepcionistaUI);
//        executor.submit(recepcionistaService);

        org.example.ui.entities.Recepcionista recepcionistaUI= new org.example.ui.entities.Recepcionista(350,650);
        FXGL.getGameWorld().addEntity(recepcionistaUI.getEntity());
        Recepcionista recepcionista = new Recepcionista("Itadori");
        RecepcionistaService recepcionistaService = new RecepcionistaService(recepcionista, mesaMonitor);
        executor.submit(recepcionistaService);

        // Crear y ejecutar meseros
        for (int i = 0; i < config.getNumMeseros(); i++) {
            org.example.entities.Mesero mesero = new org.example.entities.Mesero(i);
            executor.submit(new MeseroService(mesero, mesaMonitor, cocinaMonitor));
        }

        // Crear y ejecutar chefs
        for (int i = 0; i < config.getNumChefs(); i++) {
            Chef chef = new Chef(i);
            executor.submit(new ChefService(chef, cocinaMonitor));
        }

        // Crear y ejecutar clientes
        for (int i = 0; i < config.getNumClientes(); i++) {
            Cliente cliente = new Cliente(i, mesaMonitor, recepcionistaService);
            executor.submit(cliente);
            try {
                // Simula el tiempo de llegada de clientes
                Thread.sleep(1000); // Ajusta el intervalo si es necesario
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        executor.shutdown();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
