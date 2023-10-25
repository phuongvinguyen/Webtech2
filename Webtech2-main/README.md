# Fitter - Deine Schrittzähler Community!

Teile deine Stats mit Freunden und vergleicht eure Ergebnisse miteinander.

- Erstellen von täglichen Posts mit Schritten.
- Reaktionen über Emojis (Können auch wieder entfernt werden)
- Man kann Usern folgen (follow) und sieht dabei auf der Startseite die Posts dieser User.
- Globale Statistiken sind einsehbar
  - Bisher nur Leaderboard fertig, weiteres geplant.
- Security Model:
  - Accountpflicht, nach außen sind keine Informationen sichtbar
  - User können eigene Posts bearbeiten, Password kann geändert oder Profil gelöscht werden.
  - Admins können all diese Funktionen auch für andere User ausführen.

## Technologien

- Datenbank H2DB, bislang nur in-memory getestet.
- JPA mit Hibernate
- Spring Anwendungsserver
- Apache Shiro
- Angular-Frontend

## Anwendung

- Es lässt sich ein WAR-Archiv mit dem Maven-Profil ``with-frontend`` und der lifecycle-phase `install` erzeugen. Dieses beinhaltet _backend_ und _frontend_ und lässt sich mit **Java 11** ausführen.
- Erreichbar unter http://ls5vs019.cs.tu-dortmund.de:9001/