package com.example.service;

import com.example.domain.NotFoundException;
import com.example.dto.BranchInfo;
import com.example.dto.GithubRepositoriesResponse;
import com.example.dto.GithubRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GithubRestService {
    private final HttpClient httpClient;
    @Value("${github.api.url}")
    private String githubApiUrl;
    private final ObjectMapper objectMapper;

    public GithubRepositoriesResponse getRepositories(String username) {
        try {
            HttpGet request = new HttpGet(githubApiUrl + "/users/" + username + "/repos");
            request.addHeader("Accept", "application/json");

            HttpResponse httpResponse = httpClient.execute(request);
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            if (HttpStatus.NOT_FOUND.value() == statusCode) {
                throw new NotFoundException("Username [%s] not found".formatted(username));
            }

            String responseBody = EntityUtils.toString(httpResponse.getEntity());
            JsonNode root = objectMapper.readTree(responseBody);
            List<GithubRepository> repositories = new ArrayList<>();

            for (JsonNode jsonNode : root) {
                if (jsonNode.get("fork").asBoolean()) {
                    continue;
                }

                repositories.add(GithubRepository.builder()
                        .name(jsonNode.get("name").asText())
                        .ownerLogin(jsonNode.get("owner").get("login").asText())
                        .branches(getBranches(jsonNode))
                        .build()
                );
            }
            return GithubRepositoriesResponse.builder()
                    .repositories(repositories)
                    .build();


        } catch (NotFoundException ex) {
            throw ex;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    private List<BranchInfo> getBranches(JsonNode jsonNode) {
        HttpGet request = new HttpGet(
                githubApiUrl
                        + "/repos/"
                        + jsonNode.get("owner").get("login").asText()
                        + "/"
                        + jsonNode.get("name").asText()
                        + "/branches");

        request.addHeader("Accept", "application/json");

        HttpResponse httpResponse;
        try {
            httpResponse = httpClient.execute(request);

            String responseBody = EntityUtils.toString(httpResponse.getEntity());
            List<BranchInfo> branchInfos = new ArrayList<>();

            JsonNode jsonNodeBranches = objectMapper.readTree(responseBody);
            for (JsonNode jsonNodeBranch : jsonNodeBranches) {
                branchInfos.add(BranchInfo.builder()
                        .name(jsonNodeBranch.get("name").asText())
                        .lastCommitSha(jsonNodeBranch.get("commit").get("sha").asText())
                        .build());
            }

            return branchInfos;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }
}

