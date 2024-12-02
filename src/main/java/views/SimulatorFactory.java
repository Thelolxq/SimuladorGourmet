package views;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import enums.SimulatorTypes;
import models.Cliente;
import services.Monitor;
import services.OrderMonitor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SimulatorFactory implements EntityFactory {
    private final Monitor Monitor;
    private final OrderMonitor OrderMonitor;

    public SimulatorFactory(Monitor Monitor,OrderMonitor OrderMonitor) {
        this.Monitor =Monitor;
        this.OrderMonitor = OrderMonitor;
    }
    @Spawns("recepcionista")
    public Entity createRecepcionista(SpawnData data) {
        return FXGL.entityBuilder()
                .from(data)
                .type(SimulatorTypes.RECEPCIONISTA)
                .viewWithBBox("recepcionista.png")
                .build();
    }
    @Spawns("mesa")
    public Entity createMesa(SpawnData data) {
        return FXGL.entityBuilder()
                .from(data)
                .type(SimulatorTypes.MESA)
                .viewWithBBox("mesa.png")
                .build();
    }


    @Spawns("cliente")
    public Entity createCliente(SpawnData data) {
        String name = "Comensal " + data.get("id");
        return FXGL.entityBuilder()
                .from(data)
                .type(SimulatorTypes.CLIENTE)
                .with(new Cliente(name, Monitor, OrderMonitor, 0.5))
                .viewWithBBox("comensal.png")
                .build();
    }

    @Spawns("chef")
    public Entity createChef(SpawnData data) {
        return FXGL.entityBuilder()
                .from(data)
                .type(SimulatorTypes.CHEF)
                .viewWithBBox("chef.png")
                .build();
    }

    @Spawns("mesero")
    public Entity createMesero(SpawnData data) {

        return FXGL.entityBuilder()
                .from(data)
                .type(SimulatorTypes.MESERO)
                .viewWithBBox("mesero.png")
                .build();
    }
}
