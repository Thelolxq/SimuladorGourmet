package models;

import services.Monitor;

public class Recepcionista implements Runnable {
    private final Monitor monitor;

    public Recepcionista(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        try {
            while (true) {
                synchronized (monitor) {
                    monitor.wait();
                    int mesasDisponibles = monitor.mesasDisponibles();
                    System.out.println("Recepcionista: Hay " + mesasDisponibles + " mesas disponibles.");
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Recepcionista interrumpido.");
        }
    }
}