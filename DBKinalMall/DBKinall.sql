Drop Database if exists DBKinalMall2017403;
Create Database DBKinalMall2017403;
use DBKinalMall2017403;

/* Base de Datos */
Create table Departamentos(
	codigoDepartamento int not null auto_increment,
    nombreDepartamento varchar(45) not null,
	primary key PK_codigoDepartamento(codigoDepartamento)
);

Create Table Cargos(
	codigoCargo int not null auto_increment,
    nombreCargo varchar(45) not null,
	primary key PK_codigoCargo(codigoCargo)
);

Create Table Horarios(
	codigoHorario int not null auto_increment,
    horarioEntrada  varchar(45) not null, 
    horarioSalida varchar(45) not null,
    lunes boolean,
    martes boolean,
    miercoles boolean,
    jueves boolean,
    viernes boolean,
	primary key PK_codigoHorario(codigoHorario)
);

Create Table Administracion(
	codigoAdministracion int not null auto_increment,
    direccion varchar(45) not null,
	telefono varchar(45) not null,
    primary key PK_codigoAdministracion(codigoAdministracion)
);

Create Table TipoCliente(
	codigoTipoCliente int not null auto_increment,
    descripcion varchar(45) not null,
    primary key PK_codigoTipoCliente(codigoTipoCliente)
);

Create Table Locales(
	codigoLocal int not null auto_increment,
    saldoFavor Double (11,2) default 0.0,
    saldoContra Double (11,2) default 0.0,
	mesesPendientes int default 0,
    disponibilidad boolean not null,
    valorLocal Double (11,2) not null,
    valorAdministracion Double(11,2) not null,
    primary key PK_codigoLocal(codigoLocal)
);


Create Table Empleados(
	codigoEmpleados int not null auto_increment,
    nombreEmpleado varchar(45) not null,
    apellidoEmpleado varchar(45) not null,
    correoElectronico varchar(45) not null,
	telefonoEmpleado varchar(8) not null,
	fechaContrato date not null,
    sueldo double (11,2) not null,
	codigoDepartamentos int not null, 
    codigoCargo int not null,
    codigoHorario int not null, /* Referencia a las siguientes entidades */
    codigoAdministracion int null null,
    primary key PK_codigoEmpleados(codigoEmpleados),
    constraint FK_Empleados_Departamentos foreign key (codigoDepartamentos)
		references Departamentos(codigoDepartamento),
    constraint FK_Empleados_Cargos foreign key (codigoCargo) references Cargos(codigoCargo),
    constraint FK_Empleados_Administracion foreign key (codigoAdministracion) references Administracion(codigoAdministracion),
    constraint FX_Empleados_Horarios foreign key (codigoHorario) references Horarios(codigoHorario)
);

Create Table Proveedores(
	codigoProveedores int not null auto_increment,
	NITProveedor varchar(45) not null, 
    servicioPrestado varchar(45) not null, 
    telefonoProveedor varchar(8) not null,
    direccionProveedor varchar(45) not null,
	saldoFavor Double (11,2) not null,
    saldoContra Double (11,2) not null,
	administracion_codigoAdministracion int not null, /**/
    primary key PK_codigoProveedores(codigoProveedores),
    constraint FK_Proveedores_Administracion foreign key (administracion_codigoAdministracion)/**/
		references Administracion(codigoAdministracion)
);

Create Table CuentasPorPagar(
	codigoCuentasPorPagar int  auto_increment, 
    numeroFactura varchar(45) not null,
    fechaLimitePago date not null,
	estadoPago varchar(45) not null,
    valorNetoPago double (11,2) not null, 
    codigoAdministracion int not null, 
    codigoProveedor int not null, 
	primary key PK_codigoCuentasPorPagar(codigoCuentasPorPagar),
    constraint FK_CuentasPorPagar_Administracion foreign key (codigoAdministracion) 
		references Administracion(codigoAdministracion),
    constraint FK_CuentasPorPagar_Proveedores foreign key (codigoProveedor ) references 
		Proveedores(codigoProveedores)
);



Create Table Clientes(
	codigoCliente int not null auto_increment,
    nombreCliente varchar(45) not null,
	apellidoCliente varchar(45) not null,
	telefonoCliente varchar(8) not null,
	direccionCliente varchar(60) not null,
	email varchar(45) not null,
	codigoLocal int not null,
    codigoAdministracion int not null,
    codigoTipoCliente int not null,
    primary key PK_codigoCliente(codigoCliente),
	constraint FK_Clientes_Locales foreign key (codigoLocal) 
		references Locales(codigoLocal),
	constraint FK_Clientes_TipoClientes foreign key (codigoTipoCliente)
		references TipoCliente(codigoTipoCliente),
	constraint FK_Clientes_Administracion foreign key (codigoAdministracion)
		references Administracion(codigoAdministracion)
);

Create Table CuentasPorCobrar(
	codigoCuentasPorCobrar int not null auto_increment,
    codigoFactura varchar(45) not null,
	anio varchar(4) not null,
	mes int(2) not null,
	valorNetoPago double (10,2) not null,
    estadoPago varchar(45) not null,
    codigoAdministracion int not null,
	codigoCliente int not null,
    codigoLocal int not null,
    primary key PK_codigoCuentasPorCobrar(codigoCuentasPorCobrar),
		constraint FK_CuentasPorCobrar_Administracion foreign key (codigoAdministracion) 
		references Administracion(codigoAdministracion),
	constraint FK_CuentasPorCobrar_Clientes foreign key (codigoCliente)
		references Clientes(codigoCliente),
	constraint FK_CuentasPorCobrar_Locales foreign key (codigoLocal)
		references Locales(codigoLocal)
);


Create Table Usuario(
	codigoUsuario int not null auto_increment,
    nombreUsuario varchar(100) not null,
    apellidoUsuario varchar(100) not null,
    usuarioLogin varchar(100) not null,
    contrasena varchar(50) not null,
    primary key PK_codigoUsuario (codigoUsuario)
); 
 
Create Table Login(
	usuarioMaster varchar(50) not null,
	passwordLogin varchar(50) not null,
    primary key PK_usuarioMaster (usuarioMaster)
);

/*Procedimientos Almacenados*/

/* ----------------------------------Departamentos-----------------------------*/
Delimiter $$
	Create procedure sp_AgregarDepartamentos( in nombreDepartamento varchar(45))
	begin 
		insert into Departamentos(nombreDepartamento)
			values(nombreDepartamento);
	End $$
Delimiter ;

/*-------------------------------------Cargos-----------------------------------*/
Delimiter $$
	Create procedure sp_AgregarCargos(in nombreCargo varchar(45))
    begin
		insert into Cargos(nombreCargo)
			values(nombreCargo);
	End$$
Delimiter ;

/*------------------------------------Horarios----------------------------------*/
Delimiter $$ 
	Create procedure sp_AgregarHorarios(in horarioEntrada  varchar(45), in horarioSalida varchar(45),
    in lunes boolean, in martes boolean, in miercoles boolean, in jueves boolean, in viernes boolean)
	begin
		insert into Horarios( horarioEntrada,horarioSalida,lunes,martes,miercoles,jueves,viernes)
			values(horarioEntrada,horarioSalida,lunes,martes,miercoles,jueves,viernes);
	end$$
delimiter ; 

/*-----------------------------------------Administracion--------------------------------*/
Delimiter $$
	Create procedure sp_AgregarAdministracion(in direccion varchar(45), in telefono varchar(45))
	begin
		insert into Administracion(direccion,telefono)
        values(direccion,telefono);
	end$$
Delimiter ; 

/*----------------------------------Tipo CLiente-----------------------------------*/
Delimiter $$
	Create procedure sp_AgregarTipoCliente(in descripcion varchar(45))
    begin
		insert into TipoCliente(descripcion)
        values(descripcion);
	end$$
Delimiter ; 

/*--------------------------------------Locales----------------------------------------*/
Delimiter $$ 
	Create procedure sp_AgregarLocales(in saldoFavor Double (11,2), in saldoContra Double (11,2), 
			in mesesPendientes int,in disponibilidad boolean, in valorLocal Double (11,2), 
			in valorAdministracion Double(11,2))
	begin
			insert into Locales(saldoFavor,saldoContra,mesesPendientes,disponibilidad,valorLocal,valorAdministracion)
			values(saldoFavor,saldoContra,mesesPendientes,disponibilidad,valorLocal,valorAdministracion);
	end$$
Delimiter ; 

/*--------------------------------------------------------------------Separador----------------------------------------------------*/

/*----------------------------------Empleados---------------------------------------- */
Delimiter $$
	Create procedure sp_AgregarEmpleados(in nombreEmpleado varchar(45), in apellidoEmpleado varchar(45),
			in correoElectronico varchar(45), in telefonoEmpleado varchar(8), in fechaContrato date, 
			in sueldo double (11,2), in codigoDepartamentos int, in  codigoCargo int, 
				in codigoHorario int, in codigoAdministracion int)
	begin 
			insert into Empleados(nombreEmpleado,apellidoEmpleado,correoElectronico,telefonoEmpleado,fechaContrato,sueldo,
			codigoDepartamentos,codigoCargo,codigoHorario,codigoAdministracion )
            
            Values(nombreEmpleado,apellidoEmpleado,correoElectronico,telefonoEmpleado,fechaContrato,sueldo,
			codigoDepartamentos,codigoCargo,codigoHorario,codigoAdministracion);
	end $$
Delimiter ; 

/*--------------------------------Proovedores------------------------------------------*/
Delimiter $$
	Create procedure sp_AgregarProveedores(in NITProveedor varchar(45),in servicioPrestado varchar(45),telefonoProveedor varchar(8),
    in direccionProveedor varchar(45), in saldoFavor Double (11,2), in saldoContra Double (11,2), in administracion_codigoAdministracion int)
	begin 
    insert into Proveedores(NITProveedor,servicioPrestado,telefonoProveedor,direccionProveedor,saldoFavor,saldoContra,administracion_codigoAdministracion)
    values(NITProveedor,servicioPrestado,telefonoProveedor,direccionProveedor,saldoFavor,saldoContra,administracion_codigoAdministracion);
	end$$
Delimiter ;


/*-------------------------------CuentasPorAgregar-----------------------------------------*/
Delimiter $$
	Create Procedure sp_AgregarCuentasPorPagar(in numeroFactura varchar(45),in fechaLimitePago date,in estadoPago varchar(45) ,in valorNetoPago double (11,2),
    in codigoAdministracion int,in codigoProveedor int)
	Begin 
		insert into CuentasPorPagar(numeroFactura,fechaLimitePago,estadoPago,valorNetoPago,codigoAdministracion,codigoProveedor)
        values(numeroFactura,fechaLimitePago,estadoPago,valorNetoPago,codigoAdministracion,codigoProveedor);
	end$$
Delimiter ; 

/*-----------------------------------AgregarClientes----------------------------------------*/
Delimiter $$ 
	Create procedure sp_AgregarClientes(in nombreCliente varchar(45),in apellidoCliente varchar(45) , in telefonoCliente varchar(8), in direccionCliente varchar(60),
    in email varchar(45), in codigoLocal int,in codigoAdministracion int,in codigoTipoCliente int)
	begin
		insert into Clientes (nombreCliente,apellidoCliente,telefonoCliente,direccionCliente,email,codigoLocal,
        codigoAdministracion,codigoTipoCliente)
        values(nombreCliente,apellidoCliente,telefonoCliente,direccionCliente,email,codigoLocal,
        codigoAdministracion,codigoTipoCliente);
	end$$
Delimiter ; 


/*-----------------------------------CuentasPorCobrar---------------------------------------------*/
Delimiter $$ 
	Create procedure sp_AgregarCuentasPorCobrar(in codigoFactura int,in anio varchar(4),in mes int(2),in valorNetoPago double (10,2),
    in estadoPago varchar(45),in codigoAdministracion int,in codigoCliente int,in codigoLocal int)
	begin
		insert into CuentasPorCobrar(codigoFactura,anio,mes,valorNetoPago,estadoPago,codigoAdministracion,codigoCliente,codigoLocal)
        values(codigoFactura,anio,mes,valorNetoPago,estadoPago,codigoAdministracion,codigoCliente,codigoLocal);
	end$$
Delimiter ; 

/*---------------------------------Login--------------------------------------------------------------*/
Delimiter $$ 
	Create procedure sp_AgregarUsuario( in nombreUsuario varchar(100), in apellidoUsuario varchar(100), in usuarioLogin varchar(100), in contrasena varchar(50))
	Begin
		insert into Usuario(nombreUsuario,apellidoUsuario,usuarioLogin,contrasena)
        values(nombreUsuario,apellidoUsuario,usuarioLogin,contrasena);
	End$$ 
Delimiter ;

/*--------------------------------------------Ingreso de Datos---------------------------------------------------*/
/*Informacion*/
/*---------------------Departamentos--------------*/
call sp_AgregarDepartamentos('Jutiapa');
call sp_AgregarDepartamentos('Chimaltenango');
call sp_AgregarDepartamentos('Jalapa');
call sp_AgregarDepartamentos('Alta Verapaz');
call sp_AgregarDepartamentos('El Progreso');

/*----------------------Cargos-------------------*/
call sp_AgregarCargos('Ejecutivo de Ventas');
call sp_AgregarCargos('Investigador de Mercado');
call sp_AgregarCargos('Vendedor');
call sp_AgregarCargos('Asistente de Impuestos');
call sp_AgregarCargos('Asistente de Ventas');

/*------------------Horarios-----------------------*/
call sp_AgregarHorarios('7:00 am','5:00 pm',true,true,true,false,true);
call sp_AgregarHorarios('8:00 am','6:00 pm',false,true,true,true,false);
call sp_AgregarHorarios('7:00  am','5:00 pm',true,true,true,true,true);
call sp_AgregarHorarios('8:00  am','6:00 pm',false,false,true,true,true);
call sp_AgregarHorarios('7:00  am','5:00 pm',true,true,false,true,true);

/*---------------------Administracion----------------*/
call sp_AgregarAdministracion('Zona 12 Monte Maria Iii','53814608');
call sp_AgregarAdministracion('Zona 7 ciudad de Guatemala','52747966');
call sp_AgregarAdministracion('Zona 1 Residencia dos','43401109');
call sp_AgregarAdministracion('Antigua Guatemala','42421109');
call sp_AgregarAdministracion('zona 6 de villa nueva','55554141');
call sp_AgregarAdministracion('zona 1 ciudad de guatemala','12345678');
call sp_AgregarAdministracion('zona 10 ','45451159');
call sp_AgregarAdministracion('Mixco','14140033');
call sp_AgregarAdministracion('zona 2','78781109');
call sp_AgregarAdministracion('zona 7 landivar','87456123');
call sp_AgregarAdministracion('Zona 4 calle del reloj','12398745');


/*-----------------------TipoCliente------------------*/
call sp_AgregarTipoCliente('Cliente de compra frecuente');
call sp_AgregarTipoCliente('Cliente de compra habitual:');
call sp_AgregarTipoCliente('Cliente de compra ocasional');
call sp_AgregarTipoCliente('Clientes potenciales');
call sp_AgregarTipoCliente('Cliente activo');
call sp_AgregarTipoCliente('Cliente Practico');

/*-------------------------Locales---------------------*/
-- Andy Daniel Cuyuch Chitay 2017403 --
call sp_AgregarLocales('1000.00','3000.00','2',true,'3000.00','5000.00');
call sp_AgregarLocales('2200.00','4500.00','5',true,'3600.00','4000.00');
call sp_AgregarLocales('5000.00','2000.00','1',true,'4000.00','5000.00');
call sp_AgregarLocales('1200.00','2000.00','3',true,'1000.00','3500.00');
call sp_AgregarLocales('1600.00','3000.00','1',true,'4000.00','1100.00');
call sp_AgregarLocales('1200.00','2000.00','1',false,'4000.00','1100.00');

call sp_AgregarLocales('10000',' 500.00','9',false,'4000.00','2100.00');
call sp_AgregarLocales('2100.00','6600.00','8',true,'8000.00','3100.00');
call sp_AgregarLocales('1100.00','5500.00','7',false,'7000.00','4100.00');
call sp_AgregarLocales('4900.00','4400.00','6',true,'9000.00','2100.00');
call sp_AgregarLocales('1900.00','3300.00','5',false,'4000.00','2100.00');

/*----------------------------Empleado-------------------*/
call sp_AgregarEmpleados('Andy Daniel','Cuyuch Chitay','daniel1chitay@gmail.com','53814608','2021/5/30','4500.00',1,1,1,1);
call sp_AgregarEmpleados('Brizeyda Yulianna', 'Boror Cardona','yulianna2004boror@gmail.com','42421109','2021/6/14','3000.00',2,2,2,2);
call sp_AgregarEmpleados('Juan Carlos', 'bravo tuc','carlos2bravo@gmail.com','48481106','2021/2/15','2500.00',3,3,3,3);
call sp_AgregarEmpleados('Josue Alexander','ixcoy Cali','josuecali@gmail.com','47475566','2020/8/17','1000.00',4,4,4,4);
call sp_AgregarEmpleados('Laura Carolina','Ochoa','laura33ochoa@gmail.com','78875588','2020/12/11','3500.00',5,5,5,5);

/*---------------------------Proveedores------------------*/
call sp_AgregarProveedores('45545454','Servicio de Agua','88552200',' 8-00 zona 12','5000.00','2000.00',1);
call sp_AgregarProveedores('77889944','Servicio de luz ','99135548','2a calle 8-00 zona 6','8000.00','1500.00',2); 
call sp_AgregarProveedores('78879988','Servicio de Internet','78784455','9a calle 10-44 zona 3','4500.00','1000.00',3);
call sp_AgregarProveedores('56562233','Servicio de conexos','87878787','1a calle 4-34 zona 1','4600.00','1500.00',4);
call sp_AgregarProveedores('45543311','Servicio de telefono','78889955','9a Avenida A 1-66 zona 11','8000.00','3000.00',5);
call sp_AgregarProveedores('45543311','Servicio de medico','78889955','9a Avenida A 1-66 zona 11','8000.00','3000.00',6);
call sp_AgregarProveedores('45543311','Servicio de entrega','78889955','9a Avenida A 1-66 zona 11','8000.00','3000.00',7);
call sp_AgregarProveedores('45543311','Servicio de limpieza','78889955','9a Avenida A 1-66 zona 11','8000.00','3000.00',8);
call sp_AgregarProveedores('45543311','Servicio de seguridad','78889955','9a Avenida A 1-66 zona 11','8000.00','3000.00',9);
call sp_AgregarProveedores('45543311','Servicio de publicidad','78889955','9a Avenida A 1-66 zona 11','8000.00','3000.00',10);

-- Andy Daniel Cuyuch Chitay 2017403 -- 

/*------------------------CuentasPorPagar------------------*/
call  sp_AgregarCuentasPorPagar('55887788','2021/4/5','Finalizado','4555.00',1,1);
call  sp_AgregarCuentasPorPagar('45351535','2021/5/9','Pendiente','3000.00',2,2);
call  sp_AgregarCuentasPorPagar('45465645','2021/8/14','Finalizado','2000.00',3,3);
call  sp_AgregarCuentasPorPagar('78945622','2021/5/6','Pendiente','4000.00',4,4);
call  sp_AgregarCuentasPorPagar('52525252','2021/6/12','Finalizado','3200.00',5,5);

/*-------------------------Clientes--------------------------*/
call sp_AgregarClientes('Alejandra Jazmin','Guzman Cal','12345678','zona 6 9-00','alejandra00jaz@gmail.com',1,1,1);
call sp_AgregarClientes('Sara Elena','Gonzales Gomez','87456123','zona 7 8-00','saralol12@gmail.com',2,2,2);
call sp_AgregarClientes('janeth jaky','Lara Prado','98745612','zona 2 5-00','daniel122leon@gmail.com',3,3,3);
call sp_AgregarClientes('Daniel Alexander','leon Cuyuch','96325874','zona 11 7-11','danialex22@gmail.com',4,4,4);
call sp_AgregarClientes('Gadiel Arron','Orellana Guy','17485296','zona 6 2-12','arrongadi11@gmail.com',5,5,5);
call sp_AgregarClientes('Andy Daniel','Cuyuch Chitay','11112222','zona 43 1-11','123andy123@gmail.com',6,6,6);

/*------------------------CuentasPorCobrar-------------------*/
call sp_AgregarCuentasPorCobrar('78945621','2021','6','5000.00','Finalizado',1,1,1);
call sp_AgregarCuentasPorCobrar('45612389','2021','5','3000.00','Finalizado',2,2,2);
call sp_AgregarCuentasPorCobrar('54654645','2020','9','2000.00','Pendiente',3,3,3);
call sp_AgregarCuentasPorCobrar('45656545','2019','4','1500.00','Pendiente',4,4,4);
call sp_AgregarCuentasPorCobrar('54415654','2018','1','2500.00','Finalizado',5,5,5);

/*--------------------------Login------------------------------*/
call sp_AgregarUsuario('Andy','Cuyuch','acuyuch','123');
/*-------------------------------------------------(Bucar,Listar,Eliminar,Editar)-----------------------------------*/

/*--------------------------------------Departamentos---------------------------------*/
/*--Listar--*/
Delimiter $$ 
	Create procedure sp_ListarDepartamentos()
    begin
		Select
			D.codigoDepartamento,
            D.nombreDepartamento
		From Departamentos D ;
	End$$
Delimiter ; 

/*--ELiminar--*/
Delimiter $$
	Create procedure sp_EliminarDepartamentos(in codDepartamento int)
		begin
			Delete from  Departamentos
            where codigoDepartamento = codDepartamento ;
		End$$
Delimiter ; 

/*--Editar--*/
Delimiter $$
	Create procedure sp_EditarDepartamentos(in codDepartamento int, in nomDepartamento varchar(45))
		begin
			update Departamentos
				set
					codigoDepartamento = codDepartamento,
					nombreDepartamento = nomDepartamento
				where codigoDepartamento =  codDepartamento;
        End$$
Delimiter ; 

/*--Buscar--*/
Delimiter $$
	Create procedure sp_BuscarDepartamentos(in codDepartamento int)
		Begin
			select 
				D.codigoDepartamento,
                D.nombreDepartamento
                from Departamentos D
                where codigoDepartamento = codDepartamento;
		End$$
Delimiter ; 

/*------------------------------------Cargos-----------------------------------*/
/*----Listar----*/
Delimiter $$ 
	Create procedure sp_ListarCargos()
    begin
		Select
			C.codigoCargo,
            C.nombreCargo
		from Cargos C;
	End$$
Delimiter ;

/*---Eliminar---*/
Delimiter $$ 
	Create procedure sp_EliminarCargo(in codCargo int)
	Begin
		Delete from Cargos
			where codigoCargo = codCargo;
	End$$
Delimiter ; 

/*---Editar---*/
Delimiter $$ 
	Create procedure sp_EditarCargos(in codCargo int, in nomCargo varchar(45))
	begin
		update Cargos
			set
				nombreCargo = nomCargo
                where codigoCargo = codCargo;
	End$$
Delimiter ;

/*----Buscar----*/
Delimiter $$
	Create procedure sp_BuscarCargos(in codCargos int)
	Begin
		Select
			C.codigoCargo,
            C.nombreCargo
            from Cargos C 
            where codigoCargo = codCargos;
	End$$
Delimiter ;

/*------------------------------------------Horarios-----------------------------------------*/
/*Listar*/
Delimiter $$
	Create procedure sp_ListarHorarios()
    begin
		Select
			H.codigoHorario,
            H.horarioEntrada,
            H.horarioSalida,
            H.lunes,
            H.martes,
            H.miercoles,
            H.jueves,
			H.viernes
		from Horarios H;
		End$$
Delimiter ;

/*Eliminar*/
Delimiter $$
	Create procedure sp_EliminarHorarios(in codHorario int )
    Begin
		Delete from Horarios
        where codigoHorario = codHorario;
	End$$
Delimiter ; 

/*----Editar-----*/
Delimiter $$ 
	Create procedure sp_EditarHorarios(in codHorario int, in horaEntrada varchar(45),in horaSalida varchar(45),
						in lun boolean, in mar boolean, in mie boolean,in jue boolean,in vier boolean)
    Begin 
		update Horarios
			set
				horarioEntrada = horaEntrada,
                horarioSalida = horaSalida,
                lunes = lun,
                martes = mar,
                miercoles = mie,
                jueves = jue,
                viernes = vier 
                where codigoHorario = codHorario;
	End$$ 
Delimiter ; 

/*----Buscar----- */
Delimiter $$ 
	Create procedure sp_BuscarHorarios(in codHorario int)
		Begin
			Select 
				BH.codigoHorario,
                BH.horarioEntrada,
                BH.horarioSalida,
                BH.lunes,
				BH.martes,
                BH.miercoles,
				BH.jueves,
                BH.viernes
                from Horarios BH
                where codigoHorario = codHorario;
		End$$
Delimiter ; 

/*-------------------------------------Administracion-------------------------------------*/
/*---Listar---*/
Delimiter $$ 	
    Create procedure sp_ListarAdministracion()
	begin	
		Select
			A.codigoAdministracion,
            A.direccion,
            A.telefono
		from Administracion A; 
	End $$ 
Delimiter ;

/*---Eliminar--*/
Delimiter $$
	Create procedure sp_EliminarAdministracion(in idAdministracion int)
		begin
			Delete from  Administracion
            where codigoAdministracion = idAdministracion;
		End$$
Delimiter ; 

/*---Editar---*/
Delimiter $$
	Create procedure sp_EditarAdministracion(in codAdministracion int, in nomDireccion varchar(45), in numTelefono varchar(45))
		begin
			update Administracion
				set
					direccion = nomDireccion,
                    telefono = numTelefono
				where codigoAdministracion =  codAdministracion;
        End$$
Delimiter ; 

/*---Buscar---*/
Delimiter $$
	Create procedure sp_BuscarAdministracion(in codAdministracion int )
		begin
			Select 
				A.codigoAdministracion,
                A.direccion,
                A.telefono
                from Administracion A
                where codigoAdministracion = codAdministracion;
		End$$
Delimiter ; 

/*--------------------------------------------TipoCliente-------------------------------------------------------*/
/*----Listar----*/
Delimiter $$ 
	Create procedure sp_ListarTipoCliente()
    begin
		Select
			TC.codigoTipoCliente,
            TC.descripcion
		from TipoCliente TC;
	End$$
Delimiter ; 

/*---Eliminar---*/
Delimiter $$ 
	Create procedure sp_EliminarTipoCliente(in codTipoCliente int)
	Begin 
		Delete from TipoCliente 
        where codigoTipoCliente =  codTipoCliente; 
	End $$ 
Delimiter ; 

/*---Editar----*/
Delimiter $$ 
	Create procedure sp_EditarTipoCliente(in codTipoCliente int, in decripcion varchar(45))
    begin
		update TipoCliente
			set
				codigoTipoCliente =  codTipoCliente,
                descripcion =  decripcion
                where codigoTipoCliente = codTipoCliente;
	End $$
Delimiter ; 

/*--Buscar----*/
Delimiter $$
	Create procedure sp_BuscarTipoCliente(in codTipoCliente int)
	Begin
		select 
			T.codigoTipoCliente,
            T.descripcion
            from TipoCliente T 
            where codigoTipoCliente = codTipoCliente ;
	End $$
Delimiter ; 

/*------------------------------------------------Locales------------------------------------------*/
/*Listar*/
Delimiter $$
	Create procedure sp_ListarLocales()
    begin
		Select
			L.codigoLocal,
            L.saldoFavor,
            L.saldoContra,
            L.mesesPendientes,
            L.disponibilidad,
			L.valorLocal,
            L.valorAdministracion
		from Locales L; 
	End$$
Delimiter ; 

/*Eliminar*/
Delimiter $$ 
	create procedure sp_EliminarLocales(in codLocal int)
	begin
		Delete  from Locales 
			where codigoLocal = codLocal;
	End $$ 
Delimiter ; 

/*Editar*/
Delimiter $$ 
	Create procedure sp_EditarLocales(in codLocal int, in saldFavor Double (11,2), in saldContra Double (11,2),
						in mesPendientes int, in disponibilida boolean, in valorLoc Double (11,2),
                        in valorAdministra Double(11,2))
	Begin 
		update Locales set 
			codigoLocal = codLocal,
            saldoFavor =  saldFavor,
            saldoContra =  saldContra,
            mesesPendientes =  mesPendientes,
            disponibilidad = disponibilida,
            valorLocal = valorLoc,
            valorAdministracion = valorAdministra
			where codigoLocal = codLocal;
	End$$ 
Delimiter ; 

/*Buscar*/
Delimiter $$ 
	Create procedure sp_BuscarLocales(in codLocal int)
	Begin 
		select 
			lc.codigoLocal,
            lc.saldoFavor,
            lc.saldoContra,
            lc.mesesPendientes,
            lc.disponibilidad,
            lc.valorLocal,
            lc.valorAdministracion
            from Locales lc
		where codigoLocal = codLocal;
	End $$
Delimiter ; 

/*------------------------------------------------Empleados-----------------------------------------*/
/* Listar */
Delimiter $$ 
	Create procedure sp_ListarEmpleados()
    begin
		Select
			E.codigoEmpleados,
            E.nombreEmpleado,
            E.apellidoEmpleado,
            E.correoElectronico,
            E.telefonoEmpleado,
			E.fechaContrato,
            E.sueldo,
            E.codigoDepartamentos,
            E.codigoCargo,
            E.codigoHorario,
			E.codigoAdministracion
		from Empleados E;
	End$$
Delimiter ; 

/*Eliminar*/
Delimiter $$
	Create procedure sp_EliminarEmpleados(in codEmpleado int)
    Begin
		delete from Empleados
			where codigoEmpleados = codEmpleado;
	End $$ 
Delimiter ; 

/*Editar*/
Delimiter $$ 
	create procedure sp_EditarEmpleados(in codEmpleado int, in nomEmpleado varchar(45), in apeyidoEmpleado varchar(45),
    in correoElec varchar(45), in teleEmpleado varchar(8), in feContrato date, in sueldoD double(11,2))
	Begin 
		update Empleados
			set 
				codigoEmpleados = codEmpleado,
                nombreEmpleado =  nomEmpleado, 
                apellidoEmpleado = apeyidoEmpleado,
                correoElectronico = correoElec,
                telefonoEmpleado = teleEmpleado,
                fechaContrato = feContrato, 
                sueldo = sueldoD
			where codigoEmpleados = codEmpleado;
	End $$ 
Delimiter ; 

/*Buscar*/
Delimiter $$ 
	Create procedure sp_BuscarEmpleados(in codEmpleado int)
    Begin
		select 
			BE.codigoEmpleados,
            BE.nombreEmpleado,
            BE.apellidoEmpleado,
            BE.correoElectronico,
            BE.telefonoEmpleado,
            BE.fechaContrato,
            BE.sueldo,
            BE.codigoDepartamentos,
            BE.codigoCargo,
            BE.codigoHorario,
            BE.codigoAdministracion
            from Empleados BE 
            where codigoEmpleados = codEmpleado ; 
	End $$
Delimiter ; 

/*--------------------------------------------Proveedores------------------------------------------*/
Delimiter $$
	Create procedure sp_ListarProveedores()
	begin
		select
			P.codigoProveedores,
            P.NITProveedor,
            P.servicioPrestado,
            P.telefonoProveedor,
            P.direccionProveedor,
            P.saldoFavor,
            P.saldoContra,
            P.administracion_codigoAdministracion
            from Proveedores P;
    End $$
Delimiter ;

/*Eliminar*/
Delimiter $$ 
	Create procedure sp_EliminarProveedores(in codProveedores int)
    Begin
		Delete from Proveedores
			where codigoProveedores = codProveedores;
	End $$ 
Delimiter ; 

/*Editar*/
Delimiter $$
	Create procedure sp_EditarProveedores(in codProveedores int, in Nit varchar(45), in servicioPresta varchar(45),
    in telefono varchar(8), in direccion varchar(45), in saldoFa double(11,2), in saldoCon double (11,2))
    begin
		update  Proveedores
			set 
				codigoProveedores = codProveedores, 
                NITProveedor = Nit,
                servicioPrestado = servicioPresta,
                telefonoProveedor  = telefono,
                direccionProveedor = direccion,
                saldoFavor = saldoFa,
				saldoContra  = saldoCon
                where codigoProveedores = codProveedores;
	End $$
Delimiter ; 

/*Buscar*/
Delimiter $$ 
	Create procedure sp_BuscarProveedores(in codProveedores int )
	Begin
		select 
			PR.codigoProveedores,
            PR.NITProveedor,
            PR.servicioPrestado,
            PR.telefonoProveedor,
            PR.direccionProveedor,
            PR.saldoFavor,
            PR.saldoContra,
            PR.administracion_codigoAdministracion
            from Proveedores PR 
            where codigoProveedores = codProveedores;
	End $$ 
Delimiter ; 

/*--------------------------------------------------Clientes------------------------------------------*/}7
/*Listar*/
Delimiter $$
	Create procedure sp_ListarClientes()
    Begin
		select 
			C.codigoCliente,
            C.nombreCliente,
            C.apellidoCliente,
            C.telefonoCliente,
            C.direccionCliente,
            C.email,
            C.codigoLocal,
            C.codigoAdministracion,
            C.codigoTipoCliente
            from Clientes C; 
	End$$
Delimiter ;

/*Eliminar*/
Delimiter $$ 
	Create procedure sp_EliminarClientes(in codCliente int)
    begin
		Delete from Clientes
				where codigoCliente = codCliente;
	End $$
Delimiter ; 

/*Editar*/
Delimiter $$ 
	Create procedure sp_EditarClientes(in codigoCliente int, in nombreCliente varchar(45), in apellidoCliente varchar(45),
    in telefonoCliente varchar(8),in direccionCliente varchar(60), in email varchar(45),in codigoLocal int, in codigoAdministracion int, 
    in codigoTipoCliente int)
    begin
		update Clientes
			set codigoCliente = codigoCliente,
				nombreCliente = nombreCliente, 
                apellidoCliente = apellidoCliente, 
                telefonoCliente = telefonoCliente,
                direccionCliente = direccionCliente, 
                email = email,
                codigoLocal = codigoLocal,
                codigoAdministracion = codigoAdministracion,
                codigoTipoCliente = codigoTipoCliente
                where codigoCliente = codigoCliente; 
	End $$ 
Delimiter ;

/*Buscar*/
Delimiter $$ 
	Create procedure sp_BuscarClientes(in codCliente int)
    Begin
		select 
			C.codigoCliente,
            C.nombreCliente, 
            C.apellidoCliente,
            C.telefonoCliente,
            C.direccionCliente,
            C.email,
			C.codigoLocal,
            C.codigoAdministracion,
            C.codigoTipoCliente
            from Clientes C
			where codigoCliente = codCliente;
	End $$
Delimiter ; 

Delimiter $$
	Create procedure sp_ListarCuentasPorPagar()
    Begin
		select
			   C.codigoCuentasPorPagar,
			   C.numeroFactura,
               C.fechaLimitePago,
               C.estadoPago,
               C.valorNetoPago,
               C.codigoAdministracion,
               C.codigoProveedor
               from CuentasPorPagar C;
	End $$
Delimiter ;

Delimiter $$ 
	Create procedure sp_EliminarCuentasPorPagar(in CodCuentasPorPagar int)
	Begin
		Delete from CuentasPorPagar
			where codigoCuentasPorPagar = CodCuentasPorPagar ;
	End $$
Delimiter ; 

Delimiter $$ 
	 Create procedure sp_EditarCuentasPorPagar(in codCuentasPorPagar int, in numeroFactura varchar(45), in fechaLimitePago date,
	 in estadoPago varchar(45), in valorNetoPago double (11,2))
     Begin 
		update CuentasPorPagar set
			 codigoCuentasPorPagar = codCuentasPorPagar,
             numeroFactura = numeroFactura,
             fechaLimitePago = fechaLimitePago,
             estadoPago = estadoPago,
             valorNetoPago = valorNetoPago
			 where codigoCuentasPorPagar = codCuentasPorPagar;

	End$$
Delimiter ;

Delimiter $$ 
	Create procedure sp_BuscarCuentasPorPagar(in codCuentasPorPagar int)
    Begin
		select 
			CU.codigoCuentasPorPagar,
            CU.numeroFactura,
            CU.fechaLimitePago,
            CU.estadoPago,
            CU.valorNetoPago,
            CU.codigoAdministracion,
            CU.codigoProveedor
            where codigoCuentasPorPagar =  codCuentasPorPagar;
	End $$ 
Delimiter ; 

/*--------------------------------CunetasPorCobrar-----------------------------*/
Delimiter $$ 
	Create procedure sp_ListarCuentasPorCobrar()
    Begin
		select 
			C.codigoCuentasPorCobrar,
            C.codigoFactura,
            C.anio, 
            C.mes,
            C.valorNetoPago,
            C.estadoPago,
            C.codigoAdministracion,
            C.codigoCliente,
            C.codigoLocal
            from CuentasPorCobrar C ;
	End $$ 
Delimiter ; 

Delimiter $$ 
	Create procedure sp_EliminarCuentasPorCobrar(in codCuentasPorCobrar int )
    Begin
		delete from CuentasPorCobrar
			where codigoCuentasPorCobrar = codCuentasPorCobrar;
	End $$
Delimiter ; 


Delimiter $$
	Create procedure sp_EditarCuentasPorCobrar(in codigoCuentasPorCobrar int, in codigoFactura varchar(45), in anio varchar(4),
												in mes int(2), in valorNetoPago double (10,2), in estadoPago varchar(45))
    Begin
		update CuentasPorCobrar
			set
				codigoCuentasPorCobrar = codigoCuentasPorCobrar,
                codigoFactura = codigoFactura,
                anio = anio, 
                mes = mes, 
                valorNetoPago = valorNetoPago,
                estadoPago  = estadoPago
                where codigoCuentasPorCobrar = codigoCuentasPorCobrar;
	End $$ 
Delimiter ; 
 
 
 Delimiter $$ 
	Create procedure sp_BuscarCuentasPorCobrar(in codigoCuentasPorCobrar int)
	Begin
		select
			C.codigoCuentasPorCobrar,
            C.codigoFactura,
            C.anio,
            C.mes,
            C.valorNetoPago, 
            C.estadoPago,
            C.codigoAdministracion,  
            C.codigoCliente,
            C.codigoLocal 
            where codigoCuentasPorCobrar = codigoCuentasPorCobrar;            
	End $$
 Delimiter ; 
 
 /*-----------------------------Login---------------------------------*/
 Delimiter $$ 
	Create procedure sp_ListarUsuarios()
    Begin 
		Select
			U.codigoUsuario, 
            U.nombreUsuario, 
            U.apellidoUsuario,
            U.usuarioLogin,
            U.contrasena
            from Usuario U;
	End $$
 Delimiter ; 
 
 ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password by 'admin';
 
 call sp_BuscarAdministracion(3);

/*call sp_CalculoLiquido(7); */

/*---------------------------------------------------Call sp-call------------------------------------------------*/
/*-------------------------------------Deártamentos-------------------------------------*/
/*Listar*/
/*call sp_ListarDepartamentos();
/*Eliminar*/
/*call sp_EliminarDepartamentos(1);
call sp_EliminarDepartamentos(2);
call sp_EliminarDepartamentos(3);
call sp_EliminarDepartamentos(4);
call sp_EliminarDepartamentos(5);
/*Editar*/
/*call sp_EditarDepartamentos();
call sp_EditarDepartamentos();
call sp_EditarDepartamentos();
call sp_EditarDepartamentos();
call sp_EditarDepartamentos();
/*Buscar*/
/*call sp_BuscarDepartamentos(1);
call sp_BuscarDepartamentos(2);
call sp_BuscarDepartamentos(3);
call sp_BuscarDepartamentos(4);
call sp_BuscarDepartamentos(5);*/

/*-----------------------------------------Cargos--------------------------------*/
/*Listar*/
/*call sp_ListarCargos();
/*Eliminar*/
/*call sp_EliminarCargo(1);
call sp_EliminarCargo(2);
call sp_EliminarCargo(3);
call sp_EliminarCargo(4);
call sp_EliminarCargo(5);
/*Editar*/
/*call sp_EditarCargos();
call sp_EditarCargos();
/*Buscar*/
/*call sp_BuscarCargos(1);
call sp_BuscarCargos(2);
call sp_BuscarCargos(3);
call sp_BuscarCargos(4);
call sp_BuscarCargos(5);*/

/*---------------------------------------Horarios------------------------------*/

/*listar*/
/*call sp_ListarHorarios();
/*Eliminar*/
/*call sp_EliminarHorarios();
call sp_EliminarHorarios();
call sp_EliminarHorarios();
call sp_EliminarHorarios();
call sp_EliminarHorarios();
/*Editar*/
/*call sp_EditarHorarios();
call sp_EditarHorarios();
call sp_EditarHorarios();
call sp_EditarHorarios();
call sp_EditarHorarios();
/*Buscar*/
/*call sp_BuscarHorarios(3);
call sp_BuscarHorarios(2);
call sp_BuscarHorarios(3);
call sp_BuscarHorarios(4);
call sp_BuscarHorarios(5);*/

/*--------------------------------------Administracion----------------------------*/
/*call sp_ListarAdministracion();
/*-------Eliminar call------*/
/*call sp_EliminarAdministracion(1);
call sp_EliminarAdministracion(2);
call sp_EliminarAdministracion(3);
call sp_EliminarAdministracion(4);
call sp_EliminarAdministracion(5);
/*-------Editar call---------*/
/*call sp_EditarAdministracion();
call sp_EditarAdministracion();
/*------Buscar call----------*/
/*Codigo Administracion*/
/*call sp_BuscarAdministracion(1);
call sp_BuscarAdministracion(2);
call sp_BuscarAdministracion(3);
call sp_BuscarAdministracion(4);
call sp_BuscarAdministracion(5);*/


/*---------------------------------TipoCliente----------------------------------------*/

/*Listar*/
/*call sp_ListarTipoCliente();
/*Eliminar*/
/*call sp_EliminarTipoCliente(1);
call sp_EliminarTipoCliente(2);
call sp_EliminarTipoCliente(3);
call sp_EliminarTipoCliente(4);
call sp_EliminarTipoCliente(5);
/*Editar*/
/*call sp_EditarTipoCliente();
call sp_EditarTipoCliente();
/*BUscar*/
/*call sp_BuscarTipoCliente(1);
call sp_BuscarTipoCliente(2);
call sp_BuscarTipoCliente(3);*/

/*-------------------------------------Locales----------------------------------------*/

 /*Elimnar*/
/*call sp_EliminarLocales(11);
/*Editar*/
/*call sp_EditarLocales();
/*Buscar*/
/*call sp_BuscarLocales(1);
call sp_BuscarLocales(2);
call sp_BuscarLocales(3);
call sp_BuscarLocales(4);
call sp_BuscarLocales(5);*/

/*---------------------------------Emppleados-------------------------------------------*/

/*call sp_ListarEmpleados(); 

call sp_EliminarEmpleados(1);

call sp_EditarEmpleados();

call sp_BuscarEmpleados(2);*/

/*--------------------------------Proveeodres--------------------------------------------*/

/*4call sp_ListarProveedores();

call sp_EliminarProveedores(2);

call sp_EditarProveedores();

call  sp_BuscarProveedores(3); 

/*------------------------------Cuentas por Pagar---------------------------------------*/
/*call sp_ListarCuentasPorPagar();

call sp_EliminarCuentasPorPagar();

call sp_EditarCuentasPorPagar();

call sp_BuscarCuentasPorPagar();*/

/*-----------------------------------Clientes--------------------------------------*/

/*call sp_ListarClientes();

call sp_EliminarClientes();

call sp_EditarClientes();

call sp_BuscarClientes(); */

/*-------------------------------Cuenstas por cobrar---------------------------*/


/* call sp_ListarCuentasPorCobrar();
 
 call sp_EliminarCuentasPorCobrar();

 call sp_EditarCuentasPorCobrar();
 
 call sp_BuscarCuentasPorCobrar();

*/

/*nos brinda mas facilidad de manejar la informacion de la base de datos*/

/*-----------------------------------------Calculo--------------------------------------*/
Delimiter  $$ 
	Create procedure sp_SaldoPagar()
    Begin 
		select SP.codigoLocal,
			   SP.saldoFavor,
               SP.saldoContra,
               SP.mesesPendientes,
               SP.disponibilidad,
               SP.valorLocal,
               SP.valorAdministracion,
               ((mesesPendientes * valorLocal) - (saldoFavor -saldoContra)) 
               AS SaldoPagar from locales SP;
	End $$ 
Delimiter ; 

-- Andy Daniel Cuyuch Chitay 2017403 
Delimiter $$ 
	Create procedure sp_calculoLocales(in codLocales int)
	Begin
		select CL.codigoLocal,
			   CL.saldoFavor,
               CL.saldoContra,
               CL.mesesPendientes,
               CL.disponibilidad,
               CL.valorLocal,
               CL.valorAdministracion,
               ((mesesPendientes * valorLocal) - (saldoFavor -saldoContra)) 
               as debe from Locales CL where codigoLocal = codLocales;
	End $$
Delimiter ;

-- call sp_calculoLocales(3);


-- Andy Daniel Cuyuch Chitay 
Delimiter $$ 
		Create procedure sp_ContarLocales()
        Begin
			select count(codigoLocal) from locales;
		End $$
Delimiter ; 

-- call sp_ContarLocales();



-- Andy Daniel Cuyuch Chitay --
-- disponibles
Delimiter $$ 
	Create procedure sp_DisLocales()
    Begin
		select count(codigoLocal) from Locales where disponibilidad = true;
	End $$
Delimiter ; 

-- call sp_DisLocales();


-- Andy Daniel Cuyuch Chitay --
-- no disponibles--
Delimiter $$ 
	Create procedure sp_NoDisLocales()
    Begin
		select count(codigoLocal) from Locales where disponibilidad = false;
	End $$
Delimiter ; 

-- call sp_NoDisLocales();

 -- Andy Daniel Cuyuch Chitay --
Delimiter $$  
	Create procedure sp_calculoLiquidoProveedor(in codProveedor int)
    Begin
		select(saldoFavor - saldoContra) from 
						Proveedores P where codigoProveedores = codProveedor;
	End$$
Delimiter ; 

-- call  sp_calculoLiquidoProveedor(1);

-- Andy Daniel Cuyuch Chitay 2017403 --
Delimiter $$ 
	Create procedure sp_SaldoLiquido()
    Begin 
		select SL.codigoProveedores,
			   SL.NITProveedor,
               SL.servicioPrestado,
               SL.telefonoProveedor,
               SL.direccionProveedor,
               SL.saldoFavor,
               SL.saldoContra,
               SL.administracion_codigoAdministracion,
			   (saldoFavor - saldoContra) AS SaldoLiquido from Proveedores SL;
	End $$ 
Delimiter ; 

/* call sp_SaldoLiquido(); */

-- Andy Daniel Cuyuch Chitay 2017403 --

Delimiter  $$ 
	Create procedure sp_SaldoPagarDos()
    Begin 
		select SP.codigoLocal,
			   SP.saldoFavor,
               SP.saldoContra,
               SP.mesesPendientes,
               SP.disponibilidad,
               SP.valorLocal,
               SP.valorAdministracion,
               ((mesesPendientes * valorLocal) - (saldoFavor -saldoContra)) 
               AS SaldoPagar from locales SP;
	End $$ 
Delimiter ; 

-- call sp_SaldoPagar();

/*----------Procedimiento almacenado para calcular el saldo liquido para calcular el local X*/
Delimiter $$
	Create procedure sp_CalculoLiquido(in codLocal int)
	begin 
		select (saldoFavor - saldoContra) from Locales where codigoLocal = codLocal;
	End$$ 
Delimiter ;