package controllers;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import javafx.application.Platform;
import javafx.util.Duration;
import models.Chef;
import models.Recepcionista;
import models.Mesero;
import services.Monitor;
import services.OrderMonitor;
import services.Poisson;


public class SimulatorControllers{

    private final Monitor monitor;
    private final OrderMonitor orderMonitor;

    public SimulatorControllers() {
        this.monitor = new Monitor(10);
        this.orderMonitor = new OrderMonitor();

    }

    public void iniciarSimulacion(int capacidadRestaurante, double tasaLlegada, Monitor monitor, OrderMonitor orderMonitor) {
        Platform.runLater(() -> {
            FXGL.getGameScene().setBackgroundRepeat("fondo.png");

            var mesasDisponiblesText = FXGL.getUIFactoryService().newText("", 10);
            mesasDisponiblesText.setTranslateX(185);
            mesasDisponiblesText.setTranslateY(265);
            FXGL.addUINode(mesasDisponiblesText);


            FXGL.run(() -> {
                mesasDisponiblesText.setText( ""+monitor.mesasDisponibles());
            }, Duration.seconds(0.5)); // Actualizamos cada 0.5 segundos
        });

        FXGL.spawn("recepcionista", 205, 240);

        int[] xPos = {370, 570};

        int[] yPos = {60, 160, 260, 360, 460, 560};


        for (int x : xPos) {
            for (int y : yPos) {
                FXGL.spawn("mesa", x, y);
            }
        }

        int numMeseros = (int) Math.ceil(capacidadRestaurante * 0.1);
        int numCocineros = (int) Math.ceil(capacidadRestaurante * 0.15);

        // Creaamos y ejecutamos recepcionista
        Recepcionista recepcionista = new Recepcionista(monitor);
        new Thread(recepcionista).start();

        for (int i = 0; i < numMeseros; i++) {
            final int meseroIndex = i;
            Entity meseroEntity = FXGL.spawn("mesero", 700 + meseroIndex * 50, 230);
            new Thread(new Mesero("Mesero " + (meseroIndex + 1), monitor, orderMonitor, meseroEntity)).start();
        }

        for (int i = 0; i < numCocineros; i++) {
            final int cocineroIndex = i;
            Entity chefEntity = FXGL.spawn("chef", 850 , 200+ cocineroIndex * 70);
            // En la clase SimuladorController
            new Thread(new Chef("Cocinero " + (cocineroIndex + 1), orderMonitor)).start();

        }
        crearComensales(capacidadRestaurante, tasaLlegada, monitor, orderMonitor);
    }

    private void crearComensales(int capacidadRestaurante, double tasaLlegada, Monitor monitor, OrderMonitor orderMonitor) {
        int comensalesTotal = 25;
        double xMin = 60;
        double xMax = 120;
        double yMin = 100;
        double yMax = 400;

        new Thread(() -> {
            for (int i = 0; i < comensalesTotal; i++) {
                final int comensalId = i + 1;

                double xPos = xMin + Math.random() * (xMax - xMin);
                double yPos = yMin + Math.random() * (yMax - yMin);

                Platform.runLater(() -> {
                    try {
                        FXGL.spawn("cliente", new SpawnData(xPos, yPos).put("id", comensalId));
                    } catch (Exception e) {
                        System.err.println("Error al generar el comensal " + comensalId + ": " + e.getMessage());
                    }
                });


                try {
                    long tiempoLlegada = Poisson.generate(tasaLlegada);
                    System.out.println("Comensal " + comensalId + " llegará en " + tiempoLlegada + " ms");
                    Thread.sleep(tiempoLlegada);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("El hilo de llegada de comensales fue interrumpido.");
                }
            }
        }).start();
    }
}