package br.com.restspring.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.restspring.exceptions.ResourceNotFoundException;
import br.com.restspring.model.PersonVO;
import br.com.restspring.repositories.PersonVORepository;


@Service
public class PersonVOServices {
	
	private Logger logger = Logger.getLogger(PersonVOServices.class.getName());
	
	@Autowired
	PersonVORepository repository;
	
	public List<PersonVO> findAll(){
		
		logger.info("Finding everyone!");
		return repository.findAll();
	}

	public PersonVO findById(Long id) {
		logger.info("Finding one PersonVO!");
		
		
		return repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
	}
	
	public PersonVO create(PersonVO PersonVO) {
		logger.info("Creating one PersonVO");
		
		return repository.save(PersonVO);
	}
	
	public PersonVO update(PersonVO PersonVO) {
		logger.info("Updating one PersonVO");
		
		var entity = repository.findById(PersonVO.getId())
		.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		
		entity.setFirstName(PersonVO.getFirstName());
		entity.setLastName(PersonVO.getLastName());
		entity.setAddress(PersonVO.getAddress());
		entity.setGender(PersonVO.getGender());
		return repository.save(PersonVO);
	}
	
	public void delete(Long id) {
		logger.info("Deleting one PersonVO");
		
		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		repository.delete(entity);
	}
}
