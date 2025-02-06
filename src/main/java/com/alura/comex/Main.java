package com.alura.comex;

import com.alura.comex.repository.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        CacheInforme cache = CacheInforme.getInstance(); // Obtén la instancia del caché
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Seleccione el tipo de archivo a leer:");
            System.out.println("1. CSV");
            System.out.println("2. JSON");
            System.out.println("3. XML");
            System.out.println("4. Salir");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            if (opcion == 4) {
                System.out.println("Saliendo del programa.");
                break;
            }

            ProcesadorDeArchivo procesador = null;
            String nombreArchivo = null;

            switch (opcion) {
                case 1:
                    procesador = new ProcesadorDeCsv();
                    nombreArchivo = "pedidos.csv";
                    break;
                case 2:
                    procesador = new ProcesadorDeJson();
                    nombreArchivo = "pedidos.json";
                    break;
                case 3:
                    procesador = new ProcesadorDeXml();
                    nombreArchivo = "pedidos.xml";
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
                    continue;
            }

            try (InputStream inputStream = Main.class.getResourceAsStream("/".concat(nombreArchivo))) {
                if (inputStream == null) {
                    throw new FileNotFoundException("Archivo " + nombreArchivo + " no encontrado en el classpath");
                }
                ArrayList<Pedido> pedidos = procesador.listaPedidos(inputStream);

                // Intenta recuperar el informe del caché
                InformeSintetico informeSintetico = cache.getInforme(nombreArchivo);
                if (informeSintetico == null) {
                    // Si no está en el caché, genera el informe y guárdalo en el caché
                    System.out.println("Generando nuevo informe...");
                    informeSintetico = new InformeSintetico(pedidos);
                    cache.putInforme(nombreArchivo, informeSintetico);
                } else {
                    System.out.println("Utilizando informe existente del caché.");
                }

                mostrarInforme(informeSintetico);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        scanner.close();
    }

    private static void mostrarInforme(InformeSintetico informeSintetico) {
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
    }
}
