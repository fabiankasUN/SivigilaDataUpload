drop table i exist DATOS
drop table  if exist ESTACIONES

PRAGMA foreign_keys = 1;

CREATE TABLE IF NOT EXISTS DATOS(
			    id int not NULL AUTO_INCREMENT, 
	                    anio int not NULL, 
	                    estadoinfo TEXT, 
	                    enero FLOAT, 
	                    febrero FLOAT,
	                    marzo FLOAT, 
	                    abril FLOAT, 
	                    mayo FLOAT, 
	                    junio FLOAT,  
	                    julio FLOAT, 
	                    agosto FLOAT, 
	                    septiembre FLOAT,
	                    octubre FLOAT, 
	                    noviembre FLOAT, 
	                    diciembre FLOAT,  
	                    estacion TEXT,
	                    tipo TEXT, 
	                    PRIMARY KEY ( id )
); 


CREATE TABLE IF NOT EXISTS ESTACIONES ( 
		       codigo VARCHAR(255) NOT NULL, 
	               nombre VARCHAR(255) NOT NULL, 
	               latitud DOUBLE NOT NULL, 
	               longitud DOUBLE NOT NULL, 
	              PRIMARY KEY ( codigo )
);

