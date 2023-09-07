package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@AllArgsConstructor
@Builder
@Data
public class GithubRepositoriesResponse {
    private List<GithubRepository> repositories;
}
