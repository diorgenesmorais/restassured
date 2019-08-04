package com.dms.exemplo.restassured;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundoTest {

	@Test
	public void testeOlaMundo() throws Exception {
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
	}

	@Test
	public void devoTestarARequisicao() throws Exception {
		given()
		.when()
			.get("http://restapi.wcaquino.me/ola")
		.then()
			.assertThat()
			.statusCode(200);
	}
	
	@Test
	public void devoConhecerMatchersHamcrest() throws Exception {
		assertThat(128, Matchers.isA(Integer.class));
		
		assertThat(128, Matchers.greaterThan(120));
		
		assertThat(7.99, Matchers.lessThan(8d));
		
		List<Integer> impares = Arrays.asList(1,3,5,7,9);
		assertThat(impares, hasSize(5));
		assertThat(impares, contains(1,3,5,7,9));
		assertThat(impares, hasItem(1));
		assertThat(impares, not(hasItem(2)));
		assertThat("Maria", anyOf(is("Maria"), is("Joaquina")));
		assertThat("Joaquina", allOf(startsWith("Joa"), endsWith("ina"), containsString("qui")));
	}

	@Test
	public void deveValidarBody() throws Exception {
		given()
		.when()
			.get("http://restapi.wcaquino.me/ola")
		.then()
			.assertThat()
			.statusCode(200)
			.and()
			.body(is("Ola Mundo!"))
			.and()
			.body(containsString("Mundo"))
			.and()
			.body(is(not(nullValue())));
	}
}
