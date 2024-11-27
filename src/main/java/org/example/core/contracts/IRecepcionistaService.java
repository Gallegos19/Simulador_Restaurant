package org.example.core.contracts;

import org.example.entities.Cliente;

public interface IRecepcionistaService {
    int asignarMesa(Cliente cliente) throws InterruptedException;
}
