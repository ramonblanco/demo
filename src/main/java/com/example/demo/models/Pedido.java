package com.example.demo.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pedido {

	private String numeroPedido;

	private Cliente cliente;

	private Producto producto;
}
