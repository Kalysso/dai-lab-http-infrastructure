package ch.heig.dai;

import io.javalin.*;

public class Main {
    public static void main(String[] args) {

        Javalin app = Javalin.create().start(8000);

        ConstellationController controller = new ConstellationController();
        app.get("/constellations", controller::getAll);
        app.get("/constellations/{id}", controller::getOne);
        app.post("/constellations/", controller::create);
        app.put("/constellations/{id}", controller::update);
        app.delete("/constellations/{id}", controller::delete);
    }
}