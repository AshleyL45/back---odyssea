-- MySQL dump 10.13  Distrib 8.0.36, for Linux (x86_64)
--
-- Host: localhost    Database: odyssea_db
-- ------------------------------------------------------
-- Server version	8.0.42-0ubuntu0.24.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET FOREIGN_KEY_CHECKS = 1;
--
-- Table structure for table `activity`
--
SET FOREIGN_KEY_CHECKS = 0;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `activity` (
  `id` int NOT NULL AUTO_INCREMENT,
  `city_id` int NOT NULL,
  `name` text NOT NULL,
  `type` text NOT NULL,
  `physical_effort` varchar(255) NOT NULL,
  `duration` time DEFAULT NULL,
  `description` text NOT NULL,
  `price` decimal(7,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `cityId` (`city_id`),
  CONSTRAINT `activity_ibfk_1` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
SET FOREIGN_KEY_CHECKS = 1;
--
-- Table structure for table `city`
--
SET FOREIGN_KEY_CHECKS = 0;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `city` (
  `id` int NOT NULL AUTO_INCREMENT,
  `country_id` int NOT NULL,
  `name` varchar(255) NOT NULL,
  `iata_code` varchar(255) NOT NULL,
  `longitude` decimal(9,6) NOT NULL,
  `latitude` decimal(9,6) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniqueIata` (`iata_code`),
  KEY `countryId` (`country_id`),
  CONSTRAINT `city_ibfk_1` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
SET FOREIGN_KEY_CHECKS = 1;
--
-- Table structure for table `city_distance`
--
SET FOREIGN_KEY_CHECKS = 0;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `city_distance` (
  `from_city_id` int NOT NULL,
  `to_city_id` int NOT NULL,
  `driving_duration_seconds` int NOT NULL,
  `distance_km` decimal(10,2) NOT NULL,
  PRIMARY KEY (`from_city_id`,`to_city_id`),
  UNIQUE KEY `unique_route` (`from_city_id`,`to_city_id`),
  KEY `toCityId` (`to_city_id`),
  CONSTRAINT `city_distance_ibfk_1` FOREIGN KEY (`from_city_id`) REFERENCES `city` (`id`),
  CONSTRAINT `city_distance_ibfk_2` FOREIGN KEY (`to_city_id`) REFERENCES `city` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
SET FOREIGN_KEY_CHECKS = 1;
--
-- Table structure for table `country`
--
SET FOREIGN_KEY_CHECKS = 0;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `country` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `continent` varchar(255) NOT NULL,
  `price` decimal(7,2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
SET FOREIGN_KEY_CHECKS = 1;
--
-- Table structure for table `daily_itinerary`
--
SET FOREIGN_KEY_CHECKS = 0;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `daily_itinerary` (
  `id` int NOT NULL AUTO_INCREMENT,
  `itinerary_id` int NOT NULL,
  `city_id` int NOT NULL,
  `country_id` int NOT NULL,
  `hotel_id` int NOT NULL,
  `day_number` int NOT NULL,
  `description_per_day` text,
  `activity_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `itineraryId` (`itinerary_id`),
  KEY `cityId` (`city_id`),
  KEY `countryId` (`country_id`),
  KEY `hotelId` (`hotel_id`),
  KEY `activities_constraint` (`activity_id`),
  CONSTRAINT `activities_constraint` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`),
  CONSTRAINT `daily_itinerary_ibfk_1` FOREIGN KEY (`itinerary_id`) REFERENCES `itinerary` (`id`),
  CONSTRAINT `daily_itinerary_ibfk_2` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`),
  CONSTRAINT `daily_itinerary_ibfk_3` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`),
  CONSTRAINT `daily_itinerary_ibfk_4` FOREIGN KEY (`hotel_id`) REFERENCES `hotel` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
SET FOREIGN_KEY_CHECKS = 1;
--
-- Table structure for table `draft_activities`
--
SET FOREIGN_KEY_CHECKS = 0;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `draft_activities` (
  `draft_user_itinerary_id` int NOT NULL,
  `activity_id` int NOT NULL,
  `position` int DEFAULT NULL,
  PRIMARY KEY (`draft_user_itinerary_id`,`activity_id`),
  KEY `fk_draft_activities_2_idx` (`activity_id`),
  CONSTRAINT `fk_draft_activities_1` FOREIGN KEY (`draft_user_itinerary_id`) REFERENCES `user_itinerary_draft` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_draft_activities_2` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
SET FOREIGN_KEY_CHECKS = 1;
--
-- Table structure for table `draft_cities`
--
SET FOREIGN_KEY_CHECKS = 0;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `draft_cities` (
  `draft_user_itinerary_id` int NOT NULL,
  `city_id` int NOT NULL,
  `position` int DEFAULT NULL,
  PRIMARY KEY (`draft_user_itinerary_id`,`city_id`),
  KEY `fk_draft_cities_2_idx` (`city_id`),
  CONSTRAINT `fk_draft_cities_1` FOREIGN KEY (`draft_user_itinerary_id`) REFERENCES `user_itinerary_draft` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_draft_cities_2` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
SET FOREIGN_KEY_CHECKS = 1;
--
-- Table structure for table `draft_countries`
--
SET FOREIGN_KEY_CHECKS = 0;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `draft_countries` (
  `draft_user_itinerary_id` int NOT NULL,
  `country_id` int NOT NULL,
  `position` int DEFAULT NULL,
  PRIMARY KEY (`draft_user_itinerary_id`,`country_id`),
  KEY `fk_draft_countries_2_idx` (`country_id`),
  CONSTRAINT `fk_draft_countries_1` FOREIGN KEY (`draft_user_itinerary_id`) REFERENCES `user_itinerary_draft` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_draft_countries_2` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
SET FOREIGN_KEY_CHECKS = 1;
--
-- Table structure for table `draft_options`
--
SET FOREIGN_KEY_CHECKS = 0;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS`draft_options` (
  `draft_user_itinerary_id` int NOT NULL,
  `option_id` int NOT NULL,
  PRIMARY KEY (`draft_user_itinerary_id`,`option_id`),
  KEY `fk_draft_options_2_idx` (`option_id`),
  CONSTRAINT `fk_draft_options_1` FOREIGN KEY (`draft_user_itinerary_id`) REFERENCES `user_itinerary_draft` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_draft_options_2` FOREIGN KEY (`option_id`) REFERENCES `options` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
SET FOREIGN_KEY_CHECKS = 1;
--
-- Table structure for table `flight_segment`
--
SET FOREIGN_KEY_CHECKS = 0;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `flight_segment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `departure_airport_iata` varchar(255) DEFAULT NULL,
  `arrival_airport_iata` varchar(255) DEFAULT NULL,
  `departure_date_time` datetime DEFAULT NULL,
  `arrival_date_time` datetime DEFAULT NULL,
  `carrier_code` varchar(255) DEFAULT NULL,
  `carrier_name` varchar(255) DEFAULT NULL,
  `aircraft_code` varchar(255) DEFAULT NULL,
  `aircraft_name` varchar(255) DEFAULT NULL,
  `duration` time DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
SET FOREIGN_KEY_CHECKS = 1;
--
-- Table structure for table `flight_segment_ride`
--
SET FOREIGN_KEY_CHECKS = 0;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `flight_segment_ride` (
  `plane_ride_id` int NOT NULL,
  `flight_segment_id` int NOT NULL,
  PRIMARY KEY (`plane_ride_id`,`flight_segment_id`),
  KEY `flightSegmentId` (`flight_segment_id`),
  CONSTRAINT `flight_segment_ride_ibfk_1` FOREIGN KEY (`plane_ride_id`) REFERENCES `plane_ride` (`id`),
  CONSTRAINT `flight_segment_ride_ibfk_2` FOREIGN KEY (`flight_segment_id`) REFERENCES `flight_segment` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
SET FOREIGN_KEY_CHECKS = 1;
--
-- Table structure for table `hotel`
--
SET FOREIGN_KEY_CHECKS = 0;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `hotel` (
  `id` int NOT NULL AUTO_INCREMENT,
  `city_id` int NOT NULL,
  `name` text NOT NULL,
  `star_rating` int NOT NULL,
  `description` text NOT NULL,
  `price` decimal(7,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `cityId` (`city_id`),
  CONSTRAINT `hotel_ibfk_1` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
SET FOREIGN_KEY_CHECKS = 1;
--
-- Table structure for table `images`
--
SET FOREIGN_KEY_CHECKS = 0;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `images` (
  `id` int NOT NULL AUTO_INCREMENT,
  `size_type` varchar(255) NOT NULL DEFAULT 'gallery',
  `link` text NOT NULL,
  `alt_text` varchar(255) NOT NULL,
  `activity_id` int DEFAULT NULL,
  `hotel_id` int DEFAULT NULL,
  `itinerary_id` int DEFAULT NULL,
  `article_id` int DEFAULT NULL,
  `data` longblob NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
SET FOREIGN_KEY_CHECKS = 1;
--
-- Table structure for table `itinerary`
--
SET FOREIGN_KEY_CHECKS = 0;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `itinerary` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` text NOT NULL,
  `description` text NOT NULL,
  `short_description` text NOT NULL,
  `stock` int NOT NULL,
  `price` decimal(7,2) DEFAULT NULL,
  `total_duration` int DEFAULT NULL,
  `theme_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `themeId_constraint` (`theme_id`),
  CONSTRAINT `themeId_constraint` FOREIGN KEY (`theme_id`) REFERENCES `theme` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
SET FOREIGN_KEY_CHECKS = 1;
--
-- Table structure for table `itinerary_images`
--
SET FOREIGN_KEY_CHECKS = 0;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `itinerary_images` (
  `itinerary_id` int NOT NULL,
  `image_id` int NOT NULL,
  `role` varchar(50) NOT NULL,
  PRIMARY KEY (`itinerary_id`,`image_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
SET FOREIGN_KEY_CHECKS = 1;
--
-- Table structure for table `my_selection`
--
SET FOREIGN_KEY_CHECKS = 0;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `my_selection` (
  `user_id` int NOT NULL,
  `itinerary_id` int NOT NULL,
  PRIMARY KEY (`user_id`,`itinerary_id`),
  KEY `itineraryId` (`itinerary_id`),
  CONSTRAINT `itinerary_id_selection` FOREIGN KEY (`itinerary_id`) REFERENCES `itinerary` (`id`) ON DELETE CASCADE,
  CONSTRAINT `user_id_selection` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
SET FOREIGN_KEY_CHECKS = 1;
--
-- Table structure for table `options`
--
SET FOREIGN_KEY_CHECKS = 0;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `options` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `price` decimal(7,2) NOT NULL,
  `description` text,
  `category` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
SET FOREIGN_KEY_CHECKS = 1;
--
-- Table structure for table `plane_ride`
--
SET FOREIGN_KEY_CHECKS = 0;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `plane_ride` (
  `id` int NOT NULL AUTO_INCREMENT,
  `one_way` tinyint(1) DEFAULT NULL,
  `total_price` decimal(7,2) DEFAULT NULL,
  `currency` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
SET FOREIGN_KEY_CHECKS = 1;
--
-- Table structure for table `reservation`
--
SET FOREIGN_KEY_CHECKS = 0;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `reservation` (
  `user_id` int NOT NULL,
  `itinerary_id` int NOT NULL,
  `status` varchar(255) NOT NULL,
  `departure_date` date NOT NULL,
  `return_date` date NOT NULL,
  `total_price` decimal(8,2) DEFAULT NULL,
  `purchase_date` date DEFAULT (curdate()),
  `number_of_adults` int NOT NULL,
  `number_of_kids` int NOT NULL DEFAULT '0',
  `option_id` int DEFAULT NULL,
  PRIMARY KEY (`user_id`,`itinerary_id`),
  KEY `userId` (`user_id`),
  KEY `itineraryId` (`itinerary_id`),
  KEY `optionId_constraint` (`option_id`),
  CONSTRAINT `itinerary_id_reservation` FOREIGN KEY (`itinerary_id`) REFERENCES `itinerary` (`id`) ON DELETE CASCADE,
  CONSTRAINT `optionId_constraint` FOREIGN KEY (`option_id`) REFERENCES `options` (`id`),
  CONSTRAINT `user_id_reservation` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
SET FOREIGN_KEY_CHECKS = 1;
--
-- Table structure for table `reservation_draft`
--
SET FOREIGN_KEY_CHECKS = 0;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `reservation_draft` (
  `draft_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `itinerary_id` int DEFAULT NULL,
  `departure_date` date DEFAULT NULL,
  `number_of_adults` int DEFAULT NULL,
  `number_of_kids` int NOT NULL DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `type` enum('Standard','Mystery') DEFAULT NULL,
  PRIMARY KEY (`draft_id`),
  KEY `user_id` (`user_id`),
  KEY `itinerary_id` (`itinerary_id`),
  CONSTRAINT `reservation_draft_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `reservation_draft_ibfk_2` FOREIGN KEY (`itinerary_id`) REFERENCES `itinerary` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
SET FOREIGN_KEY_CHECKS = 1;
--
-- Table structure for table `reservation_option`
--
SET FOREIGN_KEY_CHECKS = 0;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `reservation_option` (
  `user_id` int NOT NULL,
  `itinerary_id` int NOT NULL,
  `option_id` int NOT NULL,
  PRIMARY KEY (`user_id`,`itinerary_id`,`option_id`),
  KEY `option_id` (`option_id`),
  CONSTRAINT `reservation_option_ibfk_1` FOREIGN KEY (`user_id`, `itinerary_id`) REFERENCES `reservation` (`user_id`, `itinerary_id`) ON DELETE CASCADE,
  CONSTRAINT `reservation_option_ibfk_2` FOREIGN KEY (`option_id`) REFERENCES `options` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
SET FOREIGN_KEY_CHECKS = 1;
--
-- Table structure for table `reservation_options_draft`
--
SET FOREIGN_KEY_CHECKS = 0;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `reservation_options_draft` (
  `reservation_draft_id` int NOT NULL,
  `option_id` int NOT NULL,
  PRIMARY KEY (`reservation_draft_id`,`option_id`),
  KEY `option_id` (`option_id`),
  CONSTRAINT `reservation_options_draft_ibfk_1` FOREIGN KEY (`reservation_draft_id`) REFERENCES `reservation_draft` (`draft_id`),
  CONSTRAINT `reservation_options_draft_ibfk_2` FOREIGN KEY (`option_id`) REFERENCES `options` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
SET FOREIGN_KEY_CHECKS = 1;
--
-- Table structure for table `theme`
--
SET FOREIGN_KEY_CHECKS = 0;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `theme` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
SET FOREIGN_KEY_CHECKS = 1;
--
-- Table structure for table `user`
--
SET FOREIGN_KEY_CHECKS = 0;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
SET FOREIGN_KEY_CHECKS = 1;
--
-- Table structure for table `user_daily_plan`
--
SET FOREIGN_KEY_CHECKS = 0;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `user_daily_plan` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `user_itinerary_id` int NOT NULL,
  `hotel_id` int DEFAULT NULL,
  `city_id` int DEFAULT NULL,
  `day_number` int NOT NULL,
  `off_day` tinyint(1) DEFAULT '0',
  `activity_id` int DEFAULT NULL,
  `plane_ride_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `userId` (`user_id`),
  KEY `userItineraryId` (`user_itinerary_id`),
  KEY `hotelId` (`hotel_id`),
  KEY `cityId` (`city_id`),
  KEY `fk_userDailyPlan_planeRide` (`plane_ride_id`),
  KEY `user_activities_constraint` (`activity_id`),
  CONSTRAINT `fk_userDailyPlan_planeRide` FOREIGN KEY (`plane_ride_id`) REFERENCES `plane_ride` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `user_activities_constraint` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `user_daily_plan_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `user_daily_plan_ibfk_2` FOREIGN KEY (`user_itinerary_id`) REFERENCES `user_itinerary` (`id`),
  CONSTRAINT `user_daily_plan_ibfk_3` FOREIGN KEY (`hotel_id`) REFERENCES `hotel` (`id`),
  CONSTRAINT `user_daily_plan_ibfk_4` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
SET FOREIGN_KEY_CHECKS = 1;
--
-- Table structure for table `user_itinerary`
--
SET FOREIGN_KEY_CHECKS = 0;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `user_itinerary` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `starting_price` decimal(9,2) DEFAULT NULL,
  `total_duration` int DEFAULT NULL,
  `departure_city` varchar(255) NOT NULL,
  `itinerary_name` text,
  `number_of_adults` int NOT NULL,
  `number_of_kids` int NOT NULL DEFAULT '0',
  `booking_date` date DEFAULT NULL,
  `status` enum('PENDING','CONFIRMED','CANCELLED') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `userId` (`user_id`),
  CONSTRAINT `user_id_userItinerary` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
SET FOREIGN_KEY_CHECKS = 1;
--
-- Table structure for table `user_itinerary_draft`
--
SET FOREIGN_KEY_CHECKS = 0;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `user_itinerary_draft` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `duration` int DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `departure_city` varchar(255) DEFAULT NULL,
  `hotel_standing` int DEFAULT NULL,
  `number_adults` int DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `number_kids` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_userItineraryDraft_1_idx` (`user_id`),
  CONSTRAINT `fk_userItineraryDraft_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
SET FOREIGN_KEY_CHECKS = 1;
--
-- Table structure for table `user_itinerary_option`
--
SET FOREIGN_KEY_CHECKS = 0;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `user_itinerary_option` (
  `user_itinerary_id` int NOT NULL,
  `option_id` int NOT NULL,
  PRIMARY KEY (`user_itinerary_id`,`option_id`),
  KEY `optionId` (`option_id`),
  CONSTRAINT `user_itinerary_option_ibfk_1` FOREIGN KEY (`user_itinerary_id`) REFERENCES `user_itinerary` (`id`) ON DELETE CASCADE,
  CONSTRAINT `user_itinerary_option_ibfk_2` FOREIGN KEY (`option_id`) REFERENCES `options` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
SET FOREIGN_KEY_CHECKS = 1;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-09  1:38:56