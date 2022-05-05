# Java chat socket
Java chat socket to aplikacja, która dzięki użyciu socketów umożliwia komunikowanie się użytkowników między sobą.

# Uruchomienie
1. Serwer można uruchomić dzięki klasie **ChatServer**. Jako parametr uruchomieniowy należy podać *port*, na którym serwer będzie nasłuchiwał.

3. Klientów można uruchomić dzięki klasie **ChatClient**. Jako parametry uruchomieniowe należy podać odpowiednio:
- IP serwera
- PORT serwera

# Komendy

***Komendy ogólne:***
|  Komenda| Działanie |
|--|--|
| \\\h   | Wyświetlenie dostępnych komend |
| \\\quit| Zamknięcie aplikacji |

<br></br>
***Komendy dotyczące kanałów:***
    					    
|  Komenda| Działanie |
|--|--|
| \\\c   | Wyświetlenie komend dotyczących kanałów |
| \\\c --channels | Wyświetlenie dostępnych kanałów |
| \\\c --join "CHANNEL_NAME" | Dołączenie do kanału |
| \\\c --create "CHANNEL_NAME" | Utworzenie kanału publicznego |
| \\\c --create --p "CHANNEL_NAME" | Utworzenie kanału prywatnego |
| \\\c --online | Wyświetlenie użytkowników dostępnych na kanale |
| \\\c --leave | Opuszczenie obecnego kanału |
| \\\c --history | Wyświetlenie historii kanału |
| \\\f "FILE_PATH" | Wysłanie pliku do uczestników kanału |

    
# Komentarz do zadań
**1. Rozmowa wielu osób na kanale grupowym**
- *Funkcjonalność ta jest realizowana przez uczestnictwo użytkowników w kanale publicznym. Do takiego kanału może dołączyć każdy i nie wymaga to niczyjego zezwolenia.*

**2. Rozmowa 2 lub więcej osób na kanale prywatnym**
- *Funkcjonalność ta jest realizowana przez uczestnictwo użytkowników w kanale prywatnym. Kanał taki jest zakładany przez dowolną osobę. Do takiego kanału mogą dołączyć tylko osoby, które zostały do tego uprawnione przez dowolnego z obecnych już użytkowników kanału.*

**3. Przesyłanie plików między osobami na danym kanale**
- *Funkcjonalność ta jest możliwa, gdy użytkownik znajduje się na dowolnym kanale i użyje odpowiedniej komendy (tabelka powyżej)*

**4. Zapamiętywanie historii rozmów po stronie serwera**
- *Rozmowy są na bieżąco zapisywane przez serwer w pliku .txt, którego nazwą jest nazwa danego kanału*

**5. Możliwość przeglądania historii rozmów z poziomu klienta**
- *Użytkownik może sprawdzić historię rozmów. Aby to zrobić, musi on znajdować się na dowolnym kanale i wpisać odpowiednią komendę (tabelka powyżej). W wyniku dostanie historię rozmów z danego kanału.*
