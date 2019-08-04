package com.dms.exemplo.restassured;

import static io.restassured.RestAssured.given;

import org.hamcrest.Matchers;
import org.junit.Test;

public class HtmlTest {

	@Test
	public void deveObterHorario() throws Exception {
		given()
		.when()
			.get("http://dmseletronica.local")
		.then()
			.assertThat().statusCode(200)
			.body("html.@lang", Matchers.is("pt-BR"))
			.appendRootPath("html.body.footer")
			.body("@class", Matchers.is("footer bg-dms-grey"))
			.appendRootPath("div.div.table[1].tbody")
			.body("tr[0].td[0]", Matchers.is("Domingo"))
			.body("tr.find{it.toString().startsWith('Seg')}.td[1].time[0]", Matchers.is("8:00"))
		;
		
	}
}
