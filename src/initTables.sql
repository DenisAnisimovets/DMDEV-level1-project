--Пользователь
--имеет несколько ролей
CREATE TABLE IF NOT EXISTS users
(
    id serial PRIMARY KEY ,
    name VARCHAR(256) ,
    email VARCHAR(256) UNIQUE NOT NULL,
    city VARCHAR(128)
);

--Роли
CREATE TABLE IF NOT EXISTS role
(
    id serial PRIMARY KEY ,
    name VARCHAR(128)
);

--Роли пользователей. Для каждого прользователя может быть несколько ролей.
CREATE TABLE IF NOT EXISTS user_role
(
    user_id INT REFERENCES users(id) ,
    role_id INT REFERENCES role(id) ,
    PRIMARY KEY (user_id, role_id)
);

--Товары
CREATE TABLE IF NOT EXISTS product
(
    id serial PRIMARY KEY ,
    name  VARCHAR(128),
    price INT,
    quantity INT
);

--Отзывы на товары
CREATE TABLE IF NOT EXISTS product_feedback
(
    id serial PRIMARY KEY ,
    data_feedback TIMESTAMP ,
    product_id INT REFERENCES product(id),
    user_id INT REFERENCES users(id),
    feedback VARCHAR(1024)

);

--Корзины. Каждый пользователь добавляет товары в корзину. После она попадает в заказ.
CREATE TABLE IF NOT EXISTS basket
(
    id serial PRIMARY KEY ,
    user_id INT REFERENCES users(id),
    product_id INT REFERENCES product(id),
    quantity INT
);

--Заказы
CREATE TABLE IF NOT EXISTS orders
(
    id serial PRIMARY KEY ,
    user_id INT REFERENCES users(id),
    sum INT
);

--Таблична часть заказов. Т.е. товары в заказах
CREATE TABLE IF NOT EXISTS product_order
(
    order_id INT REFERENCES orders(id),
    product_id INT REFERENCES product(id),
    quantity INT,
    price INT,
    sum INT
);









