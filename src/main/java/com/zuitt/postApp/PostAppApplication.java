package com.zuitt.postApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


// annotations specify the intended functionalities of classes. When spring boot runs, it scans classpath with specific annotations and assign behaviors to them based on these annotations

// @SpringBootApplication designates that this class as the main entry point of the Springboot Application
// @SprintBootApplication tells Spring Boot that this class is the starting point for the Spring Application.
// It also enables a range of Spring Boot features automatically, such as component scanning, auto-configuration, and property support
@SpringBootApplication
// @RestController indicates that this class is a controller that handles web requests and produces HTTP responses.
@RestController
public class PostAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostAppApplication.class, args);
	}

	// @GetMapping is used to map HTTP GET requests to a specific endpoint
	// This allows the hello() method/function to be invoked when accessing GET request, localhost:8080/hello
	@GetMapping("/hello")
	public String hello(){
		return "Hello from Java Spring Boot App!";
	}

	@GetMapping("/hi")
	public String hi(@RequestParam(value="name", defaultValue = "John") String nameParameter){
		// @RequestParam is an annotaction used to bind request parameter/s to method parameter/s.
		// For example, in the URL localhost:8080/hi?name=Jane, name is the query parameter to be bind in the method parameter "nameParameter"
		// if no query parameter is provided, the defualtValue "John" will be used.

//		System.out.println(nameParameter);
		return "Hi! My name is what? My name is: " + nameParameter;
	}

}
