package ma.formations.soap.microservice_commande.Service;

import ma.formations.soap.microservice_commande.Model.Commande;
import ma.formations.soap.microservice_commande.Repository.CommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CommandeService {
    @Autowired
    private CommandeRepository repository;

    public List<Commande> getAllCommandes() {
        return repository.findAll();
    }

    public Commande getCommandeById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Commande introuvable"));
    }

    public Commande createCommande(Commande commande) {
        return repository.save(commande);
    }

    public Commande updateCommande(Long id, Commande updatedCommande) {
        Commande existingCommande = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));
        existingCommande.setDescription(updatedCommande.getDescription());
        existingCommande.setQuantite(updatedCommande.getQuantite());
        existingCommande.setDate(updatedCommande.getDate());
        existingCommande.setMontant(updatedCommande.getMontant());
        existingCommande.setIdProduit(updatedCommande.getIdProduit());
        return repository.save(existingCommande);
    }

    public void deleteCommande(Long id) {
        repository.deleteById(id);
    }
}
