USE master
GO
CREATE DATABASE LjudskiResursiIPoslovniProcesi
COLLATE Croatian_CI_AS
GO
USE LjudskiResursiIPoslovniProcesi
GO

CREATE TABLE Odjel
(
IDOdjel INT CONSTRAINT PK_IDOdjel PRIMARY KEY IDENTITY NOT NULL,
Naziv NVARCHAR(50) NOT NULL
)

GO

CREATE TABLE RadnoMjesto
(
IDRadnoMjesto INT CONSTRAINT PK_IDRadnoMjesto PRIMARY KEY IDENTITY NOT NULL,
Naziv NVARCHAR(50) NOT NULL,
OdjelID INT CONSTRAINT FK_RadnoMjesto_IDOdjel FOREIGN KEY REFERENCES Odjel(IDOdjel) NOT NULL
)

GO

CREATE TABLE Valuta
(
IDValuta INT CONSTRAINT PK_IDValuta PRIMARY KEY IDENTITY NOT NULL,
Naziv NVARCHAR(3) NOT NULL,
Jedinica INT NOT NULL,
SrednjiTecaj MONEY NOT NULL,
DatumTecaja DATE NOT NULL,
JeAktivna BIT NOT NULL
)

GO

CREATE TABLE Drzava
(
IDDrzava INT CONSTRAINT PK_IDDrzava PRIMARY KEY IDENTITY NOT NULL,
Naziv NVARCHAR(50) NOT NULL,
ValutaID INT CONSTRAINT FK_Drzava_IDValuta FOREIGN KEY REFERENCES Valuta(IDValuta),
JeDomovina BIT NOT NULL,
)

GO

CREATE TABLE Grad
(
IDGrad INT CONSTRAINT PK_IDGrad	PRIMARY KEY IDENTITY NOT NULL,
Naziv NVARCHAR(50) NOT NULL,
Prirez DECIMAL(10,2),
DrzavaID INT CONSTRAINT FK_Grad_IDDrzava FOREIGN KEY REFERENCES Drzava(IDDrzava) NOT NULL
)

GO

CREATE TABLE VrstaPoreza
(
IDVrstaPoreza INT CONSTRAINT PK_IDVrstaPoreza PRIMARY KEY IDENTITY NOT NULL,
Naziv NVARCHAR(50) NOT NULL
)

GO

CREATE TABLE Porez
(
IDPorez INT CONSTRAINT PK_IDPorez PRIMARY KEY IDENTITY NOT NULL,
VrstaPorezaID INT CONSTRAINT FK_Porez_IDVrstaPoreza FOREIGN KEY REFERENCES VrstaPoreza(IDVrstaPoreza) NOT NULL,
Stopa DECIMAL(10,2) NOT NULL,
MinOsnovica MONEY NOT NULL,
MaxOsnovica MONEY,
DrzavaID INT CONSTRAINT FK_Porez_IDDrzava FOREIGN KEY REFERENCES Drzava(IDDrzava) NOT NULL
)

GO

CREATE TABLE ParametriZaObracunPlace
(
IDParametriZaObracunPlace INT CONSTRAINT PK_IDParametriZaObracunPlace PRIMARY KEY IDENTITY NOT NULL,
OsnovniOsobniOdbitak MONEY NOT NULL,
OsnovicaOsobnogOdbitka MONEY NOT NULL,
LimitGodisnjegIznosaZaStudenta MONEY NOT NULL,
ValutaID INT CONSTRAINT FK_ParametriZaObracunPlace_IDValuta FOREIGN KEY REFERENCES Valuta(IDValuta) NOT NULL
)

GO

CREATE TABLE PodaciOTvrtki
(
IDPodaciOTvrtki INT CONSTRAINT PK_IDPodaciOTvrtki PRIMARY KEY IDENTITY NOT NULL,
Naziv NVARCHAR(50) NOT NULL,
Ulica NVARCHAR(50) NOT NULL,
GradID INT CONSTRAINT FK_PodaciOTvrtci_IDGrad FOREIGN KEY REFERENCES Grad(IDGrad) NOT NULL,
OIB NVARCHAR(11) NOT NULL,
Email NVARCHAR(100) NOT NULL,
BrojTelefona NVARCHAR(12) NOT NULL
)

GO

CREATE TABLE Rola
(
IDRola INT CONSTRAINT PK_IDRola PRIMARY KEY IDENTITY NOT NULL,
Naziv NVARCHAR(50) NOT NULL
)

GO

CREATE TABLE Spol
(
IDSpol INT CONSTRAINT PK_IDSpol PRIMARY KEY IDENTITY NOT NULL,
Naziv NVARCHAR(50) NOT NULL
)

GO

CREATE TABLE Zaposlenik
(
IDZaposlenik INT CONSTRAINT PK_IDZaposlenik PRIMARY KEY IDENTITY NOT NULL,
Ime NVARCHAR(50) NOT NULL,
Prezime NVARCHAR(50) NOT NULL,
SpolID INT CONSTRAINT FK_Zaposlenik_IDSpol FOREIGN KEY REFERENCES Spol(IDSpol) NOT NULL,
DatumRodjenja DATE NOT NULL,
OIB NVARCHAR(11) NOT NULL,
Email NVARCHAR(100) NOT NULL,
Lozinka NVARCHAR(100) NOT NULL,
BrojTelefona NVARCHAR(12) NOT NULL
)

GO

CREATE TABLE Prebivaliste
(
IDPrebivaliste INT CONSTRAINT PK_IDPrebivaliste PRIMARY KEY IDENTITY NOT NULL,
Ulica NVARCHAR(50) NOT NULL,
GradID INT CONSTRAINT FK_Prebivaliste_IDGrad FOREIGN KEY REFERENCES Grad(IDGrad) NOT NULL,
ZaposlenikID INT CONSTRAINT FK_Prebivaliste_IDZaposlenik FOREIGN KEY REFERENCES Zaposlenik(IDZaposlenik) NOT NULL,
DatumOd DATE NOT NULL
)

GO

CREATE TABLE ZaposlenikRola
(
IDZaposlenikRola INT CONSTRAINT PK_IDZaposlenikRola PRIMARY KEY IDENTITY NOT NULL,
ZaposlenikID INT CONSTRAINT FK_ZaposlenikRola_IDZaposlenik FOREIGN KEY REFERENCES Zaposlenik(IDZaposlenik) NOT NULL,
RolaID INT CONSTRAINT FK_ZaposlenikRola_IDRola FOREIGN KEY REFERENCES Rola(IDRola) NOT NULL
)

GO


CREATE TABLE VrstaDavanja
(
IDVrstaDavanja INT CONSTRAINT PK_IDVrstaDavanja PRIMARY KEY IDENTITY NOT NULL,
Naziv  NVARCHAR(100) NOT NULL,
StopaNaPlacu DECIMAL(10,2) NOT NULL,
StopaIzPlace DECIMAL(10,2) NOT NULL,
VrijediDo DATE
)

GO

CREATE TABLE Davanje
(
IDDavanje INT CONSTRAINT PK_IDDavanje PRIMARY KEY IDENTITY NOT NULL,
VrstaDavanjaID INT CONSTRAINT FK_Davanje_IDVrstaDavanja FOREIGN KEY REFERENCES VrstaDavanja(IDVrstaDavanja) NOT NULL,
ZaposlenikID INT CONSTRAINT FK_Davanje_IDZaposlenik FOREIGN KEY REFERENCES Zaposlenik(IDZaposlenik) NOT NULL,
DatumOd DATE NOT NULL,
DatumDo DATE,
)

GO


CREATE TABLE VrstaOlaksice
(
IDVrstaOlaksice INT CONSTRAINT PK_IDVrstaOlaksice PRIMARY KEY IDENTITY NOT NULL,
Naziv NVARCHAR(100) NOT NULL,
Koeficjent DECIMAL(10,2) NOT NULL,
VrijediDo DATE
)

GO

CREATE TABLE Olaksica
(
IDOlaksica INT CONSTRAINT PK_IDOlaksica PRIMARY KEY IDENTITY NOT NULL,
VrstaOlaksiceID INT CONSTRAINT FK_Olaksica_IDVrstaOlaksice FOREIGN KEY REFERENCES VrstaOlaksice(IDVrstaOlaksice) NOT NULL,
ZaposlenikID INT CONSTRAINT FK_Olaksica_IDZaposlenik FOREIGN KEY REFERENCES Zaposlenik(IDZaposlenik) NOT NULL,
DatumOd DATE NOT NULL,
DatumDo DATE
)

GO

CREATE TABLE StudentskiPosaoCjenik
(
IDStudentskiPosaoCjenik INT CONSTRAINT PK_IDStudentskiPosaoCjenik PRIMARY KEY IDENTITY NOT NULL,
Naziv NVARCHAR(50) NOT NULL,
CijenaPoSatu MONEY NOT NULL,
ValutaID INT CONSTRAINT FK_StudenskiPosaoCjenik_IDValuta FOREIGN KEY REFERENCES Valuta(IDValuta) NOT NULL
)

GO

CREATE TABLE VrstaUgovora
(
IDVrstaUgovora INT CONSTRAINT PK_IDVrstaUgovora PRIMARY KEY IDENTITY NOT NULL,
Naziv NVARCHAR(50) NOT NULL
)

GO

CREATE TABLE RadniOdnos
(
IDRadniOdnos INT CONSTRAINT PK_IDRadniOdnos PRIMARY KEY IDENTITY NOT NULL,
Naziv NVARCHAR(50) NOT NULL,
VrstaUgovoraID INT CONSTRAINT FK_IDRadniOdnos_ID_VrstaUgovora FOREIGN KEY REFERENCES VrstaUgovora(IDVrstaUgovora) NOT NULL,
)

GO

CREATE TABLE Ugovor
(
IDUgovor INT CONSTRAINT PK_IDUgovor PRIMARY KEY IDENTITY NOT NULL,
RadniOdnosID INT CONSTRAINT FK_Ugovor_IDRadniOdnos FOREIGN KEY REFERENCES RadniOdnos(IDRadniOdnos) NOT NULL,
ZaposlenikID INT CONSTRAINT FK_Ugovor_IDZaposlenik FOREIGN KEY REFERENCES Zaposlenik(IDZaposlenik) NOT NULL,
RadnoMjestoID INT CONSTRAINT FK_Ugovor_IDRadnoMjesto FOREIGN KEY REFERENCES RadnoMjesto(IDRadnoMjesto) NOT NULL,
DatumOd DATE NOT NULL,
DatumDo DATE
)

GO

CREATE TABLE UgovorORadu
(
IDUgovorORadu INT CONSTRAINT PK_IDUgovorORadu PRIMARY KEY FOREIGN KEY REFERENCES Ugovor(IDUgovor) NOT NULL,
BrojRadnihSatiTjedno DECIMAL(10,2) NOT NULL,
BrutoPlaca MONEY NOT NULL,
ValutaID INT CONSTRAINT FK_UgovorORadu_IDValuta FOREIGN KEY REFERENCES Valuta(IDValuta) NOT NULL
)

GO

CREATE TABLE StudentskiUgovor
(
IDStudentskiUgovor INT CONSTRAINT PK_IDStudentskiUgovor PRIMARY KEY FOREIGN KEY REFERENCES Ugovor(IDUgovor) NOT NULL,
StudentskiPosaoCjenikID INT CONSTRAINT FK_ID_StudentskiUgovor_IDStudentskiPosaoCjenik FOREIGN KEY REFERENCES StudentskiPosaoCjenik(IDStudentskiPosaoCjenik) NOT NULL,
BrojOdradjenihSati DECIMAL(10,2) NOT NULL,
CijenaPoSatu MONEY NOT NULL,
DosadZaradjeniIznosUOvojGodini MONEY NOT NULL,
ValutaID INT CONSTRAINT FK_StudentskiUgovor_IDValuta FOREIGN KEY REFERENCES Valuta(IDValuta) NOT NULL,
JeObracunat BIT NOT NULL
)

GO

CREATE TABLE UgovorODjelu
(
IDUgovorODjelu INT CONSTRAINT PK_IDUgovorODjelu PRIMARY KEY FOREIGN KEY REFERENCES Ugovor(IDUgovor) NOT NULL,
BrutoIznos MONEY NOT NULL,
ValutaID INT CONSTRAINT FK_UgovorODjelu_IDValuta FOREIGN KEY REFERENCES Valuta(IDValuta) NOT NULL,
StopaPausalnogPriznatogTroska DECIMAL(10,2) NOT NULL,
JeObracunat BIT NOT NULL
)

GO

CREATE TABLE VrstaDodatka
(
IDVrstaDodatka INT CONSTRAINT PK_IDVrstaDodatka PRIMARY KEY IDENTITY NOT NULL,
Naziv NVARCHAR(50) NOT NULL
)

GO

CREATE TABLE Dodatak
(
IDDodatak INT CONSTRAINT PK_IDDodatak PRIMARY KEY IDENTITY NOT NULL,
Naziv NVARCHAR(150) NOT NULL,
VrstaDodatkaID INT CONSTRAINT FK_Dodatak_IDVrstaDodatka FOREIGN KEY REFERENCES VrstaDodatka(IDVrstaDodatka) NOT NULL,
Iznos MONEY NOT NULL,
ValutaID INT CONSTRAINT FK_Dodatak_IDValuta FOREIGN KEY REFERENCES Valuta(IDValuta) NOT NULL,
DatumOd DATE NOT NULL,
DatumDo DATE,
ZaposlenikID INT CONSTRAINT FK_Dodatak_IDZapsolenik FOREIGN KEY REFERENCES Zaposlenik(IDZaposlenik) NOT NULL
)

GO

CREATE TABLE VrstaObustave
(
IDVrstaObustave INT CONSTRAINT PK_IDVrstaObustave PRIMARY KEY IDENTITY NOT NULL,
Naziv NVARCHAR(50) NOT NULL
)

GO

CREATE TABLE Obustava
(
IDObustava INT CONSTRAINT PK_IDObustava PRIMARY KEY IDENTITY NOT NULL,
Naziv NVARCHAR(150) NOT NULL,
VrstaObustaveID INT CONSTRAINT FK_Obustava_IDVrstaObustave FOREIGN KEY REFERENCES VrstaObustave(IDVrstaObustave) NOT NULL,
Iznos MONEY NOT NULL,
ValutaID INT CONSTRAINT FK_Obustava_IDValuta FOREIGN KEY REFERENCES Valuta(IDValuta) NOT NULL,
DatumOd DATE NOT NULL,
DatumDo DATE,
ZaposlenikID INT CONSTRAINT FK_Obustava_IDZaposlenik FOREIGN KEY REFERENCES Zaposlenik(IDZaposlenik) NOT NULL
)

GO

CREATE TABLE VrstaPrekovremenogRada
(
IDVrstaPrekovremenogRada INT CONSTRAINT PK_IDVrstaPrekovremenogRada PRIMARY KEY IDENTITY NOT NULL,
Naziv NVARCHAR(50) NOT NULL,
Koeficjent DECIMAL(10,2) NOT NULL
)

GO

CREATE TABLE PrekovremeniRad
(
IDPrekovremeniRad INT CONSTRAINT PK_IDPrekovremeniRad PRIMARY KEY IDENTITY NOT NULL,
Naziv NVARCHAR(150) NOT NULL,
VrstaPrekovremenogRadaID INT CONSTRAINT FK_PrekovremeniRad_IDVrstaPrekovremenogRada FOREIGN KEY REFERENCES VrstaPrekovremenogRada(IDVrstaPrekovremenogRada) NOT NULL,
BrojDodatnihSati DECIMAL(10,2) NOT NULL,
DatumOd DATE NOT NULL,
DatumDo DATE,
ZaposlenikID INT CONSTRAINT FK_PrekovremeniRad_IDZaposlenik FOREIGN KEY REFERENCES Zaposlenik(IDZaposlenik) NOT NULL
)

GO

CREATE TABLE VrstaObracuna
(
IDVrstaObracuna INT CONSTRAINT FK_IDVrstaObracuna PRIMARY KEY IDENTITY NOT NULL,
Naziv NVARCHAR(50) NOT NULL
)

CREATE TABLE ObracunUgovora
(
IDObracunUgovora INT CONSTRAINT PK_IDObracunUgovora PRIMARY KEY IDENTITY NOT NULL,
VrstaObracunaID INT CONSTRAINT FK_ObracunUgovovra_IDVrstaObracuna FOREIGN KEY REFERENCES VrstaObracuna(IDVrstaObracuna) NOT NULL,
Opis NVARCHAR(100) NOT NULL,
DatumObracuna DATE NOT NULL,
ValutaID INT CONSTRAINT FK_ObracunUgovoraPorezniRazred_IDValuta FOREIGN KEY REFERENCES Valuta(IDValuta) NOT NULL
)

GO

CREATE TABLE ObracunUgovoraPorez
(
IDObracunUgovoraPorez INT CONSTRAINT PK_IDObracunUgovoraPorez PRIMARY KEY IDENTITY NOT NULL,
ObracunUgovoraID INT CONSTRAINT FK_ObracunUgovoraPorez_IDIsplatnaLista FOREIGN KEY REFERENCES ObracunUgovora(IDObracunUgovora) NOT NULL,
PorezID INT CONSTRAINT FK_ObracunUgovoraPorez_IDPorez FOREIGN KEY REFERENCES Porez(IDPorez) NOT NULL,
Stopa DECIMAL(10,2) NOT NULL,
MinOsnovica MONEY NOT NULL,
MaxOsnovica MONEY
)

GO

CREATE TABLE ObracunUgovoraORadu
(
IDObracunUgovoraORadu INT CONSTRAINT PK_IDObracunUgovoraORadu PRIMARY KEY FOREIGN KEY REFERENCES ObracunUgovora(IDObracunUgovora) NOT NULL,
DatumOd DATE NOT NULL,
DatumDo DATE NOT NULL,
OsnovniOsobniOdbitak MONEY NOT NULL,
OsnovicaOsobnogOdbitka MONEY NOT NULL
)

GO

CREATE TABLE ObracunStudentskihUgovora
(
IDObracunStudentskihUgovora INT CONSTRAINT PK_IDObracunStudentskihUgovora PRIMARY KEY FOREIGN KEY REFERENCES ObracunUgovora(IDObracunUgovora) NOT NULL,
LimitGodisnjegIznosaZaStudenta MONEY NOT NULL
)

GO

CREATE TABLE ObracunUgovoraODjelu
(
IDObracunUgovoraODjelu INT CONSTRAINT PK_IDObracunUgovoraODjelu PRIMARY KEY FOREIGN KEY REFERENCES ObracunUgovora(IDObracunUgovora) NOT NULL
)

GO

CREATE TABLE IsplatnaLista
(
IDIsplatnaLista INT CONSTRAINT PK_IDIsplatnaLista PRIMARY KEY IDENTITY NOT NULL,
ObracunUgovoraID INT CONSTRAINT FK_IsplatnaLista_IDObracunUgovoraID FOREIGN KEY REFERENCES ObracunUgovora(IDObracunUgovora) NOT NULL,
UgovorID INT CONSTRAINT FK_IsplatnaLista_IDUgovor FOREIGN KEY REFERENCES Ugovor(IDUgovor) NOT NULL,
PrebivalisteID INT CONSTRAINT FK_IsplatnaLista_IDPrebivaliste FOREIGN KEY REFERENCES Prebivaliste(IDPrebivaliste) NOT NULL,
Prirez DECIMAL(10,2) NOT NULL,
)

GO

CREATE TABLE IsplatnaListaDavanje
(
IDIsplatnaListaDavanje INT CONSTRAINT PK_IDIsplatnaListaDavanje PRIMARY KEY IDENTITY NOT NULL,
IsplatnaListaID INT CONSTRAINT FK_IsplatnaListaDavanje_IDIsplatnaLista FOREIGN KEY REFERENCES IsplatnaLista(IDIsplatnaLista) NOT NULL,
DavanjeID INT CONSTRAINT FK_IsplatnaListaDavanje_IDDavanje FOREIGN KEY REFERENCES Davanje(IDDavanje) NOT NULL,
StopaNaPlacu DECIMAL(10,2) NOT NULL,
StopaIzPlace DECIMAL(10,2) NOT NULL
)

GO

CREATE TABLE IsplatnaListaOlaksica
(
IDIsplatnaListaOlaksica INT CONSTRAINT PK_IDIsplatnaListaOlaksica PRIMARY KEY IDENTITY NOT NULL,
IsplatnaListaID INT CONSTRAINT FK_IsplatnaListaOlaksica_IDIsplatnaLista FOREIGN KEY REFERENCES IsplatnaLista(IDIsplatnaLista) NOT NULL,
OlaksicaID INT CONSTRAINT FK_IsplatnaListaOlaksica_IDOlaksica FOREIGN KEY REFERENCES Olaksica(IDOlaksica) NOT NULL,
Koeficjent DECIMAL(10,2) NOT NULL
)

GO

CREATE TABLE IsplatnaListaDodatak
(
IDIsplatnaListaDodatak INT CONSTRAINT PK_IDIsplatnaListaDodatak PRIMARY KEY IDENTITY NOT NULL,
IsplatnaListaID INT CONSTRAINT FK_IsplatnaListaDodatak_IDIsplatnaLista FOREIGN KEY REFERENCES IsplatnaLista(IDIsplatnaLista) NOT NULL,
DodatakID INT CONSTRAINT FK_IsplatnaListaDodatak_IDDodatak FOREIGN KEY REFERENCES Dodatak(IDDodatak) NOT NULL
)

GO

CREATE TABLE IsplatnaListaObustava
(
IDIsplatnaListaObustava INT CONSTRAINT PK_IDIsplatnaListaObustava PRIMARY KEY IDENTITY NOT NULL,
IsplatnaListaID INT CONSTRAINT FK_IsplatnaListaObustava_IDIsplatnaLista FOREIGN KEY REFERENCES IsplatnaLista(IDIsplatnaLista) NOT NULL,
ObustavaID INT CONSTRAINT FK_IsplatnaListaObustava_IDObustava FOREIGN KEY REFERENCES Obustava(IDObustava) NOT NULL
)

GO

CREATE TABLE IsplatnaListaPrekovremeniRad
(
IDIsplatnaListaPrekovremeniRad INT CONSTRAINT PK_IDIsplatnaListaPrekovremeniRad PRIMARY KEY IDENTITY NOT NULL,
IsplatnaListaID INT CONSTRAINT FK_IsplatnaListaPrekovremeniRad_IDIsplatnaLista FOREIGN KEY REFERENCES IsplatnaLista(IDIsplatnaLista) NOT NULL,
PrekovremeniRadID INT CONSTRAINT FK_IsplatnaListaPrekovremeniRad_IDPrekovremeniRad FOREIGN KEY REFERENCES PrekovremeniRad(IDPrekovremeniRad) NOT NULL,
Koeficjent DECIMAL(10,2) NOT NULL
)

GO

CREATE TABLE PoslovniPartner
(
IDPoslovniPartner INT CONSTRAINT PK_IDPoslovniPartner PRIMARY KEY IDENTITY NOT NULL,
Naziv NVARCHAR(100) NOT NULL,
OIB NVARCHAR(11) NOT NULL,
Email NVARCHAR(100) NOT NULL,
BrojTelefona NVARCHAR(12) NOT NULL,
Ulica NVARCHAR(50) NOT NULL,
GradID INT CONSTRAINT FK_PoslovniPartner_IDGrad FOREIGN KEY REFERENCES Grad(IDGrad) NOT NULL
)

GO

CREATE TABLE KategorijaTransakcije
(
IDKategorijaTransakcije INT CONSTRAINT PK_IDKategorijaTransakcije PRIMARY KEY IDENTITY NOT NULL,
Naziv NVARCHAR(50) NOT NULL
)

GO

CREATE TABLE VrstaTransakcije
(
IDVrstaTransakcije INT CONSTRAINT PK_IDVrstaTransakcije PRIMARY KEY IDENTITY NOT NULL,
Naziv NVARCHAR(50) NOT NULL
)

GO

CREATE TABLE Transakcija
(
IDTransakcija INT CONSTRAINT PK_IDTransakcija PRIMARY KEY IDENTITY NOT NULL,
PoslovniPartnerID INT CONSTRAINT FK_Transakcija_IDPoslovniPartner FOREIGN KEY REFERENCES PoslovniPartner(IDPoslovniPartner) NOT NULL,
Opis NVARCHAR(150) NOT NULL,
Iznos MONEY NOT NULL,
ValutaID INT CONSTRAINT FK_Transakcija_IDValuta FOREIGN KEY REFERENCES Valuta(IDValuta) NOT NULL,
SrednjiTecaj MONEY NOT NULL,
VrstaTransakcijeID INT CONSTRAINT FK_Transakcija_IDVrstaTransakcie FOREIGN KEY REFERENCES VrstaTransakcije(IDVrstaTransakcije) NOT NULL,
KategorijaTransakcijeID INT CONSTRAINT FK_Transakcija_IDKategorijaTransakcije FOREIGN KEY REFERENCES KategorijaTransakcije(IDKategorijaTransakcije) NOT NULL,
DatumTransakcije DATE NOT NULL
)

GO

CREATE TABLE PovijestPrijava
(
IDPovijestPrijava INT CONSTRAINT PK_IDPovijestPrijava PRIMARY KEY IDENTITY NOT NULL,
VrijemePrijave DATETIME NOT NULL,
ZaposlenikID INT CONSTRAINT FK_PovijestPrijave_IDZaposlenik FOREIGN KEY REFERENCES Zaposlenik(IDZaposlenik) NOT NULL,
RolaID INT CONSTRAINT FK_PovijestPrijava_IDRola FOREIGN KEY REFERENCES Rola(IDRola) NOT NULL
)

GO

INSERT INTO Valuta(Naziv, Jedinica, SrednjiTecaj, DatumTecaja, JeAktivna) VALUES('HRK', 1, 1, '2019-03-12', 1)
INSERT INTO Valuta(Naziv, Jedinica, SrednjiTecaj, DatumTecaja, JeAktivna) VALUES('EUR', 1, 7.410990, '2019-03-12', 1)
INSERT INTO Valuta(Naziv, Jedinica, SrednjiTecaj, DatumTecaja, JeAktivna) VALUES('USD', 1, 6.589304, '2019-03-12', 1)
INSERT INTO Valuta(Naziv, Jedinica, SrednjiTecaj, DatumTecaja, JeAktivna) VALUES('GBP', 1, 8.564648, '2019-03-12', 1)
INSERT INTO Valuta(Naziv, Jedinica, SrednjiTecaj, DatumTecaja, JeAktivna) VALUES('AUD', 1, 4.641732, '2019-03-12', 1)
INSERT INTO Valuta(Naziv, Jedinica, SrednjiTecaj, DatumTecaja, JeAktivna) VALUES('CAD', 1, 4.909891, '2019-03-12', 1)
INSERT INTO Valuta(Naziv, Jedinica, SrednjiTecaj, DatumTecaja, JeAktivna) VALUES('CZK', 1, 0.288849, '2019-03-12', 1)
INSERT INTO Valuta(Naziv, Jedinica, SrednjiTecaj, DatumTecaja, JeAktivna) VALUES('DKK', 1, 0.993364, '2019-03-12', 1)
INSERT INTO Valuta(Naziv, Jedinica, SrednjiTecaj, DatumTecaja, JeAktivna) VALUES('HUF', 100, 2.348520, '2019-03-12', 1)
INSERT INTO Valuta(Naziv, Jedinica, SrednjiTecaj, DatumTecaja, JeAktivna) VALUES('JPY', 100, 5.924053, '2019-03-12', 1)
INSERT INTO Valuta(Naziv, Jedinica, SrednjiTecaj, DatumTecaja, JeAktivna) VALUES('NOK', 1, 0.758856, '2019-03-12', 1)
INSERT INTO Valuta(Naziv, Jedinica, SrednjiTecaj, DatumTecaja, JeAktivna) VALUES('SEK', 1, 0.700803, '2019-03-12', 1)
INSERT INTO Valuta(Naziv, Jedinica, SrednjiTecaj, DatumTecaja, JeAktivna) VALUES('CHF', 1, 6.532960, '2019-03-12', 1)
INSERT INTO Valuta(Naziv, Jedinica, SrednjiTecaj, DatumTecaja, JeAktivna) VALUES('BAM', 1, 3.789179, '2019-03-12', 1)
INSERT INTO Valuta(Naziv, Jedinica, SrednjiTecaj, DatumTecaja, JeAktivna) VALUES('PLN', 1, 1.723767, '2019-03-12', 1)

INSERT INTO Drzava(Naziv, ValutaID, JeDomovina) VALUES('Hrvatska', 1, 1)
INSERT INTO Drzava(Naziv, ValutaID, JeDomovina) VALUES('Engleska', 4, 0)
INSERT INTO Drzava(Naziv, ValutaID, JeDomovina) VALUES('Italija', 2, 0)
INSERT INTO Drzava(Naziv, ValutaID, JeDomovina) VALUES('SAD', 3, 0)

INSERT INTO VrstaPoreza(Naziv) VALUES('Porez na dohodak')
INSERT INTO VrstaPoreza(Naziv) VALUES('Porez na dobit')

INSERT INTO Porez(VrstaPorezaID, Stopa, MinOsnovica, MaxOsnovica, DrzavaID) VALUES(1, 24, 0, 30000, 1)
INSERT INTO Porez(VrstaPorezaID, Stopa, MinOsnovica, MaxOsnovica, DrzavaID) VALUES(1, 36, 30000, NULL, 1)
INSERT INTO Porez(VrstaPorezaID, Stopa, MinOsnovica, MaxOsnovica, DrzavaID) VALUES(2, 12, 0, 3000000, 1)
INSERT INTO Porez(VrstaPorezaID, Stopa, MinOsnovica, MaxOsnovica, DrzavaID) VALUES(2, 18, 3000000, NULL, 1)

INSERT INTO Grad(Naziv, Prirez, DrzavaID) VALUES('Zagreb', 18, 1)
INSERT INTO Grad(Naziv, Prirez, DrzavaID) VALUES('Split', 12, 1)
INSERT INTO Grad(Naziv, Prirez, DrzavaID) VALUES('Rijeka', 15, 1)
INSERT INTO Grad(Naziv, Prirez, DrzavaID) VALUES('Velika Gorica', 12, 1)
INSERT INTO Grad(Naziv, Prirez, DrzavaID) VALUES('Samobor', 0, 1)
INSERT INTO Grad(Naziv, Prirez, DrzavaID) VALUES('Šibenik', 10, 1)
INSERT INTO Grad(Naziv, Prirez, DrzavaID) VALUES('Varaždin', 0, 1)
INSERT INTO Grad(Naziv, Prirez, DrzavaID) VALUES('Osijek', 0, 1)
INSERT INTO Grad(Naziv, Prirez, DrzavaID) VALUES('Sisak', 10, 1)
INSERT INTO Grad(Naziv, Prirez, DrzavaID) VALUES('Zadar', 12, 1)
INSERT INTO Grad(Naziv, Prirez, DrzavaID) VALUES('London', NULL, 2)
INSERT INTO Grad(Naziv, Prirez, DrzavaID) VALUES('Manchester', NULL, 2)
INSERT INTO Grad(Naziv, Prirez, DrzavaID) VALUES('Liverpool', NULL, 2)
INSERT INTO Grad(Naziv, Prirez, DrzavaID) VALUES('Rim', NULL, 3)
INSERT INTO Grad(Naziv, Prirez, DrzavaID) VALUES('Milano', NULL, 3)
INSERT INTO Grad(Naziv, Prirez, DrzavaID) VALUES('Venecija', NULL, 3)
INSERT INTO Grad(Naziv, Prirez, DrzavaID) VALUES('New York City', NULL, 4)
INSERT INTO Grad(Naziv, Prirez, DrzavaID) VALUES('Washington D.C.', NULL, 4)
INSERT INTO Grad(Naziv, Prirez, DrzavaID) VALUES('San Francisco', NULL, 4)

INSERT INTO PodaciOTvrtki(Naziv, Ulica, GradID, OIB, Email, BrojTelefona) VALUES('Tvrtka d.o.o.', 'Ilica 1', 1, '29382818281', 'tvrtka@gmail.com', '01321123')

INSERT INTO ParametriZaObracunPlace(OsnovniOsobniOdbitak, OsnovicaOsobnogOdbitka, LimitGodisnjegIznosaZaStudenta, ValutaID) VALUES(3800, 2500, 60600, 1)

INSERT INTO Odjel(Naziv) VALUES('Programiranje')
INSERT INTO Odjel(Naziv) VALUES('Tehnička podrška')

INSERT INTO RadnoMjesto(Naziv, OdjelID) VALUES('Software Developer', 1)
INSERT INTO RadnoMjesto(Naziv, OdjelID) VALUES('Software tester', 1)
INSERT INTO RadnoMjesto(Naziv, OdjelID) VALUES('Database Developer', 1)
INSERT INTO RadnoMjesto(Naziv, OdjelID) VALUES('System adminstrator', 2)

INSERT INTO VrstaDavanja(Naziv, StopaNaPlacu, StopaIzPlace, VrijediDo) VALUES('Doprinos za zdrastveno osiguranje', 16.5, 0, NULL)
INSERT INTO VrstaDavanja(Naziv, StopaNaPlacu, StopaIzPlace, VrijediDo) VALUES('Doprinos za ozljede na radu', 0.5, 0, '2019-01-01')
INSERT INTO VrstaDavanja(Naziv, StopaNaPlacu, StopaIzPlace, VrijediDo) VALUES('Doprinos za zapošljavanje', 1.7, 0, '2019-01-01')
INSERT INTO VrstaDavanja(Naziv, StopaNaPlacu, StopaIzPlace, VrijediDo) VALUES('Doprinos za MIO 1. stup', 0, 15, NULL)
INSERT INTO VrstaDavanja(Naziv, StopaNaPlacu, StopaIzPlace, VrijediDo) VALUES('Doprinos za MIO 2. stup', 0, 5, NULL)
INSERT INTO VrstaDavanja(Naziv, StopaNaPlacu, StopaIzPlace, VrijediDo) VALUES('Doprinos za samo MIO 1. stup', 0, 20, NULL)
INSERT INTO VrstaDavanja(Naziv, StopaNaPlacu, StopaIzPlace, VrijediDo) VALUES('Doprinos za zdrastveno osiguranje (ugovor o djelu)', 7.5, 0, NULL)
INSERT INTO VrstaDavanja(Naziv, StopaNaPlacu, StopaIzPlace, VrijediDo) VALUES('Doprinos za MIO 1. stup (ugovor o djelu)', 0, 7.5, NULL)
INSERT INTO VrstaDavanja(Naziv, StopaNaPlacu, StopaIzPlace, VrijediDo) VALUES('Doprinos za MIO 2. stup (ugovor o djelu)', 0, 2.5, NULL)
INSERT INTO VrstaDavanja(Naziv, StopaNaPlacu, StopaIzPlace, VrijediDo) VALUES('Doprinos za samo MIO 1. stup (ugovor o djelu)', 0, 10, NULL)
INSERT INTO VrstaDavanja(Naziv, StopaNaPlacu, StopaIzPlace, VrijediDo) VALUES('Doprinos za MIO 1.stup (studentski ugovor)', 5, 0, NULL)
INSERT INTO VrstaDavanja(Naziv, StopaNaPlacu, StopaIzPlace, VrijediDo) VALUES('Doprinos za zdrastveno osiguranje (studentski ugovor)', 0.5, 0, NULL)
INSERT INTO VrstaDavanja(Naziv, StopaNaPlacu, StopaIzPlace, VrijediDo) VALUES('Provizija za studentski centar', 12, 0, NULL)
INSERT INTO VrstaDavanja(Naziv, StopaNaPlacu, StopaIzPlace, VrijediDo) VALUES('Provizija za studentski zbor', 0.5, 0, NULL)

INSERT INTO VrstaOlaksice(Naziv, Koeficjent, VrijediDo) VALUES('Olakšica za prvo dijete', 0.7, NULL)
INSERT INTO VrstaOlaksice(Naziv, Koeficjent, VrijediDo) VALUES('Olakšica za drugo dijete', 1, NULL)
INSERT INTO VrstaOlaksice(Naziv, Koeficjent, VrijediDo) VALUES('Olakšica za treće dijete', 1.4, NULL)
INSERT INTO VrstaOlaksice(Naziv, Koeficjent, VrijediDo) VALUES('Olakšica za četvrto dijete', 1.9, NULL)
INSERT INTO VrstaOlaksice(Naziv, Koeficjent, VrijediDo) VALUES('Olakšica za peto dijete', 2.5, NULL)
INSERT INTO VrstaOlaksice(Naziv, Koeficjent, VrijediDo) VALUES('Olakšica za šesto dijete', 3.2, NULL)
INSERT INTO VrstaOlaksice(Naziv, Koeficjent, VrijediDo) VALUES('Olakšica za sedmo dijete', 4, NULL)
INSERT INTO VrstaOlaksice(Naziv, Koeficjent, VrijediDo) VALUES('Olakšica za osmo dijete', 4.9, NULL)
INSERT INTO VrstaOlaksice(Naziv, Koeficjent, VrijediDo) VALUES('Olakšica za deveto dijete', 5.9, NULL)
INSERT INTO VrstaOlaksice(Naziv, Koeficjent, VrijediDo) VALUES('Olakšica za deseto dijete', 7, NULL)
INSERT INTO VrstaOlaksice(Naziv, Koeficjent, VrijediDo) VALUES('Olakšica za prvog uzdržavanog člana', 0.7, NULL)
INSERT INTO VrstaOlaksice(Naziv, Koeficjent, VrijediDo) VALUES('Olakšica za drugog uzdržavanog člana', 0.7, NULL)
INSERT INTO VrstaOlaksice(Naziv, Koeficjent, VrijediDo) VALUES('Olakšica za trećeg uzdržavanog člana', 0.7, NULL)
INSERT INTO VrstaOlaksice(Naziv, Koeficjent, VrijediDo) VALUES('Olakšica za četrvtog uzdržavanog člana', 0.7, NULL)
INSERT INTO VrstaOlaksice(Naziv, Koeficjent, VrijediDo) VALUES('Olakšica za petog uzdržavanog člana', 0.7, NULL)
INSERT INTO VrstaOlaksice(Naziv, Koeficjent, VrijediDo) VALUES('Olakšica za djelomičnu invalidnost', 0.4, NULL)
INSERT INTO VrstaOlaksice(Naziv, Koeficjent, VrijediDo) VALUES('Olakšica za 100% invalidnost', 1.5, NULL)

INSERT INTO Rola(Naziv) VALUES('Administrator')
INSERT INTO Rola(Naziv) VALUES('Osoblje')

INSERT INTO Spol(Naziv) VALUES('Muško')
INSERT INTO Spol(Naziv) VALUES('Žensko')

INSERT INTO Zaposlenik(Ime, Prezime, SpolID, DatumRodjenja, OIB, Email, Lozinka, BrojTelefona)
			VALUES('Miro', 'Mirić', 1, '1992-01-10', '12345678901', 'miro.miric@gmail.com', 'c44edee00df4bd8f10f74fd51fee969bbeae53b7def9090f2f538f8ba31fc587', '0992123423')

INSERT INTO Zaposlenik(Ime, Prezime, SpolID, DatumRodjenja, OIB, Email, Lozinka, BrojTelefona)
			VALUES('Pero', 'Perić', 1, '1989-07-16', '67434323322', 'pero.peric@gmail.com', 'c44edee00df4bd8f10f74fd51fee969bbeae53b7def9090f2f538f8ba31fc587', '0913232323')

INSERT INTO Zaposlenik(Ime, Prezime, SpolID, DatumRodjenja, OIB, Email, Lozinka, BrojTelefona)
			VALUES('Ana', 'Anić', 2, '1978-03-22', '93727323237', 'ana.anic@gmail.com', 'c44edee00df4bd8f10f74fd51fee969bbeae53b7def9090f2f538f8ba31fc587', '0953923232')

INSERT INTO Zaposlenik(Ime, Prezime, SpolID, DatumRodjenja, OIB, Email, Lozinka, BrojTelefona)
			VALUES('Jura', 'Jurić', 1, '1990-11-10', '83728282721', 'jjuric@hotmail.com', 'c44edee00df4bd8f10f74fd51fee969bbeae53b7def9090f2f538f8ba31fc587', '0911345653')

INSERT INTO Zaposlenik(Ime, Prezime, SpolID, DatumRodjenja, OIB, Email, Lozinka, BrojTelefona)
			VALUES('Bruno', 'Brunić', 1, '1999-01-23', '93828292914', 'bruno.brunic@gmail.com', 'c44edee00df4bd8f10f74fd51fee969bbeae53b7def9090f2f538f8ba31fc587', '018372621')

INSERT INTO Zaposlenik(Ime, Prezime, SpolID, DatumRodjenja, OIB, Email, Lozinka, BrojTelefona)
			VALUES('Sara', 'Sarić', 2, '1998-10-01', '83726263621', 'sara.saric@outlook.com', 'c44edee00df4bd8f10f74fd51fee969bbeae53b7def9090f2f538f8ba31fc587', '0928328231')

INSERT INTO Zaposlenik(Ime, Prezime, SpolID, DatumRodjenja, OIB, Email, Lozinka, BrojTelefona)
			VALUES('Maja', 'Majić', 2, '1970-11-20', '83727372732', 'mmajic1@gmail.com', 'c44edee00df4bd8f10f74fd51fee969bbeae53b7def9090f2f538f8ba31fc587', '0983838281')

INSERT INTO Zaposlenik(Ime, Prezime, SpolID, DatumRodjenja, OIB, Email, Lozinka, BrojTelefona)
			VALUES('Marko', 'Markić', 1, '1972-04-21', '83727172712', 'marko.markic@gmail.com', 'c44edee00df4bd8f10f74fd51fee969bbeae53b7def9090f2f538f8ba31fc587', '0912929292')

INSERT INTO Zaposlenik(Ime, Prezime, SpolID, DatumRodjenja, OIB, Email, Lozinka, BrojTelefona)
			VALUES('Mate', 'Matić', 1, '1975-05-29', '29293737271', 'mate.matic@gmail.com', 'c44edee00df4bd8f10f74fd51fee969bbeae53b7def9090f2f538f8ba31fc587', '0952323232')

INSERT INTO Zaposlenik(Ime, Prezime, SpolID, DatumRodjenja, OIB, Email, Lozinka, BrojTelefona)
			VALUES('Tomislav', 'Tomislavić', 1, '1991-09-21', '82828272622', 'tomislav.tomislavic@gmail.com', 'c44edee00df4bd8f10f74fd51fee969bbeae53b7def9090f2f538f8ba31fc587', '0912828281')

INSERT INTO Prebivaliste(Ulica, GradID, ZaposlenikID, DatumOd) VALUES('Ulica 1', 1, 1, '2017-01-05')
INSERT INTO Prebivaliste(Ulica, GradID, ZaposlenikID, DatumOd) VALUES('Ulica 2', 1, 2, '2017-01-05')
INSERT INTO Prebivaliste(Ulica, GradID, ZaposlenikID, DatumOd) VALUES('Ulica 3', 1, 3, '2017-01-05')
INSERT INTO Prebivaliste(Ulica, GradID, ZaposlenikID, DatumOd) VALUES('Ulica 4', 2, 4, '2017-01-05')
INSERT INTO Prebivaliste(Ulica, GradID, ZaposlenikID, DatumOd) VALUES('Ulica 5', 2, 5, '2017-01-05')
INSERT INTO Prebivaliste(Ulica, GradID, ZaposlenikID, DatumOd) VALUES('Ulica 6', 2, 6, '2017-01-05')
INSERT INTO Prebivaliste(Ulica, GradID, ZaposlenikID, DatumOd) VALUES('Ulica 7', 3, 7, '2017-01-05')
INSERT INTO Prebivaliste(Ulica, GradID, ZaposlenikID, DatumOd) VALUES('Ulica 8', 3, 8, '2017-01-05')
INSERT INTO Prebivaliste(Ulica, GradID, ZaposlenikID, DatumOd) VALUES('Ulica 9', 3, 9, '2017-01-05')
INSERT INTO Prebivaliste(Ulica, GradID, ZaposlenikID, DatumOd) VALUES('Ulica 10', 4, 10, '2017-01-05')

INSERT INTO ZaposlenikRola(ZaposlenikID, RolaID) VALUES(1, 1)
INSERT INTO ZaposlenikRola(ZaposlenikID, RolaID) VALUES(1, 2)
INSERT INTO ZaposlenikRola(ZaposlenikID, RolaID) VALUES(2, 1)
INSERT INTO ZaposlenikRola(ZaposlenikID, RolaID) VALUES(2, 2)
INSERT INTO ZaposlenikRola(ZaposlenikID, RolaID) VALUES(3, 2)
INSERT INTO ZaposlenikRola(ZaposlenikID, RolaID) VALUES(4, 2)
INSERT INTO ZaposlenikRola(ZaposlenikID, RolaID) VALUES(5, 2)
INSERT INTO ZaposlenikRola(ZaposlenikID, RolaID) VALUES(6, 2)
INSERT INTO ZaposlenikRola(ZaposlenikID, RolaID) VALUES(7, 2)
INSERT INTO ZaposlenikRola(ZaposlenikID, RolaID) VALUES(8, 2)
INSERT INTO ZaposlenikRola(ZaposlenikID, RolaID) VALUES(9, 2)
INSERT INTO ZaposlenikRola(ZaposlenikID, RolaID) VALUES(10, 2)

INSERT INTO Davanje(VrstaDavanjaID, ZaposlenikID, DatumOd, DatumDo) VALUES(1, 1, '2018-01-01', '2022-01-01')
INSERT INTO Davanje(VrstaDavanjaID, ZaposlenikID, DatumOd, DatumDo) VALUES(4, 1, '2018-01-01', '2022-01-01')
INSERT INTO Davanje(VrstaDavanjaID, ZaposlenikID, DatumOd, DatumDo) VALUES(5, 1, '2018-01-01', '2022-01-01')
INSERT INTO Davanje(VrstaDavanjaID, ZaposlenikID, DatumOd, DatumDo) VALUES(1, 2, '2018-01-01', '2022-01-01')
INSERT INTO Davanje(VrstaDavanjaID, ZaposlenikID, DatumOd, DatumDo) VALUES(6, 2, '2018-01-01', '2022-01-01')
INSERT INTO Davanje(VrstaDavanjaID, ZaposlenikID, DatumOd, DatumDo) VALUES(11, 3, '2018-01-01', NULL)
INSERT INTO Davanje(VrstaDavanjaID, ZaposlenikID, DatumOd, DatumDo) VALUES(12, 3, '2018-01-01',  NULL)
INSERT INTO Davanje(VrstaDavanjaID, ZaposlenikID, DatumOd, DatumDo) VALUES(13, 3, '2018-01-01', NULL)
INSERT INTO Davanje(VrstaDavanjaID, ZaposlenikID, DatumOd, DatumDo) VALUES(14, 3, '2018-01-01', NULL)
INSERT INTO Davanje(VrstaDavanjaID, ZaposlenikID, DatumOd, DatumDo) VALUES(11, 4, '2018-06-02', NULL)
INSERT INTO Davanje(VrstaDavanjaID, ZaposlenikID, DatumOd, DatumDo) VALUES(12, 4, '2018-06-02', NULL)
INSERT INTO Davanje(VrstaDavanjaID, ZaposlenikID, DatumOd, DatumDo) VALUES(13, 4, '2018-06-02', NULL)
INSERT INTO Davanje(VrstaDavanjaID, ZaposlenikID, DatumOd, DatumDo) VALUES(14, 4, '2018-06-02', NULL)
INSERT INTO Davanje(VrstaDavanjaID, ZaposlenikID, DatumOd, DatumDo) VALUES(1, 5, '2019-01-01', NULL)
INSERT INTO Davanje(VrstaDavanjaID, ZaposlenikID, DatumOd, DatumDo) VALUES(4, 5, '2019-01-01', NULL)
INSERT INTO Davanje(VrstaDavanjaID, ZaposlenikID, DatumOd, DatumDo) VALUES(5, 5, '2019-01-01', NULL)
INSERT INTO Davanje(VrstaDavanjaID, ZaposlenikID, DatumOd, DatumDo) VALUES(1, 6, '2019-01-01', NULL)
INSERT INTO Davanje(VrstaDavanjaID, ZaposlenikID, DatumOd, DatumDo) VALUES(4, 6, '2019-01-01', NULL)
INSERT INTO Davanje(VrstaDavanjaID, ZaposlenikID, DatumOd, DatumDo) VALUES(5, 6, '2019-01-01', NULL)
INSERT INTO Davanje(VrstaDavanjaID, ZaposlenikID, DatumOd, DatumDo) VALUES(1, 7, '2019-01-01', NULL)
INSERT INTO Davanje(VrstaDavanjaID, ZaposlenikID, DatumOd, DatumDo) VALUES(4, 7, '2019-01-01', NULL)
INSERT INTO Davanje(VrstaDavanjaID, ZaposlenikID, DatumOd, DatumDo) VALUES(5, 7, '2019-01-01', NULL)
INSERT INTO Davanje(VrstaDavanjaID, ZaposlenikID, DatumOd, DatumDo) VALUES(1, 8, '2019-02-01', NULL)
INSERT INTO Davanje(VrstaDavanjaID, ZaposlenikID, DatumOd, DatumDo) VALUES(4, 8, '2019-02-01', NULL)
INSERT INTO Davanje(VrstaDavanjaID, ZaposlenikID, DatumOd, DatumDo) VALUES(5, 8, '2019-02-01', NULL)
INSERT INTO Davanje(VrstaDavanjaID, ZaposlenikID, DatumOd, DatumDo) VALUES(7, 9, '2019-01-01', NULL)
INSERT INTO Davanje(VrstaDavanjaID, ZaposlenikID, DatumOd, DatumDo) VALUES(10, 9, '2019-01-01', NULL)
INSERT INTO Davanje(VrstaDavanjaID, ZaposlenikID, DatumOd, DatumDo) VALUES(7, 10, '2019-01-01', NULL)
INSERT INTO Davanje(VrstaDavanjaID, ZaposlenikID, DatumOd, DatumDo) VALUES(8, 10, '2019-01-01', NULL)
INSERT INTO Davanje(VrstaDavanjaID, ZaposlenikID, DatumOd, DatumDo) VALUES(9, 10, '2019-01-01', NULL)

INSERT INTO Olaksica(VrstaOlaksiceID, ZaposlenikID, DatumOd, DatumDo) VALUES(1, 1, '2018-01-01', '2020-01-01')
INSERT INTO Olaksica(VrstaOlaksiceID, ZaposlenikID, DatumOd, DatumDo) VALUES(1, 5, '2018-01-01', '2020-01-01')
INSERT INTO Olaksica(VrstaOlaksiceID, ZaposlenikID, DatumOd, DatumDo) VALUES(2, 5, '2018-01-01', '2020-01-01')
INSERT INTO Olaksica(VrstaOlaksiceID, ZaposlenikID, DatumOd, DatumDo) VALUES(17, 7, '2018-01-01', '2020-01-01')

INSERT INTO StudentskiPosaoCjenik(Naziv, CijenaPoSatu, ValutaID) VALUES('Administrativni poslovi', 25, 1)
INSERT INTO StudentskiPosaoCjenik(Naziv, CijenaPoSatu, ValutaID) VALUES('Rad na fotokopirnom stroju', 21.50, 1)
INSERT INTO StudentskiPosaoCjenik(Naziv, CijenaPoSatu, ValutaID) VALUES('Knjigovodstveni i računovodstveni poslovi', 30, 1)
INSERT INTO StudentskiPosaoCjenik(Naziv, CijenaPoSatu, ValutaID) VALUES('Tajnički poslovi sa znanjem str. jezik + PC', 25, 1)
INSERT INTO StudentskiPosaoCjenik(Naziv, CijenaPoSatu, ValutaID) VALUES('Unos podataka na računalu', 22, 1)
INSERT INTO StudentskiPosaoCjenik(Naziv, CijenaPoSatu, ValutaID) VALUES('Programiranje na računalu, izrada web stranice', 60, 1)

INSERT INTO VrstaUgovora(Naziv) VALUES('Ugovor o radu')
INSERT INTO VrstaUgovora(Naziv) VALUES('Ugovor o djelu')
INSERT INTO VrstaUgovora(Naziv) VALUES('Studentski ugovor')

INSERT INTO RadniOdnos(Naziv, VrstaUgovoraID) VALUES('Ugovor na određeno ili neodređeno vrijeme', 1)
INSERT INTO RadniOdnos(Naziv, VrstaUgovoraID) VALUES('Pripravnik (prvo zaposlenje)', 1)
INSERT INTO RadniOdnos(Naziv, VrstaUgovoraID) VALUES('Ugovor o djelu (standradni)', 2)
INSERT INTO RadniOdnos(Naziv, VrstaUgovoraID) VALUES('Ugovor o djelu (umirovljenik)', 2)
INSERT INTO RadniOdnos(Naziv, VrstaUgovoraID) VALUES('Ugovor o djelu (autorski honorar)', 2)
INSERT INTO RadniOdnos(Naziv, VrstaUgovoraID) VALUES('Studentski rad', 3)

INSERT INTO Ugovor(RadniOdnosID, ZaposlenikID, RadnoMjestoID, DatumOd, DatumDo) VALUES(2, 1, 1, '2017-08-01', '2018-01-01')
INSERT INTO UgovorORadu(IDUgovorORadu, BrojRadnihSatiTjedno, BrutoPlaca, ValutaID) VALUES(1, 40, 9000, 1)

INSERT INTO Ugovor(RadniOdnosID, ZaposlenikID, RadnoMjestoID, DatumOd, DatumDo) VALUES(1, 1, 1, '2018-01-01', NULL)
INSERT INTO UgovorORadu(IDUgovorORadu, BrojRadnihSatiTjedno, BrutoPlaca, ValutaID) VALUES(2, 40, 8000, 1)

INSERT INTO Ugovor(RadniOdnosID, ZaposlenikID, RadnoMjestoID, DatumOd, DatumDo) VALUES(1, 2, 2, '2019-02-05', '2020-02-05')
INSERT INTO UgovorORadu(IDUgovorORadu, BrojRadnihSatiTjedno, BrutoPlaca, ValutaID) VALUES(3, 40, 9000, 1)

INSERT INTO Ugovor(RadniOdnosID, ZaposlenikID, RadnoMjestoID, DatumOd, DatumDo) VALUES(6, 3, 3, '2019-02-01', '2019-03-01')
INSERT INTO StudentskiUgovor(IDStudentskiUgovor, StudentskiPosaoCjenikID, BrojOdradjenihSati, CijenaPoSatu, DosadZaradjeniIznosUOvojGodini, ValutaID, JeObracunat) VALUES(4, 1, 100, 25, 40000, 1, 0)

INSERT INTO Ugovor(RadniOdnosID, ZaposlenikID, RadnoMjestoID, DatumOd, DatumDo) VALUES(3, 4, 4, '2019-06-01', '2019-08-01')
INSERT INTO StudentskiUgovor(IDStudentskiUgovor, StudentskiPosaoCjenikID, BrojOdradjenihSati, CijenaPoSatu, DosadZaradjeniIznosUOvojGodini, ValutaID, JeObracunat) VALUES(5, 6, 200, 60, 65000, 1, 0)

INSERT INTO Ugovor(RadniOdnosID, ZaposlenikID, RadnoMjestoID, DatumOd, DatumDo) VALUES(1, 5, 3, '2019-06-05', NULL)
INSERT INTO UgovorORadu(IDUgovorORadu, BrojRadnihSatiTjedno, BrutoPlaca, ValutaID) VALUES(6, 40, 9000, 1)

INSERT INTO Ugovor(RadniOdnosID, ZaposlenikID, RadnoMjestoID, DatumOd, DatumDo) VALUES(1, 6, 3, '2019-02-03', '2021-02-03')
INSERT INTO UgovorORadu(IDUgovorORadu, BrojRadnihSatiTjedno, BrutoPlaca, ValutaID) VALUES(7, 40, 6500, 1)

INSERT INTO Ugovor(RadniOdnosID, ZaposlenikID, RadnoMjestoID, DatumOd, DatumDo) VALUES(1, 7, 2, '2019-02-27', '2022-02-27')
INSERT INTO UgovorORadu(IDUgovorORadu, BrojRadnihSatiTjedno, BrutoPlaca, ValutaID) VALUES(8, 40, 8000, 1)

INSERT INTO Ugovor(RadniOdnosID, ZaposlenikID, RadnoMjestoID, DatumOd, DatumDo) VALUES(1, 8, 3, '2019-02-10', NULL)
INSERT INTO UgovorORadu(IDUgovorORadu, BrojRadnihSatiTjedno, BrutoPlaca, ValutaID) VALUES(9, 40, 7500, 1)

INSERT INTO Ugovor(RadniOdnosID, ZaposlenikID, RadnoMjestoID, DatumOd, DatumDo) VALUES(4, 9, 1, '2019-03-10', '2019-04-01')
INSERT INTO UgovorODjelu(IDUgovorODjelu, BrutoIznos, ValutaID, StopaPausalnogPriznatogTroska, JeObracunat) VALUES(10, 3500, 1, 0, 0)

INSERT INTO Ugovor(RadniOdnosID, ZaposlenikID, RadnoMjestoID, DatumOd, DatumDo) VALUES(3, 10, 3, '2019-02-10', '2019-03-01')
INSERT INTO UgovorODjelu(IDUgovorODjelu, BrutoIznos, ValutaID, StopaPausalnogPriznatogTroska, JeObracunat) VALUES(11, 6000, 1, 30, 0)


INSERT INTO VrstaDodatka(Naziv) VALUES('Bonus')
INSERT INTO VrstaDodatka(Naziv) VALUES('Božičnica')

INSERT INTO VrstaObustave(Naziv) VALUES('Kašnjenje')
INSERT INTO VrstaObustave(Naziv) VALUES('Alimentacija')
INSERT INTO VrstaObustave(Naziv) VALUES('Prijevoz')
INSERT INTO VrstaObustave(Naziv) VALUES('Bolovanje')

INSERT INTO VrstaPrekovremenogRada(Naziv, Koeficjent) VALUES('Standardni', 1.5)

INSERT INTO Dodatak(Naziv, VrstaDodatkaID, Iznos, ValutaID, DatumOd, DatumDo, ZaposlenikID) VALUES('Bonus', 1, 500, 1, '2019-03-01', '2019-03-31', 1)
INSERT INTO Dodatak(Naziv, VrstaDodatkaID, Iznos, ValutaID, DatumOd, DatumDo, ZaposlenikID) VALUES('Bonus', 1, 200, 1, '2019-04-01', '2019-04-30', 5)

INSERT INTO Obustava(Naziv, VrstaObustaveID, Iznos, ValutaID, DatumOd, DatumDo, ZaposlenikID) VALUES('Alimentacija za dvoje djece', 2, 2500, 1, '2019-03-01', '2021-03-01', 1)
INSERT INTO Obustava(Naziv, VrstaObustaveID, Iznos, ValutaID, DatumOd, DatumDo, ZaposlenikID) VALUES('Prijevoz ZET', 3, 380, 1, '2019-03-01', NULL, 2)


INSERT INTO PrekovremeniRad(Naziv, VrstaPrekovremenogRadaID, BrojDodatnihSati, DatumOd, DatumDo, ZaposlenikID) VALUES('Odrađivanje prekovremenih', 1, 15, '2019-04-01', '2019-04-30', 1)
INSERT INTO PrekovremeniRad(Naziv, VrstaPrekovremenogRadaID, BrojDodatnihSati, DatumOd, DatumDo, ZaposlenikID) VALUES('Odrađivanje prekovremenih', 1, 15, '2019-04-01', '2019-04-30', 2)
INSERT INTO PrekovremeniRad(Naziv, VrstaPrekovremenogRadaID, BrojDodatnihSati, DatumOd, DatumDo, ZaposlenikID) VALUES('Odrađivanje prekovremenih', 1, 15, '2019-04-01', '2019-04-30', 6)
INSERT INTO PrekovremeniRad(Naziv, VrstaPrekovremenogRadaID, BrojDodatnihSati, DatumOd, DatumDo, ZaposlenikID) VALUES('Odrađivanje prekovremenih', 1, 20, '2019-04-01', '2019-04-30', 7)

INSERT INTO VrstaObracuna(Naziv) VALUES('Obračun ugovora o radu')
INSERT INTO VrstaObracuna(Naziv) VALUES('Obračun ugovora o djelu')
INSERT INTO VrstaObracuna(Naziv) VALUES('Obračun studentskih ugovora')

INSERT INTO PoslovniPartner(Naziv, OIB, Email, BrojTelefona, Ulica, GradID)
			VALUES('HEP', '23232567788',  'hep@gmail.com', '0912020202', 'Tomašićeva 1', 1)

INSERT INTO PoslovniPartner(Naziv, OIB, Email, BrojTelefona, Ulica, GradID)
			VALUES('Svijet medija d.o.o.', '93929292921', 'info@svijet-medija.hr', '01828281', 'Iblerov trg 10', 1)

INSERT INTO PoslovniPartner(Naziv, OIB, Email, BrojTelefona, Ulica, GradID)
			VALUES('Hrvatski telekom', '73737372721', 'info@t.ht.hr', '01838221', 'Ulica A', 1)

INSERT INTO PoslovniPartner(Naziv, OIB, Email, BrojTelefona, Ulica, GradID)
			VALUES('Gradska plinara Zagreb', '93939392921', 'info@gradska-plinara.hr', '01828218', 'Ulica B', 1)

INSERT INTO PoslovniPartner(Naziv, OIB, Email, BrojTelefona, Ulica, GradID)
			VALUES('Apple store Hrvatska', '93292929322', 'store@store.com.hr', '01323243', 'Ulica C', 1)

INSERT INTO PoslovniPartner(Naziv, OIB, Email, BrojTelefona, Ulica, GradID)
			VALUES('Konzum', '93293929191', 'prodaja@konzum.hr', '018382810', 'Ulica E', 1)

INSERT INTO PoslovniPartner(Naziv, OIB, Email, BrojTelefona, Ulica, GradID)
			VALUES('Narodne novine', '83828282811', 'info@narodne-novine.hr', '01828392', 'Ulica F', 1)

INSERT INTO PoslovniPartner(Naziv, OIB, Email, BrojTelefona, Ulica, GradID)
			VALUES('Company A', '', 'companya@gmail.com', '9283727372', 'Street A', 11)

INSERT INTO PoslovniPartner(Naziv, OIB, Email, BrojTelefona, Ulica, GradID)
			VALUES('Azienda ABCD', '', 'aziendaabcd@gmail.com', '23231121', 'Strada A', 14)

INSERT INTO PoslovniPartner(Naziv, OIB, Email, BrojTelefona, Ulica, GradID)
			VALUES('Tech Company Ltd', '', 'techcompany@gmail.com', '23232323', 'Street B', 17)

INSERT INTO PoslovniPartner(Naziv, OIB, Email, BrojTelefona, Ulica, GradID)
			VALUES('Company B', '', 'companyb@gmail.com', '232323235', 'Street C', 18)

INSERT INTO VrstaTransakcije(Naziv) VALUES('Prihod')
INSERT INTO VrstaTransakcije(Naziv) VALUES('Rashod')

INSERT INTO KategorijaTransakcije(Naziv) VALUES('Račun')
INSERT INTO KategorijaTransakcije(Naziv) VALUES('Aplikacije')
INSERT INTO KategorijaTransakcije(Naziv) VALUES('Hrana i piće')
INSERT INTO KategorijaTransakcije(Naziv) VALUES('Uredski pribor')
INSERT INTO KategorijaTransakcije(Naziv) VALUES('Računala, djelovi, oprema itd')
INSERT INTO KategorijaTransakcije(Naziv) VALUES('Ostalo')

INSERT INTO Transakcija(PoslovniPartnerID, Opis, Iznos, ValutaID, SrednjiTecaj, VrstaTransakcijeID, KategorijaTransakcijeID, DatumTransakcije)
			VALUES(5, 'Apple računala', 50000, 1, 1, 2, 5, '2019-02-04')

INSERT INTO Transakcija(PoslovniPartnerID, Opis, Iznos, ValutaID, SrednjiTecaj, VrstaTransakcijeID, KategorijaTransakcijeID, DatumTransakcije)
			VALUES(1, 'Račun za struju', 300, 1, 1, 2, 1, '2019-02-23')

INSERT INTO Transakcija(PoslovniPartnerID, Opis, Iznos, ValutaID, SrednjiTecaj, VrstaTransakcijeID, KategorijaTransakcijeID, DatumTransakcije)
			VALUES(3, 'Račun za telefon i internet', 400, 1, 1, 2, 1, '2019-02-04')

INSERT INTO Transakcija(PoslovniPartnerID, Opis, Iznos, ValutaID, SrednjiTecaj, VrstaTransakcijeID, KategorijaTransakcijeID, DatumTransakcije)
			VALUES(8, 'Isporuka Android aplikacije za upravljanje projektima', 4000, 4, 8.564648, 1, 2, '2019-03-12')

INSERT INTO Transakcija(PoslovniPartnerID, Opis, Iznos, ValutaID, SrednjiTecaj, VrstaTransakcijeID, KategorijaTransakcijeID, DatumTransakcije)
			VALUES(9, 'Isporuka web aplikacije za računovodstvo', 5000, 2, 7.410990, 1, 2, '2019-03-12')