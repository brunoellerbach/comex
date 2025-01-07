package com.alura.comex;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.io.Reader;
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
public class ProcesadorDeCsv {

    public ArrayList<Pedido> listaPedidos(String nombreArchivoCsv) {
        ArrayList<Pedido> listaPedidos = new ArrayList<>();
        try {
            URL recursoCSV = ClassLoader.getSystemResource(nombreArchivoCsv);
            Path caminoDelArchivo = Paths.get(recursoCSV.toURI());

            List<String[]> registros = readAllLines(caminoDelArchivo);

            // Omitimos la primera línea que contiene los nombres de las columnas
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
        } catch (URISyntaxException e) {
            throw new RuntimeException("Archivo " + nombreArchivoCsv + " no localizado!", e);
        } catch (IOException | CsvException e) {
            throw new RuntimeException("Error al procesar el archivo!", e);
        }
        return listaPedidos;
    }

    public static List<String[]> readAllLines(Path filePath) throws IOException, CsvException {
        try (Reader reader = Files.newBufferedReader(filePath);
             CSVReader csvReader = new CSVReader(reader)) {
            return csvReader.readAll();
        }
    }
}
