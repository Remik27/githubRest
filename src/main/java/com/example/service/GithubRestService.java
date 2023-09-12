package com.example.service;

import com.example.domain.NotFoundException;
import com.example.dto.BranchInfo;
import com.example.dto.GithubRepositoriesResponse;
import com.example.dto.GithubRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GHBranch;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GithubRestService {


    public GithubRepositoriesResponse getRepositories(String username) {
        try {
            GHUser user = GitHub.connectAnonymously().getUser(username);

            List<GithubRepository> repositoryList = new ArrayList<>();
            user.getRepositories().forEach((name, repo) ->{
                    if(!repo.isFork()){
                    repositoryList.add(buildRepository(name, repo));
            }
            });
            return GithubRepositoriesResponse.builder()
                    .repositories(repositoryList)
                    .build();
        } catch (IOException e) {
            throw new NotFoundException("User with name [%s] not found".formatted(username));
        }
    }

    private GithubRepository buildRepository(String name, GHRepository repo) {
        return GithubRepository.builder()
                .name(name)
                .ownerLogin(repo.getOwnerName())
                .branches(buildBranches(repo))
                .build();
    }

    private List<BranchInfo> buildBranches(GHRepository repo) {
        List<BranchInfo> branchInfos = new ArrayList<>();
        try {
            repo.getBranches().forEach((name, branch)-> branchInfos.add(buildBranch(branch)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return branchInfos;
    }

    private BranchInfo buildBranch(GHBranch branch) {
        return BranchInfo.builder()
                .name(branch.getName())
                .lastCommitSha(branch.getSHA1())
                .build();
    }

}

