# KLIENT:

Istnieje wiele sposobów uruchomienia programu, lecz dla przykładu zostanie opisany sposób uruchomienia programu przez środowisko IntelliJ IDEA - darmowe środowisko firmy JetBrains. 

1. Upewnij się, że posiadasz zainstalowaną Jave conajmniej w wersji 8 i jest ona dodana do zmiennych środowiskowych. 

2. Otwórz projekt klienta w środowisku IntelliJ IDEA. 

3. Pobierz JavaFX SDK (darmowa biblioteka) z oficjalnej strony. (https://openjfx.io/index.html) 

3. Dodaj bibliotęke do środowiska. W tym celu przejdź do ```File -> Project Structure -> Libraries``` i dodaj JavaFX SDK jako bibliotekę do projektu. Pamiętaj żeby podczas dodawania wybrać katalog ``` lib ```. Po dokonaniu tej czynności, środowisko powinno rozpoznawać komendy JavyFX. 

4. Dodanie opcji wirtualnej maszyny. W celu pokierowania środowiska do odpowiednich modułów JavyFx należy dodać odpowiednie konfiguracje uruchomienia, w tym celu przejdź do ```Run -> Edit Configurations...``` i w opcjach wirtualnej maszyny dodaj:
- ```--module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml``` dla Mac/Linux
- ```--module-path "\path\to\javafx-sdk\lib" --add-modules javafx.controls,javafx.fxml``` dla Windows
Oczywiście w miejscu ```path/to/javafx-sdk``` należy podać własną ścieżke. 

5. Uruchom projekt przyciskiem RUN.

# SERWER:
