INSERT INTO users(user_id, firstname, lastname, email, login, password, role, approved) VALUES
(1, 'Natalia', 'Sznaza', 'natalia@wp.pl', 'natalia', '$2a$10$1tROLPBI9uHXSBJi8lCQU.FwpYn2xKIRyFxpRcA9av10i0CtvERfi', 'ADMIN', false),
(2, 'Alicja', 'Kukułka', 'alicja@wp.pl', 'alicja', '$2a$10$/OPwe.yD7aGg5EEpSMTdtOWg.OAUtbeDApgoGlCmAVZzMoScrS4.q', 'ADMIN', true),
(3, 'Przemek', 'Ziegert', 'przemek@wp.pl', 'b', '$2a$10$RJvDfYrHs77KAcjm.jae3uoJm.eaV6iuAf6LAzjWxoT7Lb5eQnyCO', 'USER', true),
(4, 'xx', 'cc', 'vv@wp.pl', 'a', '$2a$10$Hhx2PDRt2oIc4Ud8U9B2RO7X4I4P9twaQAlyowqNrWWwJUe0rNwHa', 'ADMIN', true),
(5, 'Przemek', 'Ziegert', 'przemefdk@wp.pl', 'bs', '$2a$10$RJvDfYrHs77KAcjm.jae3uoJm.eaV6iuAf6LAzjWxoT7Lb5eQnyCO', 'USER', true),
(6, 'Przemek', 'Ziegert', 'przemfdsek@wp.pl', 'ba', '$2a$10$RJvDfYrHs77KAcjm.jae3uoJm.eaV6iuAf6LAzjWxoT7Lb5eQnyCO', 'USER', true);

INSERT INTO products(product_id, product_name, description, price, user_id, available, add_date) VALUES
(1, 'product1', 'blabla', 10.00, 2, true, '2019-03-05'),
(2, 'product2', 'blabla', 15.00, 2, false , '2019-03-05'),
(3, 'product3', 'blabla', 16.00, 3, true , '2019-03-05'),
(4, 'product4', 'blabla', 17.00, 4, true , '2019-03-05'),
(5, 'product5', 'blabla', 15.00, 4, false, '2019-03-05'),
(6, 'product6', 'blabla', 16.00, 4, true, '2019-03-05');

INSERT INTO orders(order_id, address, user_id) VALUES
(1, 'Gdańsk', 3),
(2, 'Gdańsk', 3);




