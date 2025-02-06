package com.alura.comex;

import com.alura.comex.repository.ProcesadorDeArchivo;
import com.alura.comex.repository.ProcesadorDeCsv;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        ProcesadorDeArchivo procesador = new ProcesadorDeCsv();
        CacheInforme cache = CacheInforme.getInstance();

        String nombreArchivo = "pedidos.csv";

        try (InputStream inputStream = Main.class.getResourceAsStream("/".concat(nombreArchivo))) {

            if (inputStream == null) {
                throw new FileNotFoundException("Archivo " + nombreArchivo + " no encontrado en el classpath");
            }
            ArrayList<Pedido> pedidos = procesador.listaPedidos(inputStream);

            InformeSintetico informeSintetico = cache.getInforme(nombreArchivo);
            if (informeSintetico == null) {
                System.out.println("Generando nuevo informe...");
                informeSintetico = new InformeSintetico(pedidos);
                cache.putInforme(nombreArchivo, informeSintetico);
            } else {
                System.out.println("Utilizando informe existente del caché.");
            }

            System.out.println("#### INFORME DE VALORES TOTALES");
            System.out.printf("- TOTAL DE PEDIDOS REALIZADOS: %s\n", informeSintetico.getTotalDePedidosRealizados());
            System.out.printf("- TOTAL DE PRODUCTOS VENDIDOS: %s\n", informeSintetico.getTotalDeProductosVendidos());
            System.out.printf("- TOTAL DE CATEGORIAS: %s\n", informeSintetico.getTotalDeCategorias());
            System.out.printf("- MONTO DE VENTAS: %s\n", informeSintetico.getMontoDeVentasFormateado());
            System.out.printf("- PEDIDO MAS BARATO: %s (%s)\n",
                    informeSintetico.getPedidoMasBaratoValorFormateado(), informeSintetico.getPedidoMasBaratoProducto()
            );
            System.out.printf("- PEDIDO MAS CARO: %s (%s)\n\n",
                    informeSintetico.getPedidoMasCaroValorFormateado(), informeSintetico.getPedidoMasCaroProducto()
            );

            System.out.println("#### INFORME DE CLIENTES FIELES\n");
            informeSintetico.getInformeClientesFieles().forEach(System.out::println);
            System.out.println("#### INFORME DE CATEGORIAS\n");
            informeSintetico.getInformeCategorias().forEach(System.out::println);
            System.out.println("#### CLIENTES QUE MÁS GASTARON\n");
            informeSintetico.getInformeClientesQueMasGastaron().forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
