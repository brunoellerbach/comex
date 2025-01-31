package com.alura.comex.repository;

import com.alura.comex.Pedido;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProcesadorDeXml implements ProcesadorDeArchivo {
    @Override
    public ArrayList<Pedido> listaPedidos(InputStream inputStream) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        // Registrar el módulo para soporte de fechas Java 8 y el deserializador personalizado
        JavaTimeModule timeModule = new JavaTimeModule();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer());

        xmlMapper.registerModule(timeModule);
        xmlMapper.registerModule(module);
        List<Pedido> pedidos = xmlMapper.readValue(inputStream, new TypeReference<>() {});
        return new ArrayList<>(pedidos);
    }
}