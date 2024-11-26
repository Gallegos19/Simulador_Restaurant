package org.example.core.contracts;

public interface IChefService {
    void recibirOrden(int idOrden) throws InterruptedException;
    void prepararOrden() throws InterruptedException;
}
