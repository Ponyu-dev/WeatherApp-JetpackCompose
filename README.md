# WeatherApp-JetpackCompose

🚧 WORK IN PROGRESS 🚧

The project is a mobile application 'Weather Forecast', developed in Kotlin using modern Android technologies. The main purpose of the application is to provide users with up-to-date weather information based on data from the OpenWeather API. The application supports features such as displaying current weather, multi-day forecasts, and automatically fetching data based on the user's location. It also includes functionalities for saving favorite cities, animated transitions between screens, local data storage using Room, and dependency management through Hilt.


## Tech Stack
- **Jetpack Compose:** A modern UI toolkit for building native Android apps.
- **Jetpack Navigation:** A library for building navigation on Android.
- **Room:** A library for providing local data storage on Android
- **Retrofit:** A type-safe HTTP client for Android and Java that simplifies network requests.
- **Hilt:** A dependency injection library for Android
- **[OpenWeather One Call API 3.0](https://openweathermap.org/api/one-call-3):** A weather data API (One Call API) to retrieve weather data for a particular location. 
- **Google Play's Location Library:** A library for retrieving the current location of the device.
- **Kotlin Coroutines:** A library for asynchronous programming.

## Graph
<img src="https://github.com/Ponyu-dev/WeatherApp-JetpackCompose/blob/main/graph/dependency-graph.svg" alt="Graph"/>

## Features
<table>
  <tr>
    <td align="center">
      <strong>Splash Screen</strong><br/>
      <img src="https://github.com/Ponyu-dev/WeatherApp-JetpackCompose/blob/main/screenshots/splash.png" alt="Splash Screen" width="200"/>
    </td>
    <td align="center">
      <strong>Home Screen</strong><br/>
      <img src="https://github.com/Ponyu-dev/WeatherApp-JetpackCompose/blob/main/screenshots/home.png" alt="Home Screen" width="200"/>
    </td>
  </tr>
  <tr>
    <td align="center">
      <strong>Favorites Screen</strong><br/>
      <img src="https://github.com/Ponyu-dev/WeatherApp-JetpackCompose/blob/main/screenshots/favorites.png" alt="Favorites Screen" width="200"/>
    </td>
    <td align="center">
      <strong>Forecast Screen</strong><br/>
      <img src="https://github.com/Ponyu-dev/WeatherApp-JetpackCompose/blob/main/screenshots/forecast.png" alt="Forecast Screen" width="200"/>
    </td>
  </tr>
</table>

## Demo
<img src="https://github.com/Ponyu-dev/WeatherApp-JetpackCompose/blob/main/demo/demo.gif" alt="Demo GIF" width="200"/>
