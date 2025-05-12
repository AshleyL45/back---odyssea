USE odyssea_db;
TRUNCATE TABLE itineraryImages;

-- (Optionnel) TRUNCATE TABLE images; ALTER TABLE images AUTO_INCREMENT = 1;

-- firstHeader → rôle firstHeader
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'firstHeader', '', 'firstHeader pour itinerary 1', NULL, NULL, 1, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary1/brian-lundquist-TxME_LN0o0c-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (1, @imgId, 'firstHeader');

-- secondHeader → rôle secondHeader
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'secondHeader', '', 'secondHeader pour itinerary 1', NULL, NULL, 1, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary1/jorge-fernandez-salas-ChSZETOal-I-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (1, @imgId, 'secondHeader');

-- firstCountry → rôle firstCountry
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'firstCountry', '', 'firstCountry pour itinerary 1', NULL, NULL, 1, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary1/alexander-awerin-VIzeB9tB-J0-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (1, @imgId, 'firstCountry');

-- secondCountry → rôle secondCountry
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'secondCountry', '', 'secondCountry pour itinerary 1', NULL, NULL, 1, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary1/brian-lundquist-TxME_LN0o0c-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (1, @imgId, 'secondCountry');

-- thirdCountry → rôle thirdCountry
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'thirdCountry', '', 'thirdCountry pour itinerary 1', NULL, NULL, 1, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary1/grant-cai-9xjdQ8-zLKI-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (1, @imgId, 'thirdCountry');

-- day1_1 → rôle day1
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day1', '', 'day1 pour itinerary 1', NULL, NULL, 1, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary1/luis-quintero-jKTCVwtltYQ-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (1, @imgId, 'day1');

-- day2_1 → rôle day2
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day2', '', 'day2 pour itinerary 1', NULL, NULL, 1, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary1/amy-gatenby-1pJvAx76FmA-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (1, @imgId, 'day2');

-- day3_1 → rôle day3
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day3', '', 'day3 pour itinerary 1', NULL, NULL, 1, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary1/vicente-perez-olivares-n6PaDYWymto-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (1, @imgId, 'day3');

-- day4_1 → rôle day4
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day4', '', 'day4 pour itinerary 1', NULL, NULL, 1, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary1/kaitlin-dowis-VjM2t7VH9Uo-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (1, @imgId, 'day4');

-- day5_1 → rôle day5
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day5', '', 'day5 pour itinerary 1', NULL, NULL, 1, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary1/antonio-cuellar-KDS5lCrj_ew-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (1, @imgId, 'day5');

-- day6_1 → rôle day6
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day6', '', 'day6 pour itinerary 1', NULL, NULL, 1, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary1/jean-luc-benazet-tqR4EzIwtLM-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (1, @imgId, 'day6');

-- day7_1 → rôle day7
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day7', '', 'day7 pour itinerary 1', NULL, NULL, 1, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary1/kian-lem-fNNlAxGMVMw-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (1, @imgId, 'day7');

-- day8_1 → rôle day8
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day8', '', 'day8 pour itinerary 1', NULL, NULL, 1, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary1/luke-moss-gjT-kQbUK5I-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (1, @imgId, 'day8');

-- day9_1 → rôle day9
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day9', '', 'day9 pour itinerary 1', NULL, NULL, 1, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary1/amit-lahav-HffApi3okak-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (1, @imgId, 'day9');

-- day10_1 → rôle day10
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day10', '', 'day10 pour itinerary 1', NULL, NULL, 1, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary1/meg-von-haartman.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (1, @imgId, 'day10');

-- day11_1 → rôle day11
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day11', '', 'day11 pour itinerary 1', NULL, NULL, 1, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary1/tobias-smietana-vryVT4TP42k-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (1, @imgId, 'day11');

-- day12_1 → rôle day12
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day12', '', 'day12 pour itinerary 1', NULL, NULL, 1, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary1/nobu-las-vegas.webp'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (1, @imgId, 'day12');

-- firstHeaderImage2 → rôle firstHeader
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'firstHeader', '', 'firstHeader pour itinerary 2', NULL, NULL, 2, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary2/aj-robbie-BuQ1RZckYW4-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (2, @imgId, 'firstHeader');

-- secondHeaderImage22 → rôle secondHeader
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'secondHeader', '', 'secondHeader pour itinerary 2', NULL, NULL, 2, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary2/bert-b-iEh0ljA7GNM-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (2, @imgId, 'secondHeader');

-- firstCountry2 → rôle firstCountry
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'firstCountry', '', 'firstCountry pour itinerary 2', NULL, NULL, 2, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary2/magdalena-kula-manchee-gYURRvBM5JQ-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (2, @imgId, 'firstCountry');

-- secondCountry2 → rôle secondCountry
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'secondCountry', '', 'secondCountry pour itinerary 2', NULL, NULL, 2, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary2/kyle-philip-coulson-PoRBsATX8Es-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (2, @imgId, 'secondCountry');

-- thirdCountry2 → rôle thirdCountry
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'thirdCountry', '', 'thirdCountry pour itinerary 2', NULL, NULL, 2, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary2/leon-pauleikhoff-OkY3x_09CxQ-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (2, @imgId, 'thirdCountry');

-- day1_2 → rôle day1
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day1', '', 'day1 pour itinerary 2', NULL, NULL, 2, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary2/sheila-c-oPjTDB-kQlQ-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (2, @imgId, 'day1');

-- day2_2 → rôle day2
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day2', '', 'day2 pour itinerary 2', NULL, NULL, 2, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary2/pexels-dibert-1300960.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (2, @imgId, 'day2');

-- day3_2 → rôle day3
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day3', '', 'day3 pour itinerary 2', NULL, NULL, 2, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary2/pexels-droneafrica-13338819.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (2, @imgId, 'day3');

-- day4_2 → rôle day4
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day4', '', 'day4 pour itinerary 2', NULL, NULL, 2, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary2/pexels-kureng-workx-2546437-7637397.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (2, @imgId, 'day4');

-- day5_2 → rôle day5
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day5', '', 'day5 pour itinerary 2', NULL, NULL, 2, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary2/pexels-clement-proust-363898785-31262340.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (2, @imgId, 'day5');

-- day6_2 → rôle day6
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day6', '', 'day6 pour itinerary 2', NULL, NULL, 2, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary2/pexels-toni-18189037.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (2, @imgId, 'day6');

-- day7_2 → rôle day7
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day7', '', 'day7 pour itinerary 2', NULL, NULL, 2, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary2/pexels-alex-staudinger-829197-1731834.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (2, @imgId, 'day7');

-- day8_2 → rôle day8
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day8', '', 'day8 pour itinerary 2', NULL, NULL, 2, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary2/samson-tarimo-pxeA-AaEcCA-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (2, @imgId, 'day8');

-- day9_2 → rôle day9
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day9', '', 'day9 pour itinerary 2', NULL, NULL, 2, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary2/crispin-jones-DDEBAl7ULAo-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (2, @imgId, 'day9');

-- day10_2 → rôle day10
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day10', '', 'day10 pour itinerary 2', NULL, NULL, 2, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary2/ben-preater-uCtcE2T3jpQ-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (2, @imgId, 'day10');

-- day11_2 → rôle day11
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day11', '', 'day11 pour itinerary 2', NULL, NULL, 2, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary2/wynand-uys-4ZCA3xukIso-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (2, @imgId, 'day11');

-- day12_2 → rôle day12
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day12', '', 'day12 pour itinerary 2', NULL, NULL, 2, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary2/jose-mizrahi-mGmo5XEsw4M-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (2, @imgId, 'day12');

-- firstHeaderImage3 → rôle firstHeader
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'firstHeader', '', 'firstHeader pour itinerary 3', NULL, NULL, 3, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary3/luka-odak-vqBajUZQfTE-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (3, @imgId, 'firstHeader');

-- secondHeaderImage3 → rôle secondHeader
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'secondHeader', '', 'secondHeader pour itinerary 3', NULL, NULL, 3, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary3/tomas-gonzalez-de-rosenzweig-tF0izq7URoo-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (3, @imgId, 'secondHeader');

-- firstCountry3 → rôle firstCountry
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'firstCountry', '', 'firstCountry pour itinerary 3', NULL, NULL, 3, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary3/hugues-de-buyer-mimeure-oDcbtrD3Rrc-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (3, @imgId, 'firstCountry');

-- secondCountry3 → rôle secondCountry
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'secondCountry', '', 'secondCountry pour itinerary 3', NULL, NULL, 3, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary3/chris-l-dGogbPrvA-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (3, @imgId, 'secondCountry');

-- thirdCountry3 → rôle thirdCountry
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'thirdCountry', '', 'thirdCountry pour itinerary 3', NULL, NULL, 3, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary3/patrick-tomasso-z8wto8Ugnc4-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (3, @imgId, 'thirdCountry');

-- day1_3 → rôle day1
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day1', '', 'day1 pour itinerary 3', NULL, NULL, 3, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary3/qui-nguyen-0G01UI1MQhg-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (3, @imgId, 'day1');

-- day2_3 → rôle day2
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day2', '', 'day2 pour itinerary 3', NULL, NULL, 3, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary3/clem-onojeghuo-ruC8uLWQI7c-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (3, @imgId, 'day2');

-- day3_3 → rôle day3
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day3', '', 'day3 pour itinerary 3', NULL, NULL, 3, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary3/renato-marzan-WlP9h7kDRLo-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (3, @imgId, 'day3');

-- day4_3 → rôle day4
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day4', '', 'day4 pour itinerary 3', NULL, NULL, 3, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary3/rinald-rolle-Yy9M-zqMjhg-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (3, @imgId, 'day4');

-- day5_3 → rôle day5
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day5', '', 'day5 pour itinerary 3', NULL, NULL, 3, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary3/mike-swigunski-3E2zM9IF-bU-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (3, @imgId, 'day5');

-- day6_3 → rôle day6
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day6', '', 'day6 pour itinerary 3', NULL, NULL, 3, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary3/brice-cooper-KVKlIqFhvTM-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (3, @imgId, 'day6');

-- day7_3 → rôle day7
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day7', '', 'day7 pour itinerary 3', NULL, NULL, 3, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary3/huum-NHLS5hOSH0c-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (3, @imgId, 'day7');

-- day8_3 → rôle day8
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day8', '', 'day8 pour itinerary 3', NULL, NULL, 3, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary3/recal-media-ueBIGLmiI5A-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (3, @imgId, 'day8');

-- day9_3 → rôle day9
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day9', '', 'day9 pour itinerary 3', NULL, NULL, 3, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary3/pawel-wieladek-S8Dsapk7C8k-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (3, @imgId, 'day9');

-- day10_3 → rôle day10
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day10', '', 'day10 pour itinerary 3', NULL, NULL, 3, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary3/reid-naaykens-RGRNQWPqOVw-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (3, @imgId, 'day10');

-- day11_3 → rôle day11
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day11', '', 'day11 pour itinerary 3', NULL, NULL, 3, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary3/secret-travel-guide-464cs1cqar4-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (3, @imgId, 'day11');

-- day12_3 → rôle day12
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day12', '', 'day12 pour itinerary 3', NULL, NULL, 3, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary3/secret-travel-guide-464cs1cqar4-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (3, @imgId, 'day12');

-- firstHeaderImage4 → rôle firstHeader
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'firstHeader', '', 'firstHeader pour itinerary 4', NULL, NULL, 4, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary4/brian-mcgowan-dfQL08rPYnM-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (4, @imgId, 'firstHeader');

-- secondHeaderImage4 → rôle secondHeader
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'secondHeader', '', 'secondHeader pour itinerary 4', NULL, NULL, 4, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary4/john-lee-oMneOBYhJxY-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (4, @imgId, 'secondHeader');

-- firstCountry4 → rôle firstCountry
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'firstCountry', '', 'firstCountry pour itinerary 4', NULL, NULL, 4, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary4/henning-witzel-ukvgqriuOgo-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (4, @imgId, 'firstCountry');

-- secondCountry4 → rôle secondCountry
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'secondCountry', '', 'secondCountry pour itinerary 4', NULL, NULL, 4, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary4/kyler-nixon-_ZBekGTBh-c-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (4, @imgId, 'secondCountry');

-- thirdCountry4 → rôle thirdCountry
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'thirdCountry', '', 'thirdCountry pour itinerary 4', NULL, NULL, 4, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary4/andreas-m-88intec2Q5Q-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (4, @imgId, 'thirdCountry');

-- day1_4 → rôle day1
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day1', '', 'day1 pour itinerary 4', NULL, NULL, 4, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary4/capricorn-song-BLYq8Qk8Q98-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (4, @imgId, 'day1');

-- day2_4 → rôle day2
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day2', '', 'day2 pour itinerary 4', NULL, NULL, 4, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary4/aditya-vyas-HCKVHAEbkus-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (4, @imgId, 'day2');

-- day3_4 → rôle day3
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day3', '', 'day3 pour itinerary 4', NULL, NULL, 4, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary4/vincentas-liskauskas-ErMkvcFla74-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (4, @imgId, 'day3');

-- day4_4 → rôle day4
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day4', '', 'day4 pour itinerary 4', NULL, NULL, 4, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary4/gerson-repreza-Y7umJ1i_qgw-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (4, @imgId, 'day4');

-- day5_4 → rôle day5
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day5', '', 'day5 pour itinerary 4', NULL, NULL, 4, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary4/dustin-bowdige-cGPJEG9yAM8-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (4, @imgId, 'day5');

-- day6_4 → rôle day6
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day6', '', 'day6 pour itinerary 4', NULL, NULL, 4, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary4/GM65935-1440x960.webp'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (4, @imgId, 'day6');

-- day7_4 → rôle day7
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day7', '', 'day7 pour itinerary 4', NULL, NULL, 4, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary4/Heritage-Park-Calgary.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (4, @imgId, 'day7');

-- day8_4 → rôle day8
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day8', '', 'day8 pour itinerary 4', NULL, NULL, 4, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary4/patrick-mcvey--rjc29yPMRk-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (4, @imgId, 'day8');

-- day9_4 → rôle day9
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day9', '', 'day9 pour itinerary 4', NULL, NULL, 4, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary4/mario-la-pergola-huMh6cfhl_o-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (4, @imgId, 'day9');

-- day10_4 → rôle day10
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day10', '', 'day10 pour itinerary 4', NULL, NULL, 4, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary4/leo-escala-N8R2mhCuWfk-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (4, @imgId, 'day10');

-- day11_4 → rôle day11
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day11', '', 'day11 pour itinerary 4', NULL, NULL, 4, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary4/jose-vazquez-db0yt-Qgifw-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (4, @imgId, 'day11');

-- day12_4 → rôle day12
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day12', '', 'day12 pour itinerary 4', NULL, NULL, 4, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary4/dan-aragon-j9TeJl5gPvs-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (4, @imgId, 'day12');

-- firstHeaderImage5 → rôle firstHeader
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'firstHeader', '', 'firstHeader pour itinerary 5', NULL, NULL, 5, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary5/jacques-bopp-IGMABomGhr8-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (5, @imgId, 'firstHeader');

-- secondHeaderImage5 → rôle secondHeader
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'secondHeader', '', 'secondHeader pour itinerary 5', NULL, NULL, 5, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary5/nicholas-doherty-iJdFAqCVvP4-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (5, @imgId, 'secondHeader');

-- firstCountry5 → rôle firstCountry
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'firstCountry', '', 'firstCountry pour itinerary 5', NULL, NULL, 5, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary5/aaron-sebastian-bfgEYpS0Znk-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (5, @imgId, 'firstCountry');

-- secondCountry5 → rôle secondCountry
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'secondCountry', '', 'secondCountry pour itinerary 5', NULL, NULL, 5, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary5/david-clode-CIMk0FSOrAE-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (5, @imgId, 'secondCountry');

-- thirdCountry5 → rôle thirdCountry
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'thirdCountry', '', 'thirdCountry pour itinerary 5', NULL, NULL, 5, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary5/dean-mcquade-6QG53_QOtqM-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (5, @imgId, 'thirdCountry');

-- day1_5 → rôle day1
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day1', '', 'day1 pour itinerary 5', NULL, NULL, 5, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary5/laura-cros-xLvkzpsKIms-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (5, @imgId, 'day1');

-- day2_5 → rôle day2
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day2', '', 'day2 pour itinerary 5', NULL, NULL, 5, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary5/tyler-duston-ZXlfq5mExMs-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (5, @imgId, 'day2');

-- day3_5 → rôle day3
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day3', '', 'day3 pour itinerary 5', NULL, NULL, 5, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary5/antoine-barres-Abgd4mg9_W4-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (5, @imgId, 'day3');

-- day4_5 → rôle day4
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day4', '', 'day4 pour itinerary 5', NULL, NULL, 5, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary5/padraig-treanor-F2LC2eh4TCg-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (5, @imgId, 'day4');

-- day5_5 → rôle day5
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day5', '', 'day5 pour itinerary 5', NULL, NULL, 5, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary5/michael-amadeus-W47UMydgshw-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (5, @imgId, 'day5');

-- day6_5 → rôle day6
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day6', '', 'day6 pour itinerary 5', NULL, NULL, 5, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary5/tanya-guillory-ntyNGuggurI-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (5, @imgId, 'day6');

-- day7_5 → rôle day7
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day7', '', 'day7 pour itinerary 5', NULL, NULL, 5, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary5/toby-hall-vvuVkty8u84-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (5, @imgId, 'day7');

-- day8_5 → rôle day8
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day8', '', 'day8 pour itinerary 5', NULL, NULL, 5, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary5/99.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (5, @imgId, 'day8');

-- day9_5 → rôle day9
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day9', '', 'day9 pour itinerary 5', NULL, NULL, 5, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary5/24902-Mamanuca-Islands.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (5, @imgId, 'day9');

-- day10_5 → rôle day10
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day10', '', 'day10 pour itinerary 5', NULL, NULL, 5, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary5/josh-withers-IR78MAOzosg-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (5, @imgId, 'day10');

-- day11_5 → rôle day11
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day11', '', 'day11 pour itinerary 5', NULL, NULL, 5, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary5/gary-runn-V0EwMp_o6_Y-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (5, @imgId, 'day11');

-- day12_5 → rôle day12
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day12', '', 'day12 pour itinerary 5', NULL, NULL, 5, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary5/siray-li-oO5ETS8PKCA-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (5, @imgId, 'day12');

-- firstHeaderImage6 → rôle firstHeader
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'firstHeader', '', 'firstHeader pour itinerary 6', NULL, NULL, 6, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary6/elijah-g-oOHHxQ65dFE-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (6, @imgId, 'firstHeader');

-- secondHeaderImage6 → rôle secondHeader
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'secondHeader', '', 'secondHeader pour itinerary 6', NULL, NULL, 6, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary6/pexels-dave-frisch-731685-1575938.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (6, @imgId, 'secondHeader');

-- firstCountry6 → rôle firstCountry
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'firstCountry', '', 'firstCountry pour itinerary 6', NULL, NULL, 6, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary6/pexels-silviopelegrin-29732987.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (6, @imgId, 'firstCountry');

-- secondCountry6 → rôle secondCountry
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'secondCountry', '', 'secondCountry pour itinerary 6', NULL, NULL, 6, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary6/pexels-hebaysal-773471.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (6, @imgId, 'secondCountry');

-- thirdCountry6 → rôle thirdCountry
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'thirdCountry', '', 'thirdCountry pour itinerary 6', NULL, NULL, 6, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary6/pexels-pixabay-161074.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (6, @imgId, 'thirdCountry');

-- day1_6 → rôle day1
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day1', '', 'day1 pour itinerary 6', NULL, NULL, 6, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary6/alisa-anton-WzEcRMJOhKQ-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (6, @imgId, 'day1');

-- day2_6 → rôle day2
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day2', '', 'day2 pour itinerary 6', NULL, NULL, 6, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary6/miguel-alcantara-6Pz9uyP5d0I-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (6, @imgId, 'day2');

-- day3_6 → rôle day3
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day3', '', 'day3 pour itinerary 6', NULL, NULL, 6, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary6/tatiana-tochilova-0T76ohdLpjI-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (6, @imgId, 'day3');

-- day4_6 → rôle day4
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day4', '', 'day4 pour itinerary 6', NULL, NULL, 6, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary6/henrique-ferreira-6p-I-X-sPUY-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (6, @imgId, 'day4');

-- day5_6 → rôle day5
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day5', '', 'day5 pour itinerary 6', NULL, NULL, 6, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary6/marco-meyer-eAAjKAGEKmI-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (6, @imgId, 'day5');

-- day6_6 → rôle day6
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day6', '', 'day6 pour itinerary 6', NULL, NULL, 6, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary6/dylan-shaw-stVUAS-JZzY-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (6, @imgId, 'day6');

-- day7_6 → rôle day7
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day7', '', 'day7 pour itinerary 6', NULL, NULL, 6, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary6/henrique-ferreira-hJ4NTKWxsv8-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (6, @imgId, 'day7');

-- day8_6 → rôle day8
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day8', '', 'day8 pour itinerary 6', NULL, NULL, 6, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary6/mark-konig-xCEuxxhpY3o-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (6, @imgId, 'day8');

-- day9_6 → rôle day9
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day9', '', 'day9 pour itinerary 6', NULL, NULL, 6, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary6/fionn-grosse-BjFO_5ISjw4-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (6, @imgId, 'day9');

-- day10_6 → rôle day10
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day10', '', 'day10 pour itinerary 6', NULL, NULL, 6, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary6/maximalfocus-iYVw6_h6gMk-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (6, @imgId, 'day10');

-- day11_6 → rôle day11
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day11', '', 'day11 pour itinerary 6', NULL, NULL, 6, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary6/mike-kotsch-9wTWFyInJ4Y-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (6, @imgId, 'day11');

-- day12_6 → rôle day12
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day12', '', 'day12 pour itinerary 6', NULL, NULL, 6, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary6/svetlana-gumerova-Mp2Nq2ZK6FA-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (6, @imgId, 'day12');

-- firstHeaderImage7 → rôle firstHeader
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'firstHeader', '', 'firstHeader pour itinerary 7', NULL, NULL, 7, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary7/pexels-fabio2311-712392.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (7, @imgId, 'firstHeader');

-- secondHeaderImage7 → rôle secondHeader
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'secondHeader', '', 'secondHeader pour itinerary 7', NULL, NULL, 7, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary7/pexels-apasaric-2067048.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (7, @imgId, 'secondHeader');

-- firstCountry7 → rôle firstCountry
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'firstCountry', '', 'firstCountry pour itinerary 7', NULL, NULL, 7, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary7/pexels-jimmy-liao-3615017-11387299.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (7, @imgId, 'firstCountry');

-- secondCountry7 → rôle secondCountry
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'secondCountry', '', 'secondCountry pour itinerary 7', NULL, NULL, 7, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary7/pexels-apasaric-2044434.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (7, @imgId, 'secondCountry');

-- thirdCountry7 → rôle thirdCountry
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'thirdCountry', '', 'thirdCountry pour itinerary 7', NULL, NULL, 7, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary7/pexels-apasaric-2506923.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (7, @imgId, 'thirdCountry');

-- day1_7 → rôle day1
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day1', '', 'day1 pour itinerary 7', NULL, NULL, 7, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary7/ryoji-iwata-ztHc0NRbf9c-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (7, @imgId, 'day1');

-- day2_7 → rôle day2
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day2', '', 'day2 pour itinerary 7', NULL, NULL, 7, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary7/noah-holm-yOUawzzorcQ-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (7, @imgId, 'day2');

-- day3_7 → rôle day3
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day3', '', 'day3 pour itinerary 7', NULL, NULL, 7, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary7/thyla-jane-q_LrF-o3XZ0-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (7, @imgId, 'day3');

-- day4_7 → rôle day4
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day4', '', 'day4 pour itinerary 7', NULL, NULL, 7, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary7/dominic-kurniawan-suryaputra-oPqECkxRz1U-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (7, @imgId, 'day4');

-- day5_7 → rôle day5
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day5', '', 'day5 pour itinerary 7', NULL, NULL, 7, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary7/edward-he-uKyzXEc2k_s-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (7, @imgId, 'day5');

-- day6_7 → rôle day6
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day6', '', 'day6 pour itinerary 7', NULL, NULL, 7, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary7/harry-jing-V_cLRs1D8EE-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (7, @imgId, 'day6');

-- day7_7 → rôle day7
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day7', '', 'day7 pour itinerary 7', NULL, NULL, 7, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary7/nick-fewings-4PDWwUD6g_4-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (7, @imgId, 'day7');

-- day8_7 → rôle day8
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day8', '', 'day8 pour itinerary 7', NULL, NULL, 7, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary7/alina-grubnyak-nEENSEbLIsY-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (7, @imgId, 'day8');

-- day9_7 → rôle day9
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day9', '', 'day9 pour itinerary 7', NULL, NULL, 7, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary7/rushikesh-patil-_dyC1EBlDV8-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (7, @imgId, 'day9');

-- day10_7 → rôle day10
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day10', '', 'day10 pour itinerary 7', NULL, NULL, 7, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary7/hu-chen-__cBlRzLSTg-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (7, @imgId, 'day10');

-- day11_7 → rôle day11
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day11', '', 'day11 pour itinerary 7', NULL, NULL, 7, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary7/jezael-melgoza-alY6_OpdwRQ-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (7, @imgId, 'day11');

-- day12_7 → rôle day12
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day12', '', 'day12 pour itinerary 7', NULL, NULL, 7, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary7/pexels-mikhail-nilov-8319460.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (7, @imgId, 'day12');

-- firstHeaderImage8 → rôle firstHeader
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'firstHeader', '', 'firstHeader pour itinerary 8', NULL, NULL, 8, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary8/christina-victoria-craft-DFfNVwhOIWQ-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (8, @imgId, 'firstHeader');

-- secondHeaderImage8 → rôle secondHeader
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'secondHeader', '', 'secondHeader pour itinerary 8', NULL, NULL, 8, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary8/tobias-reich--7ZwuyDx2rI-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (8, @imgId, 'secondHeader');

-- firstCountry8 → rôle firstCountry
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'firstCountry', '', 'firstCountry pour itinerary 8', NULL, NULL, 8, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary8/pexels-myersmc16-29943339.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (8, @imgId, 'firstCountry');

-- secondCountry8 → rôle secondCountry
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'secondCountry', '', 'secondCountry pour itinerary 8', NULL, NULL, 8, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary8/pexels-lastly-572689.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (8, @imgId, 'secondCountry');

-- thirdCountry8 → rôle thirdCountry
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'thirdCountry', '', 'thirdCountry pour itinerary 8', NULL, NULL, 8, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary8/pexels-taryn-elliott-5271535.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (8, @imgId, 'thirdCountry');

-- day1_8 → rôle day1
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day1', '', 'day1 pour itinerary 8', NULL, NULL, 8, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary8/outward-bound-costa-rica-LGb6GWmee1U-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (8, @imgId, 'day1');

-- day2_8 → rôle day2
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day2', '', 'day2 pour itinerary 8', NULL, NULL, 8, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary8/ruedi-haberli-YXu-QYQhQwQ-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (8, @imgId, 'day2');

-- day3_8 → rôle day3
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day3', '', 'day3 pour itinerary 8', NULL, NULL, 8, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary8/kevin-rodriguez-NyRmxSkG-Yw-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (8, @imgId, 'day3');

-- day4_8 → rôle day4
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day4', '', 'day4 pour itinerary 8', NULL, NULL, 8, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary8/cesar-andres-parraga--Od5KyCfGE4-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (8, @imgId, 'day4');

-- day5_8 → rôle day5
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day5', '', 'day5 pour itinerary 8', NULL, NULL, 8, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary8/gerald-schombs-GBDkr3k96DE-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (8, @imgId, 'day5');

-- day6_8 → rôle day6
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day6', '', 'day6 pour itinerary 8', NULL, NULL, 8, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary8/rutendo-petros-Bmy54VG1j3A-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (8, @imgId, 'day6');

-- day7_8 → rôle day7
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day7', '', 'day7 pour itinerary 8', NULL, NULL, 8, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary8/arthur-hickinbotham-JbCdsqF9Di4-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (8, @imgId, 'day7');

-- day8_8 → rôle day8
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day8', '', 'day8 pour itinerary 8', NULL, NULL, 8, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary8/thomas-schaefer-ApoYbda6_3Y-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (8, @imgId, 'day8');

-- day9_8 → rôle day9
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day9', '', 'day9 pour itinerary 8', NULL, NULL, 8, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary8/anoof-junaid-J16ep2LfHwY-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (8, @imgId, 'day9');

-- day10_8 → rôle day10
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day10', '', 'day10 pour itinerary 8', NULL, NULL, 8, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary8/toby-hall-vvuVkty8u84-unsplash (1).jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (8, @imgId, 'day10');

-- day11_8 → rôle day11
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day11', '', 'day11 pour itinerary 8', NULL, NULL, 8, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary8/kamil-pietrzak-G0FsO2Ca8nQ-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (8, @imgId, 'day11');

-- day12_8 → rôle day12
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day12', '', 'day12 pour itinerary 8', NULL, NULL, 8, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary8/toomas-tartes-Yizrl9N_eDA-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (8, @imgId, 'day12');

-- firstHeaderImage9 → rôle firstHeader
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'firstHeader', '', 'firstHeader pour itinerary 9', NULL, NULL, 9, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary9/yang-yang-UfCJ2WrdTBw-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (9, @imgId, 'firstHeader');

-- secondHeaderImage9 → rôle secondHeader
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'secondHeader', '', 'secondHeader pour itinerary 9', NULL, NULL, 9, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary9/chris-8iCdRKKoG7g-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (9, @imgId, 'secondHeader');

-- firstCountry9 → rôle firstCountry
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'firstCountry', '', 'firstCountry pour itinerary 9', NULL, NULL, 9, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary9/pexels-pixabay-259585.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (9, @imgId, 'firstCountry');

-- secondCountry9 → rôle secondCountry
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'secondCountry', '', 'secondCountry pour itinerary 9', NULL, NULL, 9, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary9/pexels-murielfiona-58865367-7946518.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (9, @imgId, 'secondCountry');

-- thirdCountry9 → rôle thirdCountry
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'thirdCountry', '', 'thirdCountry pour itinerary 9', NULL, NULL, 9, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary9/pexels-pixabay-208444.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (9, @imgId, 'thirdCountry');

-- day1_9 → rôle day1
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day1', '', 'day1 pour itinerary 9', NULL, NULL, 9, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary9/jessica-pamp-Qsc-ewfUJEs-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (9, @imgId, 'day1');

-- day2_9 → rôle day2
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day2', '', 'day2 pour itinerary 9', NULL, NULL, 9, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary9/datingscout-lDfnGWcvtbY-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (9, @imgId, 'day2');

-- day3_9 → rôle day3
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day3', '', 'day3 pour itinerary 9', NULL, NULL, 9, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary9/jared-rice-PibraWHb4h8-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (9, @imgId, 'day3');

-- day4_9 → rôle day4
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day4', '', 'day4 pour itinerary 9', NULL, NULL, 9, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary9/james-ting-6N2mSJsKTtA-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (9, @imgId, 'day4');

-- day5_9 → rôle day5
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day5', '', 'day5 pour itinerary 9', NULL, NULL, 9, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary9/igor-ladigin-KDWcqPWGpKw-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (9, @imgId, 'day5');

-- day6_9 → rôle day6
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day6', '', 'day6 pour itinerary 9', NULL, NULL, 9, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary9/anthony-delanoix-vmrCxMRdq58-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (9, @imgId, 'day6');

-- day7_9 → rôle day7
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day7', '', 'day7 pour itinerary 9', NULL, NULL, 9, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary9/kirk-photographer-lTrtVpPKj-c-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (9, @imgId, 'day7');

-- day8_9 → rôle day8
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day8', '', 'day8 pour itinerary 9', NULL, NULL, 9, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary9/miltiadis-fragkidis-2Fbn7JWAZkc-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (9, @imgId, 'day8');

-- day9_9 → rôle day9
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day9', '', 'day9 pour itinerary 9', NULL, NULL, 9, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary9/louis-hansel-mVZ_gjm_TOk-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (9, @imgId, 'day9');

-- day10_9 → rôle day10
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day10', '', 'day10 pour itinerary 9', NULL, NULL, 9, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary9/antonio-araujo-n8pfsDU_xZE-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (9, @imgId, 'day10');

-- day11_9 → rôle day11
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day11', '', 'day11 pour itinerary 9', NULL, NULL, 9, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary9/sonaal-bangera-T_3hvLGAnJQ-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (9, @imgId, 'day11');

-- day12_9 → rôle day12
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day12', '', 'day12 pour itinerary 9', NULL, NULL, 9, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary9/mathew-schwartz-gsllxmVO4HQ-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (9, @imgId, 'day12');

-- firstHeaderImage10 → rôle firstHeader
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'firstHeader', '', 'firstHeader pour itinerary 10', NULL, NULL, 10, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary10/yu-kato-6xj9jgxcxm4-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (10, @imgId, 'firstHeader');

-- secondHeaderImage10 → rôle secondHeader
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'secondHeader', '', 'secondHeader pour itinerary 10', NULL, NULL, 10, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary10/mtsjrdl--RIHgVIKjYI-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (10, @imgId, 'secondHeader');

-- firstCountry10 → rôle firstCountry
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'firstCountry', '', 'firstCountry pour itinerary 10', NULL, NULL, 10, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary10/pexels-markus-winkler-1430818-5059929.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (10, @imgId, 'firstCountry');

-- secondCountry10 → rôle secondCountry
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'secondCountry', '', 'secondCountry pour itinerary 10', NULL, NULL, 10, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary10/pexels-enric-cruz-lopez-6642506.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (10, @imgId, 'secondCountry');

-- thirdCountry10 → rôle thirdCountry
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'thirdCountry', '', 'thirdCountry pour itinerary 10', NULL, NULL, 10, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary10/pexels-freestockpro-2166643.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (10, @imgId, 'thirdCountry');

-- day1_10 → rôle day1
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day1', '', 'day1 pour itinerary 10', NULL, NULL, 10, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary10/zequn-gui-xVFS3meofYM-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (10, @imgId, 'day1');

-- day2_10 → rôle day2
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day2', '', 'day2 pour itinerary 10', NULL, NULL, 10, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary10/guillermo-perez-4imxvpB7sjM-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (10, @imgId, 'day2');

-- day3_10 → rôle day3
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day3', '', 'day3 pour itinerary 10', NULL, NULL, 10, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary10/the-creativv-TvIjwonf-k0-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (10, @imgId, 'day3');

-- day4_10 → rôle day4
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day4', '', 'day4 pour itinerary 10', NULL, NULL, 10, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary10/47.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (10, @imgId, 'day4');

-- day5_10 → rôle day5
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day5', '', 'day5 pour itinerary 10', NULL, NULL, 10, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary10/alfiano-sutianto-exFdOWkYBQw-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (10, @imgId, 'day5');

-- day6_10 → rôle day6
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day6', '', 'day6 pour itinerary 10', NULL, NULL, 10, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary10/niklas-weiss-7wwlkzHI5aw-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (10, @imgId, 'day6');

-- day7_10 → rôle day7
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day7', '', 'day7 pour itinerary 10', NULL, NULL, 10, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary10/harry-kessell-eE2trMn-6a0-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (10, @imgId, 'day7');

-- day8_10 → rôle day8
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day8', '', 'day8 pour itinerary 10', NULL, NULL, 10, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary10/pier-francesco-grizi-wtJret3oAE8-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (10, @imgId, 'day8');

-- day9_10 → rôle day9
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day9', '', 'day9 pour itinerary 10', NULL, NULL, 10, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary10/csaba-veres-ZBBJy9kNDww-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (10, @imgId, 'day9');

-- day10_10 → rôle day10
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day10', '', 'day10 pour itinerary 10', NULL, NULL, 10, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary10/a-miah-HFizNGIm6dM-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (10, @imgId, 'day10');

-- day11_10 → rôle day11
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day11', '', 'day11 pour itinerary 10', NULL, NULL, 10, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary10/idan-gil-lmUB9C6OurQ-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (10, @imgId, 'day11');

-- day12_10 → rôle day12
INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES ( 'day12', '', 'day12 pour itinerary 10', NULL, NULL, 10, NULL, LOAD_FILE('/var/lib/mysql-files/itinerary10/dorian-d1-aX5NLrKgRBc-unsplash.jpg'));
SET @imgId = LAST_INSERT_ID();
INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (10, @imgId, 'day12');

