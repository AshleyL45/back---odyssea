// Pour l'executer, se placer dans le terminal au niveau de ce fichier et -> node generate_image_inserts.js
const fs   = require('fs');
const path = require('path');

// 1) Chemin vers votre image.tsx
const tsxPath = path.resolve(__dirname, '../src/main/resources/static/images/itineraries/image.tsx');
const content = fs.readFileSync(tsxPath, 'utf8');

// 2) Regex pour capturer alias, itineraryId et nom de fichier
const importRegex = /import\s+(\w+)\s+from\s+['"]\.\/itineraries\/itinerary(\d+)\/([^'"]+)['"]/g;

const sqlStmts = [];
let match;
while ((match = importRegex.exec(content)) !== null) {
  const [ , alias, itinId, fileName ] = match;
  let role;

  // 3) Déduction du rôle
  if (/^day(\d+)_\d+$/.test(alias)) {
    role = `day${alias.match(/^day(\d+)_/)[1]}`;
  } else if (/^(first|second)HeaderImage\d+$/.test(alias)) {
    const ordMap = { first: '1', second: '2' };
    role = `header${ordMap[alias.match(/^(first|second)HeaderImage/)[1]]}`;
  } else if (/^(first|second|third)Country\d+$/.test(alias)) {
    const ordMap = { first: '1', second: '2', third: '3' };
    role = `country${ordMap[alias.match(/^(first|second|third)Country/)[1]]}`;
  } else {
    console.warn(`– alias non géré : ${alias}`);
    continue;
  }

  // 4) Construction du lien et alt_text
  const link    = `/images/itineraries/${itinId}/${fileName}`;
  const altText = fileName
    .replace(/\.[^.]+$/, '')
    .replace(/[-_]/g, ' ');

  // 5) Génération des SQL
  sqlStmts.push(
    `INSERT INTO images (size_type, link, alt_text, itinerary_id)\n` +
    `VALUES ('gallery', '${link}', '${altText}', ${itinId});`
  );
  sqlStmts.push(
    `INSERT INTO itinerary_images (itinerary_id, image_id, role)\n` +
    `VALUES (${itinId}, LAST_INSERT_ID(), '${role}');`
  );
}

if (sqlStmts.length === 0) {
  console.error("Aucune image trouvée, vérifiez la regex et le chemin de image.tsx");
  process.exit(1);
}

// 6) Écriture du fichier SQL à la racine du projet
const outPath = path.resolve(__dirname, '../insert_images.sql');
fs.writeFileSync(outPath, sqlStmts.join('\n\n'), 'utf8');
console.log(`✅ ${sqlStmts.length/2} images prêtes à insérer dans ${outPath}`);


// Executer  mysql -u root -p odyssea_db < insert_images.sql à la racine du projet (au niveau de insert_images.sql)