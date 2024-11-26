package org.example.core.contracts;

public interface IMeseroService {
    void atenderMesa(int idMesa);
    void entregarPedido(int idMesa);
    void limpiarMesa(int idMesa);

}
