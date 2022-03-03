package io.quarkus.workshop.superheroes.fight;

import org.eclipse.microprofile.faulttolerance.Timeout;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.Random;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/api/fights")
@Produces(APPLICATION_JSON)
public class FightResource {

    private static final Random RANDOM = new Random();

    @Inject Logger logger;

    @Inject
    FightService service;

    private void veryLongProcess() {
        try {
            Thread.sleep(RANDOM.nextInt(5000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @GET
    @Path("/randomfighters")
    @Timeout(2500)
    public Response getRandomFighters() {
//        veryLongProcess();
        Fighters fighters = service.findRandomFighters();
        logger.debug("Get random fighters " + fighters);
        return Response.ok(fighters).build();
    }

    @GET
    public Response getAllFights() {
        List<Fight> fights = service.findAllFights();
        logger.debug("Total number of fights " + fights);
        return Response.ok(fights).build();
    }

    @GET
    @Path("/{id}")
    public Response getFight(Long id) {
        Fight fight = service.findFightById(id);
        if (fight != null) {
            logger.debug("Found fight " + fight);
            return Response.ok(fight).build();
        } else {
            logger.debug("No fight found with id " + id);
            return Response.noContent().build();
        }
    }

    @POST
    public Fight fight(@Valid Fighters fighters, UriInfo uriInfo) {
        return service.persistFight(fighters);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/hello")
    public String hello() {
        return "Hello Fight Resource";
    }
}