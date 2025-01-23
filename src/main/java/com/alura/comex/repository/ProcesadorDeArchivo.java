package com.alura.comex.repository;

import com.alura.comex.Pedido;

import java.io.IOException;
import java.util.ArrayList;

public interface ProcesadorDeArchivo {
    ArrayList<Pedido> listaPedidos(String archivo) throws IOException;
}
