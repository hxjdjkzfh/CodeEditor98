#!/data/data/com.termux/files/usr/bin/bash

echo -e "\n[1/3] Cleaning project..."
./gradlew clean || { echo "Gradle clean failed"; exit 1; }

echo -e "\n[2/3] Building APK..."
./gradlew assembleRelease > build.log 2>&1

if [ $? -eq 0 ]; then
    echo -e "\n[3/3] BUILD SUCCESSFUL!"
    APK=$(find app/build/outputs/apk/release -name "*.apk" | head -n1)
    echo -e "APK: $APK"
else
    echo -e "\nBUILD FAILED. See build.log for details:"
    tail -n 30 build.log
    exit 1
fi
