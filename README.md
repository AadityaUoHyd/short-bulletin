# Short Bulletin
ShortBulletin is an online news platform that delivers concise insights from English daily newspapers. Using SpringAI and LLM, it extracts and summarizes the top 20 news stories from a full newspaper PDF uploaded for a specific date.

## Tech Stack
- Spring Boot
- Docker
- Local LLM (SmolLM2)
- PostgreSQL
- PDFBox
- iText
- Thymeleaf
- JWT

## Features
- AI-Powered News Summarizer Dashboard using Spring Boot, Docker, and Local LLM (SmolLM2).
- This project creates a web dashboard where admins can upload daily newspaper PDFs (e.g., Indian Express), 
  processes them using a local AI model (SmolLM2 via Docker Model Runner) to extract and summarize
  the top 20 news stories (10-15 word header + 30-40 word explanation), stores them in a PostgreSQL database,
  and publishes them for users.
- Users can register/login to view summaries by date and download a generated ShortBulletin PDF.
- This app uses AI to condense news into short briefs.
- Frontend uses Thymeleaf (text-only, no images). Security includes admin login (default: Admin/Admin123)
  for uploads and user accounts for viewing.

## Prerequisites
- Docker Desktop (version 4.40+ with Model Runner enabled).
- Java 17+ and Maven.
- PostgreSQL driver (handled via Docker).
- Apache PDFBox and iText for PDF handling (Maven deps).
- No internet required for LLM after pulling model.

## Run these command after starting docker
```
docker desktop enable model-runner --tcp 12434
docker model pull ai/smollm2:360M-Q4_K_M
```

## Project Structure
```
short-bulletin/
├── pom.xml
├── src/main/java/org/aadi/short_bulletin/
│   ├── ShortBulletinApplication.java
│   ├── config/
│   │   ├── SecurityConfig.java
│   │   └── LlmConfig.java
│   ├── controller/
│   │   ├── AuthController.java
│   │   ├── AdminController.java
│   │   └── UserController.java
│   ├── service/
│   │   ├── UserService.java
│   │   ├── PdfProcessingService.java
│   │   ├── LlmService.java
│   │   └── PdfGenerationService.java
│   ├── repository/
│   │   ├── UserRepository.java
│   │   └── BulletinRepository.java
│   ├── entity/
│   │   ├── User.java
│   │   ├── Bulletin.java
│   │   └── NewsItem.java
│   └── dto/
│       └── NewsSummaryDto.java
├── src/main/resources/
│   ├── application.yml
│   ├── templates/
│   │   ├── login.html
│   │   ├── register.html
│   │   ├── admin-upload.html
│   │   ├── user-bulletin.html
│   │   └── bulletin-detail.html
│   └── static/ (empty, text-only)
├── docker-compose.yml
└── README.md
```

# Running the Project
- Start Postgres: docker-compose up db
- Run Model Runner (host commands above).
    ```
    mvn spring-boot:run or docker-compose up app
    ```
- Login as Admin/Admin123 at http://localhost:8080/login
- Upload PDF (e.g., Indian Express full newspaper) with date.
- System extracts text, chunks if needed, prompts LLM for top 20 summaries, saves to DB.
- Users register at /register, login, view at /user/bulletins.
- Download PDF link generates on-the-fly ShortBulletin PDF.
