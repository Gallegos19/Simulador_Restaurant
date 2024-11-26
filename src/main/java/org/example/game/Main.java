package org.example.game;

import org.example.config.Constants;
import org.example.entities.Cliente;
import org.example.infrastructure.concurrency.CocinaMonitor;
import org.example.infrastructure.concurrency.MesaMonitor;
import org.example.core.services.ClienteService;
import org.example.core.services.ChefService;
import org.example.core.services.MeseroService;
import org.example.core.services.RecepcionistaService;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Inicialización de configuración
        Constants config = new Constants();
        CocinaMonitor cocinaMonitor = new CocinaMonitor();

        // Inicialización de monitores y servicios
        MesaMonitor mesaMonitor = new MesaMonitor(config.getMesasDisponibles());

        ClienteService clienteService = new ClienteService(mesaMonitor);
        RecepcionistaService recepcionistaService = new RecepcionistaService(mesaMonitor);
        ChefService chefService = new ChefService(cocinaMonitor);
        MeseroService meseroService = new MeseroService(mesaMonitor, cocinaMonitor);

        // Crear clientes basados en las constantes
        List<Cliente> clientes = new ArrayList<>();
        for (int i = 1; i <= config.getNumClientes(); i++) {
            clientes.add(new Cliente(i));
        }

        // Simulación
        System.out.println("Iniciando simulación...");

        for (Cliente cliente : clientes) {
            // Clientes llegan al restaurante
            clienteService.entrarRestaurante(cliente);

            // Recepcionista busca lugar para los clientes
            recepcionistaService.asignarMesa(cliente);

            // Chef prepara una orden (simulación simple para cliente)
            chefService.prepararOrden();

            // Mesero atiende y limpia mesas
            meseroService.atenderMesa(cliente.getId());
            meseroService.limpiarMesa(cliente.getId());

            // Cliente sale del restaurante
            clienteService.salirRestaurante(cliente);
        }

        System.out.println("Simulación finalizada.");
    }
}
