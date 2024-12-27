package ma.formations.soap.microservice_commande.Controller;

import ma.formations.soap.microservice_commande.Repository.CommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CommandeHealthIndicator implements HealthIndicator {

    @Autowired
    private CommandeRepository repository;

    @Override
    public Health health() {
        boolean hasCommandes = repository.count() > 0;
        if (hasCommandes) {
            return Health.up().build();
        } else {
            return Health.down().withDetail("error", "No commandes found").build();
        }
    }
}

