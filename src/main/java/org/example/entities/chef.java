package org.example.entities;

import org.example.core.contracts.ICocinaMonitor;

public class Chef implements Runnable {
    private final int id;
    private final ICocinaMonitor cocinaMonitor;

    public Chef(int id, ICocinaMonitor cocinaMonitor) {
        this.id = id;
        this.cocinaMonitor = cocinaMonitor;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String pedido = cocinaMonitor.obtenerPedido(); // Espera un pedido del monitor
                System.out.println("Chef " + id + " está preparando el pedido: " + pedido);

                Thread.sleep(2000); // Simula tiempo de preparación

                cocinaMonitor.agregarComidaLista("Comida preparada por Chef " + id + " para el pedido: " + pedido);
                System.out.println("Chef " + id + " ha completado el pedido: " + pedido);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Chef " + id + " ha sido interrumpido.");
        }
    }
}
