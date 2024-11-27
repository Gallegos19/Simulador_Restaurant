package org.example.core.services;

import org.example.core.contracts.IMesaMonitor;

import java.util.LinkedList;
import java.util.Queue;

public class MesaMonitorServices  {
    private final int numMesas;
    private final boolean[] mesas;
    private final Queue<Integer> mesasPendientes;

    public MesaMonitorServices(int numMesas) {
        this.numMesas = numMesas;
        this.mesas = new boolean[numMesas];
        this.mesasPendientes = new LinkedList<>();
    }


    public synchronized int asignarMesa() throws InterruptedException {
        while (true) {
            for (int i = 0; i < numMesas; i++) {
                if (!mesas[i]) {
                    mesas[i] = true;
                    return i;
                }
            }
            wait();
        }
    }


    public synchronized void liberarMesa(int mesaId) {
        mesas[mesaId] = false;
        notifyAll();
    }
}
