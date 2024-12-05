package org.example.infrastructure.concurrency;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.example.core.controllers.MesaController;
import org.example.entities.Cliente;
import org.example.entities.Mesero;

public class MesaMonitor {
    private final boolean[] mesas; // Estado de si las mesas están ocupadas
    private final boolean[] atendiendoMesas; // Estado de si las mesas están siendo atendidas
    private final Map<Integer, Cliente> clientesEnMesas; // Relación mesa -> cliente
    private final Queue<Integer> colaEspera;

    public MesaMonitor(int numMesas) {
        this.mesas = new boolean[numMesas];
        this.atendiendoMesas = new boolean[numMesas]; // Inicialmente, ninguna mesa está siendo atendida
        this.clientesEnMesas = new HashMap<>(); // Inicialización del mapa para clientes en mesas

        Arrays.fill(mesas, false); // Todas las mesas están libres al inicio
        Arrays.fill(atendiendoMesas, false); // Ninguna mesa está siendo atendida
        this.colaEspera = new LinkedList<>();
    }

    public synchronized int ocuparMesa(Cliente cliente, MesaController mesaController) throws InterruptedException {
        while (!hayMesasDisponibles()) {
            System.out.println("No hay mesas disponibles. Cliente " + cliente.getId() + " espera.");
            wait();
        }

        for (int i = 0; i < mesas.length; i++) {
            if (!mesas[i]) {
                mesas[i] = true;
                atendiendoMesas[i] = false;
                clientesEnMesas.put(i, cliente);
                System.out.println("Mesa " + i + " ocupada por cliente " + cliente.getId());
                notifyAll(); // Notifica a otros hilos que una mesa ha cambiado de estado
                return i;
            }
        }

        return -1;
    }

    public synchronized void liberarMesa(int idMesa, MesaController mesaController) throws InterruptedException {
        System.out.println("Liberando mesa " + idMesa + "...");
        Cliente cliente = clientesEnMesas.get(idMesa);

        if (cliente != null) {
            synchronized (cliente) {
                cliente.recibirPedido();
            }
        }

        Thread.sleep(3000); // Simulación de tiempo para liberar la mesa
        mesas[idMesa] = false;
        atendiendoMesas[idMesa] = false;
        clientesEnMesas.remove(idMesa);
        System.out.println("Mesa " + idMesa + " ahora está libre.");
        mesaController.cambiarEstadoMesa(idMesa, "vacia");
        notifyAll(); // Notifica a otros hilos que una mesa está disponible
    }

    public synchronized void liberarMesaCliente(int idMesa, Cliente cliente, MesaController mesaController) throws InterruptedException {
        clientesEnMesas.remove(idMesa);
        //mesaController.cambiarEstadoMesa(idMesa, "comiendo");
        Thread.sleep(5000); // Simulación de tiempo para que el cliente termine
        //System.out.println("Mesa " + idMesa + " liberada por el cliente " + cliente.getId());
        //mesaController.cambiarEstadoMesa(idMesa, "sucia");
        notifyAll();
    }

    public synchronized Cliente getClienteEnMesa(int idMesa) {
        return clientesEnMesas.get(idMesa);
    }

    public synchronized int getMesaOcupada(Mesero mesero) throws InterruptedException {
        while (!hayMesasOcupadas()) {
            System.out.println("No hay mesas ocupadas. Mesero " + mesero.getId() + " espera.");
            wait(); // Espera hasta que haya mesas ocupadas
        }

        for (int i = 0; i < mesas.length; i++) {
            if (mesas[i] && !atendiendoMesas[i]) {
                atendiendoMesas[i] = true;
                System.out.println("Mesero " + mesero.getId() + " atiende la mesa " + i);
                return i;
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
            if (mesas[i] && !atendiendoMesas[i]) {
                return true;
            }
        }
        return false;
    }
}
