package com.dms.exemplo.restassured;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.hamcrest.Matchers;
import org.junit.Test;

public class CaixaTest {

	@Test
	public void deveObterUmaBandeira() throws Exception {
		given()
		.when()
			.get("http://localhost:8080/bandeiras/1")
		.then()
			.assertThat()
			.statusCode(200)
			.and()
			.body("id", is(1))
			.and()
			.body("nome", Matchers.containsString("Master"));
	}
}
