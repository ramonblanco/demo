package com.example.demo.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.models.Pedido;
import com.example.demo.models.Reporte;
import com.example.demo.service.FacturacionSertvice;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

@Service
public class FacturacionSertviceImpl implements FacturacionSertvice {

	@Override
	public void asyncFacturarPedido(final List<Pedido> pedidos) throws IOException {

		String dateStr = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
		String userDirPath = "facturas/".concat(dateStr);

		File userDir = new File(userDirPath);
		userDir.mkdirs();

		for (Pedido pedido : pedidos) {

			File newFactura = new File(userDirPath, pedido.getNumeroPedido().concat(".csv"));
			newFactura.getAbsoluteFile().createNewFile();

			FileWriter output = new FileWriter(newFactura);
			CSVWriter writer = new CSVWriter(output, ',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER,
					CSVWriter.DEFAULT_LINE_END);

			writer.writeNext(new String[] { "CLIENTE", "DOCUMENTO", "FECHA", "MONTO" });
			writer.writeNext(new String[] { pedido.getCliente().getNumeroCliente(),
					pedido.getCliente().getNumeroDocumento(), dateStr,
					calculaIva(pedido.getProducto().getPrecio(), pedido.getCliente().getTipoDocumento()) + "" });

			writer.close();
		}

	}

	private double calculaIva(double monto, String tipoDocumento) {

		switch (tipoDocumento) {
		case "A":
			return monto + ((monto * 10.05) / 100);
		case "B":
			return monto + ((monto * 21) / 100);
		case "X":
			return monto + ((monto * 70) / 100);

		default:
			return monto;
		}
	}

	@Override
	public List<Reporte> obtenerReporteDiario() throws IOException {
		String dateStr = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
		File userFolder = new File("facturas/".concat(dateStr));

		List<Reporte> facturas = new ArrayList<>();

		for (int idx = 0; idx < userFolder.listFiles().length; idx++) {
			File transactionFile = userFolder.listFiles()[idx];
			Reporte reporte = getTransactionFromFile(transactionFile);
			facturas.add(reporte);
		}

		return facturas;
	}

	public void cancelarPedido(final String numeroPedido) {
		String dateStr = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
		File userFolder = new File("facturas/".concat(dateStr).concat("/" + numeroPedido + ".csv"));

		userFolder.delete();
	}

	private Reporte getTransactionFromFile(final File userFile) throws IOException {
		Reader reader = new InputStreamReader(new FileInputStream(userFile.getAbsoluteFile()));

		CsvToBean<Reporte> csvBean = new CsvToBeanBuilder<Reporte>(reader).withType(Reporte.class)
				.withIgnoreLeadingWhiteSpace(true).build();

		return csvBean.parse().get(0);
	}

}
