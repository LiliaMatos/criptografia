package com.paulasantana.criptografia.criptografia.service;

import java.io.IOException;

import org.junit.Test;


public class ServiceCriptografiaTest {

	@Test
	public void testaCriptografiaComSucesso() throws IOException {
		
		ServiceCriptografia serviceCriptografia = new ServiceCriptografia(
				"https://api.codenation.dev/v1/challenge/dev-ps/generate-data?token=de8f693681b29156549ed41d82cab65902eb80bd",
				"https://api.codenation.dev/v1/challenge/dev-ps/submit-solution?token=de8f693681b29156549ed41d82cab65902eb80bd"
				);
	
		serviceCriptografia.generateData();
	}
	
}
