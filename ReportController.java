package com.ayesha.github_access_reporter.controller;

import com.ayesha.github_access_reporter.model.RepositoryDetail;
import com.ayesha.github_access_reporter.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ReportController {

    @Autowired
    private GitHubService gitHubService;

    @GetMapping("/report/{org}")
    public Map<String, List<RepositoryDetail>> generateReport(@PathVariable String org) {
        return gitHubService.getAccessReport(org);
    }
}