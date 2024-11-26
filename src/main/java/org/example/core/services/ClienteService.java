package org.example.core.services;

import org.example.core.contracts.IClienteService;
import org.example.entities.Cliente;
import org.example.infrastructure.concurrency.MesaMonitor;

public class ClienteService implements IClienteService {
    private final MesaMonitor mesaMonitor;

    public ClienteService(MesaMonitor mesaMonitor) {
        this.mesaMonitor = mesaMonitor;
    }

    @Override
    public void entrarRestaurante(Cliente cliente) {
        System.out.println("Cliente " + cliente.getId() + " entra al restaurante.");
    }

    @Override
    public void salirRestaurante(Cliente cliente) {
        System.out.println("Cliente " + cliente.getId() + " sale del restaurante.");
    }
}

