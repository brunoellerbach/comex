package com.alura.comex;

import com.alura.comex.repository.ProcesadorDeArchivo;
import com.alura.comex.repository.ProcesadorDeXml;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        ProcesadorDeArchivo procesador = new ProcesadorDeXml(); // Cambia esto si quieres usar ProcesadorDeCsv

        try {
            ArrayList<Pedido> pedidos = procesador.listaPedidos("src/main/resources/pedidos.xml");

            InformeSintetico informeSintetico = new InformeSintetico(pedidos);

            System.out.println("#### INFORME DE VALORES TOTALES");
            System.out.printf("- TOTAL DE PEDIDOS REALIZADOS: %s\n", informeSintetico.getTotalDePedidosRealizados());
            System.out.printf("- TOTAL DE PRODUCTOS VENDIDOS: %s\n", informeSintetico.getTotalDeProductosVendidos());
            System.out.printf("- TOTAL DE CATEGORIAS: %s\n", informeSintetico.getTotalDeCategorias());
            System.out.printf("- MONTO DE VENTAS: %s\n", informeSintetico.getMontoDeVentas());
            System.out.printf("- PEDIDO MAS BARATO: %s (%s)\n",
                    informeSintetico.getPedidoMasBaratoValor(), informeSintetico.getPedidoMasBaratoProducto()
            );
            System.out.printf("- PEDIDO MAS CARO: %s (%s)\n\n",
                    informeSintetico.getPedidoMasCaroValor(), informeSintetico.getPedidoMasCaroProducto()
            );

            // Generar e imprimir informe de clientes fieles
            System.out.println("#### INFORME DE CLIENTES FIELES\n");
            informeSintetico.getInformeClientesFieles().forEach(System.out::println);
            // Imprimir informe de categorias
            System.out.println("#### INFORME DE CATEGORIAS\n");
            informeSintetico.getInformeCategorias().forEach(System.out::println);
            // Generar e imprimir informe de los dos clientes que más gastaron
            System.out.println("#### CLIENTES QUE MÁS GASTARON\n");
            informeSintetico.getInformeClientesQueMasGastaron().forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
