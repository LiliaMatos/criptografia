package com.paulasantana.criptografia.criptografia.entity;

import org.apache.commons.codec.digest.DigestUtils;

public class CriptografiaData {

	private Integer numero_casas;
	
	private String token;
	
	private String cifrado;
	
	private String decifrado;
	
	private String resumo_criptografico;
	
	public Integer getNumero_casas() {
		return numero_casas;
	}

	public void setNumero_casas(Integer numero_casas) {
		this.numero_casas = numero_casas;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCifrado() {
		return cifrado;
	}

	public void setCifrado(String cifrado) {
		this.cifrado = cifrado;
	}

	public String getDecifrado() {
		return decifrado;
	}

	public void setDecifrado(String decifrado) {
		this.decifrado = decifrado;
	}

	public String getResumo_criptografico() {
		return resumo_criptografico;
	}

	public void setResumo_criptografico(String resumo_criptografico) {
		this.resumo_criptografico = resumo_criptografico;
	}

	public void gerarResumo() {
		this.setResumo_criptografico(
				DigestUtils.sha1Hex(this.getDecifrado()));
	}
	
	public void decifrarCriptografia() {
		Integer numeroDeCasas = this.getNumero_casas();
		String alfabeto = "abcdefghijklmnopqrstuvwxyz";
		
		String decifrado = "";

		for (int posicaoCaracterere = 0; posicaoCaracterere < this.getCifrado().length(); posicaoCaracterere++) {
			
			if(!alfabeto.contains(String.valueOf(this.getCifrado().charAt(posicaoCaracterere)))) {
				decifrado = decifrado.concat(String.valueOf(this.getCifrado().charAt(posicaoCaracterere)));
			}else {
				decifrado = decifrado.concat(
						String.valueOf(
								getCaracteredcifrado(this.getCifrado().charAt(posicaoCaracterere),numeroDeCasas)));
			}
		}
		this.setDecifrado(decifrado);
	}
	
	private char getCaracteredcifrado(char caractere, Integer numeroDeCasas) {
		String alfabeto = "abcdefghijklmnopqrstuvwxyz";
		
		int posicaoCaractere = alfabeto.indexOf(caractere);
		
		if(posicaoCaractere < numeroDeCasas) {
			return alfabeto.charAt((alfabeto.length()-1)-numeroDeCasas);
		}
		
		return alfabeto.charAt(posicaoCaractere-numeroDeCasas);
	}
	
}
