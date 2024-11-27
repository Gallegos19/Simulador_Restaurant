package org.example.infrastructure.concurrency;

import org.example.core.contracts.IMesaMonitor;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class MesaMonitor {
    private final boolean[] mesas;
    private int posicion;
    private int mesaOcupadas;
    private final Queue<Integer> colaEspera;

    public MesaMonitor(int numMesas) {
        this.mesas = new boolean[numMesas];
        Arrays.fill(mesas, false); // Todas las mesas están libres al inicio
        this.colaEspera = new LinkedList<>();
    }

    public int getPosicion() {
        return posicion;
    }

    public synchronized int ocuparMesa() throws InterruptedException {
        while (!hayMesasDisponibles()) {
            wait();
        }
        for (int i = 0; i < mesas.length; i++) {
            if (!mesas[i]) {
                mesas[i] = true; // Ocupa la mesa
                notifyAll();
                System.out.println("Mesa " + i + " ocupada.");
                posicion = i;
                return i;
            }
        }
        return -1; // Nunca debería llegar aquí
    }

    public synchronized void liberarMesa(int idMesa) {
        mesas[idMesa] = false; // Libera la mesa
        System.out.println("Mesa " + idMesa + " liberada.");
        notifyAll();
    }

    private boolean hayMesasDisponibles() {
        for (boolean mesa : mesas) {
            if (!mesa) {
                return true;
            }
        }
        return false;
    }
    public synchronized int getMesaOcupada() throws InterruptedException {
        while (!hayMesasOcupadas()) {
            wait(); // Espera si no hay mesas ocupadas
        }
        for (int i = 0; i < mesas.length; i++) {
            if (mesas[i]) { // Si la mesa está ocupada
                System.out.println("Mesero detecta que la mesa " + i + " está ocupada.");
                return i; // Retorna el ID de la mesa ocupada
            }
        }
        return -1; // Nunca debería llegar aquí
    }

    private boolean hayMesasOcupadas() {
        for (boolean mesa : mesas) {
            if (mesa) {
                return true;
            }
        }
        return false;
    }
}
