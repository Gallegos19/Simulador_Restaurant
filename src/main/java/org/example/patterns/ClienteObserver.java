package org.example.patterns;

import org.example.entities.Cliente;

public interface ClienteObserver {
    void onMesa(Cliente cliente, int id_mesa);
    void agregarObservador(Observador o);
    void eliminarObservador(Observador o);
    void notificarObservadores(String mensaje);
}
