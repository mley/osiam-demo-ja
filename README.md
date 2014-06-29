osiam-demo-ja
=============

OSIAM demo projekt für Java aktuell Artikel.

Vorraussetzungen:
* git
* Java 7
* Maven3
* Docker.io

== OSIAM Docker image bauen ==

Zuerst sicherstellen, dass Docker korrekt installiert und konfiguriert ist. Docker sollte auf einem Unix-Socket und dem TCP Socket 4243 lauschen.
Dazu muss in /etc/defaults/docker folgende Zeile stehen:

  DOCKER_OPTS="-H tcp://127.0.0.1:4243 -H unix:///var/run/docker.sock"

Dann das GIT des OSIAM Docker Image auschecken und bauen:

  git clone https://github.com/osiam/docker-image.git
  cd docker-image

  # docker image bauen
  mvn clean initialize docker:package

  # docker image taggen und ins private repo hinzufügen
  docker tag osiam-docker_osiam-dev osiam/dev

Das Docker-Image kann nun gestartet werden. Je nach Konfiguration muss docker mit root-Rechten gestartet werden (z.B. sudo docker ...)

  docker run -it -p 8080:8080 -p 5432:5432 osiam-docker_osiam-dev

Der Tomcat und der Postgresql Dienst werden dadurch auf der lokalen Maschine auf die Ports 8080 und 5432 gebunden. Damit die Demo richtig läuft, darf auf Port 8080 kein anderer Dienst laufen.

Wenn das Docker-Image erfolgreich gestartet wurde, sollte es auf der Konsole folgende Ausgabe geben:

  2014-06-27 11:00:55,445 CRIT Supervisor running as root (no user in config file)
  2014-06-27 11:00:55,445 WARN Included extra file "/etc/supervisor/conf.d/osiam.conf" during parsing
  2014-06-27 11:00:55,481 INFO RPC interface 'supervisor' initialized
  2014-06-27 11:00:55,481 WARN cElementTree not installed, using slower XML parser for XML-RPC
  2014-06-27 11:00:55,481 CRIT Server 'unix_http_server' running without any HTTP authentication checking
  2014-06-27 11:00:55,481 INFO supervisord started with pid 1
  2014-06-27 11:00:56,483 INFO spawned: 'tomcat7' with pid 8
  2014-06-27 11:00:56,485 INFO spawned: 'postgres' with pid 9
  2014-06-27 11:00:57,492 INFO success: tomcat7 entered RUNNING state, process has stayed up for > than 1 seconds (startsecs)
  2014-06-27 11:00:57,493 INFO success: postgres entered RUNNING state, process has stayed up for > than 1 seconds (startsecs)


