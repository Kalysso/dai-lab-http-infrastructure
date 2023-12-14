package ch.heig.dai;

import ch.heig.dai.ConstellationController;
import io.javalin.*;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);

        ConstellationController controller = new ConstellationController();
        app.get("/api/constellations", controller::getAll);
        app.get("/api/constellations/{id}", controller::getOne);
        app.post("/api/constellations/", controller::create);
        app.put("/api/constellations/{id}", controller::update);
        app.delete("/api/constellations/{id}", controller::delete);
    }
}