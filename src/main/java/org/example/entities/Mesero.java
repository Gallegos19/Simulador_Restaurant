package org.example.entities;

import org.example.core.contracts.IMesaMonitor;
import org.example.core.contracts.ICocinaMonitor;

public class Mesero {
    private final int id;
    private final IMesaMonitor mesaMonitor;
    private final ICocinaMonitor cocinaMonitor;

    public Mesero(int id, IMesaMonitor mesaMonitor, ICocinaMonitor cocinaMonitor) {
        this.id = id;
        this.mesaMonitor = mesaMonitor;
        this.cocinaMonitor = cocinaMonitor;
    }


}
