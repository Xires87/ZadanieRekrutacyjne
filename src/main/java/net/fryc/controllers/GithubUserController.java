package net.fryc.controllers;

import net.fryc.services.ExternalApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubUserController {

    @Autowired
    public ExternalApiService externalApiService;

    @GetMapping("/{user}")
    public ResponseEntity<String> listGithubReposFor(@PathVariable String user){
        ResponseEntity<String> response = this.externalApiService.getGithubRepos(user);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
}
