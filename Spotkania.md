# Spotkania zespołu
Zaplanowane spotkania będą się odbywać co tydzień w celu kontroli obecnej fazy projektu gry w statki.

## Spotkanie 1
###### 17.05.2021 16:00
Pierwsze spotkanie na którym decydowaliśmy czy podejmiemy się wybranego tematu projektu. Ustaliliśmy na nim że podejmiemy się go oraz, jak będzie wyglądał nasz własny portokół do tej gry który do następnego spotkania miał zostać spisany na github przez Oskara Wołosiuka i skontrolowany przez reszte członków zespołu.

## Spotkanie 2  
###### 23.05.2021 17:00
Było to drugie podstawowe spotkanie na którym omawialiśmy podstawowe założenia, które nasze programy mają spełnić. Na tym spotkaniu ustaliliśmy w jakich językach programowania będą napisane oba programy klienta i serwera. Serwer będzie napisany w pythonie przez Rafała Kossowskiego oraz Patryka Suruło. Natomiast realizacją klienta oraz zwizualizowaniem mapy gry zajmie się Oskar Wołosiuk przy pomocy języka programowania Java oraz biblioteki JavaX. Ustaliliśmy też, że testowanie danych funkcjonalności projektu będzie odbywało się przez wyznaczoną do tego osobę. Innymi ważnymi zagadnieniami, którymi omówiliśmy było:

* Serwer będzie posiadał obsługe wielu klientów naraz oraz będzie to serwer zdarzeniowy.

* Serwer będzie działał na zasadzie dwuosobowych pokojów do których będą dołączali klienci i w chwili gdy pokój będzie pełny gra wystartuje i nie będzie można do niego dołączyć.

* Serwer oraz klient będą obsługiwali IPv4 oraz IPv6 oraz połączenie bęzie szyfrowane przy pomocy protokołu TLS.

Następnie określiliśmy jakie zadanie wykonać w tym tygodniu. Oskar Wołosiuk będzie wykonywał do następnego spotkania wizualizacje mapy gry oraz wysyłanie odpowiednich komend przez klienta do serwera. Rafał Kossowski stworzy cały system klas odpowiedzialny za imitację pokojów graczy. Natomiast Patryk Suruło stworzy prototyp serwera do przetestowania funkcjonalności pokojów.


## Spotkanie 3  
###### 31.05.2021 18:00

Na tym spotkaniu mieliśmy już gotowy program klienta  demo oraz działajacy serwer z obsługą graczy i przydzielaniem ich do pokojów gry. Następnym krokiem zostało zaimplementować pełny protokół który obsługiwałby graczy będących w grze. Do kolejnego spotkania ustaliliśmy że Rafał Kossowski do kończy demo serwera aby mogło już w pełni współpracować z klientem. Następnie Patryk Suruło będzie testował demo aplikacji serwera oraz klienta.


## Spotkanie 4 
###### 08.06.2021 19:00
 W chwili tego spotkania posiadaliśmy już w pełni funkcjonujące demo serwera i klienta bez zaimplementowanych obsług IPv4 i IPv6, oraz szyfrowania połączenia. Do następnego tygodnia zaplanowaliśmy że wspólnie dodać te funkcjonalności aby przesłać pełny program do 15 czerwca.
 
## Spotkanie 5 
###### 10.06.2021 17:00
Na tym spotkaniu dopracowaliśmy cały program który stworzyliśmy i poszerzyliśmy go o szyfrowanie połączenia oraz obsługe IPv4 i IPv6. Został też naprawiony błąd z wysyłaniem danych wysyłanych przez klienta. Patryk Suruło podjął się zadania zmodyfikowania serwera aby był serwerem zdarzeniowym wykorzystującym asynchroniczne zarządzanie gniazdami z biblioteki asyncore.

## Spotkanie 6 
###### 15.06.2021 19:00
Zmiana działania serwera na tego wykorzustującego bilbiotekę asyncore nie była udana więc został zostawiony serwer zdarzeniowy działajacy na podstawie nowych tworzonych wątków. Całość została sprawdzona, zostały dokonane małe zmiany estetyczne oraz projekt został wysłany do prowadzącego laboratorium.



