# Client
Dieses Kapitel beschäftigt sich mit dem Smartphone Client der den Projektnamen AmbientClient trägt. Hierbei wird auf die Konzipierung des Clients sowie den Ablauf einer anzulegenden Umgebung und das senden dieser eingegangen. 

## Konzipierung und Entwicklung der Benutzeroberfläche

Nach der Festlegung der Lösungsstrategie die InstantAmbient verfocht musste ein Bedienkonzept für die App erarbeitet werden. Diese soll, wie schon mehrfach angesprochen wurde, schnell verständlich und kurze Bedientere für den Benutzer bereitstellen. \\ 
Wie im Kapitel der Konfigurationen bereits angesprochen gibt es Sektionen. Da InstantAmbient sowohl für Autos als auch für Hotels funktionieren soll, gibt es eine Allgemeine Konfiguration die elementare Möglichkeiten enthält, die in beiden Umgebungen einstellbar sind. Da diese sozusagen als Globale Konfiguration dienen, werden sie mit dem Start der Anwendung abgefragt. Dies gibt den Benutzer die Möglichkeit von vornherein die ersten Einstellungen vorzunehmen und auf seinem Gerät zu speichern. 

> Mock Up Registrierung

Nachdem der Benutzer diese angelegt hat, wird er in die Umgebungsübersicht geführt. Hier sieht er alle von ihm angelegten Umgebungen. Durch das anklicken dieser kommt er auf die Detailseite der Umgebung und kann diese gegebenenfalls ändern.       
Des Weiteren hat er die Möglichkeit in der Übersicht im Menü eine neue Umgebung anzulegen oder die Allgemeine Konfiguration zu ändern. Dadurch ist diese von den Umgebungen getrennt veränderbar. Allerdings haben die Benutzer die Möglichkeit die Allgemeinen Konfigurationsdaten für eine Spezifische Umgebung zu ändern ohne die allgemeine Konfiguration zu verändern.   

> Mock Up Umgebungsübersicht 

Es gibt also drei verschiedene Hauptansichten. Die Umgebungsübersicht, die Umgebungsansicht und die Ansicht für die Allgemeine Konfiguration. Die Anwendung wurde sehr schlicht gehalten und nicht mit Auffälligen optischen Spielereien bestückt. Da es den Benutzer nicht ablenken soll sonder der Fokus auf die Einstellungen liegen. 
\\\\
Des Weiteren wurde auch eine mögliche Konzeption entwickelt wenn sich ein Benutzer zum zweiten mal in einem Hotel befindet, nur das dieses mal die Ausstattung des Zimmers mehr Möglichkeiten bietet. Hierbei sollte das Backend den Client über die neuen Möglichkeiten informieren und dieser Meldet dies dem Benutzer und gibt ihm hierbei gleich die Möglichkeit ein neues Profil für die Umgebung anzulegen. Dieses Konzept ist aber nicht in der Anwendung umgesetzt worden. Da hierbei das Ziel war eine einfache Konfiguration zu erstellen und zu Übertragen.

> Mock Up Profilerweiterung

# Umsetzung

Nach dem die Konzeption und die Benutzerführung anhand der Mock Ups stand, wurde die Anwendung entwickelt. Bei der Verwendung des SDK wurde auf die letzte verbreitete Smartphone Version genommen. Damit die Konfiguration vom Licht oder von der Temperatur eingestellt werden kann wurde ein NumberPicker verwendet. Dies führte ein Problem mit sich, auf das ich im nächsten Abschnitt eingehe. Als Datenbank wurde die von Android bereitgestellte SQLite Datenbank verwendet. Ein wichtiger Bestandteil neben der Oberfläche ist die Bluetoothschnittstelle. 
Diese wurde als Service implementiert und kann somit unabhängig von der Anwendung im Hintergrund laufen. Dies ist ein wichtiger Punkt von Instant Ambiente, da der Benutzer möglichst wenig mit dem starten des Übertragungablauf zu tun haben soll. Zumindest soll es für den Benutzer nicht offensichtlich sein. In der Prototypen wird der Service immer mit dem öffnen der Anwendung in der Umgebungsübersicht gestartet. Damit die Anwendung Bluetooth verwenden kann muss dieses zu aller erst eingeschalten werden. Dies wird bei ersten Start nachdem die Allgemeine Konfiguration angelegt wurde abgefragt und der Benutzer wird gegebenenfalls darum gebeten dieses einzuschalten. Der Service kann jederzeit erweitern und verändert werden. Dies ist ein Grund warum diese Schnittstelle als Service aufgebaut wurde. Der Service ist in der Lage eine Verbindung mit einem Backend herzustellen. Hierbei muss dieser aber den Namen dieser Einheit kennen bevor er mit diesem ein pairing durchführen kann. Sobald der Client etwas gefunden hat beginnt er eine Verbindung aufzubauen, werden die Daten aus der Datenbank geladen und als JSON Objekt an den Connector gesendet. Da dies völlig automatisch im Hintergrund passiert, erhält der Benutzer keine Rückmeldung falls die Übertragung erfolgreich war. Er erfährt nur wenn etwas nicht funktioniert hat, damit diesem Problem gegebenenfalls entgegengewirkt werden kann.         







## Probleme

Ein Problem was sich bei Android als immer wiederkehrend zeigt ist, dass bei der Konzeption von Android einige Fehler gemacht wurden. Zum Beispiel ist es möglich den in der Anwendung verwendete NumberPicker in der Grafischen Oberfläche einzubauen, doch man kann diesen dann nicht im Code einbinden da er erst ab dem API Level 11 welches auch als Android 3.0 bezeichnet wird zur offenen Verfügung steht. Diese Android Version ist allerdings nur für Tablets. Aus diesem Grund mussten extra zwei Klassen die den NumberPicker bilden eingebunden werden. Es ist ein typischen Androidphänomen das gewisse Möglichkeiten erst in späteren Versionen bereitgestellt werden, obwohl der Ansatz schon seit API Level eins vorhanden ist. Ein weiteres Problem bei Android sind die verschiedenen Benutzeranischten. Jede Anwendung verfolgt ihren eigenen Weg. Es entsteht keine Einheitliche Struktur. Der einzigen Struktur zur Navigation ist das Menü welches mittels Taste aufgerufen werden kann und die Backtaste, die einen gegebenenfalls in die letzte Ansicht zurückbringt oder die App schließt.  

## Zusammenfassung Client

Dieser Abschnitt sollte einen überblick über das Gewählte Konzept, die Benutzerführung sowie die für den Prototypen Umgesetzten Funktionen geben. Im nächsten Abschnitt folgt die Umsetzung des Backend.  