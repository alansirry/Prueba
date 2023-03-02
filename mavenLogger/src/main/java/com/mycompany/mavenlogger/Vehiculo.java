package com.mycompany.mavenlogger;

public class Vehiculo {
	
	private String nombre_vehiculo;
	private int id_auto;
	private int modelo;
	private String color;
	private float precio;
	private String seguro_cobertura;
	private String descripcion_siniestro;
	
	public int getId_auto() {
		return id_auto;
	}
	public void setId_auto(int id_auto) {
		this.id_auto = id_auto;
	}
	
	public String getNombre_vehiculo() {
		return nombre_vehiculo;
	}
	public void setNombre_vehiculo(String nombre_vehiculo) {
		this.nombre_vehiculo = nombre_vehiculo;
	}

	public int getModelo() {
		return modelo;
	}
	public void setModelo(int modelo) {
		this.modelo = modelo;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public float getPrecio() {
		return precio;
	}
	public void setPrecio(float precio) {
		this.precio = precio;
	}
	public String getSeguro_cobertura() {
		return seguro_cobertura;
	}
	public void setSeguro_cobertura(String seguro_cobertura) {
		this.seguro_cobertura = seguro_cobertura;
	}
	public String getDescripcion_siniestro() {
		return descripcion_siniestro;
	}
	public void setDescripcion_siniestro(String descripcion_siniestro) {
		this.descripcion_siniestro = descripcion_siniestro;
	}
	
	
	
}
