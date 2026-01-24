# Tic\-Tac\-Toe\-Server (Spring Boot)

Backend do wieloosobowej gry kółko\-krzyżyk w Java (Spring Boot). Aplikacja udostępnia API HTTP (REST) oraz komunikację
w czasie rzeczywistym przez WebSocket (STOMP) dla obsługi rozgrywek, pokoi i czatu.

## 1\. Cel projektu

Celem aplikacji jest zapewnienie serwera gry multiplayer, który:

- umożliwia rejestrację i logowanie użytkowników (w tym konto gościa),
- pozwala tworzyć/joinować/opuszczać pokoje,
- obsługuje stan gry (ruchy, koniec gry, rewanż),
- zapewnia czat w pokoju gry,
- przechowuje i udostępnia dane w bazie danych przez JPA/Hibernate,
- publikuje powiadomienia WebSocket do klientów.

## 2\. Wymagania funkcjonalne

1. **Użytkownicy**

- rejestracja nowego konta,
- logowanie użytkownika,
- logowanie jako gość.

2. **Pokoje**

- utworzenie pokoju,
- pobranie listy pokoi,
- dołączenie do pokoju,
- opuszczenie pokoju.

3. **Rozgrywka**

- inicjalizacja gry w pokoju,
- wykonanie ruchu (oznaczenie pola),
- walidacja ruchu,
- wykrycie zakończenia gry (wygrana/remis),
- rewanż.

4. **Komunikacja**

- czat i wymiana zdarzeń w pokoju gry przez WebSocket,
- powiadomienia o zmianie stanu (np\. ruch, dołączenie/opuszczenie, start gry).

5. **Dane**

- operacje CRUD na wybranych tabelach przez endpointy,
- zapytania realizowane przez JPA `@Query` oraz `NativeQuery`.

## 3\. Wymagania niefunkcjonalne

- **Architektura**: Spring Boot + kontrolery REST + warstwa serwisów + repozytoria JPA.
- **Dokumentacja API**: Swagger/OpenAPI.
- **Logowanie**: Log4j (konfiguracja dla profili).
- **Konfiguracja**: wczytywanie wartości z plików `application\-\*.properties` w klasach aplikacji.
- **Profile**: co najmniej `dev` i `prod` (różne konfiguracje środowiskowe).
- **Baza danych**: obsługa bazy z JPA/Hibernate; w `dev` wykorzystywana jest baza H2 in\-memory.
- **Testowalność**: testy jednostkowe (Junit).
- **Bezpieczeństwo**: obsługa autoryzacji użytkownika dla endpointów wymagających dostępu (jeśli włączone w projekcie).

## 4\. Aktorzy

- **Gość** \- użytkownik bez konta, może wejść do gry jako guest i korzystać z podstawowych funkcji.
- **Użytkownik zarejestrowany** \- posiada konto, może logować się i korzystać z pełnej funkcjonalności.
- **Administrator** (opcjonalnie, jeśli występuje w systemie) \- zarządza zasobami/porządkiem danych, ewentualnie
  przegląda statystyki.

## 5\. Przypadki użycia (Use Cases)

1. **Rejestracja użytkownika** (aktor: Gość)
2. **Logowanie użytkownika** (aktor: Gość)
3. **Logowanie jako gość** (aktor: Gość)
4. **Utworzenie pokoju** (aktor: Użytkownik)
5. **Pobranie listy pokoi** (aktor: Użytkownik/Gość)
6. **Dołączenie do pokoju** (aktor: Użytkownik/Gość)
7. **Opuszczenie pokoju** (aktor: Użytkownik/Gość)
8. **Rozpoczęcie gry w pokoju** (aktor: Użytkownik/Gość)
9. **Wykonanie ruchu** (aktor: Użytkownik/Gość)
10. **Otrzymanie powiadomień o stanie gry** (aktor: Użytkownik/Gość)
11. **Wysłanie/odbiór wiadomości czatu** (aktor: Użytkownik/Gość)
12. **Rewanż** (aktor: Użytkownik/Gość)

### Diagram przypadków użycia

Diagram powinien znajdować się w repozytorium jako plik, np\.:

- `docs/use\-case\-diagram.png` (lub `.pdf` / `.drawio`)

## 6\. Obiekty istotne (model domenowy)

Przykładowe obiekty istotne dla aplikacji:

- **User** \- dane użytkownika systemu (login, hasło/identyfikator, rola).
- **Room** \- pokój rozgrywki (nazwa, status, gracze).
- **Game** \- aktualna rozgrywka (stan planszy, aktualny gracz, wynik).
- **Move** \- pojedynczy ruch (pozycja, gracz, znacznik, czas).
- **ChatMessage** \- wiadomość na czacie (autor, treść, czas, pokój).
- **Session/Connection** (logicznie) \- identyfikacja połączenia WebSocket i użytkownika.

## 7\. Projekt kontrolerów (REST API) i konwencje

### Konwencje

- JSON jako format odpowiedzi.
- W przypadku powodzenia zwracane są kody `2xx`, w błędach `4xx/5xx`.
- Struktura błędu powinna zawierać co najmniej: `timestamp`, `status`, `error`, `message`, `path` (lub analogicznie).

### Przykładowy podział kontrolerów

- `AuthController` \- rejestracja i logowanie.
- `RoomController` \- operacje na pokojach.
- `GameController` \- operacje związane z rozgrywką.
- `ChatController` (opcjonalnie REST) \- historia czatu/CRUD, jeśli realizowane przez HTTP.
- `UserController` \- CRUD użytkowników (wymagane dla prezentacji CRUD na bazie).

### Przykładowe endpointy i odpowiedzi HTTP

1. `POST /api/auth/register`

- `201 Created` \- użytkownik utworzony
- `400 Bad Request` \- błędne dane wejściowe

2. `POST /api/auth/login`

- `200 OK` \- zalogowano
- `401 Unauthorized` \- błędne dane logowania

3. `GET /api/rooms`

- `200 OK` \- lista pokoi

4. `POST /api/rooms`

- `201 Created` \- utworzono pokój

5. `POST /api/rooms/{roomId}/join`

- `200 OK` \- dołączono
- `404 Not Found` \- brak pokoju

6. `POST /api/games/{gameId}/move`

- `200 OK` \- ruch zaakceptowany, zwrot zaktualizowanego stanu
- `409 Conflict` \- ruch niedozwolony (np\. zajęte pole, nie kolej gracza)

## 8\. Swagger / OpenAPI

Endpointy są dokumentowane za pomocą Swagger (OpenAPI).  
Wymagane jest udostępnienie:

- UI Swagger (np\. `/swagger\-ui.html` lub `/swagger\-ui/index.html`)
- specyfikacji OpenAPI (np\. `/v3/api\-docs`)

W dokumentacji projektu należy dołączyć zrzuty ekranu lub plik OpenAPI, jeżeli jest eksportowany.

## 9\. Baza danych (ERD + opis tabel)

### Diagram ERD

Diagram ERD powinien znajdować się w:

- `docs/erd.png` (lub `.pdf` / `.drawio`)

### Opis tabel

Dla każdej tabeli:

- krótki cel istnienia tabeli,
- relacje (FK),
- kolumny o nieoczywistym przeznaczeniu z krótkim opisem (do późniejszego użycia jako komentarze w SQL).

## 10\. Skrypt SQL (pełny zrzut bazy)

Do projektu należy dołączyć kompletny dump bazy (struktura + dane), np\.:

- `docs/db/dump.sql`

Skrypt ma umożliwiać odtworzenie bazy danych od zera.

## 11\. Logi (Log4j)

Aplikacja loguje zdarzenia biznesowe i techniczne przez Log4j, w tym:

- logowanie żądań krytycznych (np\. tworzenie pokoju, start gry),
- logowanie błędów walidacji i wyjątków,
- osobna konfiguracja w zależności od profilu `dev`/`prod`.

## 12\. Profile Spring Boot: dev i prod

Aplikacja posiada minimum dwa profile:

- **dev** \- lokalne uruchomienie, szybkie testy (np\. H2 in\-memory),
- **prod** \- konfiguracja produkcyjna (inna baza, inne logowanie, inne ustawienia).

Konfiguracje znajdują się w plikach:

- `src/main/resources/application\-dev.properties`
- `src/main/resources/application\-prod.properties`

## 13\. WebSocket (powiadomienia)

Aplikacja udostępnia kanały WebSocket służące do:

- powiadomień o zmianach w pokoju (join/leave),
- aktualizacji stanu gry (ruch, koniec gry, rewanż),
- czatu w pokoju.

W dokumentacji należy opisać:

- endpoint handshake (np\. `/ws`),
- prefixy (np\. `/topic`, `/app`) oraz przykładowe kanały.

## 14\. Wczytywanie properties w klasach

Konfiguracja jest wczytywana w kodzie (np\. URL klienta) z plików properties.  
Przykład: wartość `client.url` jest używana w klasach aplikacji (CORS/konfiguracja klienta).

## 15\. Komunikacja z drugą usługą

Aplikacja komunikuje się z drugą usługą (zewnętrzną lub projektową), np\.:

- walidacja/uzupełnianie danych,
- pobieranie informacji użytkownika,
- wysyłanie powiadomień.

W dokumentacji należy podać:

- adres usługi,
- wykorzystywane endpointy,
- format danych i obsługę błędów.

Jeżeli druga usługa jest częścią projektu grupowego, jej kod musi być dołączony do ZIP.

## 16\. Integracja ze zdalnym repozytorium

Repozytorium Git jest synchronizowane ze zdalnym serwisem (GitHub).  
Do dokumentacji należy dołączyć zrzuty ekranu potwierdzające:

- historię commitów,
- gałęzie (jeśli używane),
- adres repozytorium.

## 17\. Zawartość paczki ZIP (PUW)

Do oddania w 1 pliku ZIP:

1. Dokument wymagań + funkcje aplikacji (sekcje 1\-3).
2. Projekt kontrolerów: struktura odpowiedzi HTTP + kody + konwencje (sekcja 7).
3. Diagram ERD (sekcja 9).
4. Skrypt SQL dump (sekcja 10).
5. Kody źródłowe aplikacji.
6. Pliki towarzyszące (`docs/`, grafiki, diagramy, zrzuty).
7. Skompilowane artefakty (np\. `.jar` lub inne binaria).

## 18\. Uruchomienie (dev)

Wymagania:

- Java + Maven

Uruchomienie profilu dev:

- `mvn spring\-boot:run \-Dspring\-boot.run.profiles=dev`

Domyślne ustawienia dev używają H2 in\-memory.

## 19\. Technologie

- Java, Spring Boot
- JPA/Hibernate
- SQL
- WebSocket (STOMP)
- Swagger/OpenAPI
- Log4j
- Maven
- Junit
