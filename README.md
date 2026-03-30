# AI-Powered Fitness Microservices Stack 🏋️‍♂️🚀

A state-of-the-art, high-performance microservices architecture designed to provide personalized fitness tracking and AI-driven health recommendations. Built with a focus on scalability, security, and modern user experience.

---

## 🏗️ System Architecture

The project follows a **Microservices Architecture** pattern, ensuring high availability and independent scalability of features.

- **Config Server**: Centralized configuration management across all services.
- **Eureka Server**: Service Discovery and registration.
- **API Gateway**: Unified entry point with OAuth2 security and request routing.
- **Activity Service**: Manages user workouts and fitness data (MongoDB).
- **AI Service**: Integrates with **Google Gemini AI** to provide smart health insights via Kafka events.
- **User Service**: Handles user profile and identity synchronization.

---

## 🛠️ Tech Stack

### Backend (Java/Spring)
- **Framework**: Spring Boot 3.4.3
- **Microservices**: Spring Cloud (Gateway, Eureka, Config)
- **Security**: Keycloak (OAuth2 / OpenID Connect / JWT)
- **Messaging**: Apache Kafka (Event-driven processing)
- **Database**: MongoDB (NoSQL)
- **Build Tool**: Maven

### Frontend (React)
- **Core**: React 19 + Vite (Next-gen frontend tooling)
- **UI/UX**: Material UI (MUI) with a premium dark-mode aesthetic.
- **State Management**: Redux Toolkit
- **Auth Integration**: PKCE-based authentication with Keycloak.

### AI Integration
- **Engine**: Google Gemini AI API
- **Logic**: Real-time analysis of user activity to generate safety guidelines, improvements, and personalized suggestions.

---

## 🌟 Key Features

### 1. Unified Security
- All requests are protected by **Keycloak**.
- The API Gateway acts as a Resource Server, validating JWT tokens and injecting User IDs into downstream services safely.

### 2. Event-Driven AI Insights
- When a user tracks an activity, an event is published to **Kafka**.
- The AI Service consumes the event and asynchronously generates professional recommendations using Generative AI.

### 3. Modern Responsive UI
- Premium glassmorphism design.
- Real-time data visualization of fitness metrics.
- Seamless authentication flow.

---

## 🚀 Getting Started

1. **Prerequisites**: Java 17+, MongoDB, Kafka, Keycloak.
2. **Setup**:
   - Start the **Config Server** and **Eureka Server**.
   - Start the **Infrastructure** (Kafka, MongoDB, Keycloak).
   - Launch individual microservices.
   - Run the frontend: `cd frontend && npm install && npm run dev`.

---

## 👨‍💻 Developer Focus
- **Clean Code**: SOLID principles and hexagonal architecture patterns.
- **Scalability**: Designed to handle high-throughput fitness data events.
- **UX First**: Prioritizing a "WOW" effect for end-users through design and speed.

---

*This project demonstrates expertise in Full-Stack development, Cloud-Native architectures, and AI integration.*
