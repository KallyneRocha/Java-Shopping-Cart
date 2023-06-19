CREATE TABLE IF NOT EXISTS products (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  quantity INT NOT NULL
);

INSERT INTO products (name, price, quantity)
VALUES
  ('Scooby-doo', 89.90, 20),
  ('Charmander', 90.50, 15),
  ('Squirtle', 82.00, 30),
  ('Bulbasaur', 85.45, 25),
  ('Joel', 119.90, 10),
  ('Michael J.', 220.00, 50),
  ('Rammus', 199.90, 18),
  ('Thresh', 185.00, 12),
  ('Lara Croft', 120.80, 35),
  ('Jinx', 400.00, 28);

CREATE TABLE IF NOT EXISTS shopping_cart (
  id SERIAL PRIMARY KEY,
  product_id INT NOT NULL,
  quantity INT NOT NULL,
  FOREIGN KEY (product_id) REFERENCES products (id)
);
