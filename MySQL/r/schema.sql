-- MySQL Workbench Synchronization
-- Generated: 2022-02-11 17:05
-- Model: Base de datos Servidor SIIL
-- Version: 12.53
-- Project: SIIL Application
-- Author: Azael Reyes

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

ALTER TABLE `dbssiil`.`Users` 
DROP FOREIGN KEY `fk_Users_Persons1`;

ALTER TABLE `dbssiil`.`Titem` 
DROP FOREIGN KEY `fk_Titem_StockItem1`;

ALTER TABLE `dbssiil`.`Movements` 
DROP FOREIGN KEY `fk_Movements_SalesRemision1`;

ALTER TABLE `dbssiil`.`Resumov` 
DROP FOREIGN KEY `fk_Resumov_GruaUso1`,
DROP FOREIGN KEY `fk_Resumov_Offices1`,
DROP FOREIGN KEY `fk_Resumov_SalesRemision1`;

ALTER TABLE `dbssiil`.`Charger` 
DROP FOREIGN KEY `fk_Charger_Titem2`;

ALTER TABLE `dbssiil`.`Battery` 
DROP FOREIGN KEY `fk_Battery_Titem2`;

ALTER TABLE `dbssiil`.`Forklift` 
DROP FOREIGN KEY `fk_Forklift_Titem2`;

ALTER TABLE `dbssiil`.`Movtitems` 
DROP FOREIGN KEY `fk_Movtitems_StockFlow1`;

ALTER TABLE `dbssiil`.`Orcom` 
DROP FOREIGN KEY `fk_Orcom_Persons1`,
DROP FOREIGN KEY `fk_Orcom_Persons3`,
DROP FOREIGN KEY `fk_Orcom_Persons4`;

ALTER TABLE `dbssiil`.`Minas` 
DROP FOREIGN KEY `fk_Minas_Titem2`;

ALTER TABLE `dbssiil`.`AccesTable` 
DROP FOREIGN KEY `fk_AccesTable_Policies1`,
DROP FOREIGN KEY `fk_AccesTable_Users1`;

ALTER TABLE `dbssiil`.`Trace` 
DROP FOREIGN KEY `fk_Trace_Users1`;

ALTER TABLE `dbssiil`.`OrcomOwners` 
DROP FOREIGN KEY `fk_OrcomOwners_Persons1`;

ALTER TABLE `dbssiil`.`PurchaseCR` 
DROP FOREIGN KEY `fk_PurchaseCR_Users1`,
DROP FOREIGN KEY `fk_PurchaseCR_Users2`;

ALTER TABLE `dbssiil`.`Configuration` 
DROP FOREIGN KEY `fk_Configuration_Users1`;

ALTER TABLE `dbssiil`.`Instances` 
DROP FOREIGN KEY `fk_Instances_Users1`;

ALTER TABLE `dbssiil`.`ContabilidadAsientos` 
DROP FOREIGN KEY `fk_SIILContabilidadRegistro_SIILCatalogoCuentas1`,
DROP FOREIGN KEY `fk_SIILContabilidadRegistro_SIILCatalogoCuentas2`;

ALTER TABLE `dbssiil`.`PurchasesOperational` 
DROP FOREIGN KEY `fk_PurchasesOperational_ProcessOperational1`,
DROP FOREIGN KEY `fk_PurchasesOperational_PurchasesProvider1`;

ALTER TABLE `dbssiil`.`ProcessStates` 
DROP FOREIGN KEY `fk_ProcessStates_ProcessModule1`;

ALTER TABLE `dbssiil`.`StockFlow` 
DROP FOREIGN KEY `fk_StockFlow_PurchasesOrder1`;

ALTER TABLE `dbssiil`.`StockItem` 
DROP FOREIGN KEY `fk_StockItem_PurchasesProvider1`,
DROP FOREIGN KEY `fk_StockItem_StockClaveFiscal1`;

ALTER TABLE `dbssiil`.`StockOffice` 
DROP FOREIGN KEY `fk_StockOffice_StockItem1`;

ALTER TABLE `dbssiil`.`StockRefection` 
DROP FOREIGN KEY `fk_StockRefection_StockItem1`;

ALTER TABLE `dbssiil`.`AdministracionBuilding` 
DROP FOREIGN KEY `fk_AdministracionBuilding_AdministracionBuilding1`;

ALTER TABLE `dbssiil`.`StockContainer` 
DROP FOREIGN KEY `fk_StockContainer_AdministracionBuilding1`,
DROP FOREIGN KEY `fk_StockContainer_StockContainer1`;

ALTER TABLE `dbssiil`.`AdministracionEstacion` 
DROP FOREIGN KEY `fk_AdministracionEstacion_Persons1`;

ALTER TABLE `dbssiil`.`ProcessOperational` 
DROP FOREIGN KEY `fk_ProcessOperational_Offices1`;

ALTER TABLE `dbssiil`.`StockAllocated` 
DROP FOREIGN KEY `fk_StockAllocated_StockContainer1`;

ALTER TABLE `dbssiil`.`StockSetem` 
DROP FOREIGN KEY `fk_StockSetem_StockItem1`;

ALTER TABLE `dbssiil`.`StockCross` 
DROP FOREIGN KEY `fk_StockCross_StockRefection1`,
DROP FOREIGN KEY `fk_StockCross_StockRefection2`;

ALTER TABLE `dbssiil`.`StockExternal` 
DROP FOREIGN KEY `fk_StockExternal_StockRefection1`,
DROP FOREIGN KEY `fk_StockExternal_StockRefection2`;

ALTER TABLE `dbssiil`.`PurchasesOrder` 
DROP FOREIGN KEY `fk_PurchasesOrder_PurchasesOperational1`;

ALTER TABLE `dbssiil`.`SalesQuotation` 
DROP FOREIGN KEY `fk_SalesQuotation_Orcom1`;

ALTER TABLE `dbssiil`.`SalesOperational` 
DROP FOREIGN KEY `fk_SalesOperational_Companies1`;

ALTER TABLE `dbssiil`.`ProcessOperationalRows` 
DROP FOREIGN KEY `fk_ProcessOperationalRows_ProcessOperational1`;

ALTER TABLE `dbssiil`.`StockService` 
DROP FOREIGN KEY `fk_StockService_StockItem1`;

ALTER TABLE `dbssiil`.`ServicesOrder` 
DROP FOREIGN KEY `fk_ServicesOrder_Companies1`,
DROP FOREIGN KEY `fk_ServicesOrder_Orcom1`,
DROP FOREIGN KEY `fk_ServicesOrder_StockFlow1`,
DROP FOREIGN KEY `fk_ServicesOrder_technical`;

ALTER TABLE `dbssiil`.`HistoryConsume` 
DROP FOREIGN KEY `fk_HistoryConsume_StockItem1`,
DROP FOREIGN KEY `fk_HistoryConsume_StockItem2`;

ALTER TABLE `dbssiil`.`ServiceOrderResumen` 
DROP FOREIGN KEY `fk_ServiceOrderGroups_ServicesOrder1`,
DROP FOREIGN KEY `fk_ServiceOrderResumen_ServiceOrderModules1`,
DROP FOREIGN KEY `fk_ServiceOrderResumen_StockFlow1`;

ALTER TABLE `dbssiil`.`ServicesTrabajo` 
DROP FOREIGN KEY `fk_ServicesTrabajo_Offices1`,
DROP FOREIGN KEY `fk_ServicesTrabajo_ServicesOrder1`;

ALTER TABLE `dbssiil`.`SalesRemision` 
DROP FOREIGN KEY `fk_SalesRemision_Bobeda1`;

ALTER TABLE `dbssiil`.`BobedaBusiness` 
DROP FOREIGN KEY `fk_BobeSales_Companies1`;

ALTER TABLE `dbssiil`.`ProcessMailerAttaches` 
DROP FOREIGN KEY `fk_ProcessMailerAttaches_Bobeda1`,
DROP FOREIGN KEY `fk_ProcessMailerAttaches_Companies1`;

ALTER TABLE `dbssiil`.`SalesInvoice` 
DROP FOREIGN KEY `fk_SalesInvoice_Bobeda1`,
DROP FOREIGN KEY `fk_SalesInvoice_Bobeda2`,
DROP FOREIGN KEY `fk_SalesInvoice_Bobeda3`,
DROP FOREIGN KEY `fk_SalesInvoice_ServicesTrabajo1`;

ALTER TABLE `dbssiil`.`SA_Factura` 
DROP FOREIGN KEY `fk_SI_SR_SalesInvoice1`,
DROP FOREIGN KEY `fk_SI_SR_SalesRemision1`;

ALTER TABLE `dbssiil`.`SATClaves` 
DROP FOREIGN KEY `fk_SATCatalogoClaves_SATCatalogos1`;

ALTER TABLE `dbssiil`.`Persons` 
CHANGE COLUMN `department` `department` ENUM('se', 'rf', 'vt', 'al') NULL DEFAULT NULL COMMENT 'S: Servicios\nR: Refacciones\nV: Ventas\nT: Taller' ;

ALTER TABLE `dbssiil`.`Companies` 
CHANGE COLUMN `type` `type` SET('C', 'P') NULL DEFAULT 'C' COMMENT 'C: Cliente\nP:Proveedor' ,
CHANGE COLUMN `RequirePO` `RequirePO` SET('N', 'A', 'P', 'O') NULL DEFAULT 'O' COMMENT 'A: Anterior\nP: Posterior\nO: Opcional\nN: No' ,
CHANGE COLUMN `cuenta4Pesos` `cuenta4Pesos` VARCHAR(4) NULL DEFAULT NULL COMMENT 'E:Efectivo\nC:Cheque\nT:Transferencia' ;

ALTER TABLE `dbssiil`.`Titem` 
CHANGE COLUMN `marca` `marca` VARCHAR(100) NULL DEFAULT NULL COMMENT '@deprecated' ,
CHANGE COLUMN `modelo` `modelo` VARCHAR(100) NULL DEFAULT NULL ,
CHANGE COLUMN `serie` `serie` VARCHAR(100) NULL DEFAULT NULL ;

ALTER TABLE `dbssiil`.`Forklift` 
CHANGE COLUMN `tipo` `tipo` SET('E', 'CI', 'AD') NULL DEFAULT NULL COMMENT 'E: Electrico\nCI : Combustion Intena\nAD :Aditamento' ;

ALTER TABLE `dbssiil`.`Object` 
CHANGE COLUMN `tipo` `tipo` ENUM('It','Ti','Bt','Ch','Fk','Co') NULL DEFAULT NULL COMMENT 'It: Item\nTi: Titem\nBt: Battery\nCh: Charger\nFk: Forklift\nCo: Company\n' ;

ALTER TABLE `dbssiil`.`Orcom` 
CHANGE COLUMN `folio` `folio` INT(13) NOT NULL ;

ALTER TABLE `dbssiil`.`Cheques` 
CHANGE COLUMN `numcheque` `numcheque` INT(16) NULL DEFAULT NULL COMMENT 'depretaed: sera remplazado por ChequeRow.number' ,
CHANGE COLUMN `dllCambio` `dllCambio` FLOAT(255,4) NULL DEFAULT NULL COMMENT 'Tipo de cambio Dollar\ndeprecated: seraremplazado por su correspodiente en ChequeRow' ;

ALTER TABLE `dbssiil`.`PurchaseProvider` 
CHANGE COLUMN `daycredit` `daycredit` INT(3) NULL DEFAULT NULL ;

ALTER TABLE `dbssiil`.`PurchaseCRSIIL` 
CHANGE COLUMN `daycredit` `daycredit` INT(3) NULL DEFAULT NULL ;

ALTER TABLE `dbssiil`.`ServiosOrdenEstado` 
CHANGE COLUMN `code` `code` ENUM('docpen', 'docedit', 'docautho', 'pedpen', 'pedtrans', 'pedArrb', 'pedsur', 'pedfin', 'cancel') NOT NULL COMMENT 'docedit    ->   OrdenEdit\ndocpen    ->   OrdenAuthorize\npedpen    ->   OrdenTransiting\ndocautho ->   deprecated\npedtrans  ->   OrdenArrive\npedArrb   ->   deprecated\npedsur     ->   OrdenSupply\npedfin      ->   OrdenEnd\ncancel      ->   OrdenCancel' ,
CHANGE COLUMN `ordinal` `ordinal` TINYINT(3) NOT NULL ;

ALTER TABLE `dbssiil`.`Scope` 
CHANGE COLUMN `scope` `scope` ENUM('P', 'D', 'S', 'E', '?') NOT NULL COMMENT 'P: Personal\nD: Departamento\nS: Sucursal\nE: Empres\n?: No asignado' ;

ALTER TABLE `dbssiil`.`Departments` 
CHANGE COLUMN `code` `code` ENUM('se', 'rf', 'vt', 'al') NULL DEFAULT NULL COMMENT 'S: Servicios\nR: Refacciones\nV: Ventas\nT: Taller' ;

ALTER TABLE `dbssiil`.`ProcessModule` 
CHANGE COLUMN `code` `code` VARCHAR(5) NOT NULL COMMENT 'PO: Purchases Orders\nPC : Purchases CR\nAME : Administracion Moviliario y Equipo\nPP: Purchases Provider\nOT: Relacion de Trabajo: deprecated\nCS:Cotizacion de Servicio\nMG: Movimiento de Grua\nSI : Sales Invoice\nRT : Relacion de Trabajo\nRT: Relacion de Trabajo' ;

ALTER TABLE `dbssiil`.`StockFlow` 
CHANGE COLUMN `quotation` `quotation` INT(11) NULL DEFAULT NULL COMMENT 'Donde se cotizo este item? \nSince core v9.12' ,
CHANGE COLUMN `po` `po` INT(11) NULL DEFAULT NULL COMMENT 'Donde se compro este item? \nSince core v9.12' ,
CHANGE COLUMN `estado` `estado` SET('L', 'A', 'V') NULL DEFAULT 'L' COMMENT 'L : libre\nA : Apartado\nV : Vendido' ;

ALTER TABLE `dbssiil`.`StockItem` 
CHANGE COLUMN `status` `status` ENUM('A', 'D', 'P') NULL DEFAULT 'P' COMMENT 'A: Active\nD : Delete\nP: Parcial' ,
CHANGE COLUMN `type` `type` ENUM('S', 'I', 'R') NULL DEFAULT NULL COMMENT 'Es para diferencia entre un elemento fisico y uno no fisico.\nI: Item\nS: Service\n' ;

ALTER TABLE `dbssiil`.`ProcessOperational` 
CHANGE COLUMN `folio` `folio` INT(6) NULL DEFAULT NULL ,
CHANGE COLUMN `type` `type` ENUM('TEST', 'PQ', 'PO', 'PR', 'SQ', 'SR', 'SI') NOT NULL COMMENT 'PQ : Purchases Quote\nPO : Purchases order\nPR : Purchase Remision\nSQ : Sales Quote\nSR : Sales Remision\nSI : Sales Invoice' ;

ALTER TABLE `dbssiil`.`StockSetem` 
CHANGE COLUMN `type` `type` ENUM('FR', 'BA', 'CL', 'CH', 'MN') NOT NULL COMMENT 'FR : forklift\nBA : battery\nCL : clam\nCH : charger\nMN : mina' ;

ALTER TABLE `dbssiil`.`ProcessMailer` 
CHANGE COLUMN `flag` `flag` ENUM('P', 'S', 'E') NULL DEFAULT 'P' COMMENT 'P: Pendiente\nS: Sended\nE: Error' ;

ALTER TABLE `dbssiil`.`SalesInvoice` 
CHANGE COLUMN `satEstado` `satEstado` SET('V', 'C', 'I') NULL DEFAULT NULL COMMENT 'V : viva\nC : cancelada\nI : Incomplta' ;

ALTER TABLE `dbssiil`.`Users` 
ADD CONSTRAINT `fk_Users_Persons1`
  FOREIGN KEY (`uID`)
  REFERENCES `dbssiil`.`Persons` (`pID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`Titem` 
ADD CONSTRAINT `fk_Titem_StockItem1`
  FOREIGN KEY (`id`)
  REFERENCES `dbssiil`.`StockItem` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`Movements` 
DROP FOREIGN KEY `fk_Movements_Users1`;

ALTER TABLE `dbssiil`.`Movements` ADD CONSTRAINT `fk_Movements_Users1`
  FOREIGN KEY (`createUser`)
  REFERENCES `dbssiil`.`Users` (`alias`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Movements_SalesRemision1`
  FOREIGN KEY (`sa2`)
  REFERENCES `dbssiil`.`SalesRemision` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`Resumov` 
DROP FOREIGN KEY `fk_Resumov_StockFlow1`,
DROP FOREIGN KEY `fk_Resumov_Bobeda1`;

ALTER TABLE `dbssiil`.`Resumov` ADD CONSTRAINT `fk_Resumov_GruaUso1`
  FOREIGN KEY (`use`)
  REFERENCES `dbssiil`.`GruaUso` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Resumov_Offices1`
  FOREIGN KEY (`office`)
  REFERENCES `dbssiil`.`Offices` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Resumov_StockFlow1`
  FOREIGN KEY (`titem`)
  REFERENCES `dbssiil`.`StockFlow` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Resumov_SalesRemision1`
  FOREIGN KEY (`sa`)
  REFERENCES `dbssiil`.`SalesRemision` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Resumov_Bobeda1`
  FOREIGN KEY (`poFIle`)
  REFERENCES `dbssiil`.`BobedaBusiness` (`bobeda`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`Charger` 
ADD CONSTRAINT `fk_Charger_Titem2`
  FOREIGN KEY (`id`)
  REFERENCES `dbssiil`.`Titem` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`Battery` 
ADD CONSTRAINT `fk_Battery_Titem2`
  FOREIGN KEY (`id`)
  REFERENCES `dbssiil`.`Titem` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`Forklift` 
ADD CONSTRAINT `fk_Forklift_Titem2`
  FOREIGN KEY (`id`)
  REFERENCES `dbssiil`.`Titem` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`Movtitems` 
DROP FOREIGN KEY `fk_table1_Movements1`;

ALTER TABLE `dbssiil`.`Movtitems` ADD CONSTRAINT `fk_table1_Movements1`
  FOREIGN KEY (`mov`)
  REFERENCES `dbssiil`.`Movements` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Movtitems_StockFlow1`
  FOREIGN KEY (`itemFlow`)
  REFERENCES `dbssiil`.`StockFlow` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`Orcom` 
DROP FOREIGN KEY `fk_Orcom_Users1`,
DROP FOREIGN KEY `fk_Orcom_Persons2`,
DROP FOREIGN KEY `fk_Orcom_SalesQuotation1`,
DROP FOREIGN KEY `fk_Orcom_BobedaBusiness1`,
DROP FOREIGN KEY `fk_Orcom_Companies1`;

ALTER TABLE `dbssiil`.`Orcom` ADD CONSTRAINT `fk_Orcom_Users1`
  FOREIGN KEY (`creator`)
  REFERENCES `dbssiil`.`Users` (`alias`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Orcom_Persons2`
  FOREIGN KEY (`technicalPerson`)
  REFERENCES `dbssiil`.`Persons` (`pID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Orcom_Persons1`
  FOREIGN KEY (`ownerPerson`)
  REFERENCES `dbssiil`.`Persons` (`pID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Orcom_SalesQuotation1`
  FOREIGN KEY (`quotation`)
  REFERENCES `dbssiil`.`SalesQuotation` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Orcom_Persons3`
  FOREIGN KEY (`ownerPerson2`)
  REFERENCES `dbssiil`.`Persons` (`pID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Orcom_Persons4`
  FOREIGN KEY (`pAutho`)
  REFERENCES `dbssiil`.`Persons` (`pID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Orcom_BobedaBusiness1`
  FOREIGN KEY (`poFile`)
  REFERENCES `dbssiil`.`BobedaBusiness` (`bobeda`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_Orcom_Companies1`
  FOREIGN KEY (`company`)
  REFERENCES `dbssiil`.`Companies` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`Minas` 
ADD CONSTRAINT `fk_Minas_Titem2`
  FOREIGN KEY (`id`)
  REFERENCES `dbssiil`.`Titem` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`AccesTable` 
ADD CONSTRAINT `fk_AccesTable_Policies1`
  FOREIGN KEY (`policy`)
  REFERENCES `dbssiil`.`Policies` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_AccesTable_Users1`
  FOREIGN KEY (`alias`)
  REFERENCES `dbssiil`.`Users` (`alias`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`Trace` 
ADD CONSTRAINT `fk_Trace_Users1`
  FOREIGN KEY (`user`)
  REFERENCES `dbssiil`.`Users` (`alias`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`OrcomOwners` 
ADD CONSTRAINT `fk_OrcomOwners_Persons1`
  FOREIGN KEY (`owner`)
  REFERENCES `dbssiil`.`Persons` (`pID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`PurchaseCR` 
ADD CONSTRAINT `fk_PurchaseCR_Users1`
  FOREIGN KEY (`recibe`)
  REFERENCES `dbssiil`.`Users` (`alias`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_PurchaseCR_Users2`
  FOREIGN KEY (`payUser`)
  REFERENCES `dbssiil`.`Users` (`alias`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`Configuration` 
ADD CONSTRAINT `fk_Configuration_Users1`
  FOREIGN KEY (`user`)
  REFERENCES `dbssiil`.`Users` (`alias`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`Instances` 
ADD CONSTRAINT `fk_Instances_Users1`
  FOREIGN KEY (`user`)
  REFERENCES `dbssiil`.`Users` (`alias`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`ContabilidadAsientos` 
DROP FOREIGN KEY `fk_ContabilidadAsientos_ProcessOperational1`;

ALTER TABLE `dbssiil`.`ContabilidadAsientos` ADD CONSTRAINT `fk_SIILContabilidadRegistro_SIILCatalogoCuentas1`
  FOREIGN KEY (`cuentaCargo`)
  REFERENCES `dbssiil`.`CatalogoCuentas` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_SIILContabilidadRegistro_SIILCatalogoCuentas2`
  FOREIGN KEY (`cuentaAbono`)
  REFERENCES `dbssiil`.`CatalogoCuentas` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_ContabilidadAsientos_ProcessOperational1`
  FOREIGN KEY (`operation`)
  REFERENCES `dbssiil`.`ProcessOperational` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`PurchasesOperational` 
ADD CONSTRAINT `fk_PurchasesOperational_ProcessOperational1`
  FOREIGN KEY (`id`)
  REFERENCES `dbssiil`.`ProcessOperational` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_PurchasesOperational_PurchasesProvider1`
  FOREIGN KEY (`provider`)
  REFERENCES `dbssiil`.`PurchasesProvider` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`ProcessStates` 
ADD CONSTRAINT `fk_ProcessStates_ProcessModule1`
  FOREIGN KEY (`module`)
  REFERENCES `dbssiil`.`ProcessModule` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`StockFlow` 
DROP FOREIGN KEY `fk_StockFlow_StockItem1`,
DROP FOREIGN KEY `fk_StockFlow_SalesQuotation1`;

ALTER TABLE `dbssiil`.`StockFlow` ADD CONSTRAINT `fk_StockFlow_StockItem1`
  FOREIGN KEY (`idItem`)
  REFERENCES `dbssiil`.`StockItem` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_StockFlow_SalesQuotation1`
  FOREIGN KEY (`quotation`)
  REFERENCES `dbssiil`.`SalesQuotation` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_StockFlow_PurchasesOrder1`
  FOREIGN KEY (`po`)
  REFERENCES `dbssiil`.`PurchasesOrder` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`StockItem` 
ADD CONSTRAINT `fk_StockItem_PurchasesProvider1`
  FOREIGN KEY (`provider`)
  REFERENCES `dbssiil`.`PurchasesProvider` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_StockItem_StockClaveFiscal1`
  FOREIGN KEY (`claveFical`)
  REFERENCES `dbssiil`.`StockClaveFiscal` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`StockOffice` 
ADD CONSTRAINT `fk_StockOffice_StockItem1`
  FOREIGN KEY (`idItem`)
  REFERENCES `dbssiil`.`StockItem` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`StockRefection` 
ADD CONSTRAINT `fk_StockRefection_StockItem1`
  FOREIGN KEY (`idItem`)
  REFERENCES `dbssiil`.`StockItem` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`AdministracionBuilding` 
DROP FOREIGN KEY `fk_Building_Offices1`;

ALTER TABLE `dbssiil`.`AdministracionBuilding` ADD CONSTRAINT `fk_Building_Offices1`
  FOREIGN KEY (`office`)
  REFERENCES `dbssiil`.`Offices` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_AdministracionBuilding_AdministracionBuilding1`
  FOREIGN KEY (`parent`)
  REFERENCES `dbssiil`.`AdministracionBuilding` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`StockContainer` 
ADD CONSTRAINT `fk_StockContainer_AdministracionBuilding1`
  FOREIGN KEY (`building`)
  REFERENCES `dbssiil`.`AdministracionBuilding` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_StockContainer_StockContainer1`
  FOREIGN KEY (`parent`)
  REFERENCES `dbssiil`.`StockContainer` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`AdministracionEstacion` 
DROP FOREIGN KEY `fk_table1_StockContainer1`;

ALTER TABLE `dbssiil`.`AdministracionEstacion` ADD CONSTRAINT `fk_table1_StockContainer1`
  FOREIGN KEY (`estation`)
  REFERENCES `dbssiil`.`StockContainer` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_AdministracionEstacion_Persons1`
  FOREIGN KEY (`owner`)
  REFERENCES `dbssiil`.`Persons` (`pID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`ProcessOperational` 
DROP FOREIGN KEY `fk_ProcessOperational_ProcessStates1`,
DROP FOREIGN KEY `fk_ProcessOperational_Persons1`;

ALTER TABLE `dbssiil`.`ProcessOperational` ADD CONSTRAINT `fk_ProcessOperational_ProcessStates1`
  FOREIGN KEY (`state`)
  REFERENCES `dbssiil`.`ProcessStates` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_ProcessOperational_Persons1`
  FOREIGN KEY (`operator`)
  REFERENCES `dbssiil`.`Persons` (`pID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_ProcessOperational_Offices1`
  FOREIGN KEY (`office`)
  REFERENCES `dbssiil`.`Offices` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`StockAllocated` 
DROP FOREIGN KEY `fk_StockAllocated_StockFlow1`;

ALTER TABLE `dbssiil`.`StockAllocated` ADD CONSTRAINT `fk_StockAllocated_StockFlow1`
  FOREIGN KEY (`id`)
  REFERENCES `dbssiil`.`StockFlow` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_StockAllocated_StockContainer1`
  FOREIGN KEY (`container`)
  REFERENCES `dbssiil`.`StockContainer` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`StockSetem` 
ADD CONSTRAINT `fk_StockSetem_StockItem1`
  FOREIGN KEY (`id`)
  REFERENCES `dbssiil`.`StockItem` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`StockCross` 
ADD CONSTRAINT `fk_StockCross_StockRefection1`
  FOREIGN KEY (`refection`)
  REFERENCES `dbssiil`.`StockRefection` (`idItem`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_StockCross_StockRefection2`
  FOREIGN KEY (`cross`)
  REFERENCES `dbssiil`.`StockRefection` (`idItem`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`StockExternal` 
ADD CONSTRAINT `fk_StockExternal_StockRefection1`
  FOREIGN KEY (`id`)
  REFERENCES `dbssiil`.`StockRefection` (`idItem`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_StockExternal_StockRefection2`
  FOREIGN KEY (`extern`)
  REFERENCES `dbssiil`.`StockRefection` (`idItem`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`PurchasesOrder` 
ADD CONSTRAINT `fk_PurchasesOrder_PurchasesOperational1`
  FOREIGN KEY (`id`)
  REFERENCES `dbssiil`.`PurchasesOperational` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`SalesQuotation` 
DROP FOREIGN KEY `fk_SalesQuotation_SalesOperational1`;

ALTER TABLE `dbssiil`.`SalesQuotation` ADD CONSTRAINT `fk_SalesQuotation_SalesOperational1`
  FOREIGN KEY (`id`)
  REFERENCES `dbssiil`.`SalesOperational` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_SalesQuotation_Orcom1`
  FOREIGN KEY (`orserv`)
  REFERENCES `dbssiil`.`Orcom` (`ID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`SalesOperational` 
DROP FOREIGN KEY `fk_SalesOperational_ProcessOperational1`;

ALTER TABLE `dbssiil`.`SalesOperational` ADD CONSTRAINT `fk_SalesOperational_ProcessOperational1`
  FOREIGN KEY (`id`)
  REFERENCES `dbssiil`.`ProcessOperational` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_SalesOperational_Companies1`
  FOREIGN KEY (`company`)
  REFERENCES `dbssiil`.`Companies` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`ProcessOperationalRows` 
DROP FOREIGN KEY `fk_ProcessOperationalRows_StockFlow1`;

ALTER TABLE `dbssiil`.`ProcessOperationalRows` ADD CONSTRAINT `fk_ProcessOperationalRows_StockFlow1`
  FOREIGN KEY (`item`)
  REFERENCES `dbssiil`.`StockFlow` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_ProcessOperationalRows_ProcessOperational1`
  FOREIGN KEY (`op`)
  REFERENCES `dbssiil`.`ProcessOperational` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`StockService` 
ADD CONSTRAINT `fk_StockService_StockItem1`
  FOREIGN KEY (`id`)
  REFERENCES `dbssiil`.`StockItem` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`ServicesOrder` 
DROP FOREIGN KEY `fk_ServicesOrder_Offices1`,
DROP FOREIGN KEY `fk_ServicesOrder_SalesRemision1`,
DROP FOREIGN KEY `fk_ServicesOrder_Bobeda1`;

ALTER TABLE `dbssiil`.`ServicesOrder` ADD CONSTRAINT `fk_ServicesOrder_Companies1`
  FOREIGN KEY (`company`)
  REFERENCES `dbssiil`.`Companies` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_ServicesOrder_Orcom1`
  FOREIGN KEY (`quoteService`)
  REFERENCES `dbssiil`.`Orcom` (`ID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_ServicesOrder_StockFlow1`
  FOREIGN KEY (`itemFlow`)
  REFERENCES `dbssiil`.`StockFlow` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_ServicesOrder_Offices1`
  FOREIGN KEY (`office`)
  REFERENCES `dbssiil`.`Offices` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_ServicesOrder_technical`
  FOREIGN KEY (`technical`)
  REFERENCES `dbssiil`.`Persons` (`pID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_ServicesOrder_SalesRemision1`
  FOREIGN KEY (`SalesRemision`)
  REFERENCES `dbssiil`.`SalesRemision` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_ServicesOrder_Bobeda1`
  FOREIGN KEY (`archivo`)
  REFERENCES `dbssiil`.`Bobeda` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`HistoryConsume` 
ADD CONSTRAINT `fk_HistoryConsume_StockItem1`
  FOREIGN KEY (`consumer`)
  REFERENCES `dbssiil`.`StockItem` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_HistoryConsume_StockItem2`
  FOREIGN KEY (`intake`)
  REFERENCES `dbssiil`.`StockItem` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`ServiceOrderResumen` 
ADD CONSTRAINT `fk_ServiceOrderGroups_ServicesOrder1`
  FOREIGN KEY (`lastServ`)
  REFERENCES `dbssiil`.`ServicesOrder` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_ServiceOrderResumen_ServiceOrderModules1`
  FOREIGN KEY (`module`)
  REFERENCES `dbssiil`.`ServiceOrderModules` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_ServiceOrderResumen_StockFlow1`
  FOREIGN KEY (`flowItem`)
  REFERENCES `dbssiil`.`StockFlow` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`ServicesTrabajo` 
DROP FOREIGN KEY `fk_ServiceTrabajo_ProcessStates1`,
DROP FOREIGN KEY `fk_ServiceTrabajo_Companies1`,
DROP FOREIGN KEY `fk_ServiceTrabajo_Mechanic`,
DROP FOREIGN KEY `fk_ServicesTrabajo_SalesRemision1`,
DROP FOREIGN KEY `fk_ServicesTrabajo_Orcom1`;

ALTER TABLE `dbssiil`.`ServicesTrabajo` ADD CONSTRAINT `fk_ServiceTrabajo_ProcessStates1`
  FOREIGN KEY (`state`)
  REFERENCES `dbssiil`.`ProcessStates` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_ServiceTrabajo_Companies1`
  FOREIGN KEY (`company`)
  REFERENCES `dbssiil`.`Companies` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_ServiceTrabajo_Mechanic`
  FOREIGN KEY (`mechanic`)
  REFERENCES `dbssiil`.`Persons` (`pID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_ServicesTrabajo_SalesRemision1`
  FOREIGN KEY (`sa`)
  REFERENCES `dbssiil`.`SalesRemision` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_ServicesTrabajo_Orcom1`
  FOREIGN KEY (`quotedService`)
  REFERENCES `dbssiil`.`Orcom` (`ID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_ServicesTrabajo_Offices1`
  FOREIGN KEY (`office`)
  REFERENCES `dbssiil`.`Offices` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_ServicesTrabajo_ServicesOrder1`
  FOREIGN KEY (`orderServ`)
  REFERENCES `dbssiil`.`ServicesOrder` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`SalesRemision` 
DROP FOREIGN KEY `fk_SalesRemision_SalesOperational1`;

ALTER TABLE `dbssiil`.`SalesRemision` ADD CONSTRAINT `fk_SalesRemision_SalesOperational1`
  FOREIGN KEY (`id`)
  REFERENCES `dbssiil`.`SalesOperational` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_SalesRemision_Bobeda1`
  FOREIGN KEY (`archivoOS`)
  REFERENCES `dbssiil`.`Bobeda` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`BobedaBusiness` 
DROP FOREIGN KEY `fk_table1_Bobeda1`;

ALTER TABLE `dbssiil`.`BobedaBusiness` ADD CONSTRAINT `fk_table1_Bobeda1`
  FOREIGN KEY (`bobeda`)
  REFERENCES `dbssiil`.`Bobeda` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_BobeSales_Companies1`
  FOREIGN KEY (`company`)
  REFERENCES `dbssiil`.`Companies` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`ProcessMailerAttaches` 
DROP FOREIGN KEY `fk_ProcessMailerAttaches_ProcessMailer1`;

ALTER TABLE `dbssiil`.`ProcessMailerAttaches` ADD CONSTRAINT `fk_ProcessMailerAttaches_ProcessMailer1`
  FOREIGN KEY (`mail`)
  REFERENCES `dbssiil`.`ProcessMailer` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_ProcessMailerAttaches_Bobeda1`
  FOREIGN KEY (`vault`)
  REFERENCES `dbssiil`.`Bobeda` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_ProcessMailerAttaches_Companies1`
  FOREIGN KEY (`company`)
  REFERENCES `dbssiil`.`Companies` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`SalesInvoice` 
DROP FOREIGN KEY `fk_SalesInvoice_SalesOperational1`,
DROP FOREIGN KEY `fk_SalesInvoice_ProcessMailer1`;

ALTER TABLE `dbssiil`.`SalesInvoice` ADD CONSTRAINT `fk_SalesInvoice_SalesOperational1`
  FOREIGN KEY (`id`)
  REFERENCES `dbssiil`.`SalesOperational` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_SalesInvoice_ProcessMailer1`
  FOREIGN KEY (`email`)
  REFERENCES `dbssiil`.`ProcessMailer` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_SalesInvoice_Bobeda1`
  FOREIGN KEY (`xml`)
  REFERENCES `dbssiil`.`Bobeda` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_SalesInvoice_Bobeda2`
  FOREIGN KEY (`pdf`)
  REFERENCES `dbssiil`.`Bobeda` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_SalesInvoice_Bobeda3`
  FOREIGN KEY (`ordenesArchivo`)
  REFERENCES `dbssiil`.`Bobeda` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_SalesInvoice_ServicesTrabajo1`
  FOREIGN KEY (`trabajo`)
  REFERENCES `dbssiil`.`ServicesTrabajo` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`SA_Factura` 
ADD CONSTRAINT `fk_SI_SR_SalesInvoice1`
  FOREIGN KEY (`invoice`)
  REFERENCES `dbssiil`.`SalesInvoice` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_SI_SR_SalesRemision1`
  FOREIGN KEY (`sa`)
  REFERENCES `dbssiil`.`SalesRemision` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `dbssiil`.`SATClaves` 
ADD CONSTRAINT `fk_SATCatalogoClaves_SATCatalogos1`
  FOREIGN KEY (`catalog`)
  REFERENCES `dbssiil`.`SATCatalogo` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;


-- -----------------------------------------------------
-- Placeholder table for view `dbssiil`.`Users_Resolved`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbssiil`.`Users_Resolved` (`pID` INT, `nameN1` INT, `nameNs` INT, `nameAP` INT, `nameAM` INT, `nameP` INT, `alias` INT, `passwdMD5` INT, `active` INT, `office` INT, `suc` INT, `uID` INT, `BD` INT, `email` INT, `department` INT);

-- -----------------------------------------------------
-- Placeholder table for view `dbssiil`.`Movements_Resolved`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbssiil`.`Movements_Resolved` (`id` INT, `folio` INT, `fhmov` INT, `tmov` INT, `uso` INT, `sa` INT, `compNumber` INT, `compName` INT, `firma` INT, `note` INT, `movClass` INT, `owner` INT, `suc` INT, `numeco` INT, `marca` INT, `modelo` INT, `serie` INT, `titemClass` INT, `horometro` INT);

-- -----------------------------------------------------
-- Placeholder table for view `dbssiil`.`Forklift_Resolved`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbssiil`.`Forklift_Resolved` (`BD` INT, `idT` INT, `idFlow` INT, `number` INT, `description` INT, `Marca` INT, `make` INT, `Modelo` INT, `model` INT, `activeSerie` INT, `serie` INT, `batteryBD` INT, `batteryNumber` INT, `chargerBD` INT, `chargerNumber` INT);

-- -----------------------------------------------------
-- Placeholder table for view `dbssiil`.`Resumov_Resolved`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbssiil`.`Resumov_Resolved` (`id` INT, `titemBD` INT, `titemNumber` INT, `orden` INT, `BD` INT, `marca` INT, `modelo` INT, `serie` INT, `cBD` INT, `cNumber` INT, `cName` INT, `suc` INT, `uso` INT, `note` INT, `fhmov` INT, `batteryNumber` INT, `chargerNumber` INT, `forkliftNumber` INT, `titem` INT, `idFlow` INT, `poFile` INT);

-- -----------------------------------------------------
-- Placeholder table for view `dbssiil`.`Titem_Resolved`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbssiil`.`Titem_Resolved` (`BD` INT, `idT` INT, `idFlow` INT, `number` INT, `description` INT, `Marca` INT, `make` INT, `Modelo` INT, `model` INT, `activeSerie` INT, `serie` INT, `name` INT);

-- -----------------------------------------------------
-- Placeholder table for view `dbssiil`.`Resumov_ResolvedFull`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbssiil`.`Resumov_ResolvedFull` (`fnumber` INT, `orden` INT, `fBD` INT, `fmarca` INT, `fmodelo` INT, `fserie` INT, `cBD` INT, `cnumber` INT, `cname` INT, `suc` INT, `uso` INT, `note` INT, `fhmov` INT, `batteryBD` INT, `batteryNumber` INT, `chargerBD` INT, `chargerNumber` INT, `poFile` INT, `idFlow` INT);

-- -----------------------------------------------------
-- Placeholder table for view `dbssiil`.`Orcom_Resolved`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbssiil`.`Orcom_Resolved` (`BD` INT, `estado` INT, `serie` INT, `folio` INT, `compBD` INT, `compNumber` INT, `compName` INT, `fhAutho` INT, `fhETA` INT, `fhArribo` INT, `fhSurtido` INT, `fhFin` INT, `ownerName` INT, `ownerPerson` INT, `fhFolio` INT, `suc` INT, `sa` INT, `fhEdit` INT, `creator` INT, `ID` INT, `fhETAfl` INT, `department` INT);

-- -----------------------------------------------------
-- Placeholder table for view `dbssiil`.`AccesTable_Resolved`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbssiil`.`AccesTable_Resolved` (`policy` INT, `polName` INT, `alias` INT, `acces` INT);

-- -----------------------------------------------------
-- Placeholder table for view `dbssiil`.`Cheques_Resolved`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbssiil`.`Cheques_Resolved` (`ID` INT, `fh` INT, `compBD` INT, `compNumber` INT, `compName` INT, `monto` INT, `moneda` INT, `factSerie` INT, `factFolio` INT, `fact` INT, `suc` INT, `numcheque` INT, `dllCambio` INT, `conv` INT, `pesos` INT, `fhDeposito` INT, `note` INT);

-- -----------------------------------------------------
-- Placeholder table for view `dbssiil`.`OrcomOwners_Resolved`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbssiil`.`OrcomOwners_Resolved` (`owner` INT, `nameN1` INT, `nameNs` INT, `nameAP` INT, `nameAM` INT);

-- -----------------------------------------------------
-- Placeholder table for view `dbssiil`.`PurchaseProvider_Resolved`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbssiil`.`PurchaseProvider_Resolved` (`ID` INT, `daycredit` INT, `account` INT, `name` INT, `rfc` INT);

-- -----------------------------------------------------
-- Placeholder table for view `dbssiil`.`PurchaseCR_Resolved`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbssiil`.`PurchaseCR_Resolved` (`ID` INT, `fhRev` INT, `amount` INT, `amountStr` INT, `currency` INT, `fhPg` INT, `daycredit` INT, `provName` INT, `prov` INT, `obser` INT, `recibe` INT, `recibeStr` INT, `fhFolio` INT, `suc` INT, `account` INT, `status` INT);

-- -----------------------------------------------------
-- Placeholder table for view `dbssiil`.`PurchaseCRFD_Resolved`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbssiil`.`PurchaseCRFD_Resolved` (`ID` INT, `fID` INT, `folio` INT, `serie` INT, `monto` INT, `montoStr` INT, `PO` INT, `SA` INT);

-- -----------------------------------------------------
-- Placeholder table for view `dbssiil`.`PurchaseCRFull_Resolved`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbssiil`.`PurchaseCRFull_Resolved` (`ID` INT, `fhRev` INT, `amount` INT, `amountStr` INT, `currency` INT, `fhPg` INT, `daycredit` INT, `provName` INT, `prov` INT, `obser` INT, `recibe` INT, `recibeStr` INT, `fhFolio` INT, `suc` INT, `account` INT, `status` INT, `fID` INT, `folio` INT, `serie` INT, `monto` INT, `montoStr` INT, `PO` INT, `SA` INT);

-- -----------------------------------------------------
-- Placeholder table for view `dbssiil`.`Scope_Resolved`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbssiil`.`Scope_Resolved` (`uID` INT, `alias` INT, `uActive` INT, `pID` INT, `email` INT, `pActive` INT, `department` INT, `BD` INT, `office` INT, `module` INT, `context` INT, `scope` INT, `subcat1` INT);

-- -----------------------------------------------------
-- Placeholder table for view `dbssiil`.`PurchasesOrder_Resolved`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbssiil`.`PurchasesOrder_Resolved` (`id` INT, `provider` INT, `provNameShort` INT, `provRazonSocial` INT, `state` INT, `stateModule` INT, `stateCode` INT, `stateName` INT, `stateOrdinal` INT, `operator` INT, `opeNameN1` INT, `opeNameAP` INT, `fhFolio` INT, `uxfhFolio` INT, `strFolio` INT, `serie` INT, `folio` INT, `type` INT, `flag` INT);

-- -----------------------------------------------------
-- Placeholder table for view `dbssiil`.`Hequis_Resolved`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbssiil`.`Hequis_Resolved` (`id` INT, `number` INT, `make` INT, `model` INT, `serie` INT, `flowID` INT);

-- -----------------------------------------------------
-- Placeholder table for view `dbssiil`.`ServiceOrderResumen_Resolved`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbssiil`.`ServiceOrderResumen_Resolved` (`id` INT, `sa` INT, `type` INT, `horometro` INT, `description` INT, `fhService` INT, `quoteService` INT, `itemFlow` INT, `compNumber` INT, `compName` INT, `module` INT, `titemNumber` INT, `titemMake` INT, `titemModel` INT, `titemSerie` INT);

-- -----------------------------------------------------
-- Placeholder table for view `dbssiil`.`ServicesTrabajo_Resolved`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbssiil`.`ServicesTrabajo_Resolved` (`id` INT, `state` INT, `sheet` INT, `folio` INT, `serie` INT, `company` INT, `compNumber` INT, `compName` INT, `mechanic` INT, `nameN1` INT, `nameAP` INT, `fhToDo` INT, `brief` INT);

-- -----------------------------------------------------
-- Placeholder table for view `dbssiil`.`Battery_Resolved`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbssiil`.`Battery_Resolved` (`BD` INT, `idT` INT, `idFlow` INT, `number` INT, `description` INT, `Marca` INT, `make` INT, `Modelo` INT, `model` INT, `activeSerie` INT, `serie` INT);

-- -----------------------------------------------------
-- Placeholder table for view `dbssiil`.`Charger_Resolved`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbssiil`.`Charger_Resolved` (`BD` INT, `idT` INT, `idFlow` INT, `number` INT, `description` INT, `Marca` INT, `make` INT, `Modelo` INT, `model` INT, `activeSerie` INT, `serie` INT);

-- -----------------------------------------------------
-- Placeholder table for view `dbssiil`.`Minas_Resolved`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbssiil`.`Minas_Resolved` (`BD` INT, `idT` INT, `idFlow` INT, `number` INT, `description` INT, `Marca` INT, `make` INT, `Modelo` INT, `model` INT, `activeSerie` INT, `serie` INT);

-- -----------------------------------------------------
-- Placeholder table for view `dbssiil`.`ServicesOrder_Resolved`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbssiil`.`ServicesOrder_Resolved` (`id` INT, `sa` INT, `type` INT, `horometro` INT, `description` INT, `fhService` INT, `quoteService` INT, `itemFlow` INT, `compNumber` INT, `compName` INT, `titemNumber` INT, `titemMake` INT, `titemModel` INT, `titemSerie` INT);

-- -----------------------------------------------------
-- Placeholder table for view `dbssiil`.`SalesRemision_Resolved`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbssiil`.`SalesRemision_Resolved` (`id` INT, `state` INT, `operator` INT, `fhFolio` INT, `strFolio` INT, `serie` INT, `folio` INT, `type` INT, `office` INT, `fhInit` INT, `company` INT);

-- -----------------------------------------------------
-- Placeholder table for view `dbssiil`.`BobedaBusiness_Resolved`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbssiil`.`BobedaBusiness_Resolved` (`inBobedaID` INT, `inBobedaFolio` INT, `fhFolio` INT, `nombre` INT, `brief` INT, `inTableID` INT, `inTableFolio` INT, `id` INT, `compNumber` INT);

-- -----------------------------------------------------
-- Placeholder table for view `dbssiil`.`BobedaOrcom_Resolved`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbssiil`.`BobedaOrcom_Resolved` (`inBobedaID` INT, `inBobedaFolio` INT, `fhFolio` INT, `nombre` INT, `brief` INT, `inTableID` INT, `inTableFolio` INT, `orcomNumber` INT, `company` INT, `compNumber` INT);

-- -----------------------------------------------------
-- Placeholder table for view `dbssiil`.`BobedaResumov_Resolved`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbssiil`.`BobedaResumov_Resolved` (`inBobedaID` INT, `inBobedaFolio` INT, `fhFolio` INT, `nombre` INT, `brief` INT, `inTableID` INT, `inTableFolio` INT, `company` INT, `compNumber` INT);

-- -----------------------------------------------------
-- Placeholder table for view `dbssiil`.`Item_Resolved`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbssiil`.`Item_Resolved` (`BD` INT, `idFlow` INT, `number` INT, `description` INT, `Marca` INT, `make` INT, `Modelo` INT, `model` INT, `activeSerie` INT, `serie` INT, `name` INT, `estado` INT);

-- -----------------------------------------------------
-- Placeholder table for view `dbssiil`.`SalesInvoice_Resolved`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbssiil`.`SalesInvoice_Resolved` (`id` INT, `serie` INT, `folio` INT, `state` INT);

-- -----------------------------------------------------
-- Placeholder table for view `dbssiil`.`Orcom_Resolved2`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dbssiil`.`Orcom_Resolved2` (`BD` INT, `estado` INT, `serie` INT, `folio` INT, `compBD` INT, `compNumber` INT, `compName` INT, `fhAutho` INT, `fhETA` INT, `fhArribo` INT, `fhSurtido` INT, `fhFin` INT, `ownerName` INT, `ownerPerson` INT, `fhFolio` INT, `suc` INT, `sa` INT, `fhEdit` INT, `creator` INT, `ID` INT, `fhETAfl` INT, `department` INT);


USE `dbssiil`;

-- -----------------------------------------------------
-- View `dbssiil`.`Users_Resolved`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dbssiil`.`Users_Resolved`;
USE `dbssiil`;
CREATE  OR REPLACE VIEW `Users_Resolved` AS
SELECT
	Persons.pID, 
	Persons.nameN1,
	Persons.nameNs,
	Persons.nameAP,
	Persons.nameAM, 
    CONCAT(Persons.nameN1,' ',Persons.nameAP) as nameP,
	Users.alias,
	Users.passwdMD5,
	Persons.active,
    Persons.office,
    Persons.office as suc,
    Users.uID,
    Users.BD,
    Persons.email,
    Persons.department
FROM Persons
JOIN Users
ON Persons.pID = Users.uID and Persons.BD = Users.BD;


USE `dbssiil`;

-- -----------------------------------------------------
-- View `dbssiil`.`Movements_Resolved`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dbssiil`.`Movements_Resolved`;
USE `dbssiil`;
CREATE  OR REPLACE VIEW `Movements_Resolved` AS
SELECT 
	`Movements`.`id`,
	`Movements`.`folio`,
	`Movements`.`fhmov`, 
	`Movements`.`tmov`, 
	`Movements`.`uso`,
	`Movements`.`sa`,
	`Companies`.`number` as `compNumber`,
	`Companies`.`name` as `compName`,
	`Movements`.`firma`,
	`Movements`.`note`,
	`Movements`.`movClass`,
	`Movements`.`owner`,
    `Movements`.`suc`,
    `Movtitems`.`numeco`,
    `Movtitems`.`marca`,
    `Movtitems`.`modelo`,
    `Movtitems`.`serie`,
    `Movtitems`.`titemClass`,
	`Movtitems`.`horometro`
FROM `Movements`,`Companies`,`Movtitems`
WHERE `Movements`.`compNumber` = `Companies`.`number` and `Movements`.`id` = `Movtitems`.`mov`
ORDER BY `Movements`.`fhmov` DESC, `Movements`.`folio` DESC;


USE `dbssiil`;

-- -----------------------------------------------------
-- View `dbssiil`.`Forklift_Resolved`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dbssiil`.`Forklift_Resolved`;
USE `dbssiil`;
CREATE  OR REPLACE VIEW `Forklift_Resolved` AS
SELECT
    'bc.tj' as BD,
    Titem.id as idT,
    StockFlow.id as idFlow,
	StockItem.`number`,
	StockItem.description,
    StockItem.make as Marca,
    StockItem.make,
    StockItem.model as Modelo,
    StockItem.model,
    StockFlow.activeSerie,
    StockFlow.serie,
    'tj.bc' as batteryBD,
    Forklift.batteryNumber,
    'tj.bc' as chargerBD,
    Forklift.chargerNumber
FROM
	StockItem,Titem,StockFlow,Forklift
WHERE
	StockItem.id = Titem.id AND StockFlow.idItem = StockItem.id AND Titem.id = Forklift.id;


USE `dbssiil`;

-- -----------------------------------------------------
-- View `dbssiil`.`Resumov_Resolved`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dbssiil`.`Resumov_Resolved`;
USE `dbssiil`;
CREATE  OR REPLACE VIEW `Resumov_Resolved` AS
SELECT 
	Resumov.id,
    Resumov.titemBD,
    Resumov.titemNumber,
    abs(Resumov.titemNumber) as orden,
	Titem_Resolved.BD,
	Titem_Resolved.marca,
	Titem_Resolved.modelo,
	Titem_Resolved.serie,
	Companies.BD as cBD,
	Companies.number as cNumber,
	Companies.name as cName,
	Resumov.suc,
	Resumov.uso,
	Resumov.note,
    Resumov.fhmov,
    Resumov.batteryNumber,
    Resumov.chargerNumber,
    Resumov.forkliftNumber,
    Resumov.titem,
    Titem_Resolved.idFlow,
    Resumov.poFile
FROM Resumov,Companies,Titem_Resolved
WHERE 
(
	Resumov.compBD = Companies.BD 
	and 
	(Resumov.compNumber = Companies.number or Resumov.compNumber IS NULL)
) 
and 
(Resumov.titemBD = Titem_Resolved.BD and Resumov.titem = Titem_Resolved.idFlow)
ORDER BY orden;


USE `dbssiil`;

-- -----------------------------------------------------
-- View `dbssiil`.`Titem_Resolved`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dbssiil`.`Titem_Resolved`;
USE `dbssiil`;
CREATE  OR REPLACE VIEW `Titem_Resolved` AS
SELECT
    'bc.tj' as BD,
    Titem.id as idT,
    StockFlow.id as idFlow,
	StockItem.number,
	StockItem.description,
    StockItem.make as Marca,
    StockItem.make,
    StockItem.model as Modelo,
    StockItem.model,
    StockFlow.activeSerie,
    StockFlow.serie,
	StockItem.description as `name`
FROM
	StockItem,Titem,StockFlow
WHERE
	StockItem.id = Titem.id AND Titem.id = StockFlow.idItem;


USE `dbssiil`;

-- -----------------------------------------------------
-- View `dbssiil`.`Resumov_ResolvedFull`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dbssiil`.`Resumov_ResolvedFull`;
USE `dbssiil`;
CREATE  OR REPLACE VIEW `Resumov_ResolvedFull` AS
SELECT 
	Forklift_Resolved.number as fnumber,
    abs(Forklift_Resolved.number) as orden,
	Forklift_Resolved.BD as fBD,
	Forklift_Resolved.marca as fmarca,
	Forklift_Resolved.modelo as fmodelo,
	Forklift_Resolved.serie as fserie,
	Companies.BD as cBD,
	Companies.number as cnumber,
	Companies.name as cname,
	Resumov.suc,
	Resumov.uso,
	Resumov.note,
    Resumov.fhmov,
    Forklift_Resolved.batteryBD,
    Forklift_Resolved.batteryNumber,
    Forklift_Resolved.chargerBD,
    Forklift_Resolved.chargerNumber,
    Resumov.poFile,
    Forklift_Resolved.idFlow
FROM Resumov,Companies,Forklift_Resolved
WHERE 
(
Resumov.compBD = Companies.BD 
and 
(
Resumov.compNumber = Companies.number or Resumov.compNumber is NULL)
) 
and 
((Resumov.titemBD = Forklift_Resolved.BD and Resumov.titem = Forklift_Resolved.idFlow))
ORDER BY orden;


USE `dbssiil`;

-- -----------------------------------------------------
-- View `dbssiil`.`Orcom_Resolved`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dbssiil`.`Orcom_Resolved`;
USE `dbssiil`;
CREATE  OR REPLACE VIEW `Orcom_Resolved` AS
SELECT 
	Orcom.BD,
    Orcom.estado,
    Orcom.serie,
    Orcom.folio,
    Orcom.compBD,
    Orcom.compNumber,
    Companies.name as compName,
    Orcom.fhAutho,
    Orcom.fhETA,
    Orcom.fhArribo,
    Orcom.fhSurtido,
    Orcom.fhFin,
    Orcom.ownerName,
    Orcom.ownerPerson,
    Orcom.fhFolio,
    Orcom.suc,
    Orcom.sa,
    Orcom.fhEdit,
    Orcom.creator,
    Orcom.ID,
    Orcom.fhETAfl,
    Orcom.department
FROM Orcom 
JOIN Companies ON Orcom.compNumber = Companies.number;


USE `dbssiil`;

-- -----------------------------------------------------
-- View `dbssiil`.`AccesTable_Resolved`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dbssiil`.`AccesTable_Resolved`;
USE `dbssiil`;
CREATE  OR REPLACE VIEW `AccesTable_Resolved` AS
SELECT policy,Policies.name as polName,alias,acces 
FROM AccesTable,Policies
WHERE AccesTable.policy = Policies.id;


USE `dbssiil`;

-- -----------------------------------------------------
-- View `dbssiil`.`Cheques_Resolved`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dbssiil`.`Cheques_Resolved`;
USE `dbssiil`;
CREATE  OR REPLACE VIEW `Cheques_Resolved` AS
SELECT 
Cheques.ID,
Cheques.fh,
Companies.BD as compBD,
Cheques.compNumber,
Companies.name as compName, 
Cheques.monto, 
Cheques.moneda, 
Cheques.factSerie,
Cheques.factFolio,
CONCAT(Cheques.factSerie,Cheques.factFolio) as fact,
Cheques.suc,
Cheques.numcheque,
Cheques.dllCambio,
IF(STRCMP(Cheques.moneda,'dll'),null,Cheques.dllCambio * Cheques.monto) as conv,
Cheques.pesos,
Cheques.fhDeposito,
Cheques.note
FROM Cheques JOIN Companies
ON Cheques.compBD = Companies.BD and Cheques.compNumber = Companies.number;


USE `dbssiil`;

-- -----------------------------------------------------
-- View `dbssiil`.`OrcomOwners_Resolved`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dbssiil`.`OrcomOwners_Resolved`;
USE `dbssiil`;
CREATE  OR REPLACE VIEW `OrcomOwners_Resolved` AS
SELECT owner,nameN1,nameNs,nameAP,nameAM
FROM 
OrcomOwners INNER JOIN Persons 
ON Persons.pID = OrcomOwners.owner;


USE `dbssiil`;

-- -----------------------------------------------------
-- View `dbssiil`.`PurchaseProvider_Resolved`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dbssiil`.`PurchaseProvider_Resolved`;
USE `dbssiil`;
CREATE  OR REPLACE VIEW `PurchaseProvider_Resolved` AS
SELECT 
	PurchaseProvider.ID,
	PurchaseProvider.daycredit,
	PurchaseProvider.account,
	PurchaseProvider.name,
	PurchaseProviderMX.rfc 
FROM PurchaseProvider,PurchaseProviderMX,PurchaseProviderMXSIIL
WHERE PurchaseProvider.ID = PurchaseProviderMX.ID AND PurchaseProviderMX.ID = PurchaseProviderMXSIIL.ID;


USE `dbssiil`;

-- -----------------------------------------------------
-- View `dbssiil`.`PurchaseCR_Resolved`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dbssiil`.`PurchaseCR_Resolved`;
USE `dbssiil`;
CREATE  OR REPLACE VIEW `PurchaseCR_Resolved` AS
SELECT 
	PurchaseCR.ID,
    PurchaseCR.fhRev,
    PurchaseCR.amount,
    PurchaseCR.amountStr,
    PurchaseCR.currency,
    PurchaseCRSIIL.fhPg, 
    PurchaseCRSIIL.daycredit,
    PurchaseCRSIIL.provName,
    PurchaseCRSIIL.prov,
    PurchaseCR.obser,
    PurchaseCR.recibe,
    PurchaseCR.recibeStr,
    PurchaseCR.fhFolio,
    PurchaseCR.suc,
    PurchaseCRSIIL.account,
    PurchaseCR.status
FROM PurchaseCR,PurchaseCRSIIL
WHERE PurchaseCR.ID = PurchaseCRSIIL.ID;


USE `dbssiil`;

-- -----------------------------------------------------
-- View `dbssiil`.`PurchaseCRFD_Resolved`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dbssiil`.`PurchaseCRFD_Resolved`;
USE `dbssiil`;
CREATE  OR REPLACE VIEW `PurchaseCRFD_Resolved` AS
SELECT 
	PurchaseCRFD.ID, 
	PurchaseCRFD.fID, 
	PurchaseCRFD.folio , 
	PurchaseCRFD.serie, 
	PurchaseCRF.monto, 
	PurchaseCRF.montoStr, 
	PurchaseCRFD.PO, 
	PurchaseCRFD.SA 
FROM PurchaseCRF JOIN PurchaseCRFD
ON PurchaseCRF.fID = PurchaseCRFD.fID and PurchaseCRF.folio = PurchaseCRFD.folio;


USE `dbssiil`;

-- -----------------------------------------------------
-- View `dbssiil`.`PurchaseCRFull_Resolved`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dbssiil`.`PurchaseCRFull_Resolved`;
USE `dbssiil`;
CREATE  OR REPLACE VIEW `PurchaseCRFull_Resolved` AS
SELECT  
	PurchaseCR_Resolved.ID,
    PurchaseCR_Resolved.fhRev,
    PurchaseCR_Resolved.amount,
    PurchaseCR_Resolved.amountStr,
    PurchaseCR_Resolved.currency,
    PurchaseCR_Resolved.fhPg, 
    PurchaseCR_Resolved.daycredit,
    PurchaseCR_Resolved.provName,
    PurchaseCR_Resolved.prov,
    PurchaseCR_Resolved.obser,
    PurchaseCR_Resolved.recibe,
    PurchaseCR_Resolved.recibeStr,
    PurchaseCR_Resolved.fhFolio,
    PurchaseCR_Resolved.suc,
    PurchaseCR_Resolved.account,
    PurchaseCR_Resolved.status,
	PurchaseCRFD_Resolved.fID, 
	PurchaseCRFD_Resolved.folio , 
	PurchaseCRFD_Resolved.serie, 
	PurchaseCRFD_Resolved.monto, 
	PurchaseCRFD_Resolved.montoStr, 
	PurchaseCRFD_Resolved.PO, 
	PurchaseCRFD_Resolved.SA 
FROM PurchaseCR_Resolved JOIN PurchaseCRFD_Resolved
ON PurchaseCR_Resolved.ID = PurchaseCRFD_Resolved.fID;


USE `dbssiil`;

-- -----------------------------------------------------
-- View `dbssiil`.`Scope_Resolved`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dbssiil`.`Scope_Resolved`;
USE `dbssiil`;
CREATE  OR REPLACE VIEW `Scope_Resolved` AS
SELECT 
Users.uID,
Users.alias,
Users.active as uActive,
Persons.pID,
Persons.email,
Persons.active as pActive,
Persons.department,
Scope.BD,
Persons.office,
Scope.module,
Scope.context,
Scope.scope,
Scope.subcat1
FROM Users,Persons,Scope 
WHERE Users.uID = Persons.pID and Users.alias = Scope.user;


USE `dbssiil`;

-- -----------------------------------------------------
-- View `dbssiil`.`PurchasesOrder_Resolved`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dbssiil`.`PurchasesOrder_Resolved`;
USE `dbssiil`;
CREATE  OR REPLACE VIEW `PurchasesOrder_Resolved` AS
SELECT 
	PurchasesOrder.id,
	PurchasesOperational.provider,
    PurchasesProvider.nameShort as provNameShort,
    PurchasesProvider.nameRazonSocial as provRazonSocial,
	ProcessOperational.state,
    ProcessStates.module as stateModule,
    ProcessStates.code as stateCode,
    ProcessStates.name as stateName,
    ProcessStates.ordinal as stateOrdinal,
	ProcessOperational.operator,
    Persons.nameN1 as opeNameN1,
    Persons.nameAP as opeNameAP,
	ProcessOperational.fhFolio,
    UNIX_TIMESTAMP(fhFolio) as uxfhFolio,
	ProcessOperational.strFolio,
	ProcessOperational.serie,
    ProcessOperational.folio,
	ProcessOperational.type,
	ProcessOperational.flag
FROM PurchasesOrder,PurchasesOperational,ProcessOperational,Persons,PurchasesProvider,ProcessStates
WHERE 
	PurchasesOrder.id = PurchasesOperational.id AND 
    PurchasesOperational.id = ProcessOperational.id AND 
    Persons.pID = ProcessOperational.operator AND 
    PurchasesOperational.provider = PurchasesProvider.id AND 
    ProcessStates.id = ProcessOperational.state;


USE `dbssiil`;

-- -----------------------------------------------------
-- View `dbssiil`.`Hequis_Resolved`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dbssiil`.`Hequis_Resolved`;
USE `dbssiil`;
CREATE  OR REPLACE VIEW `Hequis_Resolved` AS
SELECT 
	StockItem.id,StockItem.number,StockItem.make,StockItem.model,StockFlow.serie,StockFlow.idItem as flowID 
FROM Titem, StockItem, StockFlow 
WHERE StockItem.id = Titem.id AND StockItem.id = StockFlow.idItem;


USE `dbssiil`;

-- -----------------------------------------------------
-- View `dbssiil`.`ServiceOrderResumen_Resolved`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dbssiil`.`ServiceOrderResumen_Resolved`;
USE `dbssiil`;
CREATE  OR REPLACE VIEW `ServiceOrderResumen_Resolved` AS
SELECT 
	ServiceOrderResumen.id,
    ServicesOrder.sa,
    ServicesOrder.`type`,
    ServicesOrder.horometro,
    ServicesOrder.description,
    ServicesOrder.fhService,
    ServicesOrder.quoteService,
    ServicesOrder.itemFlow,
    Companies.`number` as compNumber,
    Companies.`name` as compName,
    ServiceOrderResumen.module,
    Titem_Resolved.number as titemNumber,
    Titem_Resolved.make as titemMake,
    Titem_Resolved.model as titemModel,
    Titem_Resolved.serie as titemSerie
FROM
	ServicesOrder,ServiceOrderResumen,Titem_Resolved,Companies
WHERE
	ServicesOrder.id = ServiceOrderResumen.lastServ AND ServicesOrder.itemFlow = Titem_Resolved.idFlow AND ServiceOrderResumen.flowItem = Titem_Resolved.idFlow AND ServicesOrder.company = Companies.id;


USE `dbssiil`;

-- -----------------------------------------------------
-- View `dbssiil`.`ServicesTrabajo_Resolved`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dbssiil`.`ServicesTrabajo_Resolved`;
USE `dbssiil`;
CREATE  OR REPLACE VIEW `ServicesTrabajo_Resolved` AS
SELECT 
	ServicesTrabajo.id,
    ServicesTrabajo.state,
    ServicesTrabajo.sheet,
    ProcessOperational.folio,
    ProcessOperational.serie,
    ServicesTrabajo.company,
    Companies.number as compNumber,
    Companies.`name` as compName,
    ServicesTrabajo.mechanic,
    Persons.nameN1,
    Persons.nameAP,
    ServicesTrabajo.fhToDo,
    ServicesTrabajo.brief 
FROM 
	ServicesTrabajo,
    SalesRemision,
    ProcessOperational,
    Companies,
    Persons 
WHERE 
	SalesRemision.id = sa AND ProcessOperational.id = sa AND Companies.id = ServicesTrabajo.company AND ServicesTrabajo.mechanic = Persons.pID;


USE `dbssiil`;

-- -----------------------------------------------------
-- View `dbssiil`.`Battery_Resolved`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dbssiil`.`Battery_Resolved`;
USE `dbssiil`;
CREATE  OR REPLACE VIEW `Battery_Resolved` AS
SELECT
    'bc.tj' as BD,
    Titem.id as idT,
    StockFlow.id as idFlow,
	StockItem.`number`,
	StockItem.description,
    StockItem.make as Marca,
    StockItem.make,
    StockItem.model as Modelo,
    StockItem.model,
    StockFlow.activeSerie,
    StockFlow.serie
FROM
	StockItem,Titem,StockFlow,Battery
WHERE
	StockItem.id = Titem.id AND StockFlow.idItem = StockItem.id AND Titem.id = Battery.id;


USE `dbssiil`;

-- -----------------------------------------------------
-- View `dbssiil`.`Charger_Resolved`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dbssiil`.`Charger_Resolved`;
USE `dbssiil`;
CREATE  OR REPLACE VIEW `Charger_Resolved` AS
SELECT
    'bc.tj' as BD,
    Titem.id as idT,
    StockFlow.id as idFlow,
	StockItem.`number`,
	StockItem.description,
    StockItem.make as Marca,
    StockItem.make,
    StockItem.model as Modelo,
    StockItem.model,
    StockFlow.activeSerie,
    StockFlow.serie
FROM
	StockItem,Titem,StockFlow,Charger
WHERE
	StockItem.id = Titem.id AND StockFlow.idItem = StockItem.id AND Titem.id = Charger.id;


USE `dbssiil`;

-- -----------------------------------------------------
-- View `dbssiil`.`Minas_Resolved`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dbssiil`.`Minas_Resolved`;
USE `dbssiil`;
CREATE  OR REPLACE VIEW `Minas_Resolved` AS
SELECT
    'bc.tj' as BD,
    Titem.id as idT,
    StockFlow.id as idFlow,
	StockItem.`number`,
	StockItem.description,
    StockItem.make as Marca,
    StockItem.make,
    StockItem.model as Modelo,
    StockItem.model,
    StockFlow.activeSerie,
    StockFlow.serie
FROM
	StockItem,Titem,StockFlow,Minas
WHERE
	StockItem.id = Titem.id AND StockFlow.idItem = StockItem.id AND Titem.id = Minas.id;


USE `dbssiil`;

-- -----------------------------------------------------
-- View `dbssiil`.`ServicesOrder_Resolved`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dbssiil`.`ServicesOrder_Resolved`;
USE `dbssiil`;
CREATE  OR REPLACE VIEW `ServicesOrder_Resolved` AS
SELECT 
	ServicesOrder.id,
    ServicesOrder.sa,
    ServicesOrder.`type`,
    ServicesOrder.horometro,
    ServicesOrder.description,
    ServicesOrder.fhService,
    ServicesOrder.quoteService,
    ServicesOrder.itemFlow,
    Companies.`number` as compNumber,
    Companies.`name` as compName,
    Titem_Resolved.number as titemNumber,
    Titem_Resolved.make as titemMake,
    Titem_Resolved.model as titemModel,
    Titem_Resolved.serie as titemSerie
FROM
	ServicesOrder,Titem_Resolved,Companies
WHERE
	ServicesOrder.itemFlow = Titem_Resolved.idFlow AND ServicesOrder.itemFlow = Titem_Resolved.idFlow AND ServicesOrder.company = Companies.id;


USE `dbssiil`;

-- -----------------------------------------------------
-- View `dbssiil`.`SalesRemision_Resolved`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dbssiil`.`SalesRemision_Resolved`;
USE `dbssiil`;
CREATE  OR REPLACE VIEW `SalesRemision_Resolved` AS
SELECT 
	ProcessOperational.id,
	ProcessOperational.state,
    ProcessOperational.operator,
    ProcessOperational.fhFolio,
    ProcessOperational.strFolio,
    ProcessOperational.serie,
    ProcessOperational.folio,
    ProcessOperational.`type`, 
    ProcessOperational.office,
    ProcessOperational.fhInit,
    SalesOperational.company 
FROM ProcessOperational,SalesOperational,SalesRemision
WHERE ProcessOperational.id = SalesOperational.id AND SalesOperational.id = SalesRemision.id;


USE `dbssiil`;

-- -----------------------------------------------------
-- View `dbssiil`.`BobedaBusiness_Resolved`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dbssiil`.`BobedaBusiness_Resolved`;
USE `dbssiil`;
CREATE  OR REPLACE VIEW `BobedaBusiness_Resolved` AS
SELECT
	Bobeda.id as inBobedaID,
	BobedaBusiness.folio as inBobedaFolio,
    Bobeda.fhFolio,
	Bobeda.nombre,
	Bobeda.brief,
	"" as inTableID,
	"" as inTableFolio,
    Companies.id,
    Companies.`number` as compNumber    
FROM Bobeda,BobedaBusiness,Companies
WHERE Bobeda.id = BobedaBusiness.bobeda AND BobedaBusiness.company = Companies.id;


USE `dbssiil`;

-- -----------------------------------------------------
-- View `dbssiil`.`BobedaOrcom_Resolved`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dbssiil`.`BobedaOrcom_Resolved`;
USE `dbssiil`;
CREATE  OR REPLACE VIEW `BobedaOrcom_Resolved` AS
SELECT 
	Bobeda.id as inBobedaID,
	BobedaBusiness.folio as inBobedaFolio,
    Bobeda.fhFolio,
	Bobeda.nombre,
	Bobeda.brief,
	Orcom.id as inTableID,
	Orcom.folio as inTableFolio,
	Orcom.compNumber as orcomNumber,
    BobedaBusiness.company,
    Companies.`number` as compNumber
FROM 
	Bobeda,BobedaBusiness,Orcom,Companies
WHERE 
	Bobeda.id = BobedaBusiness.bobeda AND BobedaBusiness.bobeda = Orcom.poFile AND Companies.id = BobedaBusiness.company;


USE `dbssiil`;

-- -----------------------------------------------------
-- View `dbssiil`.`BobedaResumov_Resolved`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dbssiil`.`BobedaResumov_Resolved`;
USE `dbssiil`;
CREATE  OR REPLACE VIEW `BobedaResumov_Resolved` AS
SELECT 
	Bobeda.id as inBobedaID,
	BobedaBusiness.folio as inBobedaFolio,
    Bobeda.fhFolio,
	Bobeda.nombre,
	Bobeda.brief,
	Resumov.id as inTableID,
	Resumov.titemNumber as inTableFolio,
    BobedaBusiness.company,
    Companies.`number` as compNumber
FROM 
	Bobeda,BobedaBusiness,Resumov,Companies
WHERE 
	Bobeda.id = BobedaBusiness.bobeda AND BobedaBusiness.bobeda = Resumov.poFile AND Companies.id = BobedaBusiness.company;


USE `dbssiil`;

-- -----------------------------------------------------
-- View `dbssiil`.`Item_Resolved`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dbssiil`.`Item_Resolved`;
USE `dbssiil`;
CREATE  OR REPLACE VIEW `Item_Resolved` AS
SELECT
    'bc.tj' as BD,
    StockFlow.id as idFlow,
	StockItem.number,
	StockItem.description,
    StockItem.make as Marca,
    StockItem.make,
    StockItem.model as Modelo,
    StockItem.model,
    StockFlow.activeSerie,
    StockFlow.serie,
	StockItem.description as `name`,
    StockFlow.`estado`
FROM
	StockItem,StockFlow
WHERE
	StockItem.id = StockFlow.idItem;


USE `dbssiil`;

-- -----------------------------------------------------
-- View `dbssiil`.`SalesInvoice_Resolved`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dbssiil`.`SalesInvoice_Resolved`;
USE `dbssiil`;
CREATE  OR REPLACE VIEW `SalesInvoice_Resolved` AS
SELECT ProcessOperational.id,ProcessOperational.serie,ProcessOperational.folio,ProcessOperational.state
FROM ProcessOperational,SalesOperational,SalesInvoice
WHERE ProcessOperational.id = SalesOperational.id AND SalesOperational.id = SalesInvoice.id;


USE `dbssiil`;

-- -----------------------------------------------------
-- View `dbssiil`.`Orcom_Resolved2`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dbssiil`.`Orcom_Resolved2`;
USE `dbssiil`;
CREATE  OR REPLACE VIEW `Orcom_Resolved2` AS
SELECT 
	Orcom.BD,
    Orcom.estado,
    Orcom.serie,
    Orcom.folio,
    Orcom.compBD,
    Orcom.compNumber,
    Companies.name as compName,
    Orcom.fhAutho,
    Orcom.fhETA,
    Orcom.fhArribo,
    Orcom.fhSurtido,
    Orcom.fhFin,
    Orcom.ownerName,
    Orcom.ownerPerson,
    Orcom.fhFolio,
    Orcom.suc,
    Orcom.sa,
    Orcom.fhEdit,
    Orcom.creator,
    Orcom.ID,
    Orcom.fhETAfl,
    Orcom.department
FROM Orcom 
JOIN Companies ON Orcom.compNumber = Companies.number;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
