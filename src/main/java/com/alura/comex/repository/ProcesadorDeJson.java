package com.alura.comex.repository;

import com.alura.comex.Pedido;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProcesadorDeJson implements ProcesadorDeArchivo {
    @Override
    public ArrayList<Pedido> listaPedidos(String archivo) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        objectMapper.registerModule(module);
        List<Pedido> pedidos = objectMapper.readValue(new File(archivo), new TypeReference<List<Pedido>>() {});
        return new ArrayList<>(pedidos);
    }
}
