# Spring AI Image & Voice Generation

A Spring Boot application that demonstrates **AI-powered image generation, image vision analysis, text-to-speech, and audio splitting** using [Spring AI](https://spring.io/projects/spring-ai) and the OpenAI API.

> ⚠️ **Educational Purpose Only** — This project is part of the course material for hands-on learning and experimentation.

## 📚 Course

This project was built as part of the Udemy course:

🎓 **[Spring AI: Beginner to Guru](https://www.udemy.com/course/spring-ai-beginner-to-guru/)**

### Credits

- **Author:** John Thompson — *Spring Framework Guru*
- **LinkedIn:** [linkedin.com/in/springguru](https://www.linkedin.com/in/springguru/)

## 🛠️ Tech Stack

| Technology        | Version          |
|-------------------|------------------|
| Java              | 21               |
| Spring Boot       | 3.4.1            |
| Spring AI (BOM)   | 1.0.0            |
| Lombok            | —                |
| Maven             | Wrapper included |

## 📁 Project Structure

```
src/main/java/guru/springframework/springaiimage
├── SpringAiImageApplication.java        # Application entry point
├── controllers/
│   ├── AudioSplitController.java        # REST controller for audio splitting
│   ├── ImageQuestionController.java     # REST controller for image generation & vision
│   └── VoiceQuestionController.java     # REST controller for text-to-speech
├── model/
│   └── Question.java                    # Request record (question prompt)
└── services/
    ├── OpenAIService.java               # Service interface
    └── OpenAIServiceImpl.java           # OpenAI implementation (image, vision, speech, audio split)
```

## 🔌 API Endpoints

### `ImageQuestionController`

Handles AI-powered image generation and image vision analysis.

#### `POST /image` — Generate an Image from a Text Prompt

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

#### `POST /vision` — Describe an Image Using AI Vision

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

### `VoiceQuestionController`

Handles AI-powered text-to-speech conversion.

#### `POST /talk` — Convert Text to Speech (Brazilian Portuguese)

Converts a text prompt into an **MP3 audio file** using OpenAI's TTS (Text-to-Speech) model (`tts-1-hd`) with the **Alloy** voice. The output is always spoken in **Brazilian Portuguese**.

- **Content-Type:** `application/json`
- **Response:** `audio/mpeg` (raw binary)

**Request body:**

```json
{
  "question": "Olá, como vai você?"
}
```

**Example (cURL):**

```bash
curl -X POST http://localhost:8080/talk \
  -H "Content-Type: application/json" \
  -d '{"question":"Olá, como vai você?"}' \
  --output speech.mp3
```

---

### `AudioSplitController`

Handles AI-powered audio file splitting using OpenAI Whisper for intelligent segmentation.

#### `POST /audio/split` — Split an MP3 into 4 Parts

Uploads an MP3 audio file. The service uses **OpenAI Whisper** (`whisper-1`) to transcribe the audio with timestamps (verbose JSON format), identifying natural segment boundaries (sentence/phrase endings). The audio is then divided into **4 equal-duration parts**, with cut points aligned to the nearest natural speech boundary detected by the AI. The result is returned as a **ZIP archive** containing `part-1.mp3` through `part-4.mp3`.

- **Content-Type:** `multipart/form-data`
- **Response:** `application/zip` (binary — `audio-parts.zip`)

**Form field:** `file` — the MP3 audio file to split.

**How it works:**

1. The MP3 is sent to OpenAI Whisper for transcription with segment-level timestamps.
2. Three cut points are calculated at 25%, 50%, and 75% of the total duration.
3. Each cut point is adjusted to the nearest segment boundary (end of a sentence/phrase) identified by Whisper — avoiding mid-sentence splits.
4. The audio is divided proportionally by byte offset and packaged into a ZIP.

> ⚠️ **Note:** Byte-proportional splitting works best with constant bitrate (CBR) MP3 files. Variable bitrate (VBR) files may have slight timing imprecision at split points.

**Example (cURL):**

```bash
curl -X POST http://localhost:8080/audio/split \
  -F "file=@podcast.mp3" \
  --output audio-parts.zip
```

**Response contents (ZIP):**

```
audio-parts.zip
├── part-1.mp3
├── part-2.mp3
├── part-3.mp3
└── part-4.mp3
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
