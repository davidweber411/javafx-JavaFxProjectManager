CALL gradlew createCustomFatJar

CALL cd .\build\libs

FOR %%i IN (*) DO start %%i
