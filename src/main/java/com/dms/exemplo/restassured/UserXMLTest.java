package com.dms.exemplo.restassured;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasXPath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.internal.path.xml.NodeImpl;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class UserXMLTest {

	public static RequestSpecification requestSpecification;
			
	public static ResponseSpecification responseSpecification;
	
	@BeforeClass
	public static void setup() {
		//RestAssured.baseURI = "http://restapi.wcaquino.me";
		requestSpecification = new RequestSpecBuilder()
				.setBaseUri("https://restapi.wcaquino.me")
				.log(LogDetail.METHOD)
				.log(LogDetail.URI)
				.build();
		
		responseSpecification = new ResponseSpecBuilder()
				.expectStatusCode(200)
				.build();
		
		// passar a configuração de forma global
		RestAssured.requestSpecification = requestSpecification;
		RestAssured.responseSpecification = responseSpecification;
	}
	/**
	 * Obtem dados de um XML. (aula 18)
	 * <pre>
	 * <!-- consultar por meio de um atributo do XML -->
	 * <user id="3">...
	 * {@value user.@id}
	 * 
	 * </pre>
	 * 
	 * @apiNote Os dados em um XML são todos strings
	 * 
	 * @throws Exception
	 */
	@Test
	public void deveConsultarUmXML() throws Exception {
		given()
		.when()
			.get("/usersXML/3")
		.then()
			.body("user.name", is("Ana Julia"))
			.body("user.@id", is("3"))
			.body("user.filhos.name.size()", is(2))
			.body("user.filhos.name[0]", is("Zezinho"))
			.body("user.filhos.name", hasItem("Zezinho"))
			;
	}
	
	@Test
	public void deveConsultarPeloNoRaiz() throws Exception {
		given()
		.when()
			.get("/usersXML/3")
		.then()
			.rootPath("user")
			.body("name", is("Ana Julia"))
			.body("@id", is("3"))
			.rootPath("user.filhos")
			.body("name.size()", is(2))
			.detachRootPath("filhos")
			.body("filhos.name[0]", is("Zezinho"))
			.appendRootPath("filhos")
			.body("name", hasItem("Zezinho"))
			;
	}
	
	/**
	 * XML fonte deste teste foi salvo na pasta RestAssured
	 * 
	 * @throws Exception
	 */
	@Test
	public void deveFazerConsultasAvancadasComXML() throws Exception {
		given()
		.when()
			.get("/usersXML")
		.then()
			.rootPath("users")
			.body("user.size()", is(3))
			.appendRootPath("user")
			// .body("findAll{it.age.toInteger() <= 25}.size()", is(2))
			.body("findAll{it.age.toInteger() <= 25}", hasSize(2))
			.body("@id", hasItems("1", "2", "3"))
			.body("find{it.age == 25}.name", is("Maria Joaquina"))
			.body("findAll{it.name.toString().contains('n')}.name", hasItems("Maria Joaquina", "Ana Julia"))
			.body("salary.find{it != null}.toDouble()", is(1234.5678d))
			.body("age.collect{it.toInteger() * 2}", hasItems(60, 50, 40))
			.body("name.findAll{it.toString().startsWith('Maria')}.collect{it.toString().toUpperCase()}", is("MARIA JOAQUINA"))
			;
	}
	
	// Aula 21 - Unindo XML com Java
	@Test
	public void deveFazerConsultasAvancadasComJava() throws Exception {
		String name = given()
		.when()
			.get("/usersXML")
		.then()
			.extract()
			.path("users.user.name.findAll{it.toString().startsWith('Maria')}");
		
		assertEquals("Maria Joaquina".toUpperCase(), name.toUpperCase());
	}
	
	// Aula 21 - Unindo XML com Java
		@Test
		public void deveFazerConsultasAvancadasComJavaComColecao() throws Exception {
			ArrayList<NodeImpl> nomes = given()
			.when()
				.get("/usersXML")
			.then()
				.extract()
				.path("users.user.name.findAll{it.toString().contains('n')}");
			
			assertEquals(2, nomes.size());
			assertEquals("Maria Joaquina".toUpperCase(), nomes.get(0).toString().toUpperCase());
			assertTrue("Ana Julia".equalsIgnoreCase(nomes.get(1).toString()));
		}
		
		// aula 22 - XPath
		@Test
		public void devefazerconsultasComXPath() throws Exception {			
			// Precisa-se configurar a porta se a baseURI não foi definida
			//requestSpecification.port(80);
			
			given()
			.when()
				.get("/usersXML")
			.then()
				.body(hasXPath("count(/users/user)", is("3")))
				.body(hasXPath("/users/user[@id = '1']"))
				.body(hasXPath("//user[@id = '2']"))
				.body(hasXPath("//name[text() = 'Luizinho']/../../name", is("Ana Julia")))
				.body(hasXPath("//name[text() = 'Ana Julia']/following-sibling::filhos", allOf(containsString("Luizinho"), containsString("Zezinho"))))
				.body(hasXPath("//name", is("João da Silva")))
				.body(hasXPath("//user[2]/name", is("Maria Joaquina")))
				.body(hasXPath("//user[last()]/name", is("Ana Julia")))
				.body(hasXPath("count(//user/name[contains(., 'n')])", is("2")))
				.body(hasXPath("//user[age < 24]/name", is("Ana Julia")))
				.body(hasXPath("//user[age > 20 and age < 30]/name", is("Maria Joaquina")))
				.body(hasXPath("//user[age > 20][age < 30]/name", is("Maria Joaquina")))
				;
		}
		
		// aula 23 - atributos estáticos
		@Test
		public void deveTrabalharComBaseURI() throws Exception {
			//RestAssured.baseURI = "http://restapi.wcaquino.me";
			//RestAssured.port = 80;
			//RestAssured.basePath = "/v2";
			RequestSpecification requestSpecification =
					new RequestSpecBuilder()
					.setBaseUri("http://restapi.wcaquino.me")
					.setBasePath("/v2")
					.build();
			
			given()
				.spec(requestSpecification)
				.log().uri()
			.when()
				.get("/users")
			.then()
				.log().all()
				.assertThat()
				.statusCode(200)
				;
		}
}
