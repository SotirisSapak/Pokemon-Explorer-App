<br/>
<br/>
<p align="center">
  <img width="150" height="150" src="./pokemon explorer app icon.png">
  <h1 align="center">Pokemon explorer app</h1>
  <h4 align="center">An application to preview all pokemon filtered by their type. Will also provide an option to save your favorite pokemon into local storage for fast access without internet connection</h1>
</p>

<br/>
<br/>

[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
![Version](https://img.shields.io/github/v/release/SotirisSapak/Pokemon-Explorer-App)


<h2 align="center">Abstract</h2>

Create a Pokémon Explorer App on android platform using the PokéAPI to fetch data about Pokémon from the 10 most popular types: <b>Fire, Water, Grass, Electric, Dragon, Psychic, Ghost, Dark, Steel, and Fairy.</b>

The app should allow users to:

* <b>Type Selection:</b> Users can choose a type from the following options: Fire, Water, Grass, Electric, Dragon, Psychic, Ghost, Dark, Steel, and Fairy.
* <b>Display Pokémon:</b> Display a list of Pokémon that belong to the selected type. It's sufficient to fetch only the first page of results initially.
* <b>Pokémon Details:</b> Provide detailed information for each Pokémon, including its name, image and base stats (HP, Attack, Defense, etc.).
* <b>Favorites:</b> Allow users to save their favorite Pokémon and access them offline.
* <b>Pagination:</b> Implement pagination to allow users to view more Pokémon beyond the initial page. This should include functionality to navigate back and forth between pages of results.

<br/>

_**Design decision:**_
_There is a clear task in pagination section...Use pages in order to navigate the list of pokemons! Although it's easy to build such a pagination system, it feels more native to just fetch more pokemon when user reaches the bottom of the list._

<br/>

## Used technologies
#### For coding
* <b>Kotlin</b> programming language
* <b>MVVM</b> architecture for separation of logic

#### For design
* <b>XML</b> views
* <b>DataBinding</b> technique for all views
* <b>Navigation component</b> for building a navigation system between application fragments

<br/>

## Screenshots
<p float="left">
  <img width="230" height="420" src="./screenshot home screen.png">
  <img width="230" height="420" src="./screenshot pokemon preview.png">
  <img width="230" height="420" src="./screenshot favorites list.png">
  <img width="230" height="420" src="./screenshot favorites empty state.png">
</p>

<br/>

## Third party libraries
This project contain some functionality from third party libraries. These libraries are:

* [Retrofit](https://github.com/square/retrofit) for HTTP requests to PokeAPI
* [Picasso](https://github.com/square/picasso) for image loading and caching from PokeAPI
* [Lottie](https://github.com/airbnb/lottie-android) for rendering After Effects animations natively

<br/>

## Structure
The application has been built with the use of 4 foundation pillars:

#### Core
Library to contain some useful classes to override and methods to use. Built with abstraction in mind and that’s why most of these classes use Generics. 

#### Framework
Library to contain all styles and themes to support this application. Will also provide some useful classes like <code>ThemeSchema</code> or <code>View.extensions</code> to use in order to manipulate view components programmatically.

#### Di
Library to provide dependencies to application. At the moment this library contains only one class called <code>PokemonApplication</code> which initialize all the services provided by <b>Backend</b> library.

#### Backend
Library to manage all the business logic of the application. Contains both remote and local repositories such as <code>PokemonService</code> or <code>FavoritesService</code> along with data models. 



## License
```copyright 
Copyright 2024 SotirisSapak

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
