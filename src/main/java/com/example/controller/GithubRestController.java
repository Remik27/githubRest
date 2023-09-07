package com.example.controller;

import com.example.dto.GithubRepositoriesResponse;
import com.example.service.GithubRestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.WrongMethodTypeException;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class GithubRestController {
    private final GithubRestService githubApiService ;
    private final ObjectMapper objectMapper;

    @GetMapping(value = "/repositories/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get data in JSON format", produces = "application/json")
    public ResponseEntity<Object> getGithubRepositories(
            @PathVariable String username)
             {

        GithubRepositoriesResponse response = githubApiService.getRepositories(username);

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/repositories/{username}", produces = MediaType.APPLICATION_XML_VALUE)
    @ApiOperation(value = "Get data in XML format", produces = "application/xml")
    public ResponseEntity<Object> getGithubRepositoriesXml(
            @PathVariable String username)
    {

        throw new WrongMethodTypeException("XML format is not supported. Try with application/json");
    }
}
