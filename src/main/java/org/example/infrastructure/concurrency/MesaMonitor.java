package org.example.infrastructure.concurrency;

import org.example.core.contracts.IMesaMonitor;
import org.example.entities.Cliente;
import org.example.entities.Mesero;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class MesaMonitor {
    private final boolean[] mesas; // Estado de si las mesas están ocupadas
    private final boolean[] atendiendoMesas; // Estado de si las mesas están siendo atendidas
    private final Map<Integer, Cliente> clientesEnMesas; // Relación mesa -> cliente
    private int posicion;
    private final Queue<Integer> colaEspera;

    public MesaMonitor(int numMesas) {
        this.mesas = new boolean[numMesas];
        this.atendiendoMesas = new boolean[numMesas]; // Inicialmente, ninguna mesa está siendo atendida
        this.clientesEnMesas = new HashMap<>(); // Inicialización del mapa para clientes en mesas
        Arrays.fill(mesas, false); // Todas las mesas están libres al inicio
        Arrays.fill(atendiendoMesas, false); // Ninguna mesa está siendo atendida
        this.colaEspera = new LinkedList<>();
    }

    public int getPosicion() {
        return posicion;
    }

    public synchronized int ocuparMesa(Cliente cliente) throws InterruptedException {
        while (!hayMesasDisponibles()) {
            wait();
        }
        for (int i = 0; i < mesas.length; i++) {
            if (!mesas[i]) {
                mesas[i] = true; // Ocupa la mesa
                clientesEnMesas.put(i, cliente); // Asocia la mesa con el cliente
                notifyAll();
                System.out.println("Mesa " + i + " ocupada por cliente " + cliente.getId());
                posicion = i;
                return i;
            }
        }
        return -1;
    }

    public synchronized void liberarMesa(int idMesa) throws InterruptedException {
        Thread.sleep(3000);
        mesas[idMesa] = false; // Libera la mesa
        atendiendoMesas[idMesa] = false; // Marca que la mesa ya no está siendo atendida
        System.out.println("Mesa " + idMesa + " limpia para su uso.");
        notifyAll();
    }

    public synchronized void liberarMesaCliente(int idMesa, Cliente cliente) {
        clientesEnMesas.remove(idMesa);
        System.out.println("Mesa " + idMesa + " liberada por el cliente" + cliente.getId());
        notifyAll();
    }

    public synchronized Cliente getClienteEnMesa(int idMesa) {
        return clientesEnMesas.get(idMesa); // Retorna el cliente asociado a la mesa
    }

    public synchronized int getMesaOcupada(Mesero mesero) throws InterruptedException {
        while (!hayMesasOcupadas()) {
            wait(); // Espera si no hay mesas ocupadas
        }
        for (int i = 0; i < mesas.length; i++) {
            if (mesas[i] && !atendiendoMesas[i]) { // Si la mesa está ocupada y no está siendo atendida
                atendiendoMesas[i] = true; // Marca la mesa como siendo atendida
                System.out.println("Mesero " + mesero.getId() + " detecta que la mesa " + i + " está ocupada.");
                return i; // Retorna el ID de la mesa ocupada
            }
        }
        return -1; // Nunca debería llegar aquí
    }

    private boolean hayMesasDisponibles() {
        for (boolean mesa : mesas) {
            if (!mesa) {
                return true;
            }
        }
        return false;
    }

    private boolean hayMesasOcupadas() {
        for (int i = 0; i < mesas.length; i++) {
            if (mesas[i] && !atendiendoMesas[i]) { // Busca mesas ocupadas que no estén siendo atendidas
                return true;
            }
        }
        return false;
    }
}
