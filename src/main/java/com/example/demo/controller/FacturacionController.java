package com.example.demo.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.Pedido;
import com.example.demo.models.Reporte;
import com.example.demo.service.FacturacionSertvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("facturacion")
@RestController
public class FacturacionController {

	@Autowired
	private FacturacionSertvice facturacionSertvice;

	@PostMapping("/")
	public ResponseEntity<Void> facturarPedido(@RequestBody List<Pedido> pedido) {
		log.info("Facturar Pedido");

		Executors.newScheduledThreadPool(1).schedule(() -> {
			try {
				this.facturacionSertvice.asyncFacturarPedido(pedido);
			} catch (IOException ex) {
				log.error("Error ----> {} ", ex);
			}
		}, 1, TimeUnit.SECONDS);

		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

	@GetMapping("/reporteDiario")
	public ResponseEntity<List<Reporte>> reporteDiario() throws IOException {
		log.info("Reporte Diario");

		return new ResponseEntity<>(facturacionSertvice.obtenerReporteDiario(), HttpStatus.OK);
	}

	@DeleteMapping("/cancelaPedido")
	public ResponseEntity<Void> cancelaPedido(@RequestParam String numeroPedido) {
		log.info("Reporte Diario");

		facturacionSertvice.cancelarPedido(numeroPedido);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
