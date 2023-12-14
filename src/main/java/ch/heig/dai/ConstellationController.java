package ch.heig.dai;

import io.javalin.http.Context;
import java.util.concurrent.ConcurrentHashMap;

public class ConstellationController {

    // "Database" of constellations
    // Since the server is multi-threaded, we need to use a thread-safe data structure
    private final ConcurrentHashMap<Integer, Constellation> constellations = new ConcurrentHashMap<Integer, Constellation>();
    private int lastId = 0;

    public ConstellationController() {

        // Add some constellation to the "database"
        constellations.put(++lastId, new Constellation("L'Aigle", "Aquila/Aquilae",  "Aql", "Ptolémée"));
        constellations.put(++lastId, new Constellation("Le Burin", "Caelum/Caeli",  "Cae", "Lacaille"));
        constellations.put(++lastId, new Constellation("Le Caméléon", "Chamaeleon/Chamaeleontis",  "Cha", "Bayer"));
        constellations.put(++lastId, new Constellation("La Couronne boréale", "Corona Borealis/Coronae Borealis",  "CrB", "Ptolémée"));
        constellations.put(++lastId, new Constellation("La Machine pneumatique", "Antlia/Antliae",  "Ant", "Lacaille"));
    }

    public void create(Context ctx) {
        Constellation newConstellation = ctx.bodyAsClass(Constellation.class);
        constellations.put(++lastId, newConstellation);
        ctx.status(201);
    }

    public void getOne(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        ctx.json(constellations.get(id));
    }

    public void getAll(Context ctx) {
        ctx.json(constellations);
    }

    public void delete(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        constellations.remove(id);
        ctx.status(204);
    }

    public void update(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Constellation updtatedConstellation = ctx.bodyAsClass(Constellation.class);
        constellations.put(id, updtatedConstellation);
        ctx.status(200);
    }

}