# Car Catalog App

## Project Summary
Car Catalog App is an Android mobile application that lets users browse a comprehensive catalog of cars fetched from a remote REST API. Users can search cars by brand or model, view detailed information on individual cars, and save their favourite cars to a local offline database.

## Goal of the Project
Build a well-structured Android app that demonstrates a single-Activity / multiple-Fragment architecture with bottom tab navigation, REST API integration (Retrofit + Glide), and local persistence (Room) for a favourites feature  all within a clean, portrait-mode UI built with ConstraintLayout.

## App Features
- **User registration**  create an account with username and password (stored locally in SharedPreferences)
- **User login / logout**  persistent session, redirects to catalog on re-launch
- **Car List**  browse up to 150 cars fetched from the MyFakeAPI REST endpoint, displayed in a RecyclerView with card-style items
- **Car Detail**  tap any car to see full details: image, price, colour, VIN, and availability badge
- **Add / Remove Favourites**  star a car from the detail screen; stored offline in a Room SQLite database
- **Search**  filter cars by brand or model name; results shown in a RecyclerView
- **Favourites List**  dedicated tab showing all saved cars with a one-tap remove option (Room LiveData, auto-refreshes)
- **Image loading**  car images loaded via Glide from loremflickr
- **Bottom Navigation**  three tabs: Cars, Search, Favourites
- **Single-Activity architecture**  one Activity, multiple Fragments (no multiple Activities)
- **Portrait mode**  fixed portrait orientation throughout

## Technical Stack
- Language: Java
- Architecture: Single Activity + Fragments
- Layout: ConstraintLayout
- Navigation: Bottom Navigation (BottomNavigationView)
- List display: RecyclerView
- REST API: Retrofit 2 + Gson converter (https://myfakeapi.com/api/cars/)
- Image loading: Glide 4
- Local database: Room (Architecture Components)
- Version control: Git (commit frequently, push to remote)
