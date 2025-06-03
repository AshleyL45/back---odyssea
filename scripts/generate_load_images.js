// Pour l'executer, se placer dans le terminal au niveau de ce fichier et -> node generate_load_images.js


// generate_load_images.js
 const fs    = require('fs');
 const path  = require('path');
 const mysql = require('mysql2/promise');
 require('dotenv').config({ path: path.resolve(__dirname, '..', '.env') });

 const DB = {
   host:     process.env.DB_HOST,
   user:     process.env.DB_USERNAME,
   password: process.env.DB_PASSWORD,
   database: process.env.DB_NAME,
 };

 // chemins relatifs
 const baseAssets  = path.resolve(__dirname, '..', '..', 'front---odyssea', 'src', 'assets');
 const frontAssets = path.join(baseAssets, 'itineraries');
 const mappingFile = path.join(baseAssets, 'image.tsx');

 // on parse d’abord le mapping TSX pour récupérer alias/dir/file
 const tsx       = fs.readFileSync(mappingFile, 'utf-8');
 const importRe  = /import\s+(\w+)\s+from\s+['"]\.\/itineraries\/(itinerary\d+)\/(.+?)['"];?/g;
 const entries   = [];
 let m;
 while ((m = importRe.exec(tsx)) !== null) {
   entries.push({ alias: m[1], dir: m[2], file: m[3] });
 }

 // normalisation de l’alias en un role lisible
 function normalizeRole(alias) {
   return alias
     .replace(/Image\d+$/i, '')
     .replace(/_?\d+$/, '');
 }

 async function main() {
   const conn = await mysql.createConnection(DB);
   await conn.query('SET FOREIGN_KEY_CHECKS = 0');
   await conn.query('TRUNCATE TABLE itineraryImages');
   await conn.query('TRUNCATE TABLE images');
   await conn.query('ALTER TABLE images AUTO_INCREMENT = 1');
   await conn.query('SET FOREIGN_KEY_CHECKS = 1');

  for (let { alias, dir, file } of entries) {
    const itinId = parseInt(dir.replace('itinerary',''), 10);

    // On récupère bien le rôle à partir de l’alias TSX
    const role   = normalizeRole(alias);

    const imgPath = path.join(frontAssets, dir, file);
    if (!fs.existsSync(imgPath)) {
      console.warn(`⚠️ Fichier manquant : ${imgPath}`);
      continue;
    }
    const blob = fs.readFileSync(imgPath);

    // 1) INSERT dans images avec le rôle correct
    const [res] = await conn.execute(
      `INSERT INTO images
         (sizeType, link, altText, activityId, hotelId, itineraryId, articleId, data)
       VALUES (?, '', ?, NULL, NULL, ?, NULL, ?)`,
      [role, `${role} pour itinéraire ${itinId}`, itinId, blob]
    );
    const imgId = res.insertId;

    // 2) Lien dans itineraryImages : on met bien `role`, pas le filename
    await conn.execute(
      `INSERT INTO itineraryImages (itineraryId, imageId, role)
       VALUES (?, ?, ?)`,
      [itinId, imgId, role]
    );

    console.log(`→ Itinéraire ${itinId}, role=${role}, id=${imgId}`);
  }


   console.log('✅ Import terminé.');
   await conn.end();
 }

 main().catch(console.error);


