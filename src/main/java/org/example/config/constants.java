package org.example.config;

public class Constants {
    public static final int NUM_CHEFS = 1;
    public static final int NUM_CLIENTES = 5;
    public static final int MESAS_DISPONIBLES = 10;

    public Constants() {
        // Este constructor no hace nada, pero deja claro que se permite instanciar la clase
    }

    public int getMesasDisponibles() {
        return MESAS_DISPONIBLES;
    }

    public int getNumClientes() {
        return NUM_CLIENTES;
    }

    public int getNumChefs() {
        return NUM_CHEFS;
    }
}
