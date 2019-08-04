package com.dms.exemplo.restassured;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;

import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class PostJsonTest {

	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "http://restapi.wcaquino.me";
	}

	@Test
	public void deveSalvarUsuario() throws Exception {
		given()
			.contentType("application/json")
			.body("{ \"name\": \"jose\", \"age\": 50 }")
			.log().method()
			.log().headers()
			.log().body()
		.when()
			.post("/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("jose"))
			.body("age", is(50))
		;
	}

	// aula 26 - Validação ao salvar
	@Test
	public void nadDeveSalvarUsuarioSemNome() throws Exception {
		given()
			.contentType("application/json")
			.body("{ \"age\": 50 }")
			.log().method()
			.log().headers()
			.log().uri()
		.when()
			.post("/users")
		.then()
			.log().all()
			.statusCode(400)
			.body("error", is("Name é um atributo obrigatório"))
			;
	}
	
	@Test
	public void deveSalvarUsuarioPorXML() throws Exception {
		given()
			.contentType(ContentType.XML)
			.body("<user><name>Jose</name><age>50</age></user>")
			.log().method()
			.log().headers()
			.log().uri()
		.when()
			.post("/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.rootPath("user")
			.body("@id", is(notNullValue()))
			.body("age", is("50"))
			;
	}
}
