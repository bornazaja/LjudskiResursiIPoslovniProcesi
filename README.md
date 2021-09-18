# Ljudski resursi i poslovni procesi
Izrada završnog rada na temu Aplikacija za unapređenje rada odjela ljudskih resursa. Aplikacija se bavi obračunima ugovora o radu, ugovora o djelu te studentskih ugovora.

### Tehnologije
* Java
* Spring Framework
* Hibernate ORM
* MS SQL Server
* JavaFX

### Zahtjevi
* Instaliran OpenJDK 11
* Instaliran OpenJFX 11
* Instaliran Netbeans 11.1
* Instaliran MS SQL Server 2012 ili više
* Instaliran MS SQL Server Managament Studio 2012 ili više

### Upute
* Preuzeti i *unzipati* projekt
* Pokrenuti SQL skriptu u MS SQL Server Managament Studiu koja se nalazi u mapi `LjudskiResursiIPosolvniProcesi\LjudskiResursiIPoslovniProcesiLibrary\sql\LjudskiResursiIPoslovniProcesiSkripta.sql`
* Po potrebi promjeniti `DataSource` koji se nalazi u projektu `LjudskiResursiIPoslovniProcesiLibrary` u *packagu* `com\bzaja\ljudskiresursiiposlovniprocesilibrary\config` u klasi `AppConfig.java` u metodi `dataSource()`
* U NetBeansu na projekt `LjudskiResursiIPoslovniProcesiJavaFX` dodati projekt `LjudskiResursiIPoslovniProcesiLibrary` (ukoliko nije) (desni klik na `Libraries` od projekta -> `Add Project...`)
* U projektu `LjudskiResursiIPoslovniProcesiJavaFX` potrebno je otići u `Properties` -> `Run` -> `VM options`, tamo gdje se nalazi `module-path "..."` tekst koji je u navodnjacima zamijeniti putanjom koja vodi do *java fx librarya*, npr.: `C:\Users\John\Documents\Java\Versions\11\javafx-sdk-11.0.2\lib`
* Pokrenuti `LjudskiResursiIPoslovniProcesiJavaFX` projekt u NetBeansu
* Podaci za prijavu zaposlenika: 
  * Email: miro.miric@gmail.com
  * Lozinka: Lozinka123@
  
### Napomena
* Aplikacija će vjerovatno raditi na starim i novim verzijama od Jave 11, neovisono da li se radi o Oracle ili OpenJDK verziji.
* Ukoliko će aplikacija biti isprobana u starijim ili novijim verzijama od Jave 11, potrebno je NetBeansu, promijenti JDK target na način: desni klik na projekt -> `Properties` -> `Sources` -> `Soruce/Binary Format`.
* Za OpenJDK 11 ili više potrebno je instalirati OpenJFX 11 ili više
* OpenJDK i OpenJFX moraju imati iste verzije, npr.: ukoliko je instaliran OpenJDK 12, potrebno je postaviti OpenJFX 12.

### Instalacija OpenJDK na Windowsima
1. Preuzeti, *unzipati* te prebaciti mapu `jdk-11` u `C:\Program Files\Java` (kreirati `Java` mapu ukoliko nije)
2. Postavljanje putanje
   * Otići u `Control Panel` -> `System and Security` -> `System` -> `Advanced system settings` -> `Environment Variables...`
   * Zatim pod `System variables` potrebno je pronaći `Path` varijablu te odabrati opciju `Edit...`, zatim `New` te *copy-pasteati* putanju `C:\Program Files\Java\jdk-11\bin` te pritisnuti `OK`.
3. Postavljanje JAVA_HOME
   * Pod `System variables` odabrati opciju `New...` te pod `Variable name:` unjeti `JAVA_HOME`, a pod `Variable value:` *copy-pasteati* putanju `C:\Program Files\Java\jdk-11` te pritisnuti `OK`.
4. Provjera instalacije
   * Kako bi se provjerilo da li je Java instalirana, potrebno je otvoriti `CMD` te unjeti naredbu `java --version`.


### Postavljanje OpenJFX 11 ili više
  1. Potrebno je kreirati *library* (ukoliko nije), postupak u NetBeansu:
  2. U opciji -> `Tools` -> `Libraries` -> `New Library` -> `Add JAR/Folder`
  3. Otići u mapu gdje se nalazi OpenJFX u  putanji `.\javafx-sdk-11\lib` te odabrati sve datoteke osim `src.zip`
  4. Zatim u zadani projekt napraviti desni na `Libraries` od projekta -> `Add Library...` te odabrati kreirani *library*