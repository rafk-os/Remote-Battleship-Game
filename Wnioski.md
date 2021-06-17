# Wnioski
Poniżej będą przedstawione wnioski ogólne zespołu a jeszcze niżej indywidualne wnioski każdego z członków zespołu przy realizowaniu projektu gry w statki.

- Nasz projekt napewno wyróżnia to że jest napisany z pomocą JavaX jako bilbioteką do zobrazowania całej gry. Jest to biblioteka nie używana powszechnie do takich rozwiązań. Innym charakterystycznym elementem jest również to, że łączymy programy napisane w dwóch różnych językach programowania  oraz są one napisane głównie z pomocą ich dokumentacji.

- Nasze programy może uruchomić większość dzisiejszych komputerów, które mają dostępny dowolny port na stworzenie gniazda TCP oraz stworzyć kilka wątków. Co do wymagań programowych to potrzeba posiadać dobrze skonfigurowaną bibliotekę JavaX oraz interpretera pythona w wersji 3.8+. Jeżeli byłby jakikolwiek problem z połączeniem się z wykorzystaniem TLS to może być to spowodowane przez firewall lub antyvirus. (zauważyliśmy taki problem u jednego z nas)

- Biblioteki jakie wykorzystywaliśmy to z pythona: socket do stworzenia dual-stack gniazda oraz operacji na nim, asyncore na którym próbowaliśmy stworzyć serwer zdarzeniowy ale jest to jeszcze w fazie testowania, _thread do stworzenia wielu wątków na serwerze oraz różnych operacji na tym, ssl aby stworzyć szyfrowane połączenie oraz zarządzać certyfikatami bezpieczeństwa, re aby rozpoznawać czy polecenia wysyłane przez klienta są poprawne. Z Javy zostały wykorzystywane różne biblioteki wchodzące w skład JavaX aby wszystko jak najlepiej ukazać oraz biblioteki do zarządzania ceryfikatami bezpieczeństwa i tworzenia połączenia tcp z serwerem.

- Najbardziej jesteśmy dumni z wszystkiego co zostało zrealizowane w projekcie po mimo wielu występujących problemów. Nasza współpraca na początku projektu nie była aż tak dobrze zsynchronizowana lecz pod koniec o wiele lepiej ona wyglądała. Połączenie programów działających w python'nie oraz Jav'ie było też momentalnie bardzo wymagające i pod względem wysyłania wiadomości czy też szyfrowania połączenia występowały liczne problemy lecz w końcu udało nam się doporowadzić te elementy do dobrej postaci.

- Jak już wcześniej wspomniałem połączenie dwóch progamów wykorzystujących inne języki programowania było problemem oraz komunikacja pomiędzy nimi. Rozwiązaniem było efektywne testowanie i szukanie potencjalnego rozwiązania w dokumentacjach. Zbieraliśmy się wspólnie i myśleliśmy nad tym co może być potencjalnym rozwiazaniem np w sprawie stworzenia szyforwaniego połączenia co ze strony Javy było troche bardziej problematyczne niz ze strony pythona. Powodem była mała popularność odnośnie takich implementacji w internecie oraz jej ewentualne skomplikowanie. Innym problemem też było zmodyfikowanie programu serwera tak aby odpowiadał na zdarzenia przy pomocy biblioteki asyncore. Aby całość działała wymagało to od nas zmodyfikowanie lekko protokołu gry na co nie zdecydowaliśmy się w końcowym rozrachunku. Innym problemem też było stworzenie obsługi IpV4 oraz Ipv6 co również jak szyfrowanie nie było bardzo jednoznacznie wytłumaczone w dokumentacji i wymagało to od nas od nas dodatkowych przemyśleń oraz testowania na spotkaniach, jak to potencjalnie rozwiązać.

## Rafał Kossowski
- Praca nad połączeniem dwóch progamów napisanych w innych językach programowania jest większym wyzwaniem niż połączenie dwóch programów działajacych w tym samym języku.
- Wyniosłem o wiele więcej doświadczenia z pracowania z pythonem nie tylko pod względem tworzenia programu serwera opierajacego się na gnieździe tcp ale też składni, skracania kodu, szeroko pojętej optymalizacji kodu w języku python.
- Praca z nową biblioteką asyncore, która służy do asynchronicznego zarządzania gniazdami sieciowymi była bardzo rozwijająca.
- Tworzenie porządnego programu, który będzie obsługiwał wiele zdarzeń oraz wystąpień błędów jest bardzo ciekawe z mojej strony.
- Współpraca z drugą osobą nad tworzeniem tej samej części projektu jest bardzo interesująca i uczy o tym jak ważne jest wspólne zrozumienie oraz zgodność aby program działał całkowicie dobrze jak było to zaplanowane a był pisany przez różne osoby.
- Omawianie na wspólnych spotkania wielu interesujących tematów odnośnie programu oraz rozwiązywanie problemów na żywo było bardzo produktywnym i rozwijającym doświadczeniem.
- Szukanie w internecie potencjalnych pomocy do stworzenia takiego projektu było bardzo ciekawą czynnością która pozwoliła mi poszzerzyć moją wiedzę na wiele tematów zwiazanych z programowaniem oraz aplikacjami sieciowymi.

## Oskar Wołosiuk

- Praktyka pracy w zespole nad dwoma projektami wykonanymi w różnych technologiach - serwer opracowany był w pythonie, natomiast klient został napisany w Javie, co za tym idzie zespół pracował nad poprawnością komunikowania się dwóch różnych programów.
- Wytwarzanie bezpiecznych aplikcji - transmisja z serwerem odbywa się w bezpieczny sposób korzystając z protokołu ```TLS```.
- Wielowątkowe zarządzanie interfejsem graficznym (miałem trochę problemu z blokującymi się wątkami).
- ```JavaFX``` jest bardzo fajną biblioteką, lecz ciężko radzi sobie z "pisaniem gier".
- W zespole można o wiele więcej - podczas wspólnych spotkań byliśmy w stanie dokonać o wiele więcej w stosunkowo krótkim czasie.
- W zespole ważna jest wymiana informacji - podczas wspólnej dyskusji byliśmy w stanie doradzać sobie nawzajem i ewentualnie skrócić czas innych członków zespołu.
- Biblioteki w różnych językach mogą różnie realizować z pozoru te same rzeczy
## Patryk Suruło
- Praca zespołowa wiele uczy, ponieważ można wspólnie rozwiązać dany problem.
- Realizowanie takich typów projektu stawia przed nowymi problemami. 
- Głównym problemem projektu było połączenie serwera napisanego w pythonie i klienta napisanego w Javie - trzeba było zadbać o poprawną komunikację.
- Trudność szukania błędów w wyniku tego, że projekt był realizowany w dwóch językach.
- Realizacja takiego projektu pozwoliła na bardziej praktyczną wiedzę dotyczącą protokołów.
- Praktyczne zastosowanie gita w zespole.
