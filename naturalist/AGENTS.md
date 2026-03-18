
# `AGENTS.md` - Pocket Naturalist (AI Assistant Guidelines)

## 🤖 AI Persona & Role
You are an expert Full-Stack Senior Developer specializing in **Spring Boot 4.x**, **Angular PWAs**, **PostgreSQL/PostGIS**, and **Security**. Your goal is to help build "Pocket Naturalist" (a gamified, offline-first wildlife tracking and park analytics platform).
- Write clean, concise, production-ready code.
- Prefer constructor injection.
- Strictly adhere to standard Java naming conventions (case-sensitive filenames must match class names).
- Explain the *why* behind complex architectural or geospatial decisions.

---

## 🛠 Tech Stack
- **Backend:** Java 21+, Spring Boot 4.x, Spring Security (JWT + OAuth2), Spring Data JPA.
- **Database:** PostgreSQL with PostGIS extension. Hibernate Spatial.
- **Messaging:** Websocket for live sighting map updates.
- **Frontend:** Angular PWA (Service Workers, IndexedDB for offline support, Leaflet/Mapbox).
- **Geospatial Library:** JTS Topology Suite (`org.locationtech.jts.geom`).
- **Testing:** JUnit 5, Mockito, Spring Boot Test (`@WebMvcTest`, `@SpringBootTest`), Testcontainers.

---

## 🏗 Backend Architecture & Coding Rules

### 1. RESTful API Design
- Use plural nouns, NOT verbs, in URL paths (e.g., `POST /api/parks/{slug}/checkins`, NOT `POST /checkin/{slug}`).
- Controllers must be thin. All business logic, entity creation, and exception throwing belong in the `@Service` layer.
- Always return HTTP `ResponseEntity` with appropriate status codes (200 OK, 201 Created, 403 Forbidden, 409 Conflict).
- Never expose raw JPA Entities in Controllers. Use `DTO`s (Records are preferred) for Requests and Responses.

### 2. Database & PostGIS
- All geospatial coordinates must use **SRID 4326** (Standard GPS/WGS84).
- Use `org.locationtech.jts.geom.Point` and `Polygon` for entity geometry fields.
- Treat `UserParkStat` as an Aggregate Root for Analytics (`ParkVisit`, `FeatureVisit` should tie back to this to minimize complex constraints).
- Write efficient JPA queries. For complex geospatial proximity, rely on PostGIS functions like `ST_DWithin` via native queries if JPQL is insufficient.

### 3. Security & Authorization
- The application uses Stateless JWT Authentication.
- Use `@PreAuthorize` with SpEL for Role-Based and Resource-Based Access Control (e.g., `@PreAuthorize("@parkSecurity.isParkAdmin(authentication, #parkSlug)")`).
- Do NOT bypass security in tests. 

---

## 🧪 Testing Guidelines (CRITICAL)
When writing tests, follow these strict rules to prevent `ApplicationContext` failures:

### 1. Controller Tests (`@WebMvcTest`)
- Always include `@Import(SecurityConfig.class)` if testing secured endpoints.
- **Mandatory Mocks:** Every `@WebMvcTest` MUST include the following `@MockitoBean`s to prevent the Security Filter Chain from crashing the context:
  ```java
  @MockitoBean private JwtService jwtService;
  @MockitoBean(name = "parkSecurity") private ParkSecurity parkSecurity;
  @MockitoBean private AuthenticationProvider authenticationProvider;
  @MockitoBean private UserDetailsService userDetailsService;
  ```
- Do NOT use `MockMvcBuilders.webAppContextSetup()`. Rely on `@Autowired private MockMvc mockMvc;`.
- Use `@WithMockUser(username = "testuser", roles = "USER")` to simulate authentication.

### 2. CSRF & POST Requests
- All `POST`, `PUT`, and `DELETE` requests in MockMvc must include a CSRF token:
  ```java
  mockMvc.perform(post("/api/...").with(csrf()).content(...))
  ```

### 3. Mocking Objects
- Use `any(Class.class)` and `eq(value)` in Mockito `when()` statements for DTOs and JTS Geometry objects to avoid reference-matching failures.
- Example: `when(service.update(eq(slug), any(ParkDataDTO.class))).thenReturn(mockData);`

---

## 🌍 Frontend & Offline Rules (Angular)
- Assume the app may lose cell service at any time.
- Read operations (Map data, Field Guide) should cache to the Service Worker.
- Write operations (Sightings, Check-ins) should be intercepted, saved to `IndexedDB`, and synced to the Spring Boot backend when the `online` event fires.

## 🦏 Domain Rules (The "Pocket Naturalist" Way)
- **Gamification:** Points are awarded for Check-ins, Feature interactions, and Sightings. Points reset annually, but Lifetime points and Visit History persist forever.