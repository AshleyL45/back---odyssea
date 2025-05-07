
# ReadMe Back-end 



Ce projet est une application permettant aux utilisateurs de réserver des voyages autour du monde haut de gamme et personnalisés. Réalisé en groupe, l'objectif était de proposer une expérience utilisateur optimale grâce à un site web sobre, élégant et intuitif.

Le back-end du projet est développé avec Java Spring Boot. Il permet de gérer efficacement les données des utilisateurs, des itinéraires, des réservations, ainsi que les options de personnalisation. Les données concernant les hôtels, activités et vols sont récupérées depuis une API.








## Équipe

Kenza Chrifi - Web Designer / Directrice Artistique / Conceptrice => Front-end

Liliana Amangoua - Scrum Master / Responsable Debuggage => Fullstack

Ashley Labory - Cheffe de Projet / Référente Git => Back-end
## Méthodologie de gestion de projet

Application de la méthode Kanban pour un flux de travail continu.

Utilisation de l'outil Trello pour organiser les tâches : "À faire", "En cours", "Terminé".
## Fonctionnalités :

- Gestion sécurisée des utilisateurs et réservations : Authentification robuste, gestion des permissions utilisateurs et sécurisation complète des processus de réservation.

- Stockage et gestion complète des itinéraires, hôtels, activités et options : Organisation efficace et complète des données nécessaires à la construction d’itinéraires sur mesure ou prédéfinis.

- Intégration des données hôtelières, des activités et des vols grâce à l'API Amadeus : Récupération en temps réel des informations actualisées concernant les disponibilités d’hôtels, activités et vols via l’API Amadeus, garantissant ainsi l’exactitude et la pertinence des informations affichées aux utilisateurs.

- APIs REST sécurisées dédiées à l’interaction avec l'application Front-end : Mise en place d’interfaces d’échange de données sécurisées et performantes, assurant une communication fiable et rapide entre le front-end et le back-end.
## Technologies utilisées

- Java Spring Boot 

- Spring Security 

- MySQL Workbench 

- Paul Schwartz 

- API Amadeus 
## Structure

```bash
├── .env.sample
├── .gitattributes
├── .gitignore
├── .mvn
    └── wrapper
    │   └── maven-wrapper.properties
├── et --hard abc1234
├── mvnw
├── mvnw.cmd
├── pom.xml
└── src
    ├── main
        ├── java
        │   └── com
        │   │   └── example
        │   │       └── odyssea
        │   │           ├── OdysseaApplication.java
        │   │           ├── config
        │   │               └── DataSourceConfig.java
        │   │           ├── controllers
        │   │               ├── AuthController.java
        │   │               ├── flight
        │   │               │   ├── FlightSegmentController.java
        │   │               │   └── PlaneRideController.java
        │   │               ├── itinerary
        │   │               │   ├── ItineraryActivityPerDayController.java
        │   │               │   └── ItineraryController.java
        │   │               ├── mainTables
        │   │               │   ├── ActivityController.java
        │   │               │   ├── BlogArticleController.java
        │   │               │   ├── CityController.java
        │   │               │   ├── CityDistanceController.java
        │   │               │   ├── CountryController.java
        │   │               │   ├── HotelController.java
        │   │               │   ├── MySelectionController.java
        │   │               │   ├── OptionController.java
        │   │               │   ├── ReservationController.java
        │   │               │   └── ThemeController.java
        │   │               └── userItinerary
        │   │               │   ├── InteractiveMapController.java
        │   │               │   └── UserItineraryController.java
        │   │           ├── daos
        │   │               ├── flight
        │   │               │   ├── FlightSegmentDao.java
        │   │               │   ├── FlightSegmentRideDao.java
        │   │               │   └── PlaneRideDao.java
        │   │               ├── itinerary
        │   │               │   ├── ItineraryActivityPerDayDao.java
        │   │               │   ├── ItineraryDao.java
        │   │               │   ├── ItineraryOptionDao.java
        │   │               │   └── ItineraryStepDao.java
        │   │               ├── mainTables
        │   │               │   ├── ActivityDao.java
        │   │               │   ├── BlogArticleDao.java
        │   │               │   ├── CityDao.java
        │   │               │   ├── CityDistanceDao.java
        │   │               │   ├── CountryDao.java
        │   │               │   ├── HotelDao.java
        │   │               │   ├── ImageDao.java
        │   │               │   ├── MySelectionDao.java
        │   │               │   ├── OptionDao.java
        │   │               │   ├── ReservationDao.java
        │   │               │   ├── ReservationOptionDao.java
        │   │               │   └── ThemeDao.java
        │   │               ├── userAuth
        │   │               │   └── UserDao.java
        │   │               └── userItinerary
        │   │               │   ├── InteractiveMapRepository.java
        │   │               │   ├── UserActivitiesPerDayDao.java
        │   │               │   ├── UserItineraryDao.java
        │   │               │   ├── UserItineraryFlightDao.java
        │   │               │   ├── UserItineraryOptionDao.java
        │   │               │   └── UserItineraryStepDao.java
        │   │           ├── dtos
        │   │               ├── amadeus
        │   │               │   └── TokenAmadeus.java
        │   │               ├── flight
        │   │               │   ├── AircraftDTO.java
        │   │               │   ├── AirportDTO.java
        │   │               │   ├── DictionnaryDTO.java
        │   │               │   ├── FlightDataDTO.java
        │   │               │   ├── FlightItineraryDTO.java
        │   │               │   ├── FlightOffersDTO.java
        │   │               │   ├── FlightPriceDTO.java
        │   │               │   ├── FlightSegmentDTO.java
        │   │               │   ├── LocationDTO.java
        │   │               │   └── PlaneRideDTO.java
        │   │               ├── mainTables
        │   │               │   ├── ActivityDto.java
        │   │               │   ├── CityDistanceDto.java
        │   │               │   ├── CityDistanceInfoDto.java
        │   │               │   ├── DailyPlanDto.java
        │   │               │   ├── DailyPlanWithCityDto.java
        │   │               │   ├── HotelDto.java
        │   │               │   ├── ItineraryDetails.java
        │   │               │   ├── ItineraryThemes.java
        │   │               │   └── MySelectionDto.java
        │   │               ├── reservation
        │   │               │   ├── ItineraryReservationDTO.java
        │   │               │   ├── ReservationRecapDTO.java
        │   │               │   └── BookingRequest.java
        │   │               └── userItinerary
        │   │               │   ├── CitySelectionDTO.java
        │   │               │   ├── CountrySelectionDTO.java
        │   │               │   ├── InteractiveMapDto.java
        │   │               │   ├── UserItineraryDTO.java
        │   │               │   ├── UserItineraryDayDTO.java
        │   │               │   └── UserRequestDTO.java
        │   │           ├── entities
        │   │               ├── BlogArticle.java
        │   │               ├── CityDistance.java
        │   │               ├── MySelection.java
        │   │               ├── itinerary
        │   │               │   ├── Itinerary.java
        │   │               │   ├── ItineraryActivityPerDay.java
        │   │               │   ├── ItineraryOption.java
        │   │               │   └── ItineraryStep.java
        │   │               ├── mainTables
        │   │               │   ├── Activity.java
        │   │               │   ├── City.java
        │   │               │   ├── Country.java
        │   │               │   ├── FlightSegment.java
        │   │               │   ├── FlightSegmentRide.java
        │   │               │   ├── Hotel.java
        │   │               │   ├── Image.java
        │   │               │   ├── Option.java
        │   │               │   ├── PlaneRide.java
        │   │               │   ├── Reservation.java
        │   │               │   ├── ReservationOption.java
        │   │               │   └── Theme.java
        │   │               ├── userAuth
        │   │               │   └── User.java
        │   │               └── userItinerary
        │   │               │   ├── UserActivitiesPerDay.java
        │   │               │   ├── UserItinerary.java
        │   │               │   ├── UserItineraryFlight.java
        │   │               │   ├── UserItineraryOption.java
        │   │               │   └── UserItineraryStep.java
        │   │           ├── exceptions
        │   │               ├── GlobalExceptionHandler.java
        │   │               ├── ReservationNotFoundException.java
        │   │               ├── ResourceNotFoundException.java
        │   │               ├── UserNotFoundException.java
        │   │               └── UsernameNotFoundException.java
        │   │           ├── mapper
        │   │               ├── DailyPlanDTOMapper.java
        │   │               ├── ItineraryThemesMapper.java
        │   │               ├── ReservationRecapDTOMapper.java
        │   │               └── ReservationRowMapper.java
        │   │           ├── security
        │   │               ├── CustomUserDetails.java
        │   │               ├── CustomUserDetailsService.java
        │   │               ├── JwtFilter.java
        │   │               ├── JwtToken.java
        │   │               ├── JwtUtil.java
        │   │               └── SecurityConfig.java
        │   │           └── services
        │   │               ├── amadeus
        │   │                   ├── APIAuthService.java
        │   │                   └── TokenService.java
        │   │               ├── flight
        │   │                   ├── FlightSegmentService.java
        │   │                   └── PlaneRideService.java
        │   │               ├── itinerary
        │   │                   ├── ItineraryActivityPerDayService.java
        │   │                   ├── ItineraryService.java
        │   │                   └── ItineraryStepService.java
        │   │               ├── mainTables
        │   │                   ├── ActivityService.java
        │   │                   ├── BlogArticleService.java
        │   │                   ├── CityDistanceService.java
        │   │                   ├── CityService.java
        │   │                   ├── CountryService.java
        │   │                   ├── HotelService.java
        │   │                   ├── MySelectionService.java
        │   │                   ├── ReservationService.java
        │   │                   └── ThemeService.java
        │   │               └── userItinerary
        │   │                   ├── InteractiveMapService.java
        │   │                   ├── UserDailyPlanService.java
        │   │                   └── UserItineraryService.java
        └── resources
        │   ├── application.properties
        │   └── schema.sql
    └── test
        └── java
            └── com
                └── example
                    └── odyssea
                        └── OdysseaApplicationTests.java

```
## Installation

Clonez le projet :

```
git clone [URL_DU_REPOSITORY]
cd backend
```


- Configuration de la base de données :

- Créez une base de données dédiée dans MySQL Workbench.

- Modifiez le fichier application.properties en renseignant vos paramètres de connexion à MySQL :

```
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

Import et lancement du projet :

- Importez le projet dans votre IDE;

- Accédez à l'application sur : http://localhost:3306
