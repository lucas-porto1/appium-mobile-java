import { createHash } from 'node:crypto';
import { mkdir, readFile, writeFile } from 'node:fs/promises';
import path from 'node:path';
import { fileURLToPath } from 'node:url';

const appUrl =
  'https://github.com/appium/android-apidemos/releases/download/v6.0.16/ApiDemos-debug.apk';
const expectedSha256 = 'bdef4302317cb474f26fdf2f4d4bb721bd19e93d4471455abaff8e7ce7275b21';
const repositoryRoot = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const appPath = path.join(repositoryRoot, 'apps', 'ApiDemos-debug.apk');

function sha256(content) {
  return createHash('sha256').update(content).digest('hex');
}

async function hasExpectedApp() {
  try {
    return sha256(await readFile(appPath)) === expectedSha256;
  } catch (error) {
    if (error.code === 'ENOENT') {
      return false;
    }

    throw error;
  }
}

if (await hasExpectedApp()) {
  console.log('ApiDemos APK is already available and verified.');
  process.exit(0);
}

const response = await fetch(appUrl);

if (!response.ok) {
  throw new Error(`Unable to download ApiDemos APK: HTTP ${response.status}`);
}

const app = Buffer.from(await response.arrayBuffer());
const actualSha256 = sha256(app);

if (actualSha256 !== expectedSha256) {
  throw new Error(`ApiDemos checksum mismatch: expected ${expectedSha256}, got ${actualSha256}`);
}

await mkdir(path.dirname(appPath), { recursive: true });
await writeFile(appPath, app);
console.log('ApiDemos APK downloaded and verified.');
