package org.example.game;

import org.example.config.Constants;
import org.example.core.contracts.IMesaMonitor;
import org.example.core.services.ChefService;
import org.example.core.services.ClienteService;
import org.example.core.services.MeseroService;
import org.example.core.services.RecepcionistaService;
import org.example.entities.Cliente;
import org.example.infrastructure.concurrency.CocinaMonitor;
import org.example.infrastructure.concurrency.MesaMonitor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        Constants config = new Constants();

        // Monitores
        MesaMonitor mesaMonitor = new MesaMonitor(config.getNumMesas());

        CocinaMonitor cocinaMonitor = new CocinaMonitor();

        // Pool de hilos
        ExecutorService executor = Executors.newCachedThreadPool();

        // Crear y ejecutar recepcionista
        RecepcionistaService recepcionista = new RecepcionistaService(mesaMonitor);
        executor.submit(recepcionista);

        // Crear y ejecutar clientes
        for (int i = 0; i < config.getNumClientes(); i++) {
            Cliente cliente = new Cliente(i, mesaMonitor, recepcionista);
            executor.submit(cliente);
            try {
                // Simula el tiempo de llegada de clientes
                Thread.sleep(1000); // Puedes ajustar el intervalo si es necesario
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        //Crear y ejecutar meseros
        for (int i = 0; i < config.getNumMeseros(); i++) {
            executor.submit(new MeseroService(i, mesaMonitor, cocinaMonitor));
        }

        // Crear y ejecutar chefs
        for (int i = 0; i < config.getNumChefs(); i++) {
            executor.submit(new ChefService(i, cocinaMonitor));
        }



        executor.shutdown();
        System.out.println("Simulación en curso...");
    }
}
