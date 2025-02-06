package com.alura.comex;

import java.util.HashMap;
import java.util.Map;

public class CacheInforme {

    private static CacheInforme instance;
    private final Map<String, InformeSintetico> cache;

    private CacheInforme() {
        cache = new HashMap<>();
    }

    public static synchronized CacheInforme getInstance() {
        if (instance == null) {
            instance = new CacheInforme();
            System.out.println("Instancia de CacheInforme creada.");
        }
        return instance;
    }

    public InformeSintetico getInforme(String key) {
        InformeSintetico informe = cache.get(key);
        if (informe != null) {
            System.out.println("Informe recuperado del caché para la clave: " + key);
        } else {
            System.out.println("Informe no encontrado en el caché para la clave: " + key);
        }
        return informe;
    }

    public void putInforme(String key, InformeSintetico informe) {
        cache.put(key, informe);
        System.out.println("Informe guardado en el caché para la clave: " + key);
    }
}
