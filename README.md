# Short Bulletin (WORK IN PROGRESS)
ShortBulletin is an online news platform that delivers concise insights from English daily newspapers.
Using SpringAI and LLM, it extracts and summarizes the top 20 news stories from a full newspaper PDF
uploaded for a specific date.

## Tech Stack
- Spring Boot
- Docker
- Local LLM (gemma3)
- PostgreSQL
- PDFBox
- iText
- Thymeleaf
- JWT

## Features
- AI-Powered News Summarizer Dashboard using Spring Boot, Docker, and Local LLM (gemma3).
- This project creates a web dashboard where admins can upload daily newspaper PDFs (e.g., Indian Express), 
  processes them using a local AI model (gemma3 via Docker Model Runner) to extract and summarize
  the top 20 news stories (10-15 word header + 30-40 word explanation), stores them in a PostgreSQL database,
  and publishes them for users.
- Users can register/login to view summaries by date and download a generated ShortBulletin PDF.
- This app uses AI to condense news into short briefs.
- Frontend uses Thymeleaf (text-only, no images). Security includes admin login (default: admin/admin123)
  for uploads and user accounts for viewing.

## Prerequisites
- Docker Desktop (version 4.40+ with Model Runner enabled).
- Java 17+ and Maven.
- PostgreSQL driver (handled via Docker).
- Apache PDFBox and iText for PDF handling (Maven deps).
- No internet required for LLM after pulling model.

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

## Run these command after starting docker
```
cmd> docker model pull ai/gemma3:1B-Q4_K_M (in case you don't have this model in your docker desktop)

cmd> docker model run ai/gemma3:1B-Q4_K_M

Go to docker desktop and ensure,
[Settings -> AI -> check mark on 'Enable host-side TCP support' and keep port '12434' with CORS allowed origin as 'ALL']

cmd> docker desktop enable model-runner --tcp=12434
```

## Running the Project
- Start Postgres: docker-compose up db
- Run Model Runner (host commands above).
  ```
    cmd> docker-compose up app
  
    OR,
  
    cmd> mvn spring-boot:run (after running postgresql with db name as 'short_bulletin_db')
  ```
- Login as admin/admin123 at http://localhost:8080/login
- Upload PDF (e.g., Indian Express full newspaper) with date.
- System extracts text, chunks if needed, prompts LLM for top 20 summaries, saves to DB.
- Users register at /register, login, view at /user/bulletins.
- Download PDF link generates on-the-fly ShortBulletin PDF.

## Pending Task
no i am getting favicon now. also style.css works. issue is with login & register. why showing jwt token post register & login?

tried to login as admin role with default credentials as admin/admin123, got output at http://localhost:8080/api/auth/login:
object
{1}
token
:
"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1OTk1ODEwMywiZXhwIjoxNzU5OTk0MTAzfQ.R6WJeOFKfyv6s2s-saHyn8OB8D7gPyzjJCztQwj0XtQ"



during trying to register as user at http://localhost:8080/api/auth/register,

object
{1}
token
:
"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYWRpIiwiaWF0IjoxNzU5OTU4MjE2LCJleHAiOjE3NTk5OTQyMTZ9.7z1fuP6PZmV23p4xYx8LXXDdGLSYGJaF3gRaWsAyCYY"

It should show me user-bulletin.html and it must have link for bulletin-detail, if user has logged in as USER role successfully, otherwise show credentials are wrong error. 
In case user role has ADMIN, he must be served page admin-upload.html, so that he can upload full_newspaper pdf, which is supposed to be read by LLM in order to filter top big 20 news out of it, 
then LLM(gemmma3) will summarize & segregate it into top 20 news with max 10-15 words as header and 40-50 word as explanation of news, 
and provide that ShortBulletin as output in user-bulletin page as per uploaded date wise. where user can consume that ShortBulletin.

I already have default admin/admin123 as admin user for security login as ADMIN user, who can insert full_newspaper. 
Any user can register(if not duplicate register) his account in order to access(after login) published shortBulletin news on webpage.

provide me codes & fixes for same.