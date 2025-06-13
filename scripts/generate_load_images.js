// Pour l'exécuter : dans le terminal à la racine du projet -> node scripts/generate_load_images.js

const fs = require('fs');
const path = require('path');
const mysql = require('mysql2/promise');
require('dotenv').config({ path: path.resolve(__dirname, '..', '.env') });

// Connexion à la base de données via .env
const DB = {
  host: process.env.DB_HOST,
  user: process.env.DB_USERNAME,
  password: process.env.DB_PASSWORD,
  database: process.env.DB_NAME,
};

// Chemins des assets et mapping
const baseAssets = path.resolve(__dirname, '..', '..', 'front---odyssea', 'src', 'assets');
const frontAssets = path.join(baseAssets, 'itineraries');
const mappingFile = path.join(baseAssets, 'image.tsx');

// Lecture et parsing du fichier image.tsx
const tsx = fs.readFileSync(mappingFile, 'utf-8');
const importRe = /import\s+(\w+)\s+from\s+["']\.\/itineraries\/(itinerary\d+)\/([^"']+)["']/g;
const entries = [];

let m;
while ((m = importRe.exec(tsx)) !== null) {
  entries.push({ alias: m[1], dir: m[2], file: m[3] });
}

console.log('🖼️ Images trouvées :', entries.length);

// Normalisation des rôles
function normalizeRole(alias) {
  return alias
    .replace(/Image\d+$/i, '')
    .replace(/_?\d+$/, '');
}

async function main() {
  const conn = await mysql.createConnection(DB);

  // Réinitialisation des tables avec désactivation des contraintes
  await conn.query(`SET FOREIGN_KEY_CHECKS = 0`);
  await conn.query(`TRUNCATE TABLE itinerary_images`);
  await conn.query(`TRUNCATE TABLE images`);
  await conn.query(`ALTER TABLE images AUTO_INCREMENT = 1`);
  await conn.query(`SET FOREIGN_KEY_CHECKS = 1`);

  for (let { alias, dir, file } of entries) {
    const itinId = parseInt(dir.replace('itinerary', ''), 10);
    const role = normalizeRole(alias);
    const imgPath = path.join(frontAssets, dir, file);

    // Vérifie que le fichier existe
    if (!fs.existsSync(imgPath)) {
      console.warn(`⚠️ Fichier manquant : ${imgPath}`);
      continue;
    }

    // Vérifie que l'itinéraire existe
    const [[{ count }]] = await conn.execute(
      'SELECT COUNT(*) AS count FROM itinerary WHERE id = ?',
      [itinId]
    );
    if (count === 0) {
      console.warn(`❌ Itinéraire ID ${itinId} introuvable en base, image ignorée.`);
      continue;
    }

    const blob = fs.readFileSync(imgPath);
    console.log(`📦 Image trouvée pour itinéraire ${itinId} (${file})`);

    // Insertion dans la table images
    const [res] = await conn.execute(
      `INSERT INTO images
        (size_type, link, alt_text, activity_id, hotel_id, itinerary_id, article_id, data)
       VALUES (?, '', ?, NULL, NULL, ?, NULL, ?)`,
      [role, `${role} pour itinéraire ${itinId}`, itinId, blob]
    );

    const imgId = res.insertId;
    console.log(`📸 Image insérée avec ID=${imgId}`);

    // Insertion dans la table de jointure
    await conn.execute(
      `INSERT INTO itinerary_images (itinerary_id, image_id, role)
       VALUES (?, ?, ?)`,
      [itinId, imgId, role]
    );

    console.log(`🔗 Lien ajouté pour itinéraire ${itinId}, rôle "${role}"`);
  }

  await conn.end();
  console.log('✅ Import terminé avec succès.');
}

// Lancement principal
main().catch((err) => {
  console.error('❌ Erreur pendant l’import :', err);
});
