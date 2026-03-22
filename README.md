# GitHub Access Reporter Service

This Spring Boot application generates a report of users and the repositories they have access to within a GitHub Organization.

## How to Run
1. Clone the repository.
2. Add your GitHub PAT (Personal Access Token) in `GitHubService.java`.
3. Open terminal in the project folder and run: `./mvnw spring-boot:run`
4. Access the report via browser: `http://localhost:8080/api/report/{orgName}`

## Key Design Decisions
- **Efficiency**: Used Java **Parallel Streams** to fetch collaborator data for 100+ repositories simultaneously, avoiding sequential bottlenecks.
- **Scalability**: Implemented API pagination (`per_page=100`) to handle large organizations.
- **Architecture**: Followed a clean Controller-Service-Model pattern for better maintainability.