package app;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import controllers.SimulatorControllers;
import services.Monitor;
import services.OrderMonitor;
import views.SimulatorFactory;


public class SimulatorRestaurant extends GameApplication {

        private OrderMonitor orderMonitor;
        private Monitor monitor;

    @Override
    protected void initSettings(GameSettings settings) {
        SimulatorConfig.configureSettings(settings);
    }

    @Override
    protected void initGame() {
        // Inicialización de los servicios
        int capacidadRestaurante = 10;
        double tasaLlegada = 0.5;

        monitor = new Monitor(capacidadRestaurante);
        orderMonitor = new OrderMonitor();


        FXGL.getGameWorld().addEntityFactory(new SimulatorFactory(monitor, orderMonitor));

        new SimulatorControllers().iniciarSimulacion(
                capacidadRestaurante,
                tasaLlegada,
                monitor,
                orderMonitor
        );

    }


    public static void main(String[] args) {
        launch(args);
    }
}
