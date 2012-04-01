# Das Backend
Dieses Kapitel beschäftigt sich mit der Umsetzung der Beispielimplementierung, deren Herausforderungen und Probleme. Die drei aufgeführten Komponenten sind auch in sich autarke Programme, welches die Austauschbarkeit und Weitereintwicklung fördert. Jedes Programm wurde als Command Line Tool entwickelt um die Möglichkeit zu geben Parameter zu variieren und es auf unterschiedlichsten Plattformen laufen zu lassen. Von dem Gedanken ebenfalls eine GUI bereitzustellen wurde abstand genommen, da die fertigen Produktivsysteme diese Anforderung nicht haben. Des Weiteren gilt es zunächst zu überprüfen ob die an das Backend gestellten Anforderungen in einen Prototypen umsetzbar sind. 

## InstantConnector
Der Connector stellt einen Bluetooth-Service für den Android-Client bereit. 
Es wird der Bluetooth-Standard der Version 2.1 eingesetzt und das SPP-Protokoll verwendet. Mit Hilfe von SPP lassen sich Daten über eine emulierte serielle Schnittstelle übertragen. Dies ist die etablierteste Möglichkeit des Datenaustauschs. Bluetooth bietet weiterhin den Vorteil integrierter kryptografischer Methoden und Pairing. 
Mit dem JSR-62 wurde eine einheitliche Spezifikation für Bluetooth-Services definiert, welcher von unterschiedlichsten Bibliotheken implementiert wurde. Die bereits genannte Bluecove-Library ist die einzige, welche nicht in aktiver Pflege ist, d.h. es kommt zu regelmäßigen Bugfixes, neue Features kamen in den letzten Jahren nicht hinzu. Ein weiterer Vorteil der Java-Spezifikation ist, dass diese gegenüber anderen Frameworks gut dokumentiert. Alternativen wie etwa die von Mac OS X mitgelieferten Bibliotheken sind ebenfalls spärlich gepflegt und verfügen über schlechte Dokumentationen. 

JRuby stellt eine Implementierung der bekannten Programmiersprache Ruby für die JVM dar. Eine der größten Vorteile neben der Unterstützung nativer Threads ist das Binding mit Java-Klassen. So ist es möglich aus JRuby heraus mit bereits existierender Java-Infrastrukturen zu kooperieren, ohne Einschränkungen. Die Bereitstellung des Environments beinhaltet folgende Abhängigkeiten:
* JRuby 1.6.7 und Java 6
* Bluecove 2.1
* JSON Pure [Ruby-Library]

Zunächst muss sich der Connector selbst als Service annoncieren, hierfür muss dieser einen SPP-Service bereitstellen. Dieser Service definiert sich durch eine zufällig generierte UUID und Namen, wodurch eine eindeutige Zuordnung möglich ist. 
> uuid = UUID.new("1101", true)
	connection_string = "btspp://		  localhost:#{uuid};name=AmbientConnector";	
Mit diesen Mechanismen wird eine Identifikation des Connectors sichergestellt und es ist sichergestellt, dass Verbindungen möglich sind. Anschliessend kann der Service innerhalb in einer Eventloop Connections entgegennehmen und Konfigurationen empfangen. Nach dem Verbindungsaufbau, wird die Integrität der Konfiguration geprüft, dafür wird diese geparst um eventuelle Fehler festzustellen. Dieser Schritt ist ebenfalls notwendig um zusätzliche Informationen des Connectors, wie dessen ID der Konfiguration hinzuzufügen. Wenn diese Schritte erfolgreich sind, werden diese dem Client mit einem "ACCEPT" quittiert. 
Als letzter Schritt wird über den TCP-Socket die valide Konfiguration an das Brain gesendet. 
Sollte es zu Fehlern bei der Bearbeitung kommen, wird dies dem Client mit einem "ERROR" mitgeteilt. Derzeit sind noch keine Mechanismen zur genaueren Fehlerbehandlung wie etwa die bekannten HTTP-Status-Codes vorgesehen. Dies sollte erst angesehen werden, wenn innerhalb von bestimmten Anwendungsszenarien vermehrt zu Fehlern kommt. 

Während der Umsetzung dieser Komponente kam es vermehrt zu Problemen bei der Portierung des Codes von Java zu JRuby. Dies war zum größten Teil schlechter Beispielanwendungen in Java geschuldet\footnote{Besonders die schlechte Umgang mit Import-Statements ist auffallend.}. Des Weiteren bestand ein gewisser zeitlicher Aufwand in die Einarbeitung des Bluetooth-Standards und dessen Möglichkeiten. 

## InstantBrain
Das Gehirn des Backends nimmt die vom Connector bereitgestellte Konfiguration entgegen, lässt diese mit einem Routing-Algorithmus  vergleichen und schickt diese zu den zuständigen Aktoren. 
Das InstantBrain ist mit Abstand die aufwändigste Komponente des Backends und zeichnet sich besonders durch zwei Herausforderungen aus: 
1. Wie ist ein gewisser Grad der Parallelität zu gewährleisten? 
2. Mit welchen Mechanismen lässt sich ein elegantes Routing bereitstellen? 
Die erste Frage ergibt sich aus der Situation, dass sich mehrere Connectors gleichzeitig mit dem Brain verbinden können. In Umgebungen wie etwa einem Auto stellt dies kein besonderes Problem da, wird jedoch die gleiche Komponente in einem großen Hotel eingesetzt, ist ein gewisser Grad der Skalierbarkeit notwendig. Die zweite Herausforderung, ist neben des Entwurfs und Definition der Konfiguration dessen Routing. Besonders die programmatische Beschreibung und Implementierung dessen. 

Wenden wir uns zunächst den ersten Problem zu. Bei genauerer Betrachtung stehen alle verteilten Anwendungen vor dem Problem mit hohen Lasten umzugehen. Eine Vielzahl bereits bestehender Frameworks nimmt sich dieser Thematik an, besonders unter dem Stichwort Non-Blocking IO. Im Grunde geht es bei dieser Technik darum, dass alle Anfragen asynchron und ohne Blockierung des Systems bearbeitet werden. In der Ruby-Welt hat sich im besonderen das Framework Eventmachine dieser Thematik angenommen. Das Arbeitsprinzip ist relativ einfach, innerhalb einer Eventloop werden Verbindungen entgegengenommen. Die Bearbeitung findet innerhalb einer zuvor implementierten Klasse oder Moduls statt, welches eine Connection repräsentiert. Im Grunde wird lediglich ein Interface bereitgestellt, dass das Programm nutzt und die Daten bearbeitet. 
> EventMachine.run {
		  EventMachine.start_server('127.0.0.1', 8081, BrainConnection, @broker)
		  puts "Starting the brain..."
		}
Mit Hilfe dieser Mittel, ist es möglich ohne weiteres mehrere hundert Verbindungen pro Sekunde zu verarbeiten. 

Die Lösung der Routing Problematik kann über die Weiterführung oder den Fall des Projektes entscheiden. Vor der Implementierung gilt es zu überlegen, welchen Mechanismen das Routing folgen soll. Diese Thematik wurde bereits im Abschnitt über die Architektur angerissen. Bei dem Routing geht es darum die von einem Connector empfangene Konfiguration an die jeweils dafür zuständigen Actors weiterzuleiten. Dies ist dafür notwendig, da unterschiedlichste Systeme für die Bereitstellung einzelner Konfigurationswünsche zuständig sind. Hierfür wird sich eine Eigenheiten der einzelnen Konfigurationen zu Nutze gemacht. Es wird angenommen, dass jeder Actor für eine bestimmte Sektion der Konfiguration zuständig ist. Beispielsweise gibt es einen Actor, welcher das Wohnzimmer steuert und die Daten aus der Sektion "living_room" empfängt. Die Frage ist wie diese Zusammenhänge in einer möglichst eleganten Form abgebildet werden können? 
In vielen Fällen kann dies Fest im Code verankert werden, verhindert jedoch einen großen Grad an Flexibilität im Code. Eine weitere Beschreibung könnte mit Hilfe von Konfigurationsdateien und einer versuchten Abbildung der Zusammenhänge geschehen. Die Darstellung der Routing-Informationen auf textueller Ebene ist leicht zu definieren: 
> Empfänger Wohnzimmer, mit Adresse 127.0.0.1 und Port 8081, kann mit XML-Dateien umgehen.

	Die Sektion "living_room" soll an das Wohnzimmer gesendet werden.

Wie zu sehen ist eine textuelle Definition des Routings im Grunde leicht verständlich und erweiterbar. Jedoch ist es für ein Programm schwer genau diese Informationen zu parsen. Genau in diesem Punkt kann Ruby seine Stärken ausspielen und es wird klar, warum gerade die Wahl auf diese Programmiersprache fiel. Eines der größten Vorteile ist die Möglichkeit sogenannte interne domänenspezifische Sprachen zu implementieren. Eine DSL ist nichts anderes eine Beschreibungssprache die sich einer wohl definierten Problematik (Domäne) annimmt. Diese Sprache hat noch den Anspruch Turing vollständig zu sein, dennoch zeichnen sich interne DSLs dadurch auch den kompletten Sprachumfang ihrer Wirtsumgebung zu nutzen. So lässt sich das Routing innerhalb einer DSL folgendermaßen beschreiben: 
> @broker = Broker.new do 

		receiver :living_actor, :address => "127.0.0.1", :port => 9123, :format => :xml, :secure => true
		receiver :bedroom_actor, :address => "127.0.0.1", :port => 9124, :format => :xml
		section :living_room do
			to :living_actor
		end

		section :bedroom do
			to :bedroom_actor
		end

	end
Auch ohne Kenntnisse über DSLs oder gar Ruby ist dieses Beispiel verständlich und lehnt sich an die textuelle Beschreibung an. Zunächst werden einzelner Empfänger definiert, welche über einen Namen und zusätzliche Attribute verfügen. Diese umfassen die Adresse, den Port, das Datenformat welche sie sprechen und zusätzliche Optionen. Als nächster Schritt wird definiert welche Sektion eine Konfiguration den Empfängern zugesendet werden soll. Dabei kann eine Sektion durchaus an mehrere Empfänger gesendet werden. Die Implementierung des Routings erfolgt genau der hier gezeigten DSL, diese ist verständlich, erweiterbar und nutzt die Vorteile zwischen eine textuellen und programmatischen Beschreibung. 

Die interne Repräsentation des Routings wird innerhalb des Brokers dargestellt, dieser hält ein Array an Receivern vor. Jede Sektion wird durch ihren Namen und ebenfalls aus einem Array der Empfänger definiert. Wird eine Konfiguration empfangen werden alle Sektionen dieser mit denen des Brokers abgeglichen und an die entsprechenden Receiver gesendet, sollte es zu keinen Übereinstimmungen kommen, wird die Sektion ignoriert. Bevor die Daten einer Sektion den Actor zugesendet werden können, müssen diese je nach gesetzten Parametern eventuell in ein anderes Format konvertiert werden. Hierfür stellt das Brain derzeit die Möglichkeit von JSON, XML und Plain Text bereit. 

Die Eleganz der internen DSLs bringt auch eine Schattenseite mit sich. Während der Implementierung kann es zu einer Vielzahl von Fehlern kommen. Daher sollte in diesem Falle ausschließlich Test-Driven entwickelt werden. Durch die Beschreibung einzelner Testfälle, finden sich genaue Spezifizierungen und Garantieren über die Richtigkeit der Implementierung. Innerhalb von AmbientBrain wurde für das Testing RSpec genutzt, welches ein Ruby-Framework für das sogenannte Behavior Driven Development\footnote{Kurz BDD ist.}. So sehen die Spezifikation für den Broker folgendermaßen aus: 
>describe Broker do
	it "holds no sections"
	it "holds several sections"
	it "adds several receivers to a section" 
	it "holds receivers" 
	it "delegates valid configurations to actors" 
	it "handles invalid configurations as false" 
	it "procceses a valid configuration" 
	it "finds a receiver" 
end

Jede einzelne und mit "it" beginnende Zeile beherbergt einen entsprechenden Test auf die getroffene Aussage. Die Test-Coverage des Brains beträgt 80%. Besonders für eine Komponente, welche stabil uns zuverlässig arbeiten soll, sind diese Maßnahmen notwendig. 

Wie zu sehen ist, hat bereits bei der prototypischen Implementierung das Brain eine gewisse Komplexität erreicht. Besonders das Design, die Umsetzung und interne Datenhalterung der DSL brachten Probleme mit sich. Besonders aus diesen Grund wurde ausschließlich Test-Driven entwickelt. Mit Unterstützung von Eventmachine steht das AmbientBrain auf soliden Füßen. 



## InstantActor 
Gegenüber dem Brain ist der Actor eine sehr schmale Teilkomponente und setzt auf bereits erwähnte Technologien auf. 
Die Aufgabe des Actors ist die empfangenen Konfigurationseinstellungen umzusetzen. Zur Bereitstellung des Servers, wird ebenfalls wie im Brain Eventmachine genutzt. Der InstantActor nimmt lediglich Daten im XML-Format entgegen. Die soll demonstrieren, dass zwischen Brain und Actor die verwendeten Serialisierungsformate keine Rolle spielen. Das valide XML wird geparst und die Einstellungen über eine serielle Schnittstelle an einen an den Rechner verbundenen Arduino gesendet. Dieser unterhält die Möglichkeit eine RGB-LED, sowie eine Single Color LED zu steuern. Die ist die letzte Station der Konfigurationsbereitstellung und macht die am Smartphone erstellte Konfiguraion sichtbar. Die Implementierung selbst ist trivial die von der XML-Datei bereitgestellten und geparsten Daten werden über eine serielle Schnittstelle zum Arduino gesendet. Die Vorgänge wurden bereits in den vorherigen Komponenten beschrieben. Zu größeren Problemen kam es nicht. 

## Zusammenfassung der Backend-Implementierung
Die Konzeption und Implementierung des Backends brachten in einigen teilen besonders bei der Bluetooth-Kommunikation und dem Konfigurationsrouting einige Probleme und Herausforderungen mit sich. Jedoch kann zusammenfassend gesagt werden, dass alle Systeme den Anforderungen gemäß implementiert worden sind und lauffähig sind. Des Weiteren wurde ein erster Schritt in Richtung Distribution mit Hilfe der Command Line Tools gegangen. Für das Backend wurde ein solider Grundstein gelegt. 
