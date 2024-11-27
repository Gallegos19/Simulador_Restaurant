package org.example.infrastructure.concurrency;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class CocinaMonitor {
    private final Queue<Integer> colaPedidos = new LinkedList<>(); // Pedidos por preparar
    private final Map<Integer, String> pedidosListos = new HashMap<>(); // Pedidos preparados con idMesa como clave

    /**
     * Agrega un nuevo pedido a la cola de pedidos pendientes.
     */
    public synchronized void nuevoPedido(int idMesa) {
        colaPedidos.add(idMesa);
        System.out.println("Nuevo pedido recibido para la mesa " + idMesa);
        notifyAll(); // Notifica a los chefs que hay un nuevo pedido
    }

    /**
     * Permite a un chef tomar un pedido para prepararlo.
     */
    public synchronized int tomarPedido() throws InterruptedException {
        while (colaPedidos.isEmpty()) {
            wait(); // Espera si no hay pedidos pendientes
        }
        int idMesa = colaPedidos.poll(); // Saca un pedido de la cola
        System.out.println("Chef toma el pedido de la mesa " + idMesa + " para prepararlo.");
        return idMesa;
    }

    /**
     * Permite a un chef notificar que un pedido está listo.
     */
    public synchronized void pedidoListo(int idMesa) {
        pedidosListos.put(idMesa, "Pedido de la mesa " + idMesa);
        System.out.println("Chef notificó que el pedido de la mesa " + idMesa + " está listo.");
        notifyAll(); // Notifica a los meseros que hay un pedido listo
    }

    /**
     * Permite a un mesero retirar un pedido listo.
     */
    public synchronized int retirarPedido() throws InterruptedException {
        while (pedidosListos.isEmpty()) {
            wait(); // Espera si no hay pedidos listos
        }
        // Obtener el primer pedido listo basado en el `idMesa`
        int idMesa = pedidosListos.keySet().iterator().next();
        pedidosListos.remove(idMesa); // Retira el pedido del mapa
        System.out.println("Mesero retiró el pedido de la mesa " + idMesa + ".");
        return idMesa;
    }
}
