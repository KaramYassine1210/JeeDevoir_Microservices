package ma.formations.soap.microservice_commande.Controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import ma.formations.soap.microservice_commande.Model.Commande;
import ma.formations.soap.microservice_commande.Service.CommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/commande")
public class CommandeController {

    @Autowired
    private CommandeService service;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${gateway.uri}")
    private String gatewayUri;

    @GetMapping
    public List<Commande> getAllCommandes() {
        return service.getAllCommandes();
    }

    @GetMapping("/{id}")
    public Commande getCommandeById(@PathVariable Long id) {
        return service.getCommandeById(id);
    }

    @PostMapping
    public Commande createCommande(@RequestBody Commande commande) {
        return service.createCommande(commande);
    }

    @PutMapping("/{id}")
    public Commande updateCommande(@PathVariable Long id, @RequestBody Commande commande) {
        return service.updateCommande(id, commande);
    }

    @DeleteMapping("/{id}")
    public void deleteCommande(@PathVariable Long id) {
        service.deleteCommande(id);
    }

    @GetMapping("/Produits/{id}")
    @CircuitBreaker(name = "MicroService-Produit", fallbackMethod = "fallbackProduit")
    public ResponseEntity<Object> getProduitById(@PathVariable Long id) {
        String produitServiceUri = gatewayUri + "/MICROSERVICE-PRODUIT/Produits/" + id;
        return restTemplate.getForEntity(produitServiceUri, Object.class);
    }

    public ResponseEntity<Object> fallbackProduit(Long id, Throwable throwable) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Le service produit est indisponible pour le moment. Error: " + throwable.getMessage());
    }
}
