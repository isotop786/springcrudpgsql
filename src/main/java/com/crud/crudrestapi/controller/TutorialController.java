package com.crud.crudrestapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crud.crudrestapi.model.Tutorial;
import com.crud.crudrestapi.repository.TutorialRepository;


@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class TutorialController {
	
	@Autowired
	TutorialRepository tutorialRepository;
	
//	Fetching all tutorials
	@GetMapping("/tutorials")
	public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam(required = false)String title)
	{
		try {
			List<Tutorial> tutorials = new ArrayList<Tutorial>();
			
			if(title == null)
				tutorialRepository.findAll().forEach(tutorials::add);
			else
				tutorialRepository.findByTitleContaining(title).forEach(tutorials::add);
				
			if(tutorials.isEmpty())
			{
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
				
			return new ResponseEntity<List<Tutorial>>(tutorials, HttpStatus.OK);
			
			
		}catch(Exception e)
		{
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// Fetch Tutorial by ID
	@GetMapping("/tutorials/{id}")
	public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") long id)
	{
		Optional<Tutorial> tutorialData = tutorialRepository.findById(id);
		
		if(tutorialData.isPresent()) {
			return new ResponseEntity<>(tutorialData.get(), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	// Create new tutorial
	@PostMapping("/tutorials")
	public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorial)
	{
		try {
			Tutorial _tutorial = tutorialRepository.save(new Tutorial(
					tutorial.getTitle(),
					tutorial.getDescription(),
					false
					));
			return new ResponseEntity<>(_tutorial,HttpStatus.CREATED);
			
		}catch(Exception e)
		{
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// updating a tutorial
	@PutMapping("/tutorials/{id}")
	public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") long id, @RequestBody Tutorial tutorial)
	{
		Optional<Tutorial> tutorialData = tutorialRepository.findById(id);
		
		if(tutorialData.isPresent())
		{
			Tutorial _tut = tutorialData.get();
			_tut.setTitle(tutorial.getTitle());
			_tut.setDescription(tutorial.getDescription());
			_tut.setPublished(tutorial.isPublished());
			
			return new ResponseEntity<>(tutorialRepository.save(_tut), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<> (HttpStatus.NOT_FOUND);
		}
	}
	
}
