package com.ayesha.github_access_reporter.service;

import com.ayesha.github_access_reporter.model.RepositoryDetail;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Service
public class GitHubService {

    private final String BASE_URL = "https://api.github.com";

    public Map<String, List<RepositoryDetail>> getAccessReport(String orgName) {
        RestTemplate restTemplate = new RestTemplate();

        // Auth Headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer YOUR_GITHUB_TOKEN_HERE");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Scale handle karne ke liye pagination [cite: 15]
        String reposUrl = BASE_URL + "/orgs/" + orgName + "/repos?per_page=100";

        try {
            ResponseEntity<Object[]> response = restTemplate.exchange(reposUrl, HttpMethod.GET, entity, Object[].class);
            Object[] repos = response.getBody();

            Map<String, List<RepositoryDetail>> userMap = new HashMap<>();

            if (repos != null) {
                // Efficient API usage ke liye parallel processing [cite: 17, 22]
                Arrays.asList(repos).parallelStream().forEach(repo -> {
                    Map<String, Object> repoData = (Map<String, Object>) repo;
                    String name = (String) repoData.get("name");

                    String collabUrl = BASE_URL + "/repos/" + orgName + "/" + name + "/collaborators";
                    try {
                        ResponseEntity<Object[]> collabResponse = restTemplate.exchange(collabUrl, HttpMethod.GET, entity, Object[].class);
                        Object[] collaborators = collabResponse.getBody();

                        if (collaborators != null) {
                            for (Object collab : collaborators) {
                                Map<String, Object> cData = (Map<String, Object>) collab;
                                String username = (String) cData.get("login");

                                synchronized (userMap) {
                                    userMap.computeIfAbsent(username, k -> new ArrayList<>())
                                            .add(new RepositoryDetail(name, "access_granted"));
                                }
                            }
                        }
                    } catch (Exception e) {
                        // Skipping repos without collaborator access permissions
                    }
                });
            }
            return userMap;
        } catch (Exception e) {
            throw new RuntimeException("GitHub API Error: " + e.getMessage());
        }
    }
}