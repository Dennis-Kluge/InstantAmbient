# Architektur & Technologien
Nachdem sich das letzte Kapitel in einer abstrakten Form die möglichen Ansätze diskutiert und die Entscheidung für den Client lastigen Aspekt gefallen wurde, konzentrieren sich die nächsten Absätze mit der Architektur und Technologieauswahl. 
Hierbei ist wichtig zu erwähnen, dass sich die folgenden Entscheidungen auf das Proof of Concept beziehen und wohl besonders im Technologischen Umfeld diskutiert werden können.

## Gesamtarchtitektur
Wie bereits angerissen, besteht die Architektur grundlegend aus zwei großen Komponenten, den Client und Backend. Diese haben grundlegend unterschiedliche Bereiche und Anforderungen abzudecken. 

So muss beispielsweise die Android-App den Nutzer leicht zugänglich, bedienbar und verständlich sein. Die Erstellung von Konfiguration, besonders in Hinsicht der Komplexität besimmter Umgebungen und deren Umsetzung in der UI ist eine Herausforderung. Es gilt Übersicht zu bewahren und den eigentlichen Fokus nicht zu verlieren. Im Grunde handelt es sich um die Visualisierung komplexer Daten, welche anschliessend visualisiert werden. Die genaue Umsetzung wird im nächsten Kapitel erläutert. 

Das Backend ist ausschließlich mit der Verarbeitung von Konfigurationen beschäftigt, Visualisierungen spielen keine Rolle. Bei genauerer Betrachtung und Überlegung ist klar, dass diese Systeme besondere Anforderungen in Hinblick auf Verfügbarkeit, Fehlertoleranzen et cetera haben. Ausserdem müssen sie je nach Szenario in der Lage sein zu skalieren. Zudem unterscheiden sich die Kriterien je nach Umgebung. Dies ist eine der Hauptherausforderungen des Backends. 

Zunächst soll ein Überblick zur Architektur und den einzelnen Komponenten gegeben werden. Die folgende Grafik zeigt die Bestandteile und ihre Relationen untereinander:
> Grafik

Am offensichtlichsten ist die Anzahl der einzelnen Komponenten, welche innerhalb von InstantAmbient arbeiten. Des Weiteren Kommunizieren diese lediglich mit einem Vorgänger. Die Kette der Konfigurationsbereitstellung beginnt mit dem Smartphone bzw. der bereitgestellten App. Hier erstellt der User nach seinen individuellen Wünschen eine Konfiguration für unterschiedlichste Umgebungen. Soll diese beispielsweise vor der Fahrt mit einem Mietwagen übertragen werden, verbindet sich der Client mit dem Connector\footnote{In Zukunft auch InstantConnector genannt.}, wird die Konfiguration mit Hilfe von Bluetooth übertragen. Eine genauere Erlätuerung der Technikauswahl ist in den nächsten Abschnitten zu finden. Der Connector an sich ist nur dafür zuständig die Daten entgegenzunehmen und diese zu validieren. Die eigentliche prozessierung und Verteilung der Konfiguration findet im "Brain"\footnote{Zugegeben ist die Namenswahl ein wenig mutig, aber der Großteil der Backend-Logik befindet sich in diesem Modul.} statt. Für diesen Zweck überträgt der Connector die empfangene und validierte Konfiguration via TCP/IP zum Brain. Dieses unterhält mit Hilfe verschiedenen Mechanismen ein Verständnis welche Teile einer Konfigurationen zum Actor verschickt werden müssen. Hierbei wurde sich dafür entschieden, dass Aktoren immer für eine bestimmte Sektion der Konfiguration zuständig sind. Daher routet das Brain Sektionen zu einzelnen Aktoren. Es kann durchaus sein, dass ein Actor nur bestimmte Dateiformate unterstützt, daher ist im Brain eine Konvertierung beispielsweise von JSON zu XML vorgesehen.
Als letzte Komponente stehen die Aktoren, welcher vom Brain die konvertierte Sektion wieder via TCP/IP empfängt und verarbeitet. Verschiedenste Dienste können als Actor gesehen werden, sei es die Steuerung, der Heizung, der Multimedianalage oder Sicherheitssysteme im Auto.

Auf den ersten Blick scheint die hohe Anzahl der einzelnen Komponenten vielleicht für etwas übertrieben, aber gerade diese Entscheidung bürgt wesentliche Vorteile. Jede einzelne Komponente ist um ein leichtes austauschbar, besonders trifft dies auf den Connector und Actor zu. Gerade bei den Aktoren ist es nahezu logisch, da es eine Vielzahl an anzusprechenden Systemen und Protokollen gibt. Des Weiteren ist die derzeitige Entscheidung für Bluetooth keine feste, sondern können mit wenig Aufwand weitere Connectoren genutzt werden. Mehr Details dazu sind im Kapitel über das Backend zu finden. Bis auf das Brain kann bzw. muss jede weitere Komponente mehrmals existieren, genau dieser Fakt ist eine grundlegende Prämisse für skalierende konfigurationsbasierte Systeme. 

Die folgenden Abschnitte, werden die Komponenten genauer erläutern.

## Aufbau Client
## Aufbau Backend
Wie bereits im Überblick erläutert, besteht das Backend aus mehreren untereinander austauschbaren Komponenten. Die Vorteile wurden bereits diskutiert, jedoch müssen eben diese drei Komponenten konzipiert und untereinander verbunden werden.
Im folgenden wird näher auf die Komponenten eingegangen ohne auf die spezifische Implementierung einzugehen.
### Connector
Die Vorgänge innerhalb des Connectors sind unspäktakulär. Bewusst soll diese Komponente einfach und leichtgewichtig gehalten werden. Mit Hilfe von Bluetooth, genauer gesagt dem Protokoll SPP, kann der Client seine serialisierte Konfiguration übertragen. Als erster Bearbeitungsschrit wird die Validität der Daten geprüft. Korrupte Dateien sollen so früh wie möglich aussortiert werden um die Sicherheit des Systems zu gewährleisten. In der Übersicht zur Gesamtübersicht wurde bewusst ein weiterer Bearbeitungsschritt nicht genannt. In vielen Fällen möchte das Brain eventuell wissen von welchem Connector die Daten kommen. Dies gilt besonders bei Szenarien wie etwa Hotels, Konfigurationen müssen eindeutig einer Umgebung zugeordnet sein. Daher modifiziert der Connector als zweiten Schritt die Konfiguration und fügt spezifische Informationen zur Umgebung zu. In den miesten Fällen reicht eine ID zur Identifiktation. Als letzter Schritt wird das Brain mit der modifizierten Konfiguration kontaktiert und die Daten gesendet. 

### Brain
Das Brain empfängt die validie Konfiguration mit Hilfe von TCP/IP. Nahezu jedes Systems stellt diese Schnittstellen bereit, viele Systeme und Archtitekturen basieren auf diese Kommunikationsmittel.
Die Hauptaufgabe des Brain liet im Routing der Konfiguration. Hier stellen sich zwei wesentliche Fragen: Wie werden welche Daten geroutet, welche Formate besitzen diese? Und wie wird das Routing selbst abgebildet und konfiguriert? 

Wie bereits angesprochen wird bei dem Routing angenommen, dass bestimmte Aktoren für einzelne Sektionen zuständig sind. Das heisst, dass ein Aktor die Teilkonfiguration beispielsweise für das Badezimmer empfängt. Dabei können mehrere Aktoren die Teilkonfiguration empfangen. Da in vielen Szenarien davon ausgegangen werdne muss, dass nicht jeder Actor JSON spricht, sollte vor dem Senden der Daten eine mögliche Konvertierung stattfinden. Anfangs wird ebenfalls angenommen, dass die Aktoren über eine TCP/IP-Schnittstelle verfügen. 

Des Weiteren ist die wohl größte Herausforderung auf einer eleganten Art und Weise das Routing zu darzustellen. Hierfür eignen sich entweder weitere Konfigurationsdateien, welche schnell unüberischtlich werden können oder auch domainspezifische Sprachen. Um ein wenig vorwegzugreiffen wurde im Proof of conecpt sich wür die zweite Alternative entschieden. Das Routing muss im wesentliche Kennitisse über zwei Aspekte haben: 
1. Der Definition von erreichbaren Aktoren
2. Der Beschreibung welche Sektionen zu den einzelnen Aktoren gehören

Besonders das Routing kann sehr schnell eine komplexe Aufgaben werden. Die Lösung ist im 8. Kapitel zu finden. 

### Actor
Ähnlich wie der Connector handelt es sich um den Actor um einer leichtgewichtige Komponente im anbetracht der Kommunikation mit dem Brain. Die Systeme welche letztendlich damit gesteuert werden wie Heimautomatisierungsanlagen, haben zudem zusätzlich einen weiteren Grad an Komplexität. 
Im Grunde empfängt der Actor einen validen und eventuell konvertierten Teil der Konfiguration und spricht damit seine angebundenen Systeme an. Diese Komponente muss schmall und leichtgewichtig sein um die Austauschbakeit zu garantieren.

### Mögliche Erweiterungen 
Die vorgestellte Architektur ist kein starres Konstrukt, sondern auch dafür vorgesehen erweitert zu werden. So sind eine Vielzahl an weiteren Services denkbar, wie beispielsweise die Integration von Authentifizierungs- oder Webservices. 
> Bild mit Authentifizierungsservice.
Da z.B. bei der Authentifiezierung nur das Brain als wesentliche Instanz mit den Service kommunizieren muss, steigt die Komplexität nur in diesem Teil der Anwendung. Damit soll gezeigt werden, dass die Wahl mehrere Teilsysteme zu nutzen den Vorteil hat, das System einfach zu erweitern und die Bedürfnisse einzelner Szenarien anzupassen. 

## Technologien
### NFC
### Client
### Backend
