package io.quarkus.workshop.superheroes.fight.health;

import io.quarkus.workshop.superheroes.fight.client.HeroProxy;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;

@Liveness
@ApplicationScoped
public class HeroHealthCheck implements HealthCheck {

    @RestClient
    HeroProxy heroProxy;

    @Override
    public HealthCheckResponse call() {
        try {
            heroProxy.findRandomHero();
            return HealthCheckResponse.up("Hero service is up");
        } catch (Exception e) {
            return HealthCheckResponse.down("Hero service is down");
        }
    }
}
