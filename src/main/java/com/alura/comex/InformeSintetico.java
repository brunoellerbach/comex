package com.alura.comex;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Esta clase favorece el principio SOLID de Single Responsibility Principle (SRP), ya que extrajimos el comportamiento
 * de generar el informe, y lo colocamos en una clase específica para eso.
 */
public class InformeSintetico {

    private final int totalDePedidosRealizados;
    private final int totalDeProductosVendidos;
    private final int totalDeCategorias;
    private final Pedido pedidoMasBarato;
    private final Pedido pedidoMasCaro;
    private final BigDecimal montoDeVentas;
    private final Map<String, Long> pedidosPorCliente;
    private final List<Pedido> pedidos;

    public InformeSintetico(ArrayList<Pedido> pedidos) {
        this.pedidos = pedidos;
        totalDePedidosRealizados = pedidos.size();
        totalDeProductosVendidos = pedidos.stream().filter(pedido -> pedido != null) // Filtramos pedidos no nulos
                .mapToInt(Pedido::getCantidad) // Convertimos cada pedido a su cantidad
                .sum(); // Sumamos todas las cantidades

        // Usamos un stream para calcular el monto de ventas
        montoDeVentas = pedidos.stream().filter(pedido -> pedido != null) // Filtramos pedidos no nulos
                .map(Pedido::getValorTotal) // Obtenemos el valor total de cada pedido
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Sumamos todos los valores totales

        // Usamos un stream para calcular el total de categorías
        totalDeCategorias = (int) pedidos.stream().filter(pedido -> pedido != null).map(Pedido::getCategoria).distinct() //Elimina los items duplicados
                .count();

        // Usamos streams para encontrar el pedido más barato y el más caro
        pedidoMasBarato = pedidos.stream().filter(pedido -> pedido != null).min(Comparator.comparing(Pedido::getValorTotal)).orElse(null);

        pedidoMasCaro = pedidos.stream().filter(pedido -> pedido != null).max(Comparator.comparing(Pedido::getValorTotal)).orElse(null);

        pedidosPorCliente = pedidos.stream().collect(Collectors.groupingBy(Pedido::getCliente, Collectors.counting()));
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

    public BigDecimal getMontoDeVentas() {
        return montoDeVentas.setScale(2, RoundingMode.HALF_DOWN);
    }

    public String getMontoDeVentasFormateado() {
        return NumberFormat.getCurrencyInstance(new Locale("es", "AR")).format(getMontoDeVentas());
    }

    public BigDecimal getPedidoMasCaroValor() {
        return pedidoMasCaro.getPrecio().multiply(new BigDecimal(pedidoMasCaro.getCantidad())).setScale(2, RoundingMode.HALF_DOWN);
    }

    public String getPedidoMasCaroValorFormateado() {
        return NumberFormat.getCurrencyInstance(new Locale("es", "AR")).format(getPedidoMasCaroValor());
    }

    public String getPedidoMasCaroProducto() {
        return pedidoMasCaro.getProducto();
    }

    public BigDecimal getPedidoMasBaratoValor() {
        return pedidoMasBarato.getPrecio().multiply(new BigDecimal(pedidoMasBarato.getCantidad())).setScale(2, RoundingMode.HALF_DOWN);
    }

    public String getPedidoMasBaratoValorFormateado() {
        return NumberFormat.getCurrencyInstance(new Locale("es", "AR")).format(getPedidoMasBaratoValor());
    }

    public String getPedidoMasBaratoProducto() {
        return pedidoMasBarato.getProducto();
    }

    // Nuevo metodo para obtener el informe de clientes fieles public
    List<String> getInformeClientesFieles() {
        return pedidosPorCliente.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(entry -> String.format("NOMBRE: %s\nNº DE PEDIDOS: %d\n", entry.getKey(), entry.getValue())).collect(Collectors.toList());
    }

    public List<String> getInformeCategorias() {
        return pedidos.stream().filter(Objects::nonNull).collect(Collectors.groupingBy(Pedido::getCategoria)).entrySet().stream().sorted(Map.Entry.comparingByKey()).map(entry -> {
            String categoria = entry.getKey();
            Pedido productoMasCaro = entry.getValue().stream().max(Comparator.comparing(Pedido::getPrecio)).orElseThrow(() -> new IllegalArgumentException("No hay productos en la categoría " + categoria));

            return String.format("CATEGORIA: %s\nPRODUCTO: %s\nPRECIO: %s\n", categoria, productoMasCaro.getProducto(), NumberFormat.getCurrencyInstance(new Locale("es", "AR")).format(productoMasCaro.getPrecio().setScale(2, RoundingMode.HALF_DOWN)));
        }).collect(Collectors.toList());
    }

    public List<String> getInformeClientesQueMasGastaron() {
        return pedidos.stream()
                .filter(Objects::nonNull)
                .collect(
                        Collectors.groupingBy(
                                Pedido::getCliente,
                                Collectors.summingDouble(
                                        pedido -> pedido.getPrecio()
                                                .multiply(new BigDecimal(pedido.getCantidad()))
                                                .doubleValue()
                                )
                        )
                )
                .entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(2)
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    String cliente = entry.getKey();
                    long cantidadPedidos = pedidos.stream().filter(p -> p.getCliente().equals(cliente)).count();
                    BigDecimal montoGastado = BigDecimal.valueOf(entry.getValue());

                    return String.format("NOMBRE: %s\nNº DE PEDIDOS: %d\nMONTO GASTADO: %s\n",
                            cliente,
                            cantidadPedidos,
                            NumberFormat.getCurrencyInstance(new Locale("es", "AR")).format(montoGastado.setScale(2, RoundingMode.HALF_DOWN)));
                })
                .collect(Collectors.toList());
    }
}
