package com.ayesha.github_access_reporter.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepositoryDetail {
    private String name;
    private String permissions;
}