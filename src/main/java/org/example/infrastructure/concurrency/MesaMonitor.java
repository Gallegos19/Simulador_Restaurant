package org.example.infrastructure.concurrency;

import java.util.Arrays;

public class MesaMonitor {
    private final boolean[] mesas;

    public MesaMonitor(int numMesas) {
        this.mesas = new boolean[numMesas];
        Arrays.fill(mesas, false); // Todas las mesas están libres al inicio
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
}
