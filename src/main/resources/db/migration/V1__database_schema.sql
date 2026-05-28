CREATE DATABASE IF NOT EXISTS cinema_booking_system;

CREATE TABLE users (
    id bigint unsigned NOT NULL AUTO_INCREMENT,
    name varchar(100) NOT NULL,
    email varchar(150) NOT NULL,
    password varchar(255) NOT NULL,
    phone varchar(30) DEFAULT NULL,
    role enum('customer', 'admin') NOT NULL DEFAULT 'customer',
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_users_email (email),
    UNIQUE KEY uk_users_phone (phone)
);

CREATE TABLE movies (
    id bigint unsigned NOT NULL AUTO_INCREMENT,
    title varchar(200) NOT NULL,
    description text,
    duration int unsigned NOT NULL DEFAULT 120,
    language enum('English', 'Arabic', 'French', 'Spanish', 'German', 'Italian', 'Hindi', 'Japanese', 'Korean', 'Chinese', 'Turkish', 'Russian', 'Portuguese') NOT NULL,
    age_rating enum('G', 'PG', 'PG_13', 'R', 'NC_17') NOT NULL,
    poster_url varchar(500) DEFAULT NULL,
    release_date date NOT NULL,
    start_date date DEFAULT NULL,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT chk_movies_duration CHECK (duration > 0)
);

CREATE TABLE genres (
    id bigint unsigned NOT NULL AUTO_INCREMENT,
    name varchar(100) NOT NULL,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_genres_name (name)
);

CREATE TABLE halls (
    id bigint unsigned NOT NULL AUTO_INCREMENT,
    name varchar(100) NOT NULL,
    total_seats int unsigned NOT NULL DEFAULT 100,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_halls_name (name),
    CONSTRAINT chk_halls_total_seats CHECK (total_seats >= 0)
);

CREATE TABLE seats (
    id bigint unsigned NOT NULL AUTO_INCREMENT,
    hall_id bigint unsigned NOT NULL,
    row_label enum('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K') NOT NULL,
    seat_number int unsigned NOT NULL,
    is_vip tinyint(1) NOT NULL DEFAULT 0,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_seats_hall_row_number (hall_id, row_label, seat_number),
    CONSTRAINT fk_seats_hall FOREIGN KEY (hall_id) REFERENCES halls (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT chk_seats_is_vip CHECK (is_vip in (0, 1))
);

CREATE TABLE movie_genres (
    id bigint unsigned NOT NULL AUTO_INCREMENT,
    movie_id bigint unsigned NOT NULL,
    genre_id bigint unsigned NOT NULL,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_movie_genres_movie_genre (movie_id, genre_id),
    CONSTRAINT fk_movie_genres_movie FOREIGN KEY (movie_id) REFERENCES movies (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_movie_genres_genre FOREIGN KEY (genre_id) REFERENCES genres (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE shows (
    id bigint unsigned NOT NULL AUTO_INCREMENT,
    movie_id bigint unsigned NOT NULL,
    hall_id bigint unsigned NOT NULL,
    start_time datetime NOT NULL,
    end_time datetime NOT NULL,
    base_price decimal(10, 2) NOT NULL DEFAULT 50.00,
    vip_price decimal(10, 2) NOT NULL DEFAULT 20.00,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_shows_hall_start_time (hall_id, start_time),
    CONSTRAINT fk_shows_hall FOREIGN KEY (hall_id) REFERENCES halls (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_shows_movie FOREIGN KEY (movie_id) REFERENCES movies (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT chk_shows_base_price CHECK (base_price >= 0),
    CONSTRAINT chk_shows_vip_price CHECK (vip_price >= 0),
    CONSTRAINT chk_shows_time CHECK (end_time > start_time)
);

CREATE TABLE bookings (
    id bigint unsigned NOT NULL AUTO_INCREMENT,
    user_id bigint unsigned NOT NULL,
    show_id bigint unsigned NOT NULL,
    booking_reference varchar(50) NOT NULL DEFAULT '',
    status enum('PENDING', 'CONFIRMED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    total_amount decimal(10, 2) NOT NULL DEFAULT 0.00,
    expires_at datetime DEFAULT NULL,
    payment_due_at datetime DEFAULT NULL,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_bookings_reference (booking_reference),
    UNIQUE KEY uk_bookings_id_show (id, show_id),
    KEY idx_bookings_status_payment_due_at (status, payment_due_at),
    CONSTRAINT fk_bookings_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_bookings_show FOREIGN KEY (show_id) REFERENCES shows (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT chk_bookings_total_amount CHECK (total_amount >= 0)
);

CREATE TABLE booking_seats (
    id bigint unsigned NOT NULL AUTO_INCREMENT,
    booking_id bigint unsigned NOT NULL,
    show_id bigint unsigned NOT NULL,
    seat_id bigint unsigned NOT NULL,
    price decimal(10, 2) NOT NULL DEFAULT 0.00,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_booking_seats_booking_seat (booking_id, seat_id),
    UNIQUE KEY uk_booking_seats_show_seat (show_id, seat_id),
    CONSTRAINT fk_booking_seats_booking_show FOREIGN KEY (booking_id, show_id) REFERENCES bookings (id, show_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_booking_seats_seat FOREIGN KEY (seat_id) REFERENCES seats (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT chk_booking_seats_price CHECK (price >= 0)
);

CREATE TABLE comments (
    id bigint unsigned NOT NULL AUTO_INCREMENT,
    movie_id bigint unsigned NOT NULL,
    user_id bigint unsigned NOT NULL,
    comment_text text NOT NULL,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_comments_movie FOREIGN KEY (movie_id) REFERENCES movies (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_comments_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE payments (
    id bigint unsigned NOT NULL AUTO_INCREMENT,
    booking_id bigint unsigned NOT NULL,
    amount decimal(10, 2) NOT NULL DEFAULT 0.00,
    payment_method varchar(50) NOT NULL,
    payment_status enum('PENDING', 'SUCCESS', 'FAILED', 'EXPIRED') NOT NULL DEFAULT 'PENDING',
    transaction_reference varchar(150) DEFAULT NULL,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_payments_booking (booking_id),
    UNIQUE KEY uk_payments_transaction_reference (transaction_reference),
    CONSTRAINT fk_payments_booking FOREIGN KEY (booking_id) REFERENCES bookings (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT chk_payments_amount CHECK (amount >= 0)
);

CREATE TABLE refresh_tokens (
    id bigint unsigned NOT NULL AUTO_INCREMENT,
    token_hash varchar(128) NOT NULL,
    user_id bigint unsigned NOT NULL,
    expires_at datetime NOT NULL,
    revoked_at datetime DEFAULT NULL,
    replaced_by_token_hash varchar(128) DEFAULT NULL,
    created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_refresh_tokens_token_hash (token_hash),
    KEY idx_refresh_tokens_user_id (user_id),
    CONSTRAINT fk_refresh_tokens_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);
