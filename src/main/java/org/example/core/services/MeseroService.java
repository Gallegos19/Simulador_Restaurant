package org.example.core.services;

import org.example.core.contracts.IMeseroService;
import org.example.infrastructure.concurrency.CocinaMonitor;
import org.example.infrastructure.concurrency.MesaMonitor;

public class MeseroService implements IMeseroService {
    private final MesaMonitor mesaMonitor;
    private final CocinaMonitor cocinaMonitor;

    public MeseroService(MesaMonitor mesaMonitor, CocinaMonitor cocinaMonitor) {
        this.mesaMonitor = mesaMonitor;
        this.cocinaMonitor = cocinaMonitor;
    }

    @Override
    public void atenderMesa(int idMesa) {
        System.out.println("Mesero atendiendo la mesa " + idMesa);
        cocinaMonitor.nuevoPedido(idMesa); // Envía el pedido a la cocina
    }

    @Override
    public void entregarPedido(int idMesa) {
        System.out.println("Mesero entregó la comida a la mesa " + idMesa);
    }

    @Override
    public void limpiarMesa(int idMesa) {
        System.out.println("Mesero limpiando la mesa " + idMesa);
        mesaMonitor.liberarMesa(idMesa);
    }
}
