package services;

import models.Order;

import java.util.LinkedList;
import java.util.Queue;

public class OrderMonitor {
    private final Queue<Order> ordenes;

    public OrderMonitor() {
        this.ordenes = new LinkedList<>();
    }

    public synchronized void addOrder(Order order) {
        boolean existe = ordenes.stream()
                .anyMatch(o -> o.getCustomerName().equals(order.getCustomerName()));

        if (existe) {
            System.out.println("La orden ya existe en el buffer para el cliente: " + order.getCustomerName());
            return;
        }

        ordenes.add(order);
        notifyAll();
    }

    public synchronized Order getNextOrder() throws InterruptedException {
        while (ordenes.isEmpty() || ordenes.peek().getStatus() != Order.OrderStatus.PENDIENTE) {
            wait(); // Esperar hasta que haya órdenes pendientes
        }
        Order order = ordenes.poll();
        order.setStatus(Order.OrderStatus.EN_PROCESO);
        notifyAll();
        return order;
    }

    public synchronized Order getOrdenLista() throws InterruptedException {
        while (ordenes.stream().noneMatch(order -> order.getStatus() == Order.OrderStatus.LISTA)) {
            wait();
        }
        Order order = ordenes.stream()
                .filter(o -> o.getStatus() == Order.OrderStatus.LISTA)
                .findFirst()
                .orElse(null);
        ordenes.remove(order);
        notifyAll();
        return order;
    }

}