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
| Spring Boot       | 3.3.6         |
| Spring AI (BOM)   | 1.0.0-M5      |
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

