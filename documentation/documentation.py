import os
from weasyprint import HTML

html_content = """
<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="UTF-8">
    <title>Dokumentacja Systemu Uwierzytelniania Kart (CAS)</title>
    <style>
        @page {
            size: A4;
            margin: 20mm 15mm 20mm 15mm;
            @bottom-right {
                content: counter(page);
                font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
                font-size: 9pt;
                color: #718096;
            }
            @bottom-left {
                content: "Card Authentication System (CAS) - Dokumentacja";
                font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
                font-size: 9pt;
                color: #718096;
            }
        }
        
        body {
            font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
            color: #2d3748;
            line-height: 1.6;
            font-size: 10.5pt;
            margin: 0;
            padding: 0;
        }

        h1, h2, h3, h4 {
            color: #1a365d;
            font-weight: 700;
        }

        h1 {
            font-size: 24pt;
            margin-bottom: 5px;
            text-transform: uppercase;
            letter-spacing: 1px;
        }

        h2 {
            font-size: 16pt;
            border-left: 4px solid #2b6cb0;
            padding-left: 10px;
            margin-top: 30px;
            margin-bottom: 15px;
            page-break-after: avoid;
        }

        h3 {
            font-size: 12.5pt;
            color: #2b6cb0;
            margin-top: 20px;
            margin-bottom: 10px;
            page-break-after: avoid;
        }

        h4 {
            font-size: 11pt;
            color: #4a5568;
            margin-top: 15px;
            margin-bottom: 5px;
            page-break-after: avoid;
        }

        p {
            margin-top: 0;
            margin-bottom: 15px;
            text-align: justify;
        }

        /* Cover Page styling using absolute layout instead of flex */
        .cover-page {
            position: relative;
            height: 250mm;
            page-break-after: always;
        }

        .cover-accent {
            position: absolute;
            top: 0;
            left: -15mm;
            width: 210mm;
            height: 15mm;
            background: linear-gradient(90deg, #1a365d, #2b6cb0);
        }

        .cover-title-container {
            position: absolute;
            top: 60mm;
            left: 0;
            width: 100%;
        }

        .subtitle {
            font-size: 14pt;
            color: #4a5568;
            margin-top: 10px;
            text-transform: none;
            letter-spacing: 0;
        }

        .cover-details {
            position: absolute;
            bottom: 30mm;
            left: 0;
            border-top: 1px solid #e2e8f0;
            padding-top: 20px;
            width: 100%;
        }

        .meta-item {
            margin-bottom: 8px;
            font-size: 11pt;
        }

        .meta-label {
            font-weight: bold;
            color: #4a5568;
            display: inline-block;
            width: 180px;
        }

        /* Code blocks */
        pre {
            background-color: #f7fafc;
            border: 1px solid #e2e8f0;
            border-radius: 4px;
            padding: 12px;
            font-family: 'Courier New', Courier, monospace;
            font-size: 9.5pt;
            overflow: hidden;
            white-space: pre-wrap;
            word-wrap: break-word;
            margin-top: 10px;
            margin-bottom: 15px;
        }

        code {
            font-family: 'Courier New', Courier, monospace;
            background-color: #edf2f7;
            padding: 2px 5px;
            border-radius: 3px;
            font-size: 9.5pt;
        }

        pre code {
            background-color: transparent;
            padding: 0;
            border-radius: 0;
        }

        /* Lists */
        ul, ol {
            margin-top: 0;
            margin-bottom: 15px;
            padding-left: 20px;
        }

        li {
            margin-bottom: 5px;
        }

        /* Tables */
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
            margin-bottom: 20px;
            page-break-inside: avoid;
        }

        th, td {
            padding: 10px 12px;
            text-align: left;
            border-bottom: 1px solid #e2e8f0;
            font-size: 10pt;
        }

        th {
            background-color: #f1f5f9;
            color: #1a365d;
            font-weight: bold;
        }

        tr:nth-child(even) td {
            background-color: #f8fafc;
        }

        /* Callout / Annotation boxes */
        .annotation {
            background-color: #ebf8ff;
            border-left: 4px solid #3182ce;
            padding: 12px;
            margin-top: 15px;
            margin-bottom: 15px;
            border-radius: 0 4px 4px 0;
            page-break-inside: avoid;
        }

        .annotation-title {
            font-weight: bold;
            color: #2b6cb0;
            margin-bottom: 5px;
            font-size: 10pt;
        }

        .annotation p {
            margin: 0;
            font-size: 9.5pt;
            color: #2d3748;
        }
    </style>
</head>
<body>

    <div class="cover-page">
        <div class="cover-accent"></div>
        <div class="cover-title-container">
            <h1>Card Authentication System (CAS)</h1>
            <div class="subtitle">Dokumentacja Techniczna i Opis Systemu</div>
        </div>
        
        <div class="cover-details">
            <div class="meta-item"><span class="meta-label">Autorzy:</span> Kacper Gędłek, Marcin Biliński, Filip Gądek, Szymon Dębkowski</div>
            <div class="meta-item"><span class="meta-label">Wersja dokumentu:</span> 1.0.0</div>
            <div class="meta-item"><span class="meta-label">Data:</span> Czerwiec 2026 r.</div>
        </div>
    </div>

    <h2>1. Opis Ogólny Systemu</h2>
    <p>
        <strong>Card Authentication System (CAS)</strong> to zintegrowany system kontroli dostępu oparty na autoryzacji zbliżeniowych kart identyfikacyjnych (RFID / Smart Card). Architektura systemu opiera się na trójwarstwowym podziale odpowiedzialności, co zapewnia modułowość, łatwość testowania oraz potencjalne wdrożenie produkcyjne.
    </p>
    
    <div class="annotation">
        <div class="annotation-title">Adnotacja Architektoniczna</div>
        <p>System został zaprojektowany z myślą o środowiskach akademickich i korporacyjnych, umożliwiając szybkie wdrożenie zarówno na fizycznej infrastrukturze czytników, jak i testowo w środowiskach zwirtualizowanych.</p>
    </div>

    <h3>Główne Komponenty Systemu</h3>
    <ul>
        <li><strong>serverCAS:</strong> Centralna aplikacja serwerowa oparta na frameworku Spring Boot, odpowiedzialna za bazę danych użytkowników, logikę biznesową przyznawania dostępu oraz interfejs zarządzania.</li>
        <li><strong>readerUtils:</strong> Biblioteka pomocnicza w języku Java stanowiąca wrapper dla standardowego API <code>Java Smart Card I/O</code>. Umożliwia niskopoziomową komunikację z czytnikami i kartami standardu MiFare oraz ELS.</li>
        <li><strong>mockClient:</strong> Narzędzie CLI i biblioteka testowa służąca do symulacji zachowania fizycznych czytników RFID, weryfikacji połączeń sieciowych oraz testów obciążeniowych serwera bez użycia fizycznego sprzętu.</li>
    </ul>

    <h2>2. Komponent serverCAS</h2>
    <p>
        Serwer aplikacji stanowi serce systemu CAS. Przetwarza żądania autoryzacji wysyłane przez czytniki, sprawdza uprawnienia w bazie danych i zwraca jednoznaczny status autoryzacji.
    </p>

    <h3>Budowanie i Uruchomienie</h3>
    <p>Aplikacja serwerowa jest w pełni zkonteneryzowana. Do zbudowania obrazu OCI (Docker) oraz uruchomienia środowiska wraz z bazą danych wykorzystywany jest Gradle oraz Docker Compose:</p>
    <pre>./gradlew serverCAS:bootBuildImage
docker compose up</pre>

    <h3>Dostępne Interfejsy i Endpointy</h3>
    <ul>
        <li><strong>Panel Zarządzania (UI):</strong> <code>http://{configured_url}/</code> – Graficzny interfejs użytkownika służący do zarządzania bazą kart, przypisywania uprawnień użytkownikom oraz monitorowania statusu czytników w czasie rzeczywistym.</li>
        <li><strong>API Walidacji:</strong> <code>http://{configured_url}/validation</code> – Endpoint przeznaczony do komunikacji maszynowej dla czytników RFID przesyłających zapytania autoryzacyjne.</li>
    </ul>

    <h3>Specyfikacja API Walidacji</h3>
    <p>Zapytanie autoryzacyjne realizowane jest za pomocą metody <code>GET</code> (lub opcjonalnie POST w zależności od konfiguracji sieciowej) z ciałem żądania w formacie JSON.</p>

    <h4>Przykład Żądania (Request)</h4>
    <pre>GET http://localhost:8080/validation
Content-Type: application/json

{
  "readerNumber": "1111222233334444",
  "cardNumber": "5555666677778888"
}</pre>

    <h4>Przykład Odpowiedzi (Response)</h4>
    <pre>Content-Type: application/json

{
  "cardOwner": "Jane Doe",
  "readerName": "Server Room",
  "validation": "ACCESS_GRANTED"
}</pre>

    <h4>Słownik wartości pola <code>validation</code></h4>
    <table>
        <thead>
            <tr>
                <th style="width: 30%;">Wartość</th>
                <th>Opis i zachowanie systemu</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td><code>ACCESS_GRANTED</code></td>
                <td>Dostęp przyznany. Karta jest zarejestrowana i posiada aktywne uprawnienia do danego przejścia/czytnika.</td>
            </tr>
            <tr>
                <td><code>ACCESS_DENIED</code></td>
                <td>Dostęp zabroniony. Karta istnieje w bazie, ale nie posiada uprawnień do tego konkretnego punktu dostępowego lub jest zablokowana.</td>
            </tr>
            <tr>
                <td><code>ERROR</code></td>
                <td>Błąd walidacji. Zwracany w przypadku, gdy numer karty lub numer czytnika nie zostanie odnaleziony w strukturach bazy danych.</td>
            </tr>
        </tbody>
    </table>

    <h2>3. Biblioteka readerUtils</h2>
    <p>
        Moduł <code>readerUtils</code> dedykowany jest dla systemów klienckich uruchamianych bezpośrednio na urządzeniach wyposażonych w fizyczne czytniki kart. Izoluje on dewelopera od skomplikowanego, niskopoziomowego API standardu PC/SC w Javie.
    </p>

    <h3>Uruchomienie Wersji Demonstracyjnej</h3>
    <p>W celu szybkiej weryfikacji poprawności działania podłączonego czytnika można uruchomić wbudowane demo:</p>
    <pre>./gradlew runDemo</pre>
    <p>Skrypt ten wylistuje wszystkie dostępne terminale i wyświetli UID pierwszej przyłożonej karty w formacie tekstowym.</p>

    <h3>Przykład Implementacji Programistycznej</h3>
    <pre>Reader reader = new Reader();
reader.initialize();

// Pobranie listy dostępnych czytników fizycznych
System.out.println(reader.getReaders());

// Wybór pierwszego dostępnego czytnika i odczyt UID karty
reader.setReader(0);
String cardUID = reader.getUID();
System.out.println("Card UID: " + cardUID);</pre>

    <h3>Integracja z Elektronicznymi Legitymacjami Studenckimi (ELS)</h3>
    <p>
        W celu przyspieszenia rejestracji i onboardingu nowych użytkowników, biblioteka posiada dedykowaną funkcję parsowania sektorów danych zabezpieczonych w Elektronicznych Legitymacjach Studenckich (ELS). Wywołanie metody <code>getElsData()</code> pozwala na natychmiastowy odczyt danych personalnych studenta.
    </p>

    <h4>Struktura Obiektu Danych ELS (Java)</h4>
    <pre>public class elsData {
    private String name;
    private String surname;
    private String albumNumber;
}</pre>

    <div class="annotation">
        <div class="annotation-title">Ważna uwaga dotycząca Javadoc</div>
        <p>Pełna dokumentacja techniczna kodu źródłowego dla klas pomocniczych i klas sterowników może zostać wygenerowana lokalnie za pomocą polecenia: <code>./gradlew readerUtils:javadoc</code>. Wyjściowe pliki HTML zostaną zapisane w katalogu <code>build/docs/javadoc/index.html</code>.</p>
    </div>

    <h2>4. Komponent mockClient</h2>
    <p>
        Aplikacja <code>mockClient</code> służy do celów laboratoryjnych, testowych oraz wdrożeniowych. Pozwala emulować zachowanie terminala wejściowego i wysyłać zapytania sieciowe bezpośrednio do instancji <code>serverCAS</code>.
    </p>

    <h3>Interfejs Linii Poleceń (CLI)</h3>
    <p>Interaktywne narzędzie tekstowe uruchamiane jest poleceniem:</p>
    <pre>./gradlew mockClient:run</pre>
    <p>Menu konsoli udostępnia intuicyjne operacje diagnostyczne:</p>
    <ul>
        <li><strong>Server Health Check:</strong> Weryfikacja połączenia i statusu żywotności serwera CAS.</li>
        <li><strong>Inicjalizacja Symulatora Czytnika:</strong> Możliwość ręcznego zdefiniowania identyfikatora punktu dostępowego lub wygenerowania losowego ID czytnika.</li>
        <li><strong>Skanowanie Karty:</strong> Wprowadzenie konkretnego numeru karty bądź test losowy w celu natychmiastowej weryfikacji uprawnień w serwerze.</li>
        <li><strong>Statystyki:</strong> Podgląd sumarycznej liczby wykonanych zapytań w bieżącej sesji.</li>
    </ul>

    <h3>Użycie Programistyczne w Testach Automatycznych</h3>
    <p>Klasa <code>MockClient</code> może zostać zintegrowana w zewnętrznych testach integracyjnych systemu:</p>
    <pre>// Inicjalizacja z domyślnym adresem serwera (http://localhost:8080)
MockClient client = new MockClient();

// Alternatywna inicjalizacja z niestandardowym adresem URL
MockClient customClient = new MockClient("http://localhost:9090");

// Konfiguracja wirtualnego punktu dostępowego
client.getRfidSimulator().initialize("1111222233334444", "Main Gate Reader");

// Symulacja przyłożenia karty i pobranie logu komunikacji HTTP
CommunicationLog log = client.simulateScanAndValidate("5555666677778888");

// Wyświetlenie szczegółowych logów żądania i odpowiedzi HTTP
System.out.println(log.toDebugString());

// Analiza i weryfikacja biznesowa wyniku
ValidationResult result = client.getValidationService().parseResult(log);
if (result != null) {
    System.out.println("Access validation: " + result.getValidation());
    System.out.println("Card Owner: " + result.getCardOwner());
}</pre>

</body>
</html>
"""

output_pdf = "Dokumentacja_CAS.pdf"
HTML(string=html_content).write_pdf(output_pdf)
print(f"File saved successfully as {output_pdf}")
