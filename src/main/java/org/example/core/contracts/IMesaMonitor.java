package org.example.core.contracts;

public interface IMesaMonitor {
    int ocuparMesa() throws InterruptedException;
    void liberarMesa(int mesaId);
}
