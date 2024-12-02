package models;

import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;
import services.Monitor;
import services.OrderMonitor;

public class Mesero implements Runnable {
    private final String name;
    private final Monitor monitor;
    private final OrderMonitor orderMonitor;
    private final Entity entity;

    public Mesero(String name, Monitor monitor, OrderMonitor orderMonitor, Entity entity) {
        this.name = name;
        this.monitor = monitor;
        this.orderMonitor = orderMonitor;
        this.entity = entity;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Order comidaLista = orderMonitor.getOrdenLista();
                if (comidaLista != null) {
                    entregarComida(comidaLista);
                } else {
                    Thread.sleep(500);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void entregarComida(Order order) throws InterruptedException {
        System.out.println(name + " está entregando la comida de " + order.getCustomerName());
        moverHaciaComensal(order);
        Thread.sleep(1000);
        System.out.println(name + " entregó la comida de " + order.getCustomerName());

        synchronized (order) {
            order.notifyAll();
        }

        regresarAPosicionInicial();
    }

    private void moverHaciaComensal(Order order) {
        Point2D posicionComensal = obtenerPosicionComensal(order.getCustomerName());
        moverHaciaPosicion(posicionComensal);
    }

    private void regresarAPosicionInicial() {
        Point2D posicionInicial = new Point2D(700, 230);
        moverHaciaPosicion(posicionInicial);
    }

    private void moverHaciaPosicion(Point2D posicion) {
        while (entity.getPosition().distance(posicion) > 5) {
            entity.translateTowards(posicion, 9);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private Point2D obtenerPosicionComensal(String customerName) {
        int mesaId = monitor.obtenerMesaPorNombre(customerName);
        int[] posicionMesa = monitor.obtenerPosicionMesa(mesaId);
        return new Point2D(posicionMesa[0], posicionMesa[1]);
    }
}
