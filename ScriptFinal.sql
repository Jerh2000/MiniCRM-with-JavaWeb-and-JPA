/*
SQLyog Community v12.2.5 (32 bit)
MySQL - 5.5.19 : Database - empresatec
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`empresatec` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `empresatec`;

/*Table structure for table `administrador` */

DROP TABLE IF EXISTS `administrador`;

CREATE TABLE `administrador` (
  `id` bigint(20) NOT NULL,
  `nombre` varchar(30) DEFAULT NULL,
  `apellido` varchar(30) DEFAULT NULL,
  `identificacion` varchar(30) DEFAULT NULL,
  `fechanacimiento` date DEFAULT NULL,
  `direccion` varchar(30) DEFAULT NULL,
  `telefono` varchar(15) DEFAULT NULL,
  `sexo` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_usuario_administrador` FOREIGN KEY (`id`) REFERENCES `usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `administrador` */

insert  into `administrador`(`id`,`nombre`,`apellido`,`identificacion`,`fechanacimiento`,`direccion`,`telefono`,`sexo`) values 
(12,'Jairo','Rojas','1010120607','2000-06-13','Cartagena - Bolivar','3654145566','Masculino');

/*Table structure for table `cliente` */

DROP TABLE IF EXISTS `cliente`;

CREATE TABLE `cliente` (
  `apellido` varchar(255) DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `identificacion` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `telefono` varchar(255) DEFAULT NULL,
  `valoracion` int(11) NOT NULL,
  `id` bigint(20) NOT NULL,
  `fechanacimiento` date DEFAULT NULL,
  `sexo` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_8n9i3v2npvqeuoik9n5j4ikaa` FOREIGN KEY (`id`) REFERENCES `usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `cliente` */

insert  into `cliente`(`apellido`,`direccion`,`identificacion`,`nombre`,`telefono`,`valoracion`,`id`,`fechanacimiento`,`sexo`) values 
('Rodriguez','El pozon','516165','Fulano','496699',0,2,'1992-07-22','Masculino'),
('Rodriguez','Las palmas','485666','Anonnymous','7766',0,3,NULL,NULL),
('Rojas Herrera','El pozon','1010120607','Jairo','300494865',0,4,NULL,NULL),
('Meza','Turbaco','123654987','Juan','123456789',0,11,NULL,NULL),
('Giraldo Herrera','El Pozon','236412665','Jesus','2143365',0,21,NULL,NULL),
('Sexo','Cartagena','123789456','Camilo','49666',0,26,'2020-11-03','Masculino');

/*Table structure for table `comentario` */

DROP TABLE IF EXISTS `comentario`;

CREATE TABLE `comentario` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(2000) DEFAULT NULL,
  `rating` int(10) DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  `cliente_id` bigint(20) DEFAULT NULL,
  `producto_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_comentario_producto` (`producto_id`),
  KEY `FK_comentario_cliente` (`cliente_id`),
  CONSTRAINT `FK_comentario_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `cliente` (`id`),
  CONSTRAINT `FK_comentario_producto` FOREIGN KEY (`producto_id`) REFERENCES `producto` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;

/*Data for the table `comentario` */

insert  into `comentario`(`id`,`descripcion`,`rating`,`fecha`,`cliente_id`,`producto_id`) values 
(1,'Comentario de ejemplo',5,'2020-11-13',2,3),
(2,'wbfbdfubsdfsb dsbfbsdfi ssdf ',4,'2020-11-14',11,3),
(4,'Me ha parecido un muy buen producto, la compre hace dos años y toma unas fotografias increibles. De las mejores camaras que he comprado en mi vida, la recomiendo mucho, es muy profesional y facil de usar.',4,'2020-11-13',2,4),
(5,'Este es un comentario de ejemplo\r\nPero quisiera poder tener ese telefono, es que el mio ya esta algo viejo y nececito uno nuevo crack, feliz noche.',3,'2020-11-13',11,7),
(6,'ggfdgegggggggggggvtvgrvrtevg  grvgrevg',3,'2020-11-13',11,3),
(7,'Este es otro comentario de pueba que no tiene calificacion pero quiero probar a ver si sirve, y respecto al telefono pues esta chido no lo tengo pero se ve genial y si me lo regalan yo lo acepto no se que mas escribir asi por hoy este comentario que no tiene nada interesante se acabo\r\n',5,'2020-11-14',2,3),
(8,'Este es una valoracion de 5',5,'2020-11-14',2,3),
(9,'sdfghjsdfghjdfghjk\r\ngdfgdfgfdg\r\nfdgdgfdg\r\nfdgfdgdfggggggggggggggggggggggggggggggggggg\r\ndfgggggggggggggggggggggggggggggg\r\nddddddddddddddddddddddddddddddddddd',5,'2020-11-14',2,3),
(10,'Muy buen telefono, compraria 1000 mas si es posible. Alguien me compra otro?',5,'2020-11-15',2,10),
(11,'Probando el producto, es bueno le doy un 4',4,'2020-11-15',11,10),
(12,'No me gusta, pesimo servicio',1,'2020-11-15',3,10),
(13,'Un comentario de prueba',5,'2020-11-16',2,5),
(14,'No le crean al de arriba, salen bien malas esas camaras xD',1,'2020-11-25',4,4),
(15,'Esto es un comentario',4,'2020-11-26',2,2),
(16,'vomenti\'rf',4,'2020-11-26',2,1);

/*Table structure for table `cotizacion` */

DROP TABLE IF EXISTS `cotizacion`;

CREATE TABLE `cotizacion` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `estado` varchar(255) DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  `fechaAprobacion` date DEFAULT NULL,
  `codigo` varchar(11) NOT NULL,
  `cliente_id` bigint(20) DEFAULT NULL,
  `empleado_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_sorg5bgy9yeajqenp3rhgjcox` (`cliente_id`),
  KEY `FK_i9jpgcnhkhe7o8fmb7fnjkph8` (`empleado_id`),
  CONSTRAINT `FK_i9jpgcnhkhe7o8fmb7fnjkph8` FOREIGN KEY (`empleado_id`) REFERENCES `empleado` (`id`),
  CONSTRAINT `FK_sorg5bgy9yeajqenp3rhgjcox` FOREIGN KEY (`cliente_id`) REFERENCES `cliente` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=latin1;

/*Data for the table `cotizacion` */

insert  into `cotizacion`(`id`,`estado`,`fecha`,`fechaAprobacion`,`codigo`,`cliente_id`,`empleado_id`) values 
(26,'Solicitado','2020-11-26',NULL,'123',2,NULL),
(27,'Solicitado','2020-11-26',NULL,'123',2,NULL),
(28,'Solicitado','2020-11-26',NULL,'123',2,15),
(29,'Solicitado','2020-11-26',NULL,'123',2,1);

/*Table structure for table `detallecotizacion` */

DROP TABLE IF EXISTS `detallecotizacion`;

CREATE TABLE `detallecotizacion` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cantidad` int(11) NOT NULL,
  `propuesta` double NOT NULL,
  `cotizacion_id` bigint(20) DEFAULT NULL,
  `producto_id` bigint(20) DEFAULT NULL,
  `descripcion` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_8u49ag7e05rmw6poww47xjpjp` (`cotizacion_id`),
  KEY `FK_o7cm6fa7ge9l7y2yuqufc86ph` (`producto_id`),
  CONSTRAINT `FK_8u49ag7e05rmw6poww47xjpjp` FOREIGN KEY (`cotizacion_id`) REFERENCES `cotizacion` (`id`),
  CONSTRAINT `FK_o7cm6fa7ge9l7y2yuqufc86ph` FOREIGN KEY (`producto_id`) REFERENCES `producto` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=latin1;

/*Data for the table `detallecotizacion` */

insert  into `detallecotizacion`(`id`,`cantidad`,`propuesta`,`cotizacion_id`,`producto_id`,`descripcion`) values 
(65,1,0,26,3,''),
(66,1,0,27,2,'Ejemplo'),
(67,1,0,27,3,'Ejemplo'),
(68,1,0,27,4,'Ejemplo'),
(69,1,0,27,12,'ejemplo'),
(70,1,0,28,2,'Cotizando...'),
(71,1,0,29,11,'Otra cotizacion');

/*Table structure for table `detalleventa` */

DROP TABLE IF EXISTS `detalleventa`;

CREATE TABLE `detalleventa` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cantidad` int(11) DEFAULT NULL,
  `producto_id` bigint(20) DEFAULT NULL,
  `venta_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_producto_detalleventa` (`producto_id`),
  KEY `FK_venta_detalleventa` (`venta_id`),
  CONSTRAINT `FK_producto_detalleventa` FOREIGN KEY (`producto_id`) REFERENCES `producto` (`id`),
  CONSTRAINT `FK_venta_detalleventa` FOREIGN KEY (`venta_id`) REFERENCES `venta` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;

/*Data for the table `detalleventa` */

insert  into `detalleventa`(`id`,`cantidad`,`producto_id`,`venta_id`) values 
(10,1,12,15),
(11,1,6,16),
(12,1,16,21),
(13,1,15,21),
(14,1,13,21),
(15,1,26,22),
(16,1,23,23),
(17,1,8,24),
(18,1,4,25),
(19,1,10,26),
(20,1,13,26);

/*Table structure for table `empleado` */

DROP TABLE IF EXISTS `empleado`;

CREATE TABLE `empleado` (
  `apellido` varchar(255) DEFAULT NULL,
  `identificacion` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `direccion` varchar(30) DEFAULT NULL,
  `telefono` varchar(15) DEFAULT NULL,
  `sexo` varchar(12) DEFAULT NULL,
  `fechanacimiento` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_k6r4un6gkl2qravlbbfu0rss8` FOREIGN KEY (`id`) REFERENCES `usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `empleado` */

insert  into `empleado`(`apellido`,`identificacion`,`nombre`,`id`,`direccion`,`telefono`,`sexo`,`fechanacimiento`) values 
('Rojas','1010120607','Jairo',1,'Cartagena - Bolivar','3004948658','Masculino','2000-10-23'),
('Meza','123456789','Juan',13,'Turbaco','123456789','Masculino','2020-11-18'),
('Suarez','456974566','Johan',14,'La union','4494565','Masculino','2020-11-18'),
('Vanegas','489669888','Emily',15,'Cartagena','24965466','Femenino','2020-11-18'),
('Florez','449664455','Candelaria',16,'Cartagena','496635','Femenino','2020-11-18');

/*Table structure for table `producto` */

DROP TABLE IF EXISTS `producto`;

CREATE TABLE `producto` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `categoria` varchar(255) DEFAULT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `estado` varchar(255) DEFAULT NULL,
  `marca` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `precio` double NOT NULL,
  `stockm` int(11) NOT NULL,
  `valoracion` int(11) DEFAULT NULL,
  `promocion_id` bigint(20) DEFAULT NULL,
  `garantia` varchar(20) DEFAULT NULL,
  `color` varchar(20) DEFAULT NULL,
  `codigosn` varchar(30) DEFAULT NULL,
  `almacenamiento` varchar(12) DEFAULT NULL,
  `ram` varchar(12) DEFAULT NULL,
  `procesador` varchar(20) DEFAULT NULL,
  `sistemaoperativo` varchar(20) DEFAULT NULL,
  `referencia` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_promocion_producto` (`promocion_id`),
  CONSTRAINT `FK_promocion_producto` FOREIGN KEY (`promocion_id`) REFERENCES `promocion` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=latin1;

/*Data for the table `producto` */

insert  into `producto`(`id`,`categoria`,`descripcion`,`estado`,`marca`,`nombre`,`precio`,`stockm`,`valoracion`,`promocion_id`,`garantia`,`color`,`codigosn`,`almacenamiento`,`ram`,`procesador`,`sistemaoperativo`,`referencia`) values 
(1,'PCAIO','Computador todo en uno con teclado  mouse incluido','Vendido','HP','Computador todo en uno',1200000,2,4,3,'1 año','Negro','M1S2J6','1000','8GB','Intel','Windows','COPCHPNEIN108GWI'),
(2,'Teclado','Teclado Holografico Rojo','Activo','Genius','Teclado Holografico',320000,1,3,NULL,'11 meses','Negro','KL46DQ','NA','NA','NA','NA','TETEGENENANANANA'),
(3,'Celular','Celular Xiaomi Baratico','Activo','Xiaomi','Celular Xiaomi',800000,1,4,NULL,'2 años','Verde','69PG6F','32GB','4GB','Qualcomm','Android','CECEXIVEQU324GAN'),
(4,'Camara','Camara canon barata','Vendido','Canon','Camara Canon',250000,1,4,2,'13 meses','Negro','1LX3SP','NA','NA','NA','NA','CACACANENANANANA'),
(5,'Tablet','Ipad Mini Dorado modelo 2019','Vendido','Apple','Ipad Mine',1000000,1,4,NULL,'5 meses','Dorado','WWETR9','64GB','6GB','Exynos','iOS','IPTAAPDOEX646GIO'),
(6,'Auriculares','Auriculares Xiaomi Mi Redmi Airdots Bluetooth 5.0 Negro Original - XIAOMI','Vendido','Xiaomi','Auriculares Xiaomi',90000,1,4,NULL,'1.5 años','Negro','8SMN2N','NA','NA','NA','NA','AUAUXINENANANANA'),
(7,'Celular','Celular Samung Morado 64GB Procesador SnapDragon 4GB RAM Android 9.0 Camara Frontal 8MP Trasera 15MP','Activo','Samsung','Samsung Galaxy A30s',1100000,1,4,NULL,'6 meses','Morado','H4YJD6','32GB','4GB','Qualcomm','Android','SACESAMOQU324GAN'),
(8,'Laptop','Nueva Laptop Asus Core i7-8550U 15.6\"FHD 8GB, 1TB SSHD Color Negro y Dorado','Vendido','Asus','Laptop Asus',2699999,1,4,NULL,'9 meses','Negro','HF783F','1TB','8GB','Intel','Windows','LALAASNEIN1T8GWI'),
(9,'Laptop','Laptop HP Pavilion 14-dw0001la 2 en 1 Corei5 8GB RAM 256GB SSD+16GB Intel Optane\r\n14 Pulgadas FullHD','Activo','HP','Laptop HP Pavilon',3399999,1,4,NULL,'3 años','Blanco','BD6UE6','256GB','8GB','Intel','Windows','LALAHPBLIN258GWI'),
(10,'Celular','Celular con pantalla FulHD Procesador Kilin RAM 8GB y 128GB almacenamiento Android 9.0\r\nCámara: Cuádruple - 48MP+16MP +2MP+2MP Cámara Frontal: alta resolución (32MP)   ','Activo','Huawei','Celular Huawei Nova 5T',1799999,1,3,4,'1 siglo','Azul','G35D56','32GB','8GB','Kirin','Android','CECEHUAZKI328GAN'),
(11,'Celular','Telefono con doble Pantalla y 128 GB de Almacenamiento, multitarea y diversión por duplicado, pantalla de 6.4\" FHD+ , certificado de resistencia de materiales Military Standard e IP68 ','Activo','LG','Celular LG G8x',1999999,1,3,NULL,'2 meses','Negro','HFGTE5','12GB','6GB','Qualcomm','Android','CECELGNEQU126GAN'),
(12,'Televisor','Televisor LED 4k de 64 pulgadas 106cm con marco metalico \r\nSmartTV','Vendido','Samsung','Televisor LED 4K',1929000,1,0,NULL,'1 año','Negro','TV34SAM','2GB','NA','NA','NA','TETESANENA2GNANA'),
(13,'Consola','Play Station Version Con lector de CD Con un mando, adaptador de corriente, y cable HDMI','Activo','Sony','Play Station 5',2700000,1,0,NULL,'6 meses','Blanco','PS20XD','1TB','64GB','Intel','Linux','PLCOSOBLIN1T64LI'),
(14,'PCAIO','Computador todo en uno con teclado  mouse incluido','Vendido','HP','Computador todo en uno',1200000,1,0,NULL,'12 meses','Blanco','C45AIO','500','8GB','Intel','Windows','COPCHPBLIN508GWI'),
(15,'PCAIO','Computador todo en uno con teclado  mouse incluido','Activo','HP','Computador todo en uno',1200000,1,0,NULL,'12 meses','Blanco','C87AIO','500','8GB','Intel','Windows','COPCHPBLIN508GWI'),
(16,'Audifono','hbdvshbcub urbcnuieqbrmxui rheqbcuinebqcrui rubgceuibg urecbuicbr eycbguicbiur ucrebucb rewcbub egcwbgyecb rebwgu','Vendido','Sony','Audífono Sony MDR-Z1R',11672000,1,0,NULL,'2 meses','Negro','AUD458','NA','NA','NA','NA','AUAUSONENANANANA'),
(22,'PCAIO','PC todo incluido','Vendido','HP','Computador todo en uno',1200000,2,0,3,'12 meses','Negro','COAIO451','1000GB','8GB','Intel','Windows','COPCHPNEIN108GWI'),
(23,'PCAIO','PC Todo incluido mouse todo','Vendido','HP','Computador todo en uno',1200000,2,0,3,'12 meses','Negro','CAIO011','1000GB','8GB','Intel','Windows','COPCHPNEIN108GWI'),
(26,'PCAIO','Ultima prueba 12:45 AM','Vendido','HP','Computador todo en uno',1200000,2,0,3,'12 meses','Negro','COAIO411','1000GB','8GB','Intel','Windows','COPCHPNEIN108GWI');

/*Table structure for table `promocion` */

DROP TABLE IF EXISTS `promocion`;

CREATE TABLE `promocion` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fechaI` date DEFAULT NULL,
  `fechaF` date DEFAULT NULL,
  `descuento` double DEFAULT NULL,
  `administrador_id` bigint(20) DEFAULT NULL,
  `estado` varchar(10) DEFAULT NULL,
  `preciopromocion` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_administrador_promocion` (`administrador_id`),
  CONSTRAINT `FK_administrador_promocion` FOREIGN KEY (`administrador_id`) REFERENCES `administrador` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

/*Data for the table `promocion` */

insert  into `promocion`(`id`,`fechaI`,`fechaF`,`descuento`,`administrador_id`,`estado`,`preciopromocion`) values 
(1,'2020-11-19','2020-11-23',25,12,'Vigente',187500),
(2,'2020-11-26','2020-11-30',75,12,'Vigente',62500),
(3,'2020-11-26','2020-11-30',50,12,'Vigente',600000),
(4,'2020-11-27','2020-11-30',15,12,'Vigente',1529999.15);

/*Table structure for table `usuario` */

DROP TABLE IF EXISTS `usuario`;

CREATE TABLE `usuario` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `estado` varchar(255) DEFAULT NULL,
  `login` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `tipo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=latin1;

/*Data for the table `usuario` */

insert  into `usuario`(`id`,`email`,`estado`,`login`,`password`,`tipo`) values 
(1,'example@gmail.com','Activo','jairo','123','Empleado'),
(2,'example2@gmail.com','Activo','jr','789','Cliente'),
(3,'user@gmail.com','Activo','user','123','Cliente'),
(4,'jairo142014@hotmail.com','Activo','jerh','123','Cliente'),
(11,'juan@gmail.com','Activo','juan','123','Cliente'),
(12,'admin@gmail.com','Activo','admin','123','Administrador'),
(13,'juan@gmail.com','Activo','ejuan','123','Empleado'),
(14,'abc@gmail.com','Activo','abc','123','Empleado'),
(15,'xyz@gmail.com','Activo','xyz','123','Empleado'),
(16,'hola@gmail.com','Activo','hola','123','Empleado'),
(21,'jesus@gmail.com','Activo','jesus','4444','Cliente'),
(26,'camilo@gmail.com','Activo','camilo','123','Cliente');

/*Table structure for table `venta` */

DROP TABLE IF EXISTS `venta`;

CREATE TABLE `venta` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `codigo` varchar(100) DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  `estado` varchar(12) DEFAULT NULL,
  `cliente_id` bigint(20) DEFAULT NULL,
  `empleado_id` bigint(20) DEFAULT NULL,
  `totalventa` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_cliente_venta` (`cliente_id`),
  KEY `FK_empleado_venta` (`empleado_id`),
  CONSTRAINT `FK_cliente_venta` FOREIGN KEY (`cliente_id`) REFERENCES `cliente` (`id`),
  CONSTRAINT `FK_empleado_venta` FOREIGN KEY (`empleado_id`) REFERENCES `empleado` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=latin1;

/*Data for the table `venta` */

insert  into `venta`(`id`,`codigo`,`fecha`,`estado`,`cliente_id`,`empleado_id`,`totalventa`) values 
(15,'VEN0','2020-11-26','Por pagar',2,14,1929000),
(16,'VEN1','2020-11-26','Por pagar',2,1,90000),
(21,'VEN2','2020-11-27','Por pagar',2,1,10000),
(22,'VEN3','2020-11-27','Por pagar',2,13,1200000),
(23,'VEN4','2020-11-27','Por pagar',4,15,1200000),
(24,'VEN5','2020-11-27','Por pagar',2,1,2699999),
(25,'VEN6','2020-11-27','Por pagar',2,1,62500),
(26,'VEN7','2020-11-27','Por pagar',2,15,4229999.15);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
