package br.com.restspring.integrationtests.controller.withxml;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import br.com.restspring.configs.TestConfigs;
import br.com.restspring.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.restspring.integrationtests.vo.AccountCredentialsVO;
import br.com.restspring.integrationtests.vo.PagedModelPerson;
import br.com.restspring.integrationtests.vo.PersonVO;
import br.com.restspring.integrationtests.vo.TokenVO;
import br.com.restspring.integrationtests.vo.wrappers.WrapperPersonVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerXmlTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static XmlMapper objectMapper;

	private static PersonVO person;

	@BeforeAll
	public static void setup() {
		objectMapper = new XmlMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		person = new PersonVO();
	}

	@Test
	@Order(0)
	public void authorization()
			throws JsonMappingException, JsonProcessingException {
		AccountCredentialsVO user = new AccountCredentialsVO("leandro",
				"admin123");

		var accessToken = given().basePath("/auth/signin")
				.port(TestConfigs.SERVER_PORT)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML).body(user).when().post()
				.then().statusCode(200).extract().body().as(TokenVO.class)
				.getAccessToken();

		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION,
						"Bearer " + accessToken)
				.setBasePath("/api/person/v1").setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL)).build();
	}

	@Test
	@Order(1)
	public void testCreate()
			throws JsonMappingException, JsonProcessingException {
		mockPerson();

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML).body(person).when().post()
				.then().statusCode(200).extract().body().asString();

		PersonVO persistedPerson = objectMapper.readValue(content,
				PersonVO.class);
		person = persistedPerson;

		assertNotNull(persistedPerson);

		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());

		assertTrue(persistedPerson.getId() > 0);

		assertEquals("Nelson", persistedPerson.getFirstName());
		assertEquals("Piquet", persistedPerson.getLastName());
		assertEquals("Brasilia - DF - Brasil", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
	}

	@Test
	@Order(2)
	public void testUpdate()
			throws JsonMappingException, JsonProcessingException {
		person.setLastName("Piquet Souto Maior");

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML).body(person).when().post()
				.then().statusCode(200).extract().body().asString();

		PersonVO persistedPerson = objectMapper.readValue(content,
				PersonVO.class);
		person = persistedPerson;

		assertNotNull(persistedPerson);

		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());

		assertTrue(persistedPerson.getEnabled());

		assertEquals(person.getId(), persistedPerson.getId());

		assertEquals("Nelson", persistedPerson.getFirstName());
		assertEquals("Piquet Souto Maior", persistedPerson.getLastName());
		assertEquals("Brasilia - DF - Brasil", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
	}

	@Test
	@Order(3)
	public void disablePersonById()
			throws JsonMappingException, JsonProcessingException {

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.header(TestConfigs.HEADER_PARAM_ORIGIN,
						TestConfigs.ORIGIN_ERUDIO)
				.pathParam("id", person.getId()).when().patch("{id}").then()
				.statusCode(200).extract().body().asString();

		PersonVO persistedPerson = objectMapper.readValue(content,
				PersonVO.class);
		person = persistedPerson;

		assertNotNull(persistedPerson);

		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		assertFalse(persistedPerson.getEnabled());

		assertEquals(person.getId(), persistedPerson.getId());

		assertEquals("Nelson", persistedPerson.getFirstName());
		assertEquals("Piquet Souto Maior", persistedPerson.getLastName());
		assertEquals("Brasilia - DF - Brasil", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());

	}

	@Test
	@Order(4)
	public void testFindById()
			throws JsonMappingException, JsonProcessingException {
		mockPerson();

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.header(TestConfigs.HEADER_PARAM_ORIGIN,
						TestConfigs.ORIGIN_ERUDIO)
				.pathParam("id", person.getId()).when().get("{id}").then()
				.statusCode(200).extract().body().asString();

		PersonVO persistedPerson = objectMapper.readValue(content,
				PersonVO.class);
		person = persistedPerson;

		assertNotNull(persistedPerson);

		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		assertFalse(persistedPerson.getEnabled());

		assertTrue(persistedPerson.getId() > 0);

		assertEquals("Nelson", persistedPerson.getFirstName());
		assertEquals("Piquet Souto Maior", persistedPerson.getLastName());
		assertEquals("Brasilia - DF - Brasil", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
	}

	@Test
	@Order(5)
	public void testDelete()
			throws JsonMappingException, JsonProcessingException {

		given().spec(specification).contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.header(TestConfigs.HEADER_PARAM_ORIGIN,
						TestConfigs.ORIGIN_ERUDIO)
				.pathParam("id", person.getId()).when().delete("{id}").then()
				.statusCode(204);

	}

	@Test
	@Order(6)
	public void testFindAll()
			throws JsonMappingException, JsonProcessingException {

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.queryParams("page", 3, "size", 10, "direction", "asc")
				.body(person).when().get().then().statusCode(200).extract()
				.body().asString();
//		.as(new TypeRef<List<PersonVO>>() {});

		PagedModelPerson wrapper = objectMapper.readValue(content,
				PagedModelPerson.class);
		var people = wrapper.getContent();

		PersonVO foundPersonOne = people.get(0);
		assertNotNull(foundPersonOne.getId());
		assertNotNull(foundPersonOne.getFirstName());
		assertNotNull(foundPersonOne.getLastName());
		assertNotNull(foundPersonOne.getAddress());
		assertNotNull(foundPersonOne.getGender());

		assertFalse(foundPersonOne.getEnabled());

		assertEquals(423, foundPersonOne.getId());

		assertEquals("Allin", foundPersonOne.getFirstName());
		assertEquals("Padilla", foundPersonOne.getLastName());
		assertEquals("1951 Sullivan Place", foundPersonOne.getAddress());
		assertEquals("Male", foundPersonOne.getGender());

		PersonVO foundPersonEight = people.get(7);
		assertNotNull(foundPersonEight.getId());
		assertNotNull(foundPersonEight.getFirstName());
		assertNotNull(foundPersonEight.getLastName());
		assertNotNull(foundPersonEight.getAddress());
		assertNotNull(foundPersonEight.getGender());

		assertTrue(foundPersonEight.getEnabled());

		assertEquals(682, foundPersonEight.getId());

		assertEquals("Aluino", foundPersonEight.getFirstName());
		assertEquals("Muldowney", foundPersonEight.getLastName());
		assertEquals("03224 Gateway Place", foundPersonEight.getAddress());
		assertEquals("Male", foundPersonEight.getGender());
	}

	@Test
	@Order(7)
	public void testFindByName()
			throws JsonMappingException, JsonProcessingException {

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.pathParam("firstName", "ay")
				.queryParams("page", 0, "size", 6, "direction", "asc").when()
				.get("findPersonByName/{firstName}").then().statusCode(200)
				.extract().body().asString();

		PagedModelPerson wrapper = objectMapper.readValue(content,
				PagedModelPerson.class);
		var people = wrapper.getContent();

		PersonVO foundPersonOne = people.get(0);

		assertNotNull(foundPersonOne.getId());
		assertNotNull(foundPersonOne.getFirstName());
		assertNotNull(foundPersonOne.getLastName());
		assertNotNull(foundPersonOne.getAddress());
		assertNotNull(foundPersonOne.getGender());

		assertTrue(foundPersonOne.getEnabled());

		assertEquals(781, foundPersonOne.getId());

		assertEquals("Aylmer", foundPersonOne.getFirstName());
		assertEquals("Croad", foundPersonOne.getLastName());
		assertEquals("16 Grayhawk Center", foundPersonOne.getAddress());
		assertEquals("Male", foundPersonOne.getGender());
	}

	@Test
	@Order(8)
	public void testFindAllWithoutToken()
			throws JsonMappingException, JsonProcessingException {

		RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
				.setBasePath("/api/person/v1").setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL)).build();

		given().spec(specificationWithoutToken)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML).body(person).when().get()
				.then().statusCode(403);
//		.as(new TypeRef<List<PersonVO>>() {});

	}

	@Test
	@Order(9)
	public void testHATEOAS()
			throws JsonMappingException, JsonProcessingException {

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.queryParams("page", 3, "size", 10, "direction", "asc")
				.body(person).when().get().then().statusCode(200).extract()
				.body().asString();
//		.as(new TypeRef<List<PersonVO>>() {});

		assertTrue(content.contains(
				"<links><rel>self</rel><href>http://localhost/api/person/v1/423</href></links>"));
		assertTrue(content.contains(
				"<links><rel>self</rel><href>http://localhost/api/person/v1/211</href></links>"));
		assertTrue(content.contains(
				"<links><rel>self</rel><href>http://localhost/api/person/v1/75</href></links>"));

		assertTrue(content.contains(
				"<links><rel>first</rel><href>http://localhost/api/person/v1?direction=asc&amp;page=0&amp;size=10&amp;sort=firstName,asc</href></links>"));
		assertTrue(content.contains(
				"<links><rel>prev</rel><href>http://localhost/api/person/v1?direction=asc&amp;page=2&amp;size=10&amp;sort=firstName,asc</href></links>"));
		assertTrue(content.contains(
				"<links><rel>self</rel><href>http://localhost/api/person/v1?page=3&amp;size=10&amp;direction=asc</href></links>"));
		assertTrue(content.contains(
				"<links><rel>next</rel><href>http://localhost/api/person/v1?direction=asc&amp;page=4&amp;size=10&amp;sort=firstName,asc</href></links>"));
		assertTrue(content.contains(
				"<links><rel>last</rel><href>http://localhost/api/person/v1?direction=asc&amp;page=100&amp;size=10&amp;sort=firstName,asc</href></links>"));

	}

	private void mockPerson() {
		person.setFirstName("Nelson");
		person.setLastName("Piquet");
		person.setAddress("Brasilia - DF - Brasil");
		person.setGender("Male");
		person.setEnabled(true);
	}

}
