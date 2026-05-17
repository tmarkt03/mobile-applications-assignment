# Car Catalog App - Project Documentation

Date: 2026-03-30
Project: CarCatalogApp
Platform: Android (Java)

## 1. Project Overview
Car Catalog App is an Android mobile application for user authentication and browsing a remote catalog of cars.

The application provides:
- Local user registration and login
- Persistent session handling
- Remote car list retrieval from REST API
- Image loading for car cards
- In-app WebView for car search/buy links

## 2. Technical Stack
- IDE: Android Studio
- Language: Java
- Build System: Gradle (Android)
- Min SDK: 26
- Target SDK: 34
- Networking: Retrofit + Gson + OkHttp Logging Interceptor
- Image Loading: Glide
- UI Components: Material Components, RecyclerView, CardView
- Local persistence for users: SharedPreferences

## 3. Project Structure
- app/src/main/java/com/carcatalog/app
  - MainActivity.java (login entry)
  - RegisterActivity.java (registration)
  - CatalogActivity.java (car list + logout)
  - WebViewActivity.java (in-app browser)
  - UserPrefs.java (local auth/session storage)
  - adapter/CarAdapter.java
  - model/Car.java
  - model/CarResponse.java
  - network/ApiService.java
  - network/RetrofitClient.java
- app/src/main/res/layout
  - activity_main.xml
  - activity_register.xml
  - activity_catalog.xml
  - activity_webview.xml
  - item_car.xml

## 4. Functional Flow
1. App opens on MainActivity.
2. User can:
   - Log in with an existing account, or
   - Navigate to RegisterActivity and create a new account.
3. On successful login, user goes to CatalogActivity.
4. CatalogActivity fetches cars from API endpoint and renders them using RecyclerView.
5. Each card displays title, price, color, availability, and image.
6. Buy button opens WebViewActivity with a generated Google query URL.
7. User can logout from toolbar menu.

## 5. API Integration
Base URL:
- https://myfakeapi.com/api/

Endpoint used:
- GET cars/

Response mapping:
- CarResponse -> List<Car>

## 6. Key Implementation Notes
- Kotlin source files were fully removed.
- Java source files now implement all app logic.
- Manifest launcher points to com.carcatalog.app.MainActivity.
- Portrait mode is enforced for all activities.
- Build validated successfully with Gradle (`:app:assembleDebug`).
- App installed and launched successfully on emulator using package `com.carcatalog.app`.

## 7. Requirement Alignment Summary
Implemented:
- Java language
- RecyclerView for list rendering
- Retrofit for REST communication
- Glide for image handling
- Portrait orientation
- README with project summary and bullet-point features

Not yet implemented in current version:
- Single Activity + multiple Fragments architecture
- Room local database
- Full CRUD and search/sort features for domain entities

## 8. Build and Run
From project root:
- Build: `./gradlew :app:assembleDebug`
- Install: `./gradlew :app:installDebug`
- Launch: `adb shell am start -n com.carcatalog.app/.MainActivity`

## 8.1 Run in Android Studio (easy)
1. Open folder `CarCatalogApp/CarCatalogApp` in Android Studio.
2. Wait for Gradle Sync to complete.
3. Select run configuration **app**.
4. Start an emulator (or connect a device).
5. Click **Run ▶**.

If you see another app (like a Hello World template), check:
- Run module is `app` from this project
- Package is `com.carcatalog.app`
- Launcher activity is `.MainActivity`

## 9. Conclusion
The app currently runs as a working Java Android application with login, catalog listing, image loading, and REST integration. The codebase is ready for further extension toward full lab requirement compliance (Fragments architecture, Room, and advanced data operations).
