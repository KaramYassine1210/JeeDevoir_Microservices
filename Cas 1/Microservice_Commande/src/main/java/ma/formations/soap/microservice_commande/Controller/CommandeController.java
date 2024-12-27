package ma.formations.soap.microservice_commande.Controller;

import ma.formations.soap.microservice_commande.Model.Commande;
import ma.formations.soap.microservice_commande.Service.CommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commandes")
public class CommandeController {

    @Autowired
    private CommandeService service;

    @Value("${mes-config-ms.commandes-last}")
    private int recentDays;

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

    @GetMapping("/recent")
    public List<Commande> getRecentCommandes() {
        return service.getRecentCommandes(recentDays);
    }
}

