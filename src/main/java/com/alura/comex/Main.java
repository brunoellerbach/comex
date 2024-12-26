package com.alura.comex;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        ProcesadorDeCsv procesadorDeCsv = new ProcesadorDeCsv();
        ArrayList<Pedido> pedidos = procesadorDeCsv.listaPedidos("pedidos.csv");

        InformeSintetico informeSintetico = new InformeSintetico(pedidos);

        System.out.println("#### INFORME DE VALORES TOTALES");
        System.out.printf("- TOTAL DE PEDIDOS REALIZADOS: %s\n", informeSintetico.getTotalDePedidosRealizados());
        System.out.printf("- TOTAL DE PRODUCTOS VENDIDOS: %s\n", informeSintetico.getTotalDeProductosVendidos());
        System.out.printf("- TOTAL DE CATEGORIAS: %s\n", informeSintetico.getTotalDeCategorias());
        System.out.printf("- MONTO DE VENTAS: %s\n", informeSintetico.getMontoDeVentas());
        System.out.printf("- PEDIDO MAS BARATO: %s (%s)\n",
                informeSintetico.getPedidoMasBaratoValor(), informeSintetico.getPedidoMasBaratoProducto()
        );
        System.out.printf("- PEDIDO MAS CARO: %s (%s)\n",
                informeSintetico.getPedidoMasCaroValor(), informeSintetico.getPedidoMasCaroProducto()
        );
    }
}
