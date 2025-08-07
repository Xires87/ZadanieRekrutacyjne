package net.fryc.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fryc.services.ExternalApiService;
import net.fryc.utils.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class GithubUserController {

    public static final Logger LOGGER = LoggerFactory.getLogger(GithubUserController.class);

    @Autowired
    public ExternalApiService externalApiService;

    @GetMapping("/{user}")
    public ResponseEntity<String> listGithubReposForUser(@PathVariable String user) {
        ResponseEntity<String> response = this.externalApiService.getGithubRepos(user);
        return ResponseEntity.status(response.getStatusCode()).body(
                response.getStatusCode() == HttpStatus.OK ?
                        this.createShorterResponse(response) :
                        this.createShorterErrorResponse(response)
        );
    }

    private String createShorterResponse(ResponseEntity<String> response) {
        try{
            JsonNode root = JsonHelper.MAPPER.readTree(response.getBody());

            List<Map<String, Object>> repoInfo = new ArrayList<>();

            for(JsonNode node : root){
                if(node.isObject()){
                    if(isFork((ObjectNode) node)){
                        continue;
                    }

                    String repoName = node.get("name").asText();
                    String ownerLogin = node.get("owner").get("login").asText();
                    List<Map<String, String>> branchInfo = getBranchInfo(ownerLogin, repoName);

                    repoInfo.add(Map.of(
                            "repo_name", repoName,
                            "owner_login", ownerLogin,
                            "branches", branchInfo
                    ));
                }
            }

            return JsonHelper.MAPPER.writeValueAsString(repoInfo);

        } catch (IOException e) {
            LOGGER.error("An error occurred while preparing response for '/{user}' endpoint.", e);
            throw new RuntimeException(e);
        }
    }

    private List<Map<String, String>> getBranchInfo(String userName, String repoName) throws IOException {
        ResponseEntity<String> response = this.externalApiService.getRepoBranches(userName, repoName);
        JsonNode root = JsonHelper.MAPPER.readTree(response.getBody());
        List<Map<String, String>> branchInfo = new ArrayList<>();

        if(root.isArray()){
            for(JsonNode node : root){
                if(node.isObject()){
                    branchInfo.add(Map.of(
                            "name", node.get("name").asText(),
                            "last_commit_sha", node.get("commit").get("sha").asText()
                    ));
                }
            }
        }

        return branchInfo;
    }

    private String createShorterErrorResponse(ResponseEntity<String> response){
        return JsonHelper.removeFieldFromJsonString(response.getBody(), "documentation_url", LOGGER);
    }

    private static boolean isFork(ObjectNode repoObject) {
        return repoObject.get("fork").asBoolean(false);
    }
}
