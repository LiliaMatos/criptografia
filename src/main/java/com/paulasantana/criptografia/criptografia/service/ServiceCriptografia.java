package com.paulasantana.criptografia.criptografia.service;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.paulasantana.criptografia.criptografia.entity.CriptografiaData;


@Service
public class ServiceCriptografia {

	@Value("url.codenation.generatedata")
	private String enderecoGetGenerateData;
	
	@Value("url.codenation.savedata")
	private String enderecoPostData;
	
	public ServiceCriptografia() {
	}
	
	public ServiceCriptografia(String enderecoGetGenerateData, String enderecoPostData) {
		this.enderecoGetGenerateData = enderecoGetGenerateData;
		this.enderecoPostData = enderecoPostData;
	}


	public void generateData() throws IOException {
	
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<CriptografiaData> requestGetData = 
				restTemplate.getForEntity(enderecoGetGenerateData, CriptografiaData.class);
	
		CriptografiaData  criptografiaData = requestGetData.getBody();
		
		Integer numeroDeCasas = Integer.parseInt(criptografiaData.getNumero_casas());
		
		String cifrado = criptografiaData.getCifrado();
		
		String decifrado = "";
		
//		{
//		    "numero_casas": 3,
//		    "token": "de8f693681b29156549ed41d82cab65902eb80bd",
		//começando com 1 o lenght 75
		//começando com 0 o length 74
//		    "cifrado": "li brx fdq qrw zulwh lw grzq lq hqjolvk, brx fdq qrw frgh lw. shwhu kdoshuq",
//		    "decifrado": "",
//		    "resumo_criptografico": ""
//		}
		
		for (int i = 0; i < cifrado.length(); i++) {
			String alfabeto = "abcdefghijklmnopqrstuvwxyz";
			
			if(!alfabeto.contains(String.valueOf(cifrado.charAt(i)))) {
				decifrado = decifrado.concat(String.valueOf(cifrado.charAt(i)));
			}else {
				String caractereDecifrado = String.valueOf(getCaracteredcifrado(cifrado.charAt(i),numeroDeCasas));
				
				decifrado = decifrado.concat(caractereDecifrado);
			}
			
		}
		
		System.out.println(decifrado);
		criptografiaData.setDecifrado(decifrado);
		
		criptografiaData.setResumo_criptografico(DigestUtils.sha1Hex(decifrado));
		
		Gson gson = new Gson();
		FileWriter file = new FileWriter("/home/paula/Documentos/projetos/criptografia/answer.json");
		file.write(gson.toJson(criptografiaData));
		file.flush();
		file.close();
		
		
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", new FileSystemResource(new File("/home/paula/Documentos/projetos/criptografia/answer.json")));

		HttpEntity<LinkedMultiValueMap<String,Object>> httpEntity = new HttpEntity(body, headers);
		
		RestTemplate restTemplatePostData = new RestTemplate();
		ResponseEntity<String> response = 
				restTemplatePostData.postForEntity(enderecoPostData, httpEntity, String.class);
		
		System.out.println(response);
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
