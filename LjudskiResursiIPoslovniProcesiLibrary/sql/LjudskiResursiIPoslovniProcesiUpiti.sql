SELECT *, u.ZaposlenikID FROM UgovorORadu uor
INNER JOIN Ugovor AS u ON u.IDUgovor = uor.IDUgovorORadu
WHERE NOT (DatumOd > GETDATE() OR DatumDo < GETDATE()) OR (DatumOd <= GETDATE() AND DatumDo IS NULL)

SELECT *, u.ZaposlenikID FROM StudentskiUgovor su
INNER JOIN Ugovor AS u ON u.IDUgovor = su.IDStudentskiUgovor

DECLARE @DatumOd DATE
SET @DatumOd = '2019-02-01'

DECLARE @DatumDo DATE
SET @DatumDo = '2019-02-28'

SELECT * FROM Olaksica o
INNER JOIN VrstaOlaksice AS vo
ON o.VrstaOlaksiceID = vo.IDVrstaOlaksice
WHERE NOT (DatumOd > @DatumDo OR DatumDo < @DatumOd OR ZaposlenikID !=5) OR (DatumOd <= @DatumDo AND DatumDo IS NULL AND ZaposlenikID =5) AND (vo.VrijediDo >= @DatumDo OR vo.VrijediDo IS NULL)

SELECT * FROM ObracunUgovoraORadu ouor
INNER JOIN ObracunUgovora AS ou ON ouor.IDObracunUgovoraORadu = ou.IDObracunUgovora

SELECT * FROM ObracunStudentskihUgovora osu
INNER JOIN ObracunUgovora AS ou ON osu.IDObracunStudentskihUgovora = ou.IDObracunUgovora

SELECT * FROM ObracunUgovora

DECLARE @Osnovica INT
SET @Osnovica = 30000

SELECT TOP 1 Stopa FROM Porez
WHERE (@Osnovica >= MinOsnovica AND @Osnovica <= MaxOsnovica AND VrstaPorezaID = 1) OR (@Osnovica > MinOsnovica AND MaxOsnovica IS NULL AND VrstaPorezaID = 1)