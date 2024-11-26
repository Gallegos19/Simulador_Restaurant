package org.example.core.contracts;

import org.example.entities.Cliente;

public interface IClienteService {
    void entrarRestaurante(Cliente cliente) throws InterruptedException;
    void salirRestaurante(Cliente cliente);
}
