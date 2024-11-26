package org.example.core.services;

import org.example.core.contracts.IRecepcionistaService;
import org.example.entities.Cliente;
import org.example.infrastructure.concurrency.MesaMonitor;

public class RecepcionistaService implements IRecepcionistaService {
    private final MesaMonitor mesaMonitor;

    public RecepcionistaService(MesaMonitor mesaMonitor) {
        this.mesaMonitor = mesaMonitor;
    }

    @Override
    public void asignarMesa(Cliente cliente) throws InterruptedException {
        System.out.println("Recepcionista está buscando una mesa para el cliente " + cliente.getId());
        mesaMonitor.ocuparMesa(); // Sincronización con el monitor
        System.out.println("Recepcionista asignó una mesa al cliente " + cliente.getId());
    }
}
