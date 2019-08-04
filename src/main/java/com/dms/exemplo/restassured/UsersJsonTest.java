package com.dms.exemplo.restassured;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UsersJsonTest {

	@Test
	public void deveVerificarPrimeiroNivel() throws Exception {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/1")
		.then()
			.assertThat()
			.statusCode(200)
			.and()
			.body("id", is(1))
			.and()
			.body("name", containsString("Silva"))
			.and()
			.body("age", greaterThan(18));
	}
	
	@Test
	public void deveVerificarPrimeiroNivelOutrasFormas() throws Exception {
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/users/1");
		
		// path (XML, Json)
		assertEquals(Integer.valueOf(1), response.path("id"));
		assertEquals(Integer.valueOf(1), response.path("%s", "id"));
		
		// Jsonpath
		JsonPath jpath = new JsonPath(response.asString());
		assertEquals(1, jpath.getInt("id"));
		
		// from
		int id = JsonPath.from(response.asString()).getInt("id");
		assertEquals(1, id);
	}
	
	@Test
	public void deveVerificarSegundoNivel() throws Exception {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/2")
		.then()
			.assertThat()
			.statusCode(200)
			.body("name", containsStringIgnoringCase("joaquina"))
			.body("endereco.rua", is("Rua dos bobos"));
	}
	
	@Test
	public void deveVerificarLista() throws Exception {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/3")
		.then()
			.assertThat()
			.statusCode(200)
			.and()
			.body("name", containsString("Ana"))
			.and()
			.body("filhos", hasSize(2))
			.and()
			.body("filhos.name", hasItem("Zezinho"))
			.and()
			.body("filhos.name", contains("Zezinho", "Luizinho"))
			.and()
			.body("filhos.name", hasItems("Zezinho", "Luizinho"));
	}
	
	@Test
	public void deveVerificarMensagemError() throws Exception {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/34")
		.then()
			.assertThat()
			.statusCode(404)
			.and()
			.body("error", is("Usuário inexistente"));
	}
	
	// 15 - Lista na raiz
	@Test
	public void deveVerificarListaRaiz() throws Exception {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users")
		.then()
			.assertThat()
			.statusCode(200)
			.body("$", hasSize(3))
			.body("name", hasItem("Maria Joaquina"))
			.body("age[1]", is(25))
			.body("filhos.name", hasItem(Arrays.asList("Zezinho", "Luizinho")))
			.body("salary", contains(1234.5678f, 2500, null))
			;
	}
	
	/**
	 * 16 - Verificações avançadas
	 * 
	 * A API retorna este Json
	 * <pre>
	 * [
	 *     {
	 *         id: 1,
	 *         name: "João da Silva",
	 *         age: 30,
	 *         salary: 1234.5678
	 *     },
	 *     {
	 *         id: 2,
	 *         name: "Maria Joaquina",
	 *         endereco: {
	 *         rua: "Rua dos bobos",
	 *         numero: 0
	 *         },
	 *         age: 25,
	 *         salary: 2500
	 *     },
	 *     {
	 *         id: 3,
	 *         name: "Ana Júlia",
	 *         age: 20,
	 *         filhos: [
	 *             {
	 *                 name: "Zezinho"
	 *             },
	 *             {
	 *                 name: "Luizinho"
	 *             }
	 *         ]
	 *     }
	 * ]
	 * </pre>
	 * {@code findAll}, {@code find} e {@code collect} são métodos do Groovy
	 * 
	 * @throws Exception
	 */
	@Test
	public void deveFazerVerificacoesAvancadas() throws Exception {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users")
		.then()
			.assertThat()
			.statusCode(200)
			.body("$", hasSize(3))
			.body("age.findAll{it <= 25}.size()", is(2))
			.body("age.findAll{it <= 25 && it > 20}.size()", is(1))
			.body("findAll{it.age <= 25 && it.age > 20}.name", hasItem("Maria Joaquina"))
			.body("findAll{it.age <= 25}[0].name", is("Maria Joaquina"))
			.body("findAll{it.age <= 25}[-1].name", is("Ana Júlia"))
			.body("find{it.age <= 25}.name", is("Maria Joaquina"))
			.body("findAll{it.name.contains('n')}.name", hasItems("Maria Joaquina", "Ana Júlia"))
			.body("findAll{it.name.length() > 10}.name", hasItems("João da Silva", "Maria Joaquina"))
			.body("findAll{it.name.length() > 10}.size()", is(2))
			.body("name.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA"))
			.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}.toArray()", allOf(Matchers.arrayContaining("MARIA JOAQUINA")))
			.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}.toArray()", allOf(Matchers.arrayWithSize(1)))
			.body("age.collect{it * 2}", hasItems(60, 50, 40))
			.body("id.max()", is(3))
			.body("salary.min()", is(1234.5678f))
			.body("salary.findAll{it != null}.sum()", is(closeTo(3734.5678d, 0.0001)))
			.body("salary.findAll{it != null}.sum()", allOf(greaterThan(3000d), lessThan(5000d)))
			;
	}

	@Test
	public void deveExtrairJsonPathParaJava() throws Exception {
		ArrayList<String> nomes =
				given()
				.when()
					.get("http://restapi.wcaquino.me/users")
				.then()
					.extract().path("name.findAll{it.startsWith('Maria')}");
		
		assertEquals(1, nomes.size());
		assertEquals("maria joaquina".toUpperCase(), nomes.get(0).toUpperCase());
	}
}
