package com.dms.exemplo.restassured;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;

public class OlaMundo {

	public static void main(String[] args) {
		Response request = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
		//Response request = RestAssured.request(Method.GET, "http://teste.local/aulas");
		System.out.println(request.getBody().asString());
		System.out.println(request.statusCode());
	}

}
