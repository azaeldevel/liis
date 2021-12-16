-- Reporte por sucursal
SELECT titemNumber,uso,fhmov,note FROM dbssiila.resumov where suc = 'bc.mx' and uso != 'vta';

-- Insertar Titem
INSERT INTO  Object(BD,number) VALUES('bc.tj','00');
INSERT INTO  Item(BD,number ) VALUES('bc.tj','00');
INSERT INTO  Titem(BD,number,class,marca,modelo,serie ) VALUES('bc.tj','00','titem','YALE','ERC050GHN48TE084','A908N07986F');
INSERT INTO Resumov(titemBD,titemNumber,compBD,compNumber,suc,uso,note,fhmov) VALUES('bc.tj','00','bc.tj','998','bc.tj','disp','','2015-09-10');

-- Clean orden de servicio
SET SQL_SAFE_UPDATES = 0;
UPDATE dbssiila.orcom SET terminalComment='*' WHERE YEAR(fhFolio) = 2015 AND MONTH(fhFolio)=12 AND fhCancel IS NULL AND fhFIN IS NULL;
SET SQL_SAFE_UPDATES = 1;

SELECT folio,ownerName as encargado FROM dbssiil.orcom WHERE YEAR(fhFolio) = 2015 AND MONTH(fhFolio)=12 AND fhCancel IS NULL AND fhFIN IS NULL AND ownerName != 'Jaime Dominguez';


-- Ordenes caducas
UPDATE Orcom SET estado='cancel',terminalComment='Cancelado por caducidad.',fhFin=now() WHERE year(fhFolio)=2015 and (estado != 'pedfin' and estado != 'cancel');


-- Buscar Movimento
SELECT * FROM dbssiil.movements where folio='XXX' and year(fhMov) = 2016 and month(fhMov) = 7;


-- RFC Duplicados
SELECT  a.id,a.`name`,a.number,a.rfc,b.totalCount AS Duplicate
FROM    dbssiil.companies a
        INNER JOIN
        (
            SELECT  rfc, COUNT(*) totalCount
            FROM    dbssiil.companies
            GROUP   BY rfc
        )
        b ON a.rfc = b.rfc
WHERE   b.totalCount >= 2 and name like 'cargador%'
order by rfc;