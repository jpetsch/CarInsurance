# Smile Car-Salesflow – Kotlin Multiplatform

Ein kleiner, lauffähiger Ausschnitt eines Kfz-Versicherungs-Salesflows: Fahrzeug suchen und Variante auswählen, Fahrer- und Versicherungsangaben erfassen und ein Angebot mit Prämie und Deckungen anzeigen. Die App verwendet ausschliesslich den Smile-INT-Endpunkt und ist für fiktive Testdaten gedacht. Ein Versicherungsabschluss ist nicht implementiert.

## Voraussetzungen

- Android Studio mit Android SDK 37
- JDK 21 (Android Studio Runtime oder separates JDK)
- Android-Emulator oder Android-Gerät
- Für iOS zusätzlich macOS mit Xcode

## Starten

### Android

1. Repository in Android Studio öffnen und Gradle-Synchronisierung abschliessen.
2. Einen Android-Emulator oder ein Gerät starten.
3. Die Run-Konfiguration `androidApp` auswählen und starten.

Alternativ unter Windows im Repository-Verzeichnis:

```powershell
.\gradlew.bat :androidApp:installDebug
```

### iOS

Auf macOS `iosApp/iosApp.xcodeproj` in Xcode öffnen, einen Simulator auswählen und das Scheme `iosApp` starten. Das iOS-Ziel kann unter Windows nicht gebaut oder ausgeführt werden.

## Technische Entscheidungen

- **Kotlin Multiplatform und Compose Multiplatform:** UI, Domain-, Data- und Präsentationslogik liegen in `shared` und werden von Android und iOS gemeinsam verwendet. So bleiben Flow und Validierung plattformübergreifend konsistent; plattformspezifisch bleiben App-Einstieg und HTTP-Engine.
- **Schichten:** Domain-Modelle, Repository-Interfaces und Use-Cases sind von den Ktor-Services und DTOs getrennt. Repository-Implementierungen übersetzen Transport-/Antwortfehler in Domain-Ergebnisse. Koin verdrahtet die Abhängigkeiten.
- **API und Daten:** Fahrzeug-, Konfigurations-, Stadt- und Empfehlungsdaten werden über die Smile-INT-API geladen. Fahrzeug-Suchergebnisse behalten ihre Gruppenstruktur. Für die Stadtauflösung werden die Eingaben entprellt; erforderliche Formulareingaben werden vor dem Erstellen der Empfehlung validiert.
- **UI:** Compose Multiplatform bildet die drei Schritte Fahrzeugauswahl, Fahrerinformationen und Empfehlung ab. Die Nationalität ist mit „Schweiz“ vorbelegt; es werden keine echten Personendaten benötigt.
- **Scope:** Die Empfehlung wird angefragt und angezeigt. Änderungen an Deckungen, ein Vertragsabschluss, Persistenz und produktionsreife Authentifizierung sind nicht Teil dieses Prototyps.

## Tests

Die gemeinsamen Tests umfassen Compose-UI-Tests sowie Domain-, Repository- und ViewModel-Tests. Dazu gehören Jahresgrenzen, API-Mapping und Fehlerfälle, Such-/PLZ-Debounce, Eingabevalidierung und die Erstellung der Empfehlungsanfrage. Netzwerkantworten der Repository-Tests werden mit Ktor MockEngine simuliert.

Android-Tests auf einem verbundenen Emulator ausführen:

```powershell
.\gradlew.bat :shared:connectedAndroidDeviceTest
```

Zuletzt wurden **51 gemeinsame Android-Tests** auf einem Emulator mit API 37 erfolgreich ausgeführt. iOS-Tests benötigen macOS und wurden hier nicht ausgeführt.

## Offene Punkte und nächste Schritte

- Deutsche Variablen der KI Entfernen. An der API mit @SerialName arbeiten um eigene Variablen wählen zu können.
- Verhalten in Fehlerfällen weiter Analyiseren und den Code ggfs härten
- Deckungen anpassen und die Empfehlung neu berechnen
- Offerte Erstellen
- Prüfung auf Barrierefreiheit

## AI-Einsatz

Für die Umsetzung wurde **GitHub Copilot** als Coding-Assistent eingesetzt. Besonders hilfreich war Copilot in folgenden Bereichen:

1. **Boilerplate-Code:** Unterstützung beim Erstellen wiederkehrender Klassenstrukturen in Domain und Data sowie bei der Analyse der Postman 
   Collection als auch der Anbindung der APIs.
2. **UI- und Unit-Tests:** Unterstützung beim Erstellen von Tests für Screens und Komponenten sowie für Domain-, Data- und ViewModel-Logik.
3. **Code Review:** Unterstützung beim Prüfen der bestehenden Implementierung und beim Erkennen möglicher Verbesserungen.
4. **`CarDetails`-Komponente:** Unterstützung bei der Umsetzung der Komponente zur Darstellung der Fahrzeugdetails.
5. **Driver-Information-UI:** Unterstützung bei der Umsetzung der vollständigen UI für die Erfassung der Fahrer- und Versicherungsangaben.
6. **Dokumentation:** Unterstützung beim Erstellen dieser `README.md`.

## Zeitaufwand

**Tatsächlich investierte Zeit:** `8 Stunden`
