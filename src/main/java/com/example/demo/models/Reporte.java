package com.example.demo.models;

import com.opencsv.bean.CsvBindByName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Reporte {

	@CsvBindByName(column = "CLIENTE")
	private String cliente;

	@CsvBindByName(column = "DOCUMENTO")
	private String documento;

	@CsvBindByName(column = "FECHA")
	private String fecha;

	@CsvBindByName(column = "MONTO")
	private String monto;

}
