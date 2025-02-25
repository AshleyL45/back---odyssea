-- MySQL dump 10.13  Distrib 8.0.36, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: odyssea_db
-- ------------------------------------------------------
-- Server version	8.0.41-0ubuntu0.24.04.1

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

--
-- Table structure for table `ItineraryFlight`
--

DROP TABLE IF EXISTS `ItineraryFlight`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ItineraryFlight` (
  `reservationId` int NOT NULL,
  `flightId` int NOT NULL,
  PRIMARY KEY (`reservationId`,`flightId`),
  KEY `flightId` (`flightId`),
  CONSTRAINT `ItineraryFlight_ibfk_1` FOREIGN KEY (`reservationId`) REFERENCES `reservation` (`id`),
  CONSTRAINT `ItineraryFlight_ibfk_2` FOREIGN KEY (`flightId`) REFERENCES `flight` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ItineraryFlight`
--

LOCK TABLES `ItineraryFlight` WRITE;
/*!40000 ALTER TABLE `ItineraryFlight` DISABLE KEYS */;
/*!40000 ALTER TABLE `ItineraryFlight` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activitiesPerDay`
--

DROP TABLE IF EXISTS `activitiesPerDay`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activitiesPerDay` (
  `itineraryStepId` int NOT NULL,
  `activityId` int NOT NULL,
  `dayNumber` int NOT NULL,
  KEY `itineraryStepId` (`itineraryStepId`),
  KEY `activityId` (`activityId`),
  CONSTRAINT `activitiesPerDay_ibfk_1` FOREIGN KEY (`itineraryStepId`) REFERENCES `itineraryStep` (`id`),
  CONSTRAINT `activitiesPerDay_ibfk_2` FOREIGN KEY (`activityId`) REFERENCES `activity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activitiesPerDay`
--

LOCK TABLES `activitiesPerDay` WRITE;
/*!40000 ALTER TABLE `activitiesPerDay` DISABLE KEYS */;
/*!40000 ALTER TABLE `activitiesPerDay` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity`
--

DROP TABLE IF EXISTS `activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cityId` int NOT NULL,
  `name` text NOT NULL,
  `type` text NOT NULL,
  `physicalEffort` varchar(255) NOT NULL,
  `duration` time NOT NULL,
  `description` text NOT NULL,
  `price` decimal(7,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `cityId` (`cityId`),
  CONSTRAINT `activity_ibfk_1` FOREIGN KEY (`cityId`) REFERENCES `city` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity`
--

LOCK TABLES `activity` WRITE;
/*!40000 ALTER TABLE `activity` DISABLE KEYS */;
/*!40000 ALTER TABLE `activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `article`
--

DROP TABLE IF EXISTS `article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `article` (
  `id` int NOT NULL AUTO_INCREMENT,
  `countryId` int NOT NULL,
  `title` varchar(255) NOT NULL,
  `intro_paragraph` text NOT NULL,
  `when_to_go_paragraph` text,
  `what_to_see_do_paragraph` text,
  `cultural_advice_paragraph` text,
  PRIMARY KEY (`id`),
  KEY `countryId` (`countryId`),
  CONSTRAINT `article_ibfk_1` FOREIGN KEY (`countryId`) REFERENCES `country` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article`
--

LOCK TABLES `article` WRITE;
/*!40000 ALTER TABLE `article` DISABLE KEYS */;
/*!40000 ALTER TABLE `article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `city`
--

DROP TABLE IF EXISTS `city`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `city` (
  `id` int NOT NULL AUTO_INCREMENT,
  `countryId` int NOT NULL,
  `name` varchar(255) NOT NULL,
  `iataCode` varchar(255) NOT NULL,
  `longitude` decimal(9,6) NOT NULL,
  `latitude` decimal(9,6) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniqueIata` (`iataCode`),
  KEY `countryId` (`countryId`),
  CONSTRAINT `city_ibfk_1` FOREIGN KEY (`countryId`) REFERENCES `country` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `city`
--

LOCK TABLES `city` WRITE;
/*!40000 ALTER TABLE `city` DISABLE KEYS */;
/*!40000 ALTER TABLE `city` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cityDistance`
--

DROP TABLE IF EXISTS `cityDistance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cityDistance` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fromCityId` int NOT NULL,
  `toCityId` int NOT NULL,
  `drivingDurationSeconds` int NOT NULL,
  `distanceKm` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_route` (`fromCityId`,`toCityId`),
  KEY `toCityId` (`toCityId`),
  CONSTRAINT `cityDistance_ibfk_1` FOREIGN KEY (`fromCityId`) REFERENCES `city` (`id`),
  CONSTRAINT `cityDistance_ibfk_2` FOREIGN KEY (`toCityId`) REFERENCES `city` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cityDistance`
--

LOCK TABLES `cityDistance` WRITE;
/*!40000 ALTER TABLE `cityDistance` DISABLE KEYS */;
/*!40000 ALTER TABLE `cityDistance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `country`
--

DROP TABLE IF EXISTS `country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `country` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `continent` varchar(255) NOT NULL,
  `price` decimal(7,2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `country`
--

LOCK TABLES `country` WRITE;
/*!40000 ALTER TABLE `country` DISABLE KEYS */;
/*!40000 ALTER TABLE `country` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flight`
--

DROP TABLE IF EXISTS `flight`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flight` (
  `id` int NOT NULL AUTO_INCREMENT,
  `companyName` varchar(255) DEFAULT NULL,
  `duration` time NOT NULL,
  `departureTime` time NOT NULL,
  `departureCityIata` varchar(255) NOT NULL,
  `arrivalTime` time NOT NULL,
  `arrivalCityIata` varchar(255) NOT NULL,
  `price` decimal(7,2) NOT NULL,
  `airplaneName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `departureCityIata` (`departureCityIata`),
  KEY `arrivalCityIata` (`arrivalCityIata`),
  CONSTRAINT `flight_ibfk_1` FOREIGN KEY (`departureCityIata`) REFERENCES `city` (`iataCode`),
  CONSTRAINT `flight_ibfk_2` FOREIGN KEY (`arrivalCityIata`) REFERENCES `city` (`iataCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flight`
--

LOCK TABLES `flight` WRITE;
/*!40000 ALTER TABLE `flight` DISABLE KEYS */;
/*!40000 ALTER TABLE `flight` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hotel`
--

DROP TABLE IF EXISTS `hotel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hotel` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cityId` int NOT NULL,
  `name` text NOT NULL,
  `starRating` int NOT NULL,
  `description` text NOT NULL,
  `price` decimal(7,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `cityId` (`cityId`),
  CONSTRAINT `hotel_ibfk_1` FOREIGN KEY (`cityId`) REFERENCES `city` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hotel`
--

LOCK TABLES `hotel` WRITE;
/*!40000 ALTER TABLE `hotel` DISABLE KEYS */;
/*!40000 ALTER TABLE `hotel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `images`
--

DROP TABLE IF EXISTS `images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `images` (
  `id` int NOT NULL AUTO_INCREMENT,
  `entityId` int NOT NULL,
  `entityType` varchar(255) NOT NULL,
  `sizeType` varchar(255) NOT NULL DEFAULT 'gallery',
  `link` text NOT NULL,
  `altText` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `images_chk_1` CHECK ((`entityType` in (_utf8mb4'article',_utf8mb4'itinerary',_utf8mb4'hotel'))),
  CONSTRAINT `images_chk_2` CHECK ((`sizeType` in (_utf8mb4'gallery',_utf8mb4'cover')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `images`
--

LOCK TABLES `images` WRITE;
/*!40000 ALTER TABLE `images` DISABLE KEYS */;
/*!40000 ALTER TABLE `images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `itinerary`
--

DROP TABLE IF EXISTS `itinerary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `itinerary` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` text NOT NULL,
  `description` text NOT NULL,
  `shortDescription` text NOT NULL,
  `stock` int NOT NULL,
  `price` decimal(7,2) DEFAULT NULL,
  `totalDuration` time NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `itinerary`
--

LOCK TABLES `itinerary` WRITE;
/*!40000 ALTER TABLE `itinerary` DISABLE KEYS */;
/*!40000 ALTER TABLE `itinerary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `itineraryOption`
--

DROP TABLE IF EXISTS `itineraryOption`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `itineraryOption` (
  `reservationId` int NOT NULL,
  `optionId` int NOT NULL,
  PRIMARY KEY (`reservationId`,`optionId`),
  KEY `optionId` (`optionId`),
  CONSTRAINT `itineraryOption_ibfk_1` FOREIGN KEY (`reservationId`) REFERENCES `reservation` (`id`),
  CONSTRAINT `itineraryOption_ibfk_2` FOREIGN KEY (`optionId`) REFERENCES `options` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `itineraryOption`
--

LOCK TABLES `itineraryOption` WRITE;
/*!40000 ALTER TABLE `itineraryOption` DISABLE KEYS */;
/*!40000 ALTER TABLE `itineraryOption` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `itineraryStep`
--

DROP TABLE IF EXISTS `itineraryStep`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `itineraryStep` (
  `id` int NOT NULL AUTO_INCREMENT,
  `itineraryId` int NOT NULL,
  `cityId` int NOT NULL,
  `countryId` int NOT NULL,
  `hotelId` int NOT NULL,
  `position` int NOT NULL,
  `dayNumber` int NOT NULL,
  `descriptionPerDay` text,
  PRIMARY KEY (`id`),
  KEY `itineraryId` (`itineraryId`),
  KEY `cityId` (`cityId`),
  KEY `countryId` (`countryId`),
  KEY `hotelId` (`hotelId`),
  CONSTRAINT `itineraryStep_ibfk_1` FOREIGN KEY (`itineraryId`) REFERENCES `itinerary` (`id`),
  CONSTRAINT `itineraryStep_ibfk_2` FOREIGN KEY (`cityId`) REFERENCES `city` (`id`),
  CONSTRAINT `itineraryStep_ibfk_3` FOREIGN KEY (`countryId`) REFERENCES `country` (`id`),
  CONSTRAINT `itineraryStep_ibfk_4` FOREIGN KEY (`hotelId`) REFERENCES `hotel` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `itineraryStep`
--

LOCK TABLES `itineraryStep` WRITE;
/*!40000 ALTER TABLE `itineraryStep` DISABLE KEYS */;
/*!40000 ALTER TABLE `itineraryStep` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mySelection`
--

DROP TABLE IF EXISTS `mySelection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mySelection` (
  `userId` int NOT NULL,
  `itineraryId` int NOT NULL,
  PRIMARY KEY (`userId`,`itineraryId`),
  KEY `itineraryId` (`itineraryId`),
  CONSTRAINT `mySelection_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`id`),
  CONSTRAINT `mySelection_ibfk_2` FOREIGN KEY (`itineraryId`) REFERENCES `itinerary` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mySelection`
--

LOCK TABLES `mySelection` WRITE;
/*!40000 ALTER TABLE `mySelection` DISABLE KEYS */;
/*!40000 ALTER TABLE `mySelection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `options`
--

DROP TABLE IF EXISTS `options`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `options` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `price` decimal(7,2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `options`
--

LOCK TABLES `options` WRITE;
/*!40000 ALTER TABLE `options` DISABLE KEYS */;
/*!40000 ALTER TABLE `options` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation` (
  `id` int NOT NULL AUTO_INCREMENT,
  `userId` int NOT NULL,
  `itineraryId` int NOT NULL,
  `status` varchar(255) NOT NULL,
  `departureDate` date NOT NULL,
  `returnDate` date NOT NULL,
  `numberOfPeople` int NOT NULL,
  `totalPrice` decimal(7,2) NOT NULL,
  `purchaseDate` date DEFAULT (curdate()),
  PRIMARY KEY (`id`),
  KEY `userId` (`userId`),
  KEY `itineraryId` (`itineraryId`),
  CONSTRAINT `reservation_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`id`),
  CONSTRAINT `reservation_ibfk_2` FOREIGN KEY (`itineraryId`) REFERENCES `itinerary` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation`
--

LOCK TABLES `reservation` WRITE;
/*!40000 ALTER TABLE `reservation` DISABLE KEYS */;
/*!40000 ALTER TABLE `reservation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `theme`
--

DROP TABLE IF EXISTS `theme`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `theme` (
  `id` int NOT NULL AUTO_INCREMENT,
  `itineraryId` int NOT NULL,
  `name` text NOT NULL,
  PRIMARY KEY (`id`),
  KEY `itineraryId` (`itineraryId`),
  CONSTRAINT `theme_ibfk_1` FOREIGN KEY (`itineraryId`) REFERENCES `itinerary` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `theme`
--

LOCK TABLES `theme` WRITE;
/*!40000 ALTER TABLE `theme` DISABLE KEYS */;
/*!40000 ALTER TABLE `theme` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userItinerary`
--

DROP TABLE IF EXISTS `userItinerary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `userItinerary` (
  `id` int NOT NULL AUTO_INCREMENT,
  `userId` int NOT NULL,
  `startDate` date NOT NULL,
  `endDate` date NOT NULL,
  `numberOfPeople` int NOT NULL,
  `startingPrice` decimal(7,2) DEFAULT NULL,
  `totalDuration` time NOT NULL,
  PRIMARY KEY (`id`),
  KEY `userId` (`userId`),
  CONSTRAINT `userItinerary_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userItinerary`
--

LOCK TABLES `userItinerary` WRITE;
/*!40000 ALTER TABLE `userItinerary` DISABLE KEYS */;
/*!40000 ALTER TABLE `userItinerary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userItineraryActivitiesPerDay`
--

DROP TABLE IF EXISTS `userItineraryActivitiesPerDay`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `userItineraryActivitiesPerDay` (
  `userItineraryStepId` int NOT NULL,
  `activityId` int NOT NULL,
  `dayNumber` int NOT NULL,
  PRIMARY KEY (`userItineraryStepId`,`activityId`,`dayNumber`),
  KEY `activityId` (`activityId`),
  CONSTRAINT `userItineraryActivitiesPerDay_ibfk_1` FOREIGN KEY (`userItineraryStepId`) REFERENCES `userItineraryStep` (`id`),
  CONSTRAINT `userItineraryActivitiesPerDay_ibfk_2` FOREIGN KEY (`activityId`) REFERENCES `activity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userItineraryActivitiesPerDay`
--

LOCK TABLES `userItineraryActivitiesPerDay` WRITE;
/*!40000 ALTER TABLE `userItineraryActivitiesPerDay` DISABLE KEYS */;
/*!40000 ALTER TABLE `userItineraryActivitiesPerDay` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userItineraryFlight`
--

DROP TABLE IF EXISTS `userItineraryFlight`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `userItineraryFlight` (
  `userItineraryId` int NOT NULL,
  `flightId` int NOT NULL,
  PRIMARY KEY (`userItineraryId`,`flightId`),
  KEY `flightId` (`flightId`),
  CONSTRAINT `userItineraryFlight_ibfk_1` FOREIGN KEY (`userItineraryId`) REFERENCES `userItinerary` (`id`),
  CONSTRAINT `userItineraryFlight_ibfk_2` FOREIGN KEY (`flightId`) REFERENCES `flight` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userItineraryFlight`
--

LOCK TABLES `userItineraryFlight` WRITE;
/*!40000 ALTER TABLE `userItineraryFlight` DISABLE KEYS */;
/*!40000 ALTER TABLE `userItineraryFlight` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userItineraryOption`
--

DROP TABLE IF EXISTS `userItineraryOption`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `userItineraryOption` (
  `userItineraryId` int NOT NULL,
  `optionId` int NOT NULL,
  PRIMARY KEY (`userItineraryId`,`optionId`),
  KEY `optionId` (`optionId`),
  CONSTRAINT `userItineraryOption_ibfk_1` FOREIGN KEY (`userItineraryId`) REFERENCES `userItinerary` (`id`),
  CONSTRAINT `userItineraryOption_ibfk_2` FOREIGN KEY (`optionId`) REFERENCES `options` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userItineraryOption`
--

LOCK TABLES `userItineraryOption` WRITE;
/*!40000 ALTER TABLE `userItineraryOption` DISABLE KEYS */;
/*!40000 ALTER TABLE `userItineraryOption` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userItineraryStep`
--

DROP TABLE IF EXISTS `userItineraryStep`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `userItineraryStep` (
  `id` int NOT NULL AUTO_INCREMENT,
  `userId` int NOT NULL,
  `userItineraryId` int NOT NULL,
  `hotelId` int NOT NULL,
  `cityId` int NOT NULL,
  `position` int NOT NULL,
  `dayNumber` int NOT NULL,
  `offDay` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `userId` (`userId`),
  KEY `userItineraryId` (`userItineraryId`),
  KEY `hotelId` (`hotelId`),
  KEY `cityId` (`cityId`),
  CONSTRAINT `userItineraryStep_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`id`),
  CONSTRAINT `userItineraryStep_ibfk_2` FOREIGN KEY (`userItineraryId`) REFERENCES `userItinerary` (`id`),
  CONSTRAINT `userItineraryStep_ibfk_3` FOREIGN KEY (`hotelId`) REFERENCES `hotel` (`id`),
  CONSTRAINT `userItineraryStep_ibfk_4` FOREIGN KEY (`cityId`) REFERENCES `city` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userItineraryStep`
--

LOCK TABLES `userItineraryStep` WRITE;
/*!40000 ALTER TABLE `userItineraryStep` DISABLE KEYS */;
/*!40000 ALTER TABLE `userItineraryStep` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-25 12:19:11
