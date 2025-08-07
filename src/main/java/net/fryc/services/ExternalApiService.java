package net.fryc.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ExternalApiService {

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
        return this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, responseType);
    }

    public ResponseEntity<String> getGithubRepos(String userName){
        return this.getResponseFromApi(GITHUB_API_URL + "/users/" + userName + "/repos", String.class, new HttpEntity<>(GITHUB_API_HEADERS));
    }
}
