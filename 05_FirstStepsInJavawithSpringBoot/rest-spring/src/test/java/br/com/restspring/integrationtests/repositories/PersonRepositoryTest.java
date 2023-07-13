package br.com.restspring.integrationtests.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.com.restspring.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.restspring.model.Person;
import br.com.restspring.repositories.PersonRepository;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(OrderAnnotation.class)
public class PersonRepositoryTest extends AbstractIntegrationTest {

	@Autowired
	PersonRepository repository;
	
	private static Person person;
	
	@BeforeAll
	public static void setup() {
		person = new Person();
		
	}
	
	@Test
	@Order(0)
	public void testFindByName() throws JsonMappingException, JsonProcessingException {
		
		Pageable pageable = PageRequest.of(0, 6, Sort.by(Direction.ASC, "firstName")); 
		person = repository.findPersonByName("ay",pageable).getContent().get(0);
		
		assertNotNull(person.getId());
		assertNotNull(person.getFirstName());
		assertNotNull(person.getLastName());
		assertNotNull(person.getAddress());
		assertNotNull(person.getGender());

		assertTrue(person.getEnabled());
		
		assertEquals(781, person.getId());
		
		assertEquals("Aylmer", person.getFirstName());
		assertEquals("Croad", person.getLastName());
		assertEquals("16 Grayhawk Center", person.getAddress());
		assertEquals("Male", person.getGender());
	}
	@Test
	@Order(1)
	public void testDisablePerson() throws JsonMappingException, JsonProcessingException {
		
		repository.disablePerson(person.getId());
		Pageable pageable = PageRequest.of(0, 6, Sort.by(Direction.ASC, "firstName")); 
		person = repository.findPersonByName("ay",pageable).getContent().get(0);
		
		assertNotNull(person.getId());
		assertNotNull(person.getFirstName());
		assertNotNull(person.getLastName());
		assertNotNull(person.getAddress());
		assertNotNull(person.getGender());
		
		assertFalse(person.getEnabled());
		
		assertEquals(781, person.getId());
		
		assertEquals("Aylmer", person.getFirstName());
		assertEquals("Croad", person.getLastName());
		assertEquals("16 Grayhawk Center", person.getAddress());
		assertEquals("Male", person.getGender());
	}
	
	
}
