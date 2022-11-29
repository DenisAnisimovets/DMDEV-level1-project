--Пользователь
--имеет несколько ролей
CREATE TABLE IF NOT EXISTS users
(
    id SERIAL PRIMARY KEY ,
    username VARCHAR(256) ,
    first_name VARCHAR(256) ,
    last_name VARCHAR(256) ,
    email VARCHAR(256) UNIQUE NOT NULL,
    password VARCHAR(256) NOT NULL,
    city VARCHAR(128),
    isBlocked BOOLEAN
);

--Роли
CREATE TABLE IF NOT EXISTS roles
(
    id SERIAL PRIMARY KEY ,
    name VARCHAR(128)
);

--Роли пользователей. Для каждого прользователя может быть несколько ролей.
CREATE TABLE IF NOT EXISTS user_role
(
    user_id INT REFERENCES users(id) NOT NULL ,
    role_id INT REFERENCES roles(id) NOT NULL ,
    PRIMARY KEY (user_id, role_id)
);

--Товары
CREATE TABLE IF NOT EXISTS products
(
    id SERIAL PRIMARY KEY ,
    name  VARCHAR(128) NOT NULL ,
    description VARCHAR(256) NOT NULL ,
    price INT,
    quantity INT
);

--Отзывы на товары
CREATE TABLE IF NOT EXISTS product_feedbacks
(
    id SERIAL PRIMARY KEY ,
    created_at TIMESTAMP ,
    product_id INT REFERENCES products(id),
    user_id INT REFERENCES users(id),
    feedback text
);

--Корзины. Каждый пользователь добавляет товары в корзину. После она попадает в заказ.
CREATE TABLE IF NOT EXISTS bucket
(
    id SERIAL PRIMARY KEY ,
    user_id INT REFERENCES users(id),
    product_id INT REFERENCES products(id),
    quantity INT
);

--Заказы
CREATE TABLE IF NOT EXISTS orders
(
    id SERIAL PRIMARY KEY ,
    user_id INT REFERENCES users(id),
    sum INT
);

--Таблична часть заказов. Т.е. товары в заказах
CREATE TABLE IF NOT EXISTS product_orders
(
    order_id INT REFERENCES orders(id),
    product_id INT REFERENCES products(id),
    quantity INT,
    price INT,
    sum INT
);