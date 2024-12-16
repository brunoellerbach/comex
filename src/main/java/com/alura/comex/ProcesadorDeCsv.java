package com.alura.comex;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

//Estamos favoreciendo el principio de responsabilidad única (La S de solid)
public class ProcesadorDeCsv {

    public ArrayList<Pedido> listaPedidos(String nombreArchivoCsv){
        ArrayList<Pedido> listaPedidos = new ArrayList<>();
        try {
            URL recursoCSV = ClassLoader.getSystemResource(nombreArchivoCsv);
            Path caminoDelArchivo = Path.of(recursoCSV.toURI());

            Scanner lectorDeLineas = new Scanner(caminoDelArchivo);

            lectorDeLineas.nextLine();

            while (lectorDeLineas.hasNextLine()) {
                String linea = lectorDeLineas.nextLine();
                String[] registro = linea.split(",");

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
            throw new RuntimeException("Archivo "+nombreArchivoCsv+" no localizado!");
        } catch (IOException e) {
            throw new RuntimeException("Error al abrir Scanner para procesar archivo!");
        }
        return listaPedidos;
    }
}
