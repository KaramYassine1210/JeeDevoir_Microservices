package ma.formations.soap.microservice_commande.Repository;

import ma.formations.soap.microservice_commande.Model.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {
    @Query("SELECT c FROM Commande c WHERE c.date >= :date")
    List<Commande> findRecentCommandes(@Param("date") LocalDate date);
}

