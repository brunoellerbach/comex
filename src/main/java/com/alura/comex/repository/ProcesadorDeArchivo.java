package com.alura.comex.repository;

import com.alura.comex.Pedido;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public interface ProcesadorDeArchivo {
    ArrayList<Pedido> listaPedidos(InputStream inputStream) throws IOException;
}