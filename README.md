
# Rest Java EE Chat

Rest Java EE Chat to aplikacja, która dzięki użyciu Javy EE umożliwia komunikowanie się użytkowników między sobą.

# Uruchomienie
1. Serwer uruchamiany jest przy użyciu serwera JBoss/WildFly

2. Klientów można uruchomić dzięki klasie **Client**. Jako parametr uruchomieniowe należy podać:
- nazwę użytkownika

# Komendy

***Komendy dotyczące kanałów:***
    					    
|  Komenda| Działanie |
|--|--|
| cht --channels | Wyświetlenie dostępnych kanałów |
| cht --join "CHANNEL_ID" | Dołączenie do kanału |
| cht --create "CHANNEL_NAME" | Utworzenie kanału publicznego |
| cht --create --p "CHANNEL_NAME" | Utworzenie kanału prywatnego |
| cht --online | Wyświetlenie użytkowników dostępnych na kanale |
| cht --leave | Opuszczenie obecnego kanału |
| cht --history | Wyświetlenie historii kanału |
| \\f "FILE_PATH" | Wysłanie pliku do uczestników kanału |

    
# Komentarz do zadań
**1. Rozmowa wielu osób na kanale grupowym**
- *Funkcjonalność ta jest realizowana przez uczestnictwo użytkowników w kanale publicznym. Do takiego kanału może dołączyć każdy i nie wymaga to niczyjego zezwolenia.*

**2. Rozmowa 2 lub więcej osób na kanale prywatnym**
- *Funkcjonalność ta jest realizowana przez uczestnictwo użytkowników w kanale prywatnym. Kanał taki jest zakładany przez dowolną osobę. Do takiego kanału mogą dołączyć tylko osoby, które zostały do tego uprawnione przez dowolnego z obecnych już użytkowników kanału.*

**3. Przesyłanie plików między osobami na danym kanale**
- *Funkcjonalność ta jest możliwa, gdy użytkownik znajduje się na dowolnym kanale i użyje odpowiedniej komendy (tabelka powyżej)*

**4. Zapamiętywanie historii rozmów po stronie serwera**
- *Rozmowy są na bieżąco zapisywane przez serwer w bazie danych*

**5. Możliwość przeglądania historii rozmów z poziomu klienta**
- *Użytkownik może sprawdzić historię rozmów. Aby to zrobić, musi on znajdować się na dowolnym kanale i wpisać odpowiednią komendę (tabelka powyżej). W wyniku dostanie historię rozmów z danego kanału.*
