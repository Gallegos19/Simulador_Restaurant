package org.example.core.services;

import org.example.core.contracts.IChefService;
import org.example.infrastructure.concurrency.CocinaMonitor;

public class ChefService implements IChefService {
    private final CocinaMonitor cocinaMonitor;

    public ChefService(CocinaMonitor cocinaMonitor) {
        this.cocinaMonitor = cocinaMonitor;
    }

    @Override
    public void recibirOrden(int idOrden) throws InterruptedException {

    }

    @Override
    public void prepararOrden() throws InterruptedException {
        int idMesa = cocinaMonitor.tomarPedido();
        System.out.println("Chef está preparando el pedido de la mesa " + idMesa);
        Thread.sleep(3000); // Simula el tiempo de preparación
        System.out.println("Chef terminó el pedido de la mesa " + idMesa);
    }
}
