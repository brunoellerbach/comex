package com.alura.comex.repository;

import com.alura.comex.Pedido;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Estamos favoreciendo el principio de responsabilidad única (La S de solid)
public class ProcesadorDeCsv implements ProcesadorDeArchivo {
    @Override
    public ArrayList<Pedido> listaPedidos(InputStream inputStream) throws IOException {
        ArrayList<Pedido> listaPedidos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            List<String[]> registros = readAllLines(reader);

            for (int i = 1; i < registros.size(); i++) {
                String[] registro = registros.get(i);
                String categoria = registro[0];
                String producto = registro[1];
                BigDecimal precio = new BigDecimal(registro[2]);
                int cantidad = Integer.parseInt(registro[3]);
                LocalDate fecha = LocalDate.parse(registro[4], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String cliente = registro[5];

                Pedido pedido = new Pedido(categoria, producto, cliente, precio, cantidad, fecha);
                listaPedidos.add(pedido);
            }
        } catch (CsvException e) {
            throw new RuntimeException("Error al procesar el archivo!", e);
        }
        return listaPedidos;
    }

    private List<String[]> readAllLines(BufferedReader reader) throws IOException, CsvException {
        try (CSVReader csvReader = new CSVReader(reader)) {
            return csvReader.readAll();
        }
    }
}
