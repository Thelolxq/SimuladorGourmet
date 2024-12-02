package models;

import services.OrderMonitor;

import java.util.concurrent.ThreadLocalRandom;
public class Chef implements Runnable {
    private final String name;
    private final OrderMonitor orderMonitor;

    public Chef(String name, OrderMonitor orderMonitor) {
        this.name = name;
        this.orderMonitor = orderMonitor;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Order order = orderMonitor.getNextOrder();
                if (order != null) {
                    procesarOrden(order);
                } else {
                    Thread.sleep(500);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void procesarOrden(Order order) throws InterruptedException {
        System.out.println(name + " está cocinando la orden de " + order.getCustomerName());
        int tiempoCoccion = ThreadLocalRandom.current().nextInt(3000, 8001);
        Thread.sleep(tiempoCoccion);

        synchronized (order) {
            order.setStatus(Order.OrderStatus.LISTA);
            orderMonitor.addOrder(order);
            System.out.println(name + " completó la orden de " + order.getCustomerName());
        }
    }
}
