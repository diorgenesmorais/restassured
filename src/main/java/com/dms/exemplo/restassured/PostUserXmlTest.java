package com.dms.exemplo.restassured;

import static io.restassured.RestAssured.given;

import org.junit.Test;

import io.restassured.http.ContentType;

public class PostUserXmlTest {

	@Test
	public void deveSalvarUsuarioEmXml() throws Exception {
		User user = new User("Rebeca", 18);
		
		given()
			.log().body()
			.contentType(ContentType.XML)
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.assertThat().statusCode(201)
			.log().all()
			;
	}
	
	@Test
	public void deveSalvarUsuarioEmXmlObterRetornoEmXml() throws Exception {
		User user = new User("Rebeca", 18);
		
		User rebeca = given()
			.log().all()
			.contentType(ContentType.XML)
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.assertThat().statusCode(201)
			.extract().body().as(User.class)
			;
		
		System.out.println(rebeca);
	}
}
