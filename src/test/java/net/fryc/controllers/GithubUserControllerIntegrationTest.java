package net.fryc.controllers;


import com.fasterxml.jackson.databind.JsonNode;
import net.fryc.utils.JsonHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GithubUserControllerIntegrationTest {

    @Autowired
    private GithubUserController userController;


    @Test
    public void givenValidUser_whenExternalApiRespondsSuccessfully_thenStatusIsOkAndReturnedReposHaveNoForks(){
        ResponseEntity<String> apiResponse = this.userController.listGithubReposForUser("Xires87");

        Assertions.assertSame(HttpStatus.OK, apiResponse.getStatusCode());
        Assertions.assertNotNull(apiResponse.getBody());

        Assertions.assertTrue(apiResponse.getBody().contains("repo_name"));
        Assertions.assertTrue(apiResponse.getBody().contains("owner_login"));
        Assertions.assertTrue(apiResponse.getBody().contains("branches"));

        ResponseEntity<String> externalApiResponse = this.userController.externalApiService.getGithubRepos("Xires87");

        Assertions.assertDoesNotThrow(() -> {
            JsonNode myJsonNode = JsonHelper.MAPPER.readTree(apiResponse.getBody());
            JsonNode externalJsonNode = JsonHelper.MAPPER.readTree(externalApiResponse.getBody());

            for(JsonNode node : externalJsonNode){
                if(node.get("fork").asBoolean()){
                    Assertions.assertTrue(
                            myJsonNode.valueStream().filter(repo -> {
                                return repo.get("repo_name").asText().equals(node.get("name").asText());
                            }).findAny().isEmpty()
                    );
                }
            }
        });
    }
}
