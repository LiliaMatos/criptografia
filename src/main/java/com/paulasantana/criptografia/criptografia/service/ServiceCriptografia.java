package com.paulasantana.criptografia.criptografia.service;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
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

	private String enderecoGetGenerateData;
	
	private String enderecoPostData;
	
	public ServiceCriptografia() {
	}
	
	public ServiceCriptografia(String enderecoGetGenerateData, String enderecoPostData) {
		this.enderecoGetGenerateData = enderecoGetGenerateData;
		this.enderecoPostData = enderecoPostData;
	}

	public void generateData() throws IOException {
		CriptografiaData  criptografiaData = getDados();
		
		criarArquivo(criptografiaData);
		
		postDosDados();
	}

	private CriptografiaData getDados() {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<CriptografiaData> requestGetData = 
				restTemplate.getForEntity(enderecoGetGenerateData, CriptografiaData.class);
		
		CriptografiaData  criptografiaData = requestGetData.getBody();
		
		criptografiaData.decifrarCriptografia();
		
		criptografiaData.gerarResumo();
		
		return criptografiaData;
	}

	private void postDosDados() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("answer", getFile());
		
		HttpEntity<MultiValueMap<String, Object>> requestEntity
		 = new HttpEntity<>(body, headers);
		 
		RestTemplate restTemplatePostData = new RestTemplate();
		ResponseEntity<String> response = restTemplatePostData.postForEntity(enderecoPostData , requestEntity, String.class);
		
		System.out.println(response.getBody());
		System.out.println("Status Code : "+response.getStatusCode());
	}

	private void criarArquivo(CriptografiaData criptografiaData) throws IOException {
		Gson gson = new Gson();
		FileWriter file = new FileWriter("/home/paula/Documentos/projetos/criptografia/answer.json");
		file.write(gson.toJson(criptografiaData));
		file.flush();
		file.close();
	}
	

	private Resource getFile() {
		File file = new File("/home/paula/Documentos/projetos/criptografia/answer.json");
		return new FileSystemResource(file);
	}

	
	
}
