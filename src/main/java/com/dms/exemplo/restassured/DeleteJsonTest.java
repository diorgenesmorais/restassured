package com.dms.exemplo.restassured;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;

public class DeleteJsonTest {

	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "https://restapi.wcaquino.me";
	}
	
	@Test
	public void deveDeletarUsuario() throws Exception {
		given()
			.log().method()
		.when()
			.delete("/users/1")
		.then()
			.assertThat()
			.statusCode(204)
		
		;
	}
	
	@Test
	public void naoDeveDeletarUsuarioInexistente() throws Exception {
		given()
			.log().method()
		.when()
			.delete("/users/1000")
		.then()
			.log().all()
			.assertThat()
			.statusCode(400)
			.body("error", is("Registro inexistente"))
			;
	}
}
