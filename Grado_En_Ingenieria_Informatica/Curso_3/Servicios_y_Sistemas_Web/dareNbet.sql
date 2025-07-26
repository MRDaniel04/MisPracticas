-- Autores:  CARBAJO ORGAZ, AINHOA /Del Val Ramos,Alfredo / PENA MARQUES, RODRIGO / Garcia Salinas, Daniel / De Diego Martín, Marcos
-- Fecha Creacion: Abril 2025
-- Descripcion: Base de datos creada para la web dareNbet (Versión con VotosPost)

DROP TABLE IF EXISTS UsuarioEntrada;
DROP TABLE IF EXISTS Comentario;
DROP TABLE IF EXISTS UsuarioApuesta;
DROP TABLE IF EXISTS UsuarioTemas;
DROP TABLE IF EXISTS OpcionApuesta;
DROP TABLE IF EXISTS VotosPost;
DROP TABLE IF EXISTS Post;
DROP TABLE IF EXISTS Entrada;
DROP TABLE IF EXISTS Apuesta;
DROP TABLE IF EXISTS Creador;
DROP TABLE IF EXISTS Usuario;
DROP TABLE IF EXISTS Temas;
DROP TABLE IF EXISTS Blog;
DROP TABLE IF EXISTS Foro;

-- Temas: Almacena los diferentes temas o categorías de interés.
CREATE TABLE Temas (
    nombre VARCHAR(50) PRIMARY KEY
);

INSERT INTO Temas (nombre) VALUES
('Celebrities'), ('Futbol'), ('Baloncesto'),
('Musica'), ('Politica'), ('Clima');

-- Usuario: Almacena la información de los usuarios registrados en la plataforma.
CREATE TABLE Usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombreUsuario VARCHAR(50) NOT NULL UNIQUE,
    nombre VARCHAR(50),
    apellido VARCHAR(50),
    edad INT,
    contraseña VARCHAR(100) NOT NULL,
    correoElectronico VARCHAR(100) UNIQUE,
    numeroTelefono VARCHAR(20),
    nCreditos INT DEFAULT 0,
    imagen VARCHAR(255),
    fechaInscripcion DATETIME DEFAULT CURRENT_TIMESTAMP,
    ultimaRecompensa DATETIME,
    isAdmin BOOL NOT NULL DEFAULT 0
);

-- UsuarioTemas: Relaciona usuarios con sus temas de interés (preferencias).
CREATE TABLE UsuarioTemas (
    usuario_id INT,
    tema_nombre VARCHAR(50),
    PRIMARY KEY (usuario_id, tema_nombre),
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (tema_nombre) REFERENCES Temas(nombre) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Creador: Identifica a los usuarios que tienen rol de creador de contenido (apuestas).
CREATE TABLE Creador (
    id INT PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES Usuario(id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Apuesta: Almacena la información de las apuestas creadas por los usuarios creadores.
CREATE TABLE Apuesta (
    id VARCHAR(50) PRIMARY KEY,
    nVisualizaciones INT DEFAULT 0,
    nLikes INT DEFAULT 0,
    nDislikes INT DEFAULT 0,
    nCreditosTotal INT DEFAULT 0,
    titulo VARCHAR(255) NOT NULL,
    descripcion TEXT,
    imagen VARCHAR(255),
    fechaPublicacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    fechaFin DATETIME,
    creador_id INT,
    tags VARCHAR(255),
    FOREIGN KEY (creador_id) REFERENCES Creador(id) ON DELETE SET NULL ON UPDATE CASCADE
);

-- Entrada: Almacena las entradas o artículos del blog.
CREATE TABLE Entrada (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    fechaPublicacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    descripcion TEXT,
    icono VARCHAR(255)
);

-- Post: Almacena los posts o mensajes creados por usuarios en el foro.
CREATE TABLE Post (
    id VARCHAR(50) PRIMARY KEY,
    contenido TEXT NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    nVisualizaciones INT DEFAULT 0,
    fechaPublicacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    autor_id INT,
    FOREIGN KEY (autor_id) REFERENCES Usuario(id) ON DELETE SET NULL ON UPDATE CASCADE
);

-- VotosPost: Registra los likes y dislikes que los usuarios dan a los posts del foro.
CREATE TABLE VotosPost (
    idVoto INT AUTO_INCREMENT PRIMARY KEY,
    idPost VARCHAR(50) NOT NULL,
    idUsuario INT NOT NULL,
    tipoVoto INT NOT NULL,
    fechaVoto DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idPost) REFERENCES Post(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (idUsuario) REFERENCES Usuario(id) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE KEY unique_voto_usuario_post (idPost, idUsuario)
);

-- Comentario: Almacena los comentarios realizados por usuarios en posts o apuestas.
CREATE TABLE Comentario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    contenido TEXT NOT NULL,
    usuario_id INT,                     -- Quién hizo el comentario
    post_id VARCHAR(50) NOT NULL,       -- A qué post pertenece
    fechaComentario DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (post_id) REFERENCES Post(id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- UsuarioEntrada: Registra qué usuarios han visualizado qué entradas del blog.
CREATE TABLE UsuarioEntrada (
    usuario_id INT,
    entrada_id INT,
    fechaVisualizacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (usuario_id, entrada_id),
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (entrada_id) REFERENCES Entrada(id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Blog: Tabla de estructura para el blog.
CREATE TABLE Blog (
    id INT AUTO_INCREMENT PRIMARY KEY
);

-- Foro: Tabla de estructura para el foro.
CREATE TABLE Foro (
    id INT AUTO_INCREMENT PRIMARY KEY
);

-- 1) OpcionApuesta primero
CREATE TABLE OpcionApuesta (
    id          VARCHAR(50) PRIMARY KEY,
    apuesta_id  VARCHAR(50) NOT NULL,
    texto       VARCHAR(255) NOT NULL,
    cuota       DECIMAL(6,2) NOT NULL,
    votos       INT DEFAULT 0,
    FOREIGN KEY (apuesta_id) REFERENCES Apuesta(id) 
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 2) Ahora UsuarioApuesta con la FK a OpcionApuesta
CREATE TABLE UsuarioApuesta (
    usuario_id    INT          NOT NULL,
    apuesta_id    VARCHAR(50)  NOT NULL,
    opcion_id     VARCHAR(50)  NOT NULL,
    importe       DECIMAL(10,2) NOT NULL,
    fecha_apuesta DATETIME     DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (usuario_id, apuesta_id, opcion_id),
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (apuesta_id) REFERENCES Apuesta(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (opcion_id) REFERENCES OpcionApuesta(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);


INSERT INTO Usuario (id, nombreUsuario, nombre, apellido, edad, contraseña, correoElectronico, numeroTelefono, nCreditos, imagen, fechaInscripcion, ultimaRecompensa,isAdmin) VALUES
(1, 'pedro', 'Pedro', 'Martínez', 30, '1', 'pedro.martinez@email.com', '600123456', 1000, NULL, '2025-04-01 00:00:00', '2025-04-24 10:00:00',1),
(2, 'ana_g', 'Ana', 'García', 25, 'ana123', 'ana.garcia@email.com', '600987654', 500, NULL, '2025-04-02 00:00:00',NULL,0);

INSERT INTO Creador (id) VALUES (1);

INSERT INTO Apuesta (id, nVisualizaciones, nLikes, nDislikes, nCreditosTotal, titulo, descripcion, imagen, fechaPublicacion, fechaFin, creador_id, tags) VALUES
('A1', 89, 50, 14, 208, '¿Lloverá este viernes en Madrid?', '¿Crees que este viernes veremos lluvia en la capital?', NULL, '2025-04-20 00:00:00', '2025-04-26 23:59:59', 1, 'Clima'),
('A2', 56, 49, 1, 345, '¿El gato de Laura tirará algo esta semana?', 'Laura tiene un gato muy travieso. ¿Romperá algo?', NULL, '2025-04-21 00:00:00', '2025-04-28 23:59:59', 1, 'Celebrities'),
('A3', 57, 30, 9, 291, '¿Saldrá el sol el lunes durante más de 3 horas en Londres?', '¿Un milagro meteorológico en Londres?', NULL, '2025-04-22 00:00:00', '2025-04-27 23:59:59', 1, 'Clima'),
('A4', 62, 48, 20, 105, '¿Ganará el Barça el próximo clásico contra el Madrid?', 'El eterno clásico, ¿quién se llevará la victoria?', 'https://tse2.mm.bing.net/th?id=OIP.5ZaIpSK1CxbGE7sA8l_tnQHaEo&pid=Api', '2025-04-23 00:00:00', '2025-04-27 23:59:59', 1, 'Futbol');

INSERT INTO OpcionApuesta (id, apuesta_id, texto, cuota, votos) VALUES
('O11', 'A1', 'Sí', 1.8, 12), ('O12', 'A1', 'No', 2.0, 8),
('O21', 'A2', 'Sí', 1.5, 16), ('O22', 'A2', 'No', 2.5, 5),
('O31', 'A3', 'Sí', 3.2, 3), ('O32', 'A3', 'No', 1.4, 17),
('O41', 'A4', 'Sí', 2.3, 9), ('O42', 'A4', 'No', 1.7, 14);

INSERT INTO Entrada (id, titulo, fechaPublicacion, descripcion, icono) VALUES
(1, 'PROMOCIÓN ESPECIAL DE SEMANA SANTA EN DareNBet', '2025-03-30',
'Esta Semana Santa en DareNBet queremos premiar tu pasión por la emoción y la adrenalina de las apuestas por eso regalamos 100 créditos virtuales a todos los usuarios que participen en una apuesta durante este período especial. Solo debes iniciar sesión en tu cuenta de DareNBet o registrarte si aún no eres usuario, realizar una apuesta en cualquiera de nuestras opciones disponibles y recibirás automáticamente 100 créditos virtuales para seguir disfrutando de la mejor experiencia. La oferta estará disponible desde el Lunes Santo hasta el Domingo de Resurrección así que no pierdas esta oportunidad única. Puedes utilizar los créditos en cualquier tipo de apuesta dentro de la plataforma, incrementar tus posibilidades de ganar sin invertir de tu saldo real y disfrutar de la diversión sin preocupaciones. No dejes pasar esta oportunidad en DareNBet la emoción de las apuestas nunca se detiene. Regístrate, apuesta y gana con nuestros créditos virtuales. Semana Santa es un momento especial y en DareNBet queremos hacerlo aún más emocionante con una promoción exclusiva que te permitirá aumentar tus oportunidades de ganar y disfrutar sin límites. Cada apuesta que realices no solo te brinda la emoción del juego sino que también te recompensa con créditos virtuales extra para que sigas apostando sin preocupaciones. No importa si eres un jugador experimentado o si estás dando tus primeros pasos en el mundo de las apuestas, en DareNBet todos tienen la oportunidad de ganar y divertirse sin riesgo adicional. Aprovecha esta increíble promoción de Semana Santa y descubre por qué DareNBet es la mejor opción para los amantes de las apuestas. No dejes pasar esta oportunidad única y disfruta de la emoción de apostar con la tranquilidad de recibir créditos extra en tu cuenta. Regístrate ahora, realiza tu apuesta y empieza a disfrutar de la mejor experiencia en apuestas en línea con DareNBet. La diversión y las grandes oportunidades te esperan.',
'resources/news/regaloNews.png'),

(2, 'Mantenimiento Programado: Servidores Fuera de Línea', '2025-03-17',
'Atención apostadores, realizaremos una actualización de mantenimiento en nuestros servidores el 22 de marzo de 2025 de 02:00 a 06:00 hora del servidor. Durante este tiempo la plataforma no estará disponible. Mejoras incluidas en esta actualización: optimización de tiempos de carga, corrección de errores en la conversión de créditos y mejor experiencia en apuestas en vivo. Gracias por tu paciencia y por seguir apostando en DareNBet.',
'resources/news/actualizacionNews.png'),

(3, 'Nuevo Modo de Apuesta: "Desafío Relámpago"', '2025-03-15',
'Llega una nueva forma de apostar. A partir del 18 de marzo podrás participar en el Desafío Relámpago, un modo en el que los jugadores tienen 60 segundos para hacer su apuesta en eventos sorpresa. Características: cuotas dinámicas que cambian en tiempo real, bonos adicionales para los más rápidos y exclusivo para usuarios con nivel Bronce o superior. Prueba tu instinto y gana a la velocidad del rayo.',
'resources/news/eventoNews.png'),

(4, 'Evento Especial: "Semana del Doble Crédito"', '2025-03-19',
'¿Te quedaste sin créditos? No te preocupes. Desde el 25 hasta el 31 de marzo todas las recargas de créditos tendrán el doble de valor. Beneficios del evento: recarga 100 créditos y recibe 200, bonos sorpresa en apuestas de alto riesgo y exclusivo para usuarios activos en el último mes. Aprovecha esta oportunidad y multiplica tus apuestas.',
'resources/news/regaloNews.png'),

(5, 'Corrección de Errores y Mejoras en la Plataforma', '2025-02-28',
'Hemos lanzado una nueva actualización con mejoras en la estabilidad de la plataforma y corrección de errores. Cambios principales: arreglo del bug que impedía retirar ganancias en apuestas múltiples, mejora en la interfaz de usuario para apuestas en vivo y reducción de tiempos de espera en pagos con moneda virtual. Si encuentras algún problema contáctanos en soporte. Gracias por seguir apostando con nosotros.',
'resources/news/actualizacionNews.png'),

(6, 'Torneo Exclusivo: "Apuesta del Mes"', '2025-04-03',
'Demuestra que eres el mejor apostador. El 5 de abril lanzamos el Torneo Apuesta del Mes donde los jugadores con las apuestas más audaces y acertadas recibirán premios especiales. Premios: 1er lugar 5000 créditos más Insignia de Élite, 2do lugar 3000 créditos, 3er lugar 1500 créditos. Para participar solo debes apostar en eventos destacados. ¡Que gane el mejor estratega!',
'resources/news/eventoNews.png');

INSERT INTO Post (id, contenido, titulo, nVisualizaciones, fechaPublicacion, autor_id) VALUES
('P1', '¿Alguien más cree que el Barça va a arrasar en el próximo clásico? Yo apuesto todo a su victoria.', 'Opinión sobre el Clásico', 85, '2025-04-24 00:00:00', 1),
('P2', '¿Realmente alguien piensa que lloverá en Madrid el viernes? Yo lo veo súper improbable.', '¿Lluvia en Madrid?', 60, '2025-04-23 00:00:00', 1),
('P3', '¡El gato de Laura seguro que rompe algo! Ese gato es una máquina de destrucción.', 'Apuesta del Gato de Laura', 75, '2025-04-22 00:00:00', 1),
('P4', 'Londres y sol son dos palabras que no van juntas... yo voto que NO sale el sol ni loco.', 'Sol en Londres, ¿en serio?', 70, '2025-04-21 00:00:00', 2),
('P5', 'Estoy emocionado por el Desafío Relámpago, eso de apostar rápido suena muy divertido.', 'Nuevo modo: Desafío Relámpago', 90, '2025-04-20 00:00:00', 1),
('P6', '¿Recibir créditos gratis solo por apostar en Semana Santa? ¡Ya estoy dentro!', 'Promoción de Semana Santa', 80, '2025-04-19 00:00:00', 2);

INSERT INTO VotosPost (idPost, idUsuario, tipoVoto) VALUES
('P1', 1, 1), ('P1', 2, -1),
('P2', 2, 1),
('P3', 1, 1), ('P3', 2, 1),
('P4', 1, -1),
('P6', 1, 1);
