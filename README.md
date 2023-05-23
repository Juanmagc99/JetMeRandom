# JetMeRandom


This is an Android project developed using Jetpack Compose, designed to provide users with information about flight prices. It is suitable for beginners as it provides a simple example of implementing Jetpack Compose in an Android application.


![searchScreen](https://github.com/Juanmagc99/JetMeRandom/assets/61785593/8d43596c-34cf-456c-b17f-4c92098bf9ae)
![liked](https://github.com/Juanmagc99/JetMeRandom/assets/61785593/95202ac0-dfeb-40ae-a43e-d038d3bd6d5e)



https://github.com/Juanmagc99/JetMeRandom/assets/61785593/7f9d0a92-a90d-4c1e-81cb-827002a64a13



https://github.com/Juanmagc99/JetMeRandom/assets/61785593/40f2a71a-8935-439e-b221-def4e28e43a7



## Features
  * View a list of available flights with their corresponding prices.
  * Filter flights based on various criteria such as departure time, arrival time, and price range.
  * Select a flight to view detailed information including the airline, departure and arrival airports, and duration of the flight.
  * Add flights to favorites for quick access.
  * Select flights to compare

## Technologies Used
  * Android Jetpack Compose: The UI framework used to build the entire user interface of the application.
  * Kotlin: The programming language used for developing the Android application.
  * ViewModel: Jetpack ViewModel is used to manage and persist UI-related data across configuration changes.
  * Room Database: Room is used as a local database to store favorite flights.
  * Retrofit: Retrofit library is used to make HTTP requests to retrieve flight data from an API.
  * Hilt: As depencency manager.
  * Kiwi API for flights, Google Maps API, positiotrack API and Teleport Public API

## Getting Started
  To get started with the project, follow these steps:

  * Clone the repository to your local machine.
  * Open the project in Android Studio.
  * Build and run the application on an emulator or a physical device.
 *Please note that you may need a local properties to put your own api keys.*

## Project Structure

* app module: Contains the main Android application code, including activities and the Jetpack Compose UI code.
* data package: Contains data-related classes, such as repositories, data models, and API service interfaces.
* di package: Contains dependency injection related classes, such as modules and components.
* ui package: Contains Jetpack Compose UI components and screens.
* util package: Contains utility classes used throughout the application.


## License
* This project is licensed under the MIT License. Feel free to use, modify, and distribute the code for personal or commercial purposes.

## Acknowledgments
* The project is inspired by various flight booking apps available in the market.

