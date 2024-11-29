package org.example.game;

import org.example.config.Constants;
import org.example.core.services.ChefService;
import org.example.core.services.MeseroService;
import org.example.core.services.RecepcionistaService;
import org.example.entities.Chef;
import org.example.entities.Cliente;
import org.example.entities.Mesero;
import org.example.entities.Recepcionista;
import org.example.infrastructure.concurrency.CocinaMonitor;
import org.example.infrastructure.concurrency.MesaMonitor;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {
    public static void main(String[] args) {
        GameScene gameScene = new GameScene();
        //gameScene.main(args);

        Constants config = new Constants();

        // Monitores
        MesaMonitor mesaMonitor = new MesaMonitor(config.getNumMesas());

        CocinaMonitor cocinaMonitor = new CocinaMonitor();

        // Pool de hilos
        ExecutorService executor = Executors.newCachedThreadPool();

        // Crear y ejecutar recepcionista
        Recepcionista recepcionista = new Recepcionista("Itadori");
        RecepcionistaService recepcionistaService = new RecepcionistaService(recepcionista, mesaMonitor);
        executor.submit(recepcionistaService);

        //Crear y ejecutar meseros
        for (int i = 0; i < config.getNumMeseros(); i++) {
            Mesero mesero = new Mesero(i);
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
                Thread.sleep(1000); // Puedes ajustar el intervalo si es necesario
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }


        executor.shutdown();

    }
}
