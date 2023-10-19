echo "Starting script...";
bash -c "./gradlew clean";
bash -c "./gradlew --version"
bash -c "./gradlew createCustomFatJar"
bash -c "cd ./build/libs/"
for f in ./build/libs/*; do
	java -jar "$f"
done