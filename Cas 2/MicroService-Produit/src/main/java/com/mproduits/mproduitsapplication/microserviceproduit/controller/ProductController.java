package com.mproduits.mproduitsapplication.microserviceproduit.controller;

import com.mproduits.mproduitsapplication.microserviceproduit.configurations.ApplicationPropertiesConfiguration;
import com.mproduits.mproduitsapplication.microserviceproduit.dao.ProductDao;
import com.mproduits.mproduitsapplication.microserviceproduit.model.Product;
import com.mproduits.mproduitsapplication.microserviceproduit.exceptions.ProductNotFoundException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
public class ProductController implements HealthIndicator {
    @Autowired
    ProductDao productDao;
    @Autowired
    ApplicationPropertiesConfiguration appProperties;
    // Affiche la liste de tous les produits disponibles
    @CircuitBreaker(name = "ProductService" ,fallbackMethod = "fallbacklistdesproduits")
    @Retry(name = "ProductService")
    @GetMapping(value = "/Produits")
    public List<Product> listeDesProduits() throws InterruptedException {
        System.out.println(" ********* ProductController listeDesProduits() ");
        List<Product> products = productDao.findAll();
        if (products.isEmpty())
            throw new ProductNotFoundException("Aucun produit n'est disponible à la vente");
        return products.subList(0,Math.min(appProperties.getLimitDeProduits(), products.size()));
    }
    /**
     * Méthode de fallback en cas d'échec de la méthode principale.
     * Retourne une liste par défaut.
     */
    public List<Product> fallbacklistdesproduits(Throwable throwable) {
        System.out.println("********** Fallback activé. Cause : " + throwable.getMessage());
        return List.of(new Product(0, "Produit de secours", "Description non disponible", "image_placeholder.jpg", 0.00));
    }

    @Retry(name = "productService", fallbackMethod = "fallbackListeDesProduits")
    @TimeLimiter(name = "productService")
    @GetMapping(value = "/Produit")
    public CompletableFuture<List<Product>> listeDesProduits2() throws InterruptedException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(6000); // Simule un délai de 3 secondes
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return productDao.findAll();
        });
    }

    public CompletableFuture<List<Product>> fallbackListeDesProduits(Throwable t) {
        System.out.println("Appel du fallback : " + t.getMessage());
        return CompletableFuture.completedFuture(Collections.emptyList());
    }

    // Récuperer un produit par son id
    @GetMapping(value = "/Produits/{id}")
    public Optional<Product> recupererUnProduit(@PathVariable int id) {
        System.out.println(" ********* ProductController recupererUnProduit(@PathVariable int id) ");
        Optional<Product> product = productDao.findById(id);
        if (!product.isPresent())
            throw new ProductNotFoundException("Le produit correspondant à l'id "
                    + id + " n'existe pas");
        return product;
    }
    @Override
    public Health health() {
        System.out.println("****** Actuator : ProductController health() ");
        List<Product> products = productDao.findAll();
        if (products.isEmpty()) {
            return Health.down().build();
        }
        return Health.up().build();
    }
}
