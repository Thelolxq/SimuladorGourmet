package services;

import java.util.HashMap;
import java.util.Map;

public class Monitor{
    private final int capacidad;
    private int mesasDisponibles;
    private final boolean[] mesasOcupadas;
    private final int[][] posicionesMesas;
    private final Map<String, Integer> asignacionesMesas;

    public Monitor(int capacidad) {
        this.capacidad = capacidad;
        this.mesasDisponibles = capacidad;
        this.mesasOcupadas = new boolean[capacidad];
        this.asignacionesMesas = new HashMap<>();
        this.posicionesMesas = new int[][]{
                {370, 60}, {370, 160}, {370, 260}, {370, 360},
                {370, 460}, {570, 60}, {570, 160}, {570, 260},
                {570, 360}, {570, 460}
        };
    }

    public synchronized int ocuparMesa(String comensal) throws InterruptedException {
        while (mesasDisponibles == 0) {
            wait();
        }
        mesasDisponibles--;
        for (int i = 0; i < capacidad; i++) {
            if (!mesasOcupadas[i]) {
                mesasOcupadas[i] = true;
                asignacionesMesas.put(comensal, i);
                notifyAll();
                return i;
            }
        }
        return -1;
    }

    public synchronized void liberarMesa(int mesaId) {
        mesasOcupadas[mesaId] = false;
        mesasDisponibles++;
        asignacionesMesas.remove(mesaId);
        notifyAll();
    }

    public synchronized int obtenerMesaPorNombre(String comensal) {
        return asignacionesMesas.getOrDefault(comensal, -1);
    }

    public int[] obtenerPosicionMesa(int mesaId) {
        return posicionesMesas[mesaId];
    }

    public synchronized int mesasDisponibles() {
        return mesasDisponibles;
    }
}
