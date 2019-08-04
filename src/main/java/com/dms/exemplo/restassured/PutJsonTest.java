package com.dms.exemplo.restassured;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class PutJsonTest {

	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "https://restapi.wcaquino.me";
	}
	
	// aula 28
	@Test
	public void deveAtualizarUsuario() throws Exception {
		given()
			.log().method()
			.log().headers()
			.log().uri()
			.contentType(ContentType.JSON)
			.body("{ \"name\": \"João Batista\", \"age\": 70 }")
		.when()
			.put("/users/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("João Batista"))
			.body("age", is(70))
			;
	}
	
	// aula 29 - URL parametrizável
	@Test
	public void deveCustomizarURL() throws Exception {
		String recurso = "users";
		Integer id = 1;

		given()
			.log().method()
			.log().headers()
			.log().uri()
			.contentType(ContentType.JSON)
			.body("{ \"name\": \"João Batista\", \"age\": 70 }")
		.when()
			.put("/{recurso}/{id}", recurso, id)
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("João Batista"))
			.body("age", is(70))
			;
	}
	
	// aula 29 - URL parametrizável
	@Test
	public void deveCustomizarURLParte2() throws Exception {
		String recurso = "users";
		Integer id = 1;

		given()
			.log().method()
			.log().headers()
			.log().uri()
			.contentType(ContentType.JSON)
			.body("{ \"name\": \"João Batista\", \"age\": 70 }")
			.pathParam("recurso", recurso)
			.pathParam("id", id)
		.when()
			.put("/{recurso}/{id}")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("João Batista"))
			.body("age", is(70))
			;
	}
}
