INSERT INTO destination (name, location, description, average_rating, total_ratings)
VALUES ('Arraial do Cabo', 'Rio de Janeiro, Brasil',
        'Conhecido como o Caribe brasileiro, com águas cristalinas e praias paradisíacas.', 9.2, 150),
       ('Chapada Diamantina', 'Bahia, Brasil',
        'Parque nacional com cachoeiras, grutas e trilhas deslumbrantes no sertão baiano.', 8.8, 98),
       ('Florianópolis', 'Santa Catarina, Brasil',
        'Ilha da Magia com mais de 40 praias, lagoas e rica cultura açoriana.', 8.5, 210),
       ('Lençóis Maranhenses', 'Maranhão, Brasil',
        'Deserto de dunas brancas com lagoas de água doce azul-turquesa únicas no mundo.', 9.5, 175),
       ('Bonito', 'Mato Grosso do Sul, Brasil',
        'Ecoturismo de alto nível com rios de água cristalina e rica biodiversidade.', 9.1, 132);

INSERT INTO travel_package (name, description, price, destination_id)
VALUES ('Arraial Completo', 'Passeio de barco, mergulho e praias em 5 dias.', 2500.00, 1),
       ('Chapada Aventura', 'Trilhas, cachoeiras e grutas em 7 dias com guia especializado.', 3200.00, 2),
       ('Floripa em Família', 'Roteiro pelas principais praias e pontos turísticos em 6 dias.', 2800.00, 3),
       ('Lençóis Inesquecível', 'Expedição pelas dunas e lagoas em 5 dias.', 3800.00, 4),
       ('Bonito Selvagem', 'Flutuação, rapel e trilhas em 4 dias no coração do Pantanal.', 2900.00, 5);
