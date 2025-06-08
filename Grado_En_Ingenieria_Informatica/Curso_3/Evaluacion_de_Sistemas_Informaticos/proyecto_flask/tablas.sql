SET FOREIGN_KEY_CHECKS=0; -- Desactivar temporalmente chequeo de claves foráneas
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS cart_items;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS users;
SET FOREIGN_KEY_CHECKS=1; -- Reactivar chequeo de claves foráneas

-- Tabla de Usuarios
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `user_type` varchar(10) NOT NULL DEFAULT 'not_admin',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  CONSTRAINT `chk_user_type` CHECK (`user_type` in ('admin','not_admin'))
);

-- Tabla de Productos (Posters)
CREATE TABLE `products` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idurl` varchar(100) NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` text DEFAULT NULL,
  `artist` varchar(100) DEFAULT NULL,
  `price` decimal(10,2) NOT NULL,
  `stock` int(11) NOT NULL, -- Cambiado de INT UNSIGNED a INT para consistencia con tu 'describe'
  `image_url` varchar(512) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `idurl` (`idurl`),
  CONSTRAINT `chk_product_price` CHECK (`price` >= 0),
  CONSTRAINT `chk_product_stock` CHECK (`stock` >= 0)
);

-- Tabla de Items en el Carrito de Compras
CREATE TABLE `cart_items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` int(10) unsigned NOT NULL DEFAULT 1,
  `added_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_user_product` (`user_id`,`product_id`), -- Añadida de versiones anteriores
  KEY `fk_cart_product` (`product_id`),
  KEY `idx_cart_user_id` (`user_id`), -- Índice que tenías en tu SHOW CREATE
  CONSTRAINT `fk_cart_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_cart_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
);

-- Tabla de Pedidos
CREATE TABLE `orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `total_amount` decimal(10,2) NOT NULL,
  `status` varchar(50) NOT NULL DEFAULT 'completed',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `fk_order_user` (`user_id`),
  CONSTRAINT `fk_order_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT, -- Cambiado a RESTRICT como en tu SHOW CREATE
  CONSTRAINT `chk_order_total` CHECK (`total_amount` >= 0)
);

-- Tabla de Items de un Pedido
CREATE TABLE `order_items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` int(10) unsigned NOT NULL,
  `price_at_purchase` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_orderitem_order` (`order_id`),
  KEY `fk_orderitem_product` (`product_id`),
  CONSTRAINT `fk_orderitem_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_orderitem_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE RESTRICT, -- Cambiado a RESTRICT como en tu SHOW CREATE
  CONSTRAINT `chk_orderitem_quantity` CHECK (`quantity` > 0),
  CONSTRAINT `chk_orderitem_price` CHECK (`price_at_purchase` >= 0)
);