package org.example.core.contracts;

import org.example.entities.Cliente;

public interface IRecepcionistaService {
    void asignarMesa(Cliente cliente) throws InterruptedException;
}
