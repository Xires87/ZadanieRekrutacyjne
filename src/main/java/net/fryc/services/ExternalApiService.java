package net.fryc.services;

import net.fryc.utils.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@Service
public class ExternalApiService {

    public static final Logger LOGGER = LoggerFactory.getLogger(ExternalApiService.class);

    private static final HttpHeaders GITHUB_API_HEADERS = new HttpHeaders(MultiValueMap.fromSingleValue(Map.of(
            "Accept", "application/vnd.github+json",
            "X-GitHub-Api-Version", "2022-11-28"
    )));

    private static final String GITHUB_API_URL = "https://api.github.com";

    public final RestTemplate restTemplate;

    public ExternalApiService(){
        this.restTemplate = new RestTemplate();
    }

    public <T> ResponseEntity<T> getResponseFromApi(String url, Class<T> responseType, HttpEntity<T> httpEntity){
        try{
            return this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, responseType);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAs(responseType));
        }
    }

    public ResponseEntity<String> getGithubRepos(String userName){
        ResponseEntity<String> response = this.getResponseFromApi(
                GITHUB_API_URL + "/users/" + userName + "/repos",
                String.class,
                new HttpEntity<>(GITHUB_API_HEADERS)
        );

        if(response.getStatusCode() == HttpStatus.NOT_FOUND){
            try{
                String newBody = JsonHelper.removeFieldFromJsonString(response.getBody(), "documentation_url");

                return ResponseEntity.status(response.getStatusCode()).body(newBody);
            }
            catch (IOException e){
                String message = "An error occurred while removing field named 'documentation_url' from the following JSON string: " + response.getBody();
                LOGGER.error(message, e);
            }
        }

        return response;
    }

}
