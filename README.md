# Spring AI Image Generation

A Spring Boot application that demonstrates **AI-powered image generation** using [Spring AI](https://spring.io/projects/spring-ai) and the OpenAI API.

> ⚠️ **Educational Purpose Only** — This project is part of the course material for hands-on learning and experimentation.

## 📚 Course

This project was built as part of the Udemy course:

🎓 **[Spring AI: Beginner to Guru](https://www.udemy.com/course/spring-ai-beginner-to-guru/)**

### Credits

- **Author:** John Thompson — *Spring Framework Guru*
- **LinkedIn:** [linkedin.com/in/springguru](https://www.linkedin.com/in/springguru/)

## 🛠️ Tech Stack

| Technology        | Version       |
|-------------------|---------------|
| Java              | 21            |
| Spring Boot       | 3.4.1         |
| Spring AI (BOM)   | 1.0.0         |
| Lombok            | —             |
| Maven             | Wrapper included |

## 📁 Project Structure

```
src/main/java/guru/springframework/springaiimage
├── SpringAiImageApplication.java      # Application entry point
├── controllers/
│   └── QuestionController.java        # REST controller for image requests
├── model/
│   └── Question.java                  # Request record (question prompt)
└── services/
    ├── OpenAIService.java             # Service interface
    └── OpenAIServiceImpl.java         # OpenAI image generation implementation
```

## 🔌 API Endpoints — `QuestionController`

The `QuestionController` is a Spring REST controller that exposes two endpoints for AI-powered image operations:

### `POST /image` — Generate an Image from a Text Prompt

Generates a **1024×1024 PNG image** using OpenAI's image generation model based on a text prompt.

- **Content-Type:** `application/json`
- **Response:** `image/png` (raw binary)

**Request body:**

```json
{
  "question": "A futuristic city skyline at sunset"
}
```

**Example (cURL):**

```bash
curl -X POST http://localhost:8080/image \
  -H "Content-Type: application/json" \
  -d '{"question":"A futuristic city skyline at sunset"}' \
  --output image.png
```

---

### `POST /vision` — Describe an Image Using AI Vision

Uploads an image file and returns an **AI-generated JSON description** of its contents using OpenAI's chat vision capabilities.

- **Content-Type:** `multipart/form-data`
- **Response:** `application/json`

**Form field:** `file` — the image to analyze (JPEG recommended).

**Example (cURL):**

```bash
curl -X POST http://localhost:8080/vision \
  -F "file=@photo.jpg"
```

**Sample response:**

```json
{
  "description": "A golden retriever sitting on a grassy field under a blue sky."
}
```

---

## ⚙️ Prerequisites

- **Java 21** or later
- An **OpenAI API key** ([platform.openai.com](https://platform.openai.com/))

## 🚀 Getting Started

1. **Clone the repository**

   ```bash
   git clone <repository-url>
   cd spring-ai-image-leandro
   ```

2. **Set your OpenAI API key**

   Export the key as an environment variable:

   ```bash
   # Linux / macOS
   export OPENAI_API_KEY=your-api-key

   # Windows PowerShell
   $env:OPENAI_API_KEY="your-api-key"
   ```

   Or pass it as a JVM argument:

   ```bash
   -DOPENAI_API_KEY=your-api-key
   ```

3. **Run the application**

   ```bash
   ./mvnw spring-boot:run
   ```

   The server will start on the default port **8080**.

## 📄 License

This project is intended for **educational purposes only**. All course content and original code are credited to [John Thompson](https://www.linkedin.com/in/springguru/).
