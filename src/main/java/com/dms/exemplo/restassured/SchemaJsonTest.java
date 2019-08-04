package com.dms.exemplo.restassured;

import static io.restassured.RestAssured.given;

import org.junit.Test;

import io.restassured.module.jsv.JsonSchemaValidator;

public class SchemaJsonTest {

	@Test
	public void deveValidarSchemaJson() throws Exception {
		given()
			.log().all()
		.when()
			.get("http://restapi.wcaquino.me/users")
		.then().assertThat().statusCode(200)
			.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("users.json"))
		;
	}
}
