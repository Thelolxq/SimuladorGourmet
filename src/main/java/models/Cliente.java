package models;

import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;
import javafx.application.Platform;
import services.Monitor;
import services.OrderMonitor;

import java.util.concurrent.ThreadLocalRandom;

public class Cliente extends Component implements Runnable {
    private final String name;
    private final Monitor monitor;
    private final OrderMonitor orderMonitor;
    private final double tasaLlegada;

    public Cliente(String name, Monitor monitor, OrderMonitor orderMonitor, double tasaLlegada) {
        this.name = name;
        this.monitor = monitor;
        this.orderMonitor = orderMonitor;
        this.tasaLlegada = tasaLlegada;
    }

    @Override
    public void onAdded() {
        super.onAdded();
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            int mesaId = monitor.ocuparMesa(name);
            asignarPosicion(mesaId);

            System.out.println(name + " está ordenando.");
            Order order = new Order(name);
            orderMonitor.addOrder(order);

            esperarOrden(order);
            comer();

            monitor.liberarMesa(mesaId);
            Platform.runLater(() -> entity.removeFromWorld());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void asignarPosicion(int mesaId) {
        int[] posicionMesa = monitor.obtenerPosicionMesa(mesaId);
        Platform.runLater(() -> entity.setPosition(new Point2D(posicionMesa[0], posicionMesa[1])));
    }

    private void esperarOrden(Order order) throws InterruptedException {
        synchronized (order) {
            while (order.getStatus() != Order.OrderStatus.LISTA) {
                order.wait();
            }
        }
    }

    private void comer() throws InterruptedException {
        System.out.println(name + " está comiendo.");
        int tiempoComer = ThreadLocalRandom.current().nextInt(3000, 8001);
        Thread.sleep(tiempoComer);
    }
}
