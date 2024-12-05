package org.example.core.controllers;

import javafx.geometry.Point2D;
import org.example.ui.entities.Mesa;

import java.util.HashMap;
import java.util.Map;

public class MesaController {
    private final Map<Integer, Mesa> mesas;

    public MesaController() {
        mesas = new HashMap<>();
    }

    /**
     * Genera una nueva mesa y la agrega al controlador.
     *
     * @param x      Coordenada X de la mesa.
     * @param y      Coordenada Y de la mesa.
     * @param idMesa Identificador único de la mesa.
     * @return La instancia de la mesa generada.
     */
    public Mesa generarMesa(double x, double y, int idMesa) {
        if (mesas.containsKey(idMesa)) {
            throw new IllegalArgumentException("Ya existe una mesa con el ID: " + idMesa);
        }

        Mesa nuevaMesa = new Mesa(x, y, idMesa);
        mesas.put(idMesa, nuevaMesa);
        return nuevaMesa;
    }

    /**
     * Cambia el estado de una mesa específica.
     *
     * @param idMesa Identificador único de la mesa.
     * @param estado Nuevo estado de la mesa.
     */
    public void cambiarEstadoMesa(int idMesa, String estado) {
        Mesa mesa = obtenerMesa(idMesa);
        mesa.cambiarEstado(estado);
    }

    /**
     * Obtiene una mesa por su ID.
     *
     * @param idMesa Identificador único de la mesa.
     * @return La instancia de la mesa correspondiente.
     */
    public Mesa obtenerMesa(int idMesa) {
        if (!mesas.containsKey(idMesa)) {
            throw new IllegalArgumentException("No se encontró ninguna mesa con el ID: " + idMesa);
        }
        return mesas.get(idMesa);
    }

    /**
     * Elimina una mesa del controlador.
     *
     * @param idMesa Identificador único de la mesa a eliminar.
     */
    public void eliminarMesa(int idMesa) {
        if (!mesas.containsKey(idMesa)) {
            throw new IllegalArgumentException("No se puede eliminar una mesa inexistente con el ID: " + idMesa);
        }
        mesas.remove(idMesa);
    }

    /**
     * Obtiene todas las mesas actualmente gestionadas.
     *
     * @return Mapa de mesas donde la clave es el ID y el valor es la instancia de la mesa.
     */
    public Map<Integer, Mesa> obtenerTodasLasMesas() {
        return mesas;
    }

    /**
     * Imprime el estado de todas las mesas para depuración.
     */
    public void imprimirEstadosDeMesas() {
        mesas.forEach((id, mesa) -> {
            System.out.println("Mesa ID: " + id + " | Estado: " + mesa.getEstado() +
                    " | Posición: (" + mesa.getX() + ", " + mesa.getY() + ")");
        });
    }

    public Point2D obtenerCoordenadasMesa(int mesaId) {
        Mesa mesa = this.obtenerMesa(mesaId); // Obtener la mesa según el ID

        if (mesa != null) {
            return new Point2D(mesa.getX(), mesa.getY());
        }

        return null;
    }

}