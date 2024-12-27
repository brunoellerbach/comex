package com.alura.comex;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

/**
 * Esta clase favorece el principio SOLID de Single Responsibility Principle (SRP), ya que extrajimos el comportamiento
 * de generar el informe, y lo colocamos en una clase específica para eso.
 */
public class InformeSintetico {

    private final HashSet<String> categoriasProcesadas = new HashSet<>();
    private final int totalDePedidosRealizados;
    private final int totalDeProductosVendidos;
    private int totalDeCategorias;
    private Pedido pedidoMasBarato;
    private Pedido pedidoMasCaro;
    private BigDecimal montoDeVentas = BigDecimal.ZERO;

    public InformeSintetico(ArrayList<Pedido> pedidos) {
        totalDePedidosRealizados = pedidos.size();
        totalDeProductosVendidos = pedidos.stream()
                .filter(pedido -> pedido != null) // Filtramos pedidos no nulos
                .mapToInt(Pedido::getCantidad) // Convertimos cada pedido a su cantidad
                .sum(); // Sumamos todas las cantidades
        pedidos.stream()
                .filter(pedido -> pedido != null) // Filtramos pedidos no nulos
                .forEach(pedidoActual -> { // Iteramos sobre cada pedido
                    if (pedidoMasBarato == null || pedidoActual.isMasBaratoQue(pedidoMasBarato)) {
                        pedidoMasBarato = pedidoActual;
                    }

                    if (pedidoMasCaro == null || pedidoActual.isMasCaroQue(pedidoMasCaro)) {
                        pedidoMasCaro = pedidoActual;
                    }

                    montoDeVentas = montoDeVentas.add(pedidoActual.getValorTotal());

                    if (!categoriasProcesadas.contains(pedidoActual.getCategoria())) {
                        totalDeCategorias++;
                        categoriasProcesadas.add(pedidoActual.getCategoria());
                    }
                });
    }

    public int getTotalDePedidosRealizados() {
        return totalDePedidosRealizados;
    }

    public int getTotalDeProductosVendidos() {
        return totalDeProductosVendidos;
    }

    public int getTotalDeCategorias() {
        return totalDeCategorias;
    }

    public String getMontoDeVentas() {
        //Pueden cambiar el Locale a la moneda de su pais, siguiendo esta documentación: https://www.oracle.com/java/technologies/javase/java8locales.html
        return NumberFormat.getCurrencyInstance(new Locale("es", "AR")).format(montoDeVentas.setScale(2, RoundingMode.HALF_DOWN));
    }

    public String getPedidoMasCaroValor() {
        return NumberFormat.getCurrencyInstance(new Locale("es", "AR")).format(pedidoMasCaro.getPrecio().multiply(new BigDecimal(pedidoMasCaro.getCantidad())).setScale(2, RoundingMode.HALF_DOWN));
    }

    public String getPedidoMasCaroProducto() {
        return pedidoMasCaro.getProducto();
    }

    public String getPedidoMasBaratoValor() {
        return NumberFormat.getCurrencyInstance(new Locale("es", "AR")).format(pedidoMasBarato.getPrecio().multiply(new BigDecimal(pedidoMasBarato.getCantidad())).setScale(2, RoundingMode.HALF_DOWN));
    }

    public String getPedidoMasBaratoProducto() {
        return pedidoMasBarato.getProducto();
    }
}
