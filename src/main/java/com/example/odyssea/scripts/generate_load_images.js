const fs = require('fs');
const path = require('path');

// Chemins absolus pour le front et fichier de mapping
const frontAssets = '/home/greta/Documents/www/Odyssea/front-odyssea/src/assets/itineraries';
const mappingFile = '/home/greta/Documents/www/Odyssea/front-odyssea/src/assets/image.tsx';
const outputSql   = path.resolve(__dirname, 'load_all_itineraries.sql');

// Lire et parser image.tsx pour récupérer alias -> chemin
const tsx = fs.readFileSync(mappingFile, 'utf-8');
const importRegex = /import\s+(\w+)\s+from\s+['\"]\.\/itineraries\/(itinerary\d+)\/(.+?)['\"];?/g;
let match;
const entries = [];
while ((match = importRegex.exec(tsx)) !== null) {
  const alias = match[1];    // ex. firstHeaderImage1
  const dir   = match[2];    // ex. itinerary1
  const file  = match[3];    // ex. brian-lundquist-...jpg
  entries.push({alias, dir, file});
}

// Fonction pour normaliser le rôle
function normalizeRole(alias) {
  // retire le suffixe 'Image' suivi de chiffres, puis retire tout suffixe numérique ou '_X'
  return alias.replace(/Image\d+$/i, '').replace(/_?\d+$/, '');
}

// Génération du SQL
let sql = "USE odyssea_db;\nTRUNCATE TABLE itineraryImages;\n\n";
sql += "-- (Optionnel) TRUNCATE TABLE images; ALTER TABLE images AUTO_INCREMENT = 1;\n\n";

entries.forEach(({alias, dir, file}) => {
  const roleRaw = normalizeRole(alias);
  const i = parseInt(dir.replace('itinerary', ''), 10);
  const filePath = path.join(frontAssets, dir, file);
  if (!fs.existsSync(filePath)) {
    console.warn(`Fichier manquant pour ${dir}/${file}`);
    return;
  }
  sql += `-- ${alias} → rôle ${roleRaw}\n`;
  sql += `INSERT INTO images (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data) VALUES (` +
         ` '${roleRaw}', '', '${roleRaw} pour itinerary ${i}', NULL, NULL, ${i}, NULL,` +
         ` LOAD_FILE('/var/lib/mysql-files/${dir}/${file}'));\n`;
  sql += `SET @imgId = LAST_INSERT_ID();\n`;
  sql += `INSERT INTO itineraryImages (itineraryId, imageId, role) VALUES (${i}, @imgId, '${roleRaw}');\n\n`;
});

fs.writeFileSync(outputSql, sql, 'utf-8');
console.log(`SQL généré: ${outputSql}`);
