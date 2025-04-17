#!/usr/bin/env bash
set -e

echo "=== 1) Устанавливаем утилиты в Termux ==="
pkg install -y grep sed aapt2 openjdk-17 unzip

echo "=== 2) Настраиваем Android SDK окружение ==="
export ANDROID_HOME="$HOME/android-sdk"
export ANDROID_SDK_ROOT="$ANDROID_HOME"
BT_VERSION=$(ls "$ANDROID_HOME/build-tools" | sort -V | tail -n1)

echo "→ Используем build-tools: $BT_VERSION"
# Правильно обновляем PATH, чтобы сохранить исходный PATH
export PATH="$ANDROID_HOME/platform-tools:$ANDROID_HOME/build-tools/$BT_VERSION:$PATH"

echo "PATH (фильтр):"
printf "%s\n" "${PATH//:/\n}" | grep -E 'android-sdk|build-tools|platform-tools' || true

echo "=== 3) Подменяем все aapt2 в SDK на Termux‑бинарник (aarch64) ==="
for dir in "$ANDROID_HOME/build-tools"/*; do
  [ -d "$dir" ] && ln -sf "$(which aapt2)" "$dir/aapt2" && \
    echo "  → linked aapt2 into $dir/"
done

echo "=== 4) Включаем загрузку нативного aapt2 из Maven ==="
grep -qx 'android.aapt2FromMaven=true' gradle.properties \
  || echo 'android.aapt2FromMaven=true' >> gradle.properties

echo "=== 5) Убираем явный buildToolsVersion из файлов сборки ==="
find app -maxdepth 1 -type f -name 'build.gradle.kts' -o -name 'build.gradle' -exec sed -i '/buildToolsVersion/d' {} +

echo "=== 6) Собираем Debug APK локально ==="
chmod +x ./gradlew
./gradlew --refresh-dependencies clean assembleDebug

echo "=== 7) Копируем APK в ~/storage/downloads ==="
mkdir -p ~/storage/downloads
cp app/build/outputs/apk/debug/app-debug.apk ~/storage/downloads/CodeEditor98-debug.apk

echo "✅ Локальная сборка завершена!"
echo "APK → ~/storage/downloads/CodeEditor98-debug.apk"
