INSERT INTO users (id, name, email, password, phone, role) VALUES
    (1, 'Ahmad Nasser', 'ahmad.nasser@example.com', '$2a$10$seedpasswordhash000000000000000000000000000000000000000000000', '+966500000001', 'customer'),
    (2, 'Sara Khaled', 'sara.khaled@example.com', '$2a$10$seedpasswordhash000000000000000000000000000000000000000000000', '+966500000002', 'customer'),
    (3, 'Omar Saleh', 'omar.saleh@example.com', '$2a$10$seedpasswordhash000000000000000000000000000000000000000000000', '+966500000003', 'customer'),
    (4, 'Lina Hassan', 'lina.hassan@example.com', '$2a$10$seedpasswordhash000000000000000000000000000000000000000000000', '+966500000004', 'customer'),
    (5, 'Yousef Ali', 'yousef.ali@example.com', '$2a$10$seedpasswordhash000000000000000000000000000000000000000000000', '+966500000005', 'customer'),
    (6, 'Maya Sami', 'maya.sami@example.com', '$2a$10$seedpasswordhash000000000000000000000000000000000000000000000', '+966500000006', 'customer'),
    (7, 'Khaled Omar', 'khaled.omar@example.com', '$2a$10$seedpasswordhash000000000000000000000000000000000000000000000', '+966500000007', 'customer'),
    (8, 'Nora Fahad', 'nora.fahad@example.com', '$2a$10$seedpasswordhash000000000000000000000000000000000000000000000', '+966500000008', 'customer'),
    (9, 'Rami Adel', 'rami.adel@example.com', '$2a$10$seedpasswordhash000000000000000000000000000000000000000000000', '+966500000009', 'customer'),
    (10, 'Admin User', 'admin@example.com', '$2a$10$seedpasswordhash000000000000000000000000000000000000000000000', '+966500000010', 'admin');

INSERT INTO movies (id, title, description, duration, language, age_rating, poster_url, release_date, start_date) VALUES
    (1, 'Inception', 'A thief who steals corporate secrets through the use of dream-sharing technology.', 148, 'English', 'PG_13', 'https://image.tmdb.org/t/p/w600_and_h900_bestv2/oYuLEt3zVCKq57qu2F8dT7NIa6f.jpg', '2010-07-16', '2026-06-01'),
    (2, 'The Godfather', 'The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.', 175, 'English', 'R', 'https://image.tmdb.org/t/p/w600_and_h900_bestv2/3bhkrj58Vtu7enYsRolD1fZdja1.jpg', '1972-03-24', '2026-06-01'),
    (3, 'Parasite', 'Greed and class discrimination threaten the newly formed symbiotic relationship between the wealthy Park family and the destitute Kim clan.', 132, 'Korean', 'R', 'https://image.tmdb.org/t/p/w600_and_h900_bestv2/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg', '2019-05-30', '2026-06-02'),
    (4, 'Spirited Away', 'During her family''s move to the suburbs, a sullen 10-year-old girl wanders into a world ruled by gods, witches, and spirits.', 125, 'Japanese', 'PG', 'https://www.themoviedb.org/t/p/w600_and_h900_face/39wmItIWsg5sZMyRUHLkWBcuVCM.jpg', '2001-07-20', '2026-06-02'),
    (5, 'The Intouchables', 'After he becomes a quadriplegic from a paragliding accident, an aristocrat hires a young man from the projects to be his caregiver.', 112, 'French', 'R', 'https://www.themoviedb.org/t/p/w600_and_h900_face/1QU7HKgsQbGpzsJbJK4pAVQV9F5.jpg', '2011-11-02', '2026-06-03'),
    (6, 'Crouching Tiger, Hidden Dragon', 'A young Chinese warrior steals a sword from a famed swordsman and then escapes into a world of romantic adventure with a mysterious man.', 120, 'Chinese', 'PG_13', 'https://www.themoviedb.org/t/p/w600_and_h900_face/iNDVBFNz4XyYzM9Lwip6atSTFqf.jpg', '2000-12-08', '2026-06-03'),
    (7, 'Capernaum', 'While serving a five-year sentence for a violent crime, a 12-year-old boy sues his parents for neglect.', 126, 'Arabic', 'R', 'https://www.themoviedb.org/t/p/w600_and_h900_face/mFnfTVADj8yOxwzprYOmTPselk8.jpg', '2018-09-20', '2026-06-04'),
    (8, '3 Idiots', 'Two friends are searching for their long lost companion. They revisit their college days and recall the memories of their friend.', 170, 'Hindi', 'PG_13', 'https://www.themoviedb.org/t/p/w600_and_h900_face/66A9MqXOyVFCssoloscw79z8Tew.jpg', '2009-12-25', '2026-06-04'),
    (9, 'Pan''s Labyrinth', 'In the Falangist Spain of 1944, the bookish young stepdaughter of a sadistic army officer escapes into an eerie but captivating fantasy world.', 118, 'Spanish', 'R', 'https://www.themoviedb.org/t/p/w600_and_h900_face/z7xXihu5wHuSMWymq5VAulPVuvg.jpg', '2006-10-11', '2026-06-05'),
    (10, 'Life Is Beautiful', 'When an open-minded Jewish librarian and his son become victims of the Holocaust, he uses a perfect mixture of will, action, and imagination to protect his son.', 116, 'Italian', 'PG_13', 'https://www.themoviedb.org/t/p/w600_and_h900_face/6tEJnof1DKWPnl5lzkjf0FVv7oB.jpg', '1997-12-20', '2026-06-05');


INSERT INTO genres (id, name) VALUES
    (1, 'Drama'),
    (2, 'Science Fiction'),
    (3, 'Comedy'),
    (4, 'Mystery'),
    (5, 'Documentary'),
    (6, 'Action'),
    (7, 'Romance'),
    (8, 'Thriller'),
    (9, 'Fantasy'),
    (10, 'Adventure');

INSERT INTO halls (id, name, total_seats) VALUES
    (1, 'Hall A', 120),
    (2, 'Hall B', 100),
    (3, 'Hall C', 90),
    (4, 'Hall D', 80),
    (5, 'Hall E', 110),
    (6, 'VIP Hall 1', 60),
    (7, 'VIP Hall 2', 55),
    (8, 'IMAX Hall', 180),
    (9, 'Kids Hall', 70),
    (10, 'Premiere Hall', 140);

INSERT INTO seats (id, hall_id, row_label, seat_number, is_vip) VALUES
    -- Hall 1 (5 rows, 8 columns = 40 seats. Row E is VIP)
    (1, 1, 'A', 1, 0), (2, 1, 'A', 2, 0), (3, 1, 'A', 3, 0), (4, 1, 'A', 4, 0), (5, 1, 'A', 5, 0), (6, 1, 'A', 6, 0), (7, 1, 'A', 7, 0), (8, 1, 'A', 8, 0),
    (9, 1, 'B', 1, 0), (10, 1, 'B', 2, 0), (11, 1, 'B', 3, 0), (12, 1, 'B', 4, 0), (13, 1, 'B', 5, 0), (14, 1, 'B', 6, 0), (15, 1, 'B', 7, 0), (16, 1, 'B', 8, 0),
    (17, 1, 'C', 1, 0), (18, 1, 'C', 2, 0), (19, 1, 'C', 3, 0), (20, 1, 'C', 4, 0), (21, 1, 'C', 5, 0), (22, 1, 'C', 6, 0), (23, 1, 'C', 7, 0), (24, 1, 'C', 8, 0),
    (25, 1, 'D', 1, 0), (26, 1, 'D', 2, 0), (27, 1, 'D', 3, 0), (28, 1, 'D', 4, 0), (29, 1, 'D', 5, 0), (30, 1, 'D', 6, 0), (31, 1, 'D', 7, 0), (32, 1, 'D', 8, 0),
    (33, 1, 'E', 1, 1), (34, 1, 'E', 2, 1), (35, 1, 'E', 3, 1), (36, 1, 'E', 4, 1), (37, 1, 'E', 5, 1), (38, 1, 'E', 6, 1), (39, 1, 'E', 7, 1), (40, 1, 'E', 8, 1),

    -- Hall 2 (4 rows, 6 columns = 24 seats. Row D is VIP)
    (41, 2, 'A', 1, 0), (42, 2, 'A', 2, 0), (43, 2, 'A', 3, 0), (44, 2, 'A', 4, 0), (45, 2, 'A', 5, 0), (46, 2, 'A', 6, 0),
    (47, 2, 'B', 1, 0), (48, 2, 'B', 2, 0), (49, 2, 'B', 3, 0), (50, 2, 'B', 4, 0), (51, 2, 'B', 5, 0), (52, 2, 'B', 6, 0),
    (53, 2, 'C', 1, 0), (54, 2, 'C', 2, 0), (55, 2, 'C', 3, 0), (56, 2, 'C', 4, 0), (57, 2, 'C', 5, 0), (58, 2, 'C', 6, 0),
    (59, 2, 'D', 1, 1), (60, 2, 'D', 2, 1), (61, 2, 'D', 3, 1), (62, 2, 'D', 4, 1), (63, 2, 'D', 5, 1), (64, 2, 'D', 6, 1),

    -- Generic small grids for remaining Halls to satisfy existing bookings (Rows A-B, 2 seats)
    (71, 3, 'A', 1, 0), (72, 3, 'A', 2, 0), (73, 3, 'B', 1, 1), (74, 3, 'B', 2, 1),
    (81, 4, 'A', 1, 0), (82, 4, 'A', 2, 0), (83, 4, 'B', 1, 1), (84, 4, 'B', 2, 1),
    (91, 5, 'A', 1, 0), (92, 5, 'A', 2, 0), (93, 5, 'B', 1, 1), (94, 5, 'B', 2, 1),
    (101, 6, 'A', 1, 1), (102, 6, 'A', 2, 1), (103, 6, 'B', 1, 1), (104, 6, 'B', 2, 1),
    (111, 7, 'A', 1, 1), (112, 7, 'A', 2, 1), (113, 7, 'B', 1, 1), (114, 7, 'B', 2, 1),
    (121, 8, 'A', 1, 0), (122, 8, 'A', 2, 0), (123, 8, 'B', 1, 1), (124, 8, 'B', 2, 1),
    (131, 9, 'A', 1, 0), (132, 9, 'A', 2, 0), (133, 9, 'B', 1, 0), (134, 9, 'B', 2, 0),
    (141, 10, 'A', 1, 0), (142, 10, 'A', 2, 0), (143, 10, 'B', 1, 1), (144, 10, 'B', 2, 1);

INSERT INTO movie_genres (id, movie_id, genre_id) VALUES
    (1, 1, 2),
    (2, 2, 1),
    (3, 3, 8),
    (4, 4, 9),
    (5, 5, 3),
    (6, 6, 6),
    (7, 7, 1),
    (8, 8, 3),
    (9, 9, 9),
    (10, 10, 1);

INSERT INTO shows (id, movie_id, hall_id, start_time, end_time, base_price, vip_price) VALUES
    (1, 1, 1, '2026-06-01 18:00:00', '2026-06-01 20:00:00', 45.00, 75.00),
    (2, 2, 2, '2026-06-01 19:00:00', '2026-06-01 21:20:00', 55.00, 85.00),
    (3, 3, 3, '2026-06-02 16:00:00', '2026-06-02 17:50:00', 35.00, 60.00),
    (4, 4, 4, '2026-06-02 20:00:00', '2026-06-02 22:10:00', 50.00, 80.00),
    (5, 5, 5, '2026-06-03 18:30:00', '2026-06-03 20:30:00', 45.00, 70.00),
    (6, 6, 6, '2026-06-03 21:00:00', '2026-06-03 23:30:00', 65.00, 100.00),
    (7, 7, 7, '2026-06-04 17:30:00', '2026-06-04 19:25:00', 40.00, 70.00),
    (8, 8, 8, '2026-06-04 20:30:00', '2026-06-04 22:40:00', 60.00, 95.00),
    (9, 9, 9, '2026-06-05 15:00:00', '2026-06-05 17:15:00', 38.00, 65.00),
    (10, 10, 10, '2026-06-05 22:00:00', '2026-06-05 23:55:00', 58.00, 90.00);

INSERT INTO bookings (id, user_id, show_id, booking_reference, status, total_amount, expires_at, payment_due_at) VALUES
    (1, 1, 1, 'BK-2026-0001', 'CONFIRMED', 45.00, '2026-06-01 17:30:00', '2026-06-01 17:30:00'),
    (2, 2, 2, 'BK-2026-0002', 'CONFIRMED', 55.00, '2026-06-01 18:30:00', '2026-06-01 18:30:00'),
    (3, 3, 3, 'BK-2026-0003', 'PENDING', 35.00, '2026-06-02 15:30:00', '2026-06-02 15:30:00'),
    (4, 4, 4, 'BK-2026-0004', 'CONFIRMED', 50.00, '2026-06-02 19:30:00', '2026-06-02 19:30:00'),
    (5, 5, 5, 'BK-2026-0005', 'CANCELLED', 45.00, '2026-06-03 18:00:00', '2026-06-03 18:00:00'),
    (6, 6, 6, 'BK-2026-0006', 'CONFIRMED', 100.00, '2026-06-03 20:30:00', '2026-06-03 20:30:00'),
    (7, 7, 7, 'BK-2026-0007', 'PENDING', 70.00, '2026-06-04 17:00:00', '2026-06-04 17:00:00'),
    (8, 8, 8, 'BK-2026-0008', 'CONFIRMED', 95.00, '2026-06-04 20:00:00', '2026-06-04 20:00:00'),
    (9, 9, 9, 'BK-2026-0009', 'CANCELLED', 38.00, '2026-06-05 14:30:00', '2026-06-05 14:30:00'),
    (10, 10, 10, 'BK-2026-0010', 'CONFIRMED', 90.00, '2026-06-05 21:30:00', '2026-06-05 21:30:00');

INSERT INTO booking_seats (id, booking_id, show_id, seat_id, price) VALUES
    (1, 1, 1, 1, 45.00),      -- Hall 1, Seat 1
    (2, 2, 2, 41, 55.00),     -- Hall 2, Seat 41
    (3, 3, 3, 71, 35.00),     -- Hall 3, Seat 71
    (4, 4, 4, 81, 50.00),     -- Hall 4, Seat 81
    (5, 5, 5, 91, 45.00),     -- Hall 5, Seat 91
    (6, 6, 6, 101, 100.00),   -- Hall 6, Seat 101
    (7, 7, 7, 111, 70.00),    -- Hall 7, Seat 111
    (8, 8, 8, 121, 95.00),    -- Hall 8, Seat 121
    (9, 9, 9, 131, 38.00),    -- Hall 9, Seat 131
    (10, 10, 10, 141, 90.00); -- Hall 10, Seat 141

INSERT INTO comments (id, movie_id, user_id, comment_text) VALUES
    (1, 1, 1, 'Mind-bending concept and stunning visuals.'),
    (2, 2, 2, 'An absolute masterpiece of cinema.'),
    (3, 3, 3, 'Brilliant social commentary and so tense!'),
    (4, 4, 4, 'Magical and beautifully animated.'),
    (5, 5, 5, 'Heartwarming and hilarious.'),
    (6, 6, 6, 'The choreography is pure art.'),
    (7, 7, 7, 'Incredibly moving and heartbreaking.'),
    (8, 8, 8, 'A perfect mix of comedy and life lessons.'),
    (9, 9, 9, 'Dark, beautiful, and haunting.'),
    (10, 10, 10, 'A beautiful story of a father''s love.');

INSERT INTO payments (id, booking_id, amount, payment_method, payment_status, transaction_reference) VALUES
    (1, 1, 45.00, 'card', 'SUCCESS', 'TXN-2026-0001'),
    (2, 2, 55.00, 'card', 'SUCCESS', 'TXN-2026-0002'),
    (3, 3, 35.00, 'cash', 'PENDING', 'TXN-2026-0003'),
    (4, 4, 50.00, 'card', 'SUCCESS', 'TXN-2026-0004'),
    (5, 5, 45.00, 'wallet', 'FAILED', 'TXN-2026-0005'),
    (6, 6, 100.00, 'card', 'SUCCESS', 'TXN-2026-0006'),
    (7, 7, 70.00, 'wallet', 'PENDING', 'TXN-2026-0007'),
    (8, 8, 95.00, 'card', 'SUCCESS', 'TXN-2026-0008'),
    (9, 9, 38.00, 'cash', 'FAILED', 'TXN-2026-0009'),
    (10, 10, 90.00, 'card', 'SUCCESS', 'TXN-2026-0010');
