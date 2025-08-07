package net.fryc.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

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
        return this.getResponseFromApi(
                GITHUB_API_URL + "/users/" + userName + "/repos",
                String.class,
                new HttpEntity<>(GITHUB_API_HEADERS)
        );
    }

    public ResponseEntity<String> getRepoBranches(String userName, String repoName){
        return this.getResponseFromApi(
                GITHUB_API_URL + "/repos/" + userName + "/" + repoName + "/branches",
                String.class,
                new HttpEntity<>(GITHUB_API_HEADERS)
        );
    }

}
