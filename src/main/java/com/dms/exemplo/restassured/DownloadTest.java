package com.dms.exemplo.restassured;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

public class DownloadTest {

	@Test
	public void deveFazerDownload() throws Exception {
		byte[] image = given()
			.log().all()
		.when()
			.get("http://restapi.wcaquino.me/download")
		.then()
			.statusCode(200)
			.extract().asByteArray()
		;
		
		assertThat(image.length, Matchers.lessThan(100000));
	}
}
