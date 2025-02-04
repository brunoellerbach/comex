package com.alura.comex.repository;

import com.alura.comex.InformeSintetico;
import com.alura.comex.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ProcesadorDeCsvTest {

    private ProcesadorDeArchivo procesador;

    @BeforeEach
    public void setUp() {
        procesador = new ProcesadorDeCsv();
    }

    @Test
    public void testInformeConArchivoCsv() throws IOException {
        String csvData = """
                categoria,producto,precio,cantidad,fecha,cliente
                Electrónica,Smartphone,699.99,1,01/01/2023,Juan
                Ropa,Camiseta,19.99,2,15/01/2023,Maria
                """;

        InputStream inputStream = new ByteArrayInputStream(csvData.getBytes());
        ArrayList<Pedido> pedidos = procesador.listaPedidos(inputStream);
        InformeSintetico informeSintetico = new InformeSintetico(pedidos);

        assertEquals(2, informeSintetico.getTotalDePedidosRealizados());
        assertEquals(3, informeSintetico.getTotalDeProductosVendidos());
        assertEquals(2, informeSintetico.getTotalDeCategorias());
        assertEquals(new BigDecimal("739.97").stripTrailingZeros(), informeSintetico.getMontoDeVentas().stripTrailingZeros());
    }

    @Test
    public void testInformeConListaVacia() {
        ArrayList<Pedido> pedidosVacios = new ArrayList<>();
        InformeSintetico informeSintetico = new InformeSintetico(pedidosVacios);

        assertEquals(0, informeSintetico.getTotalDePedidosRealizados());
        assertEquals(0, informeSintetico.getTotalDeProductosVendidos());
        assertEquals(0, informeSintetico.getTotalDeCategorias());
        assertEquals(BigDecimal.ZERO.stripTrailingZeros(), informeSintetico.getMontoDeVentas().stripTrailingZeros());
    }
}
