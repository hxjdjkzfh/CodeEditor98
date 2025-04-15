#!/data/data/com.termux/files/usr/bin/bash
set -e
cd "$(dirname "$0")"

echo "[1/4] Making gradlew executable"
chmod +x ./gradlew

echo "[2/4] Forcing local SDK path"
echo "sdk.dir=$HOME/android-sdk" > local.properties

echo "[3/4] Building APK"
./gradlew assembleDebug

echo "[4/4] Copying APK to storage"
cp -f app/build/outputs/apk/debug/app-debug.apk /storage/emulated/0/Download/CodeEditor98.apk

echo "✅ Done. Check Download folder."
