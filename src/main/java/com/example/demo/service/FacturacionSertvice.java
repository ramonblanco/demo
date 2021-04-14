package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import com.example.demo.models.Pedido;
import com.example.demo.models.Reporte;

public interface FacturacionSertvice {

	void asyncFacturarPedido(List<Pedido> pedido) throws IOException;
	
	List<Reporte> obtenerReporteDiario() throws IOException;

	void cancelarPedido(final String numeroPedido);
}
