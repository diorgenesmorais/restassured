package com.dms.exemplo.restassured;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

public class BandeiraResourceTest {

	@Test
	public void deveObterUmaBandeira() throws Exception {
		given()
		.when()
			.get("http://127.0.0.1:8080/bandeiras/1")
		.then()
			.assertThat()
			.statusCode(200)
			.body("id", is(1))
			.body("nome", is("Master"))
			.body("descricao", is("Local"))
			;
	}
}
