INSERT INTO users (username, email, password_hash) VALUES
('programmer2007', 'prog@google.com', 'jRSDf7hyNnZrxvfR4gGfdMj8U16DxuHjgGLWGJODnG4='),   -- prog@google.com / Aa1234
('tester', 'test@goooogle.com', 'xmnrzBCtL9wItfaI0GOPB5BQQNSeP7/cSYd1s6LqZ/Q='),         -- test@goooogle.com / TestPassword1
('zxc', 'zxc@ccc.com', 'xmnrzBCtL9wItfaI0GOPB5BQQNSeP7/cSYd1s6LqZ/Q='),                  -- zxc@ccc.com / TestPassword1
('good_student', 'i@love.miit', 'JsNrrnr9rukOgLeEJ7gAGr+9YoCMf7MHF64cz2mLfus='),         -- i@love.miit / RUTisTheBest2026
('joker', 'lolkek3000@yanderex.ru', 'qHiOCVoQqfq3mH6e0lwhtBD06vHiM7kLDAtz1X6V/10=');     -- lolkek3000@yanderex.ru / K_qwerty777



INSERT INTO hubs (creator_id, hubname, description) VALUES
(1, 'AI&ML', 'Все об искусственном интеллекте и машинном обучении'),
(1, 'CS', 'Информатика как наука'),
(1, 'WebDev', 'Хаб о веб-разработке (Frontend & Backend)'),
(2, 'CV', 'Компьютерное зрение'),
(2, 'HR', 'Рекрутинг в IT');



INSERT INTO posts (author_id, hub_id, label, content) VALUES
(1, 1, 'Релиз ChatttGPT 2.0', 'Вышла новая модель ChatttGPT 2.0! Количество параметров очень большое.'),
(2, 2, 'Автоматы Мура и Мили', 'Синтез структурных (конечных) автоматов Мура и Мили выполняется пошагово.'),
(3, 3, 'Vuuue.js vs Re&Act.js', 'Какая библиотека лучше для фронта? Выбор зависит от целей.'),
(2, 4, 'Об HSV', 'HSV - цветовая модель, которая описывает цвет через три параметра: тон, насыщенность, яркость.'),
(1, 5, 'IT - ВСЁ!', 'Количество вакансий в IT с зарплатой 300к/нс снизилось на 0.01%.'),
(1, 3, 'Вышел Kafkoo 4.4', 'Вышел релиз Kafkoo 4.4, который перевел брокеры сообщений на качественно новый уровень!');


INSERT INTO votes (post_id, voter_id, vote) VALUES
(1, 2, true),
(2, 1, true),
(2, 3, true),
(2, 5, true),
(2, 4, false);


INSERT INTO sessions (auth_token, user_id) VALUES
('bmmgNwnb9W/yDf7TmhigM6ujwchXYU5trykyAfngDX4=', 1),
('AbLPLVb/+ltmgc2kTsODyZlkqNYu6w51dpjtxQcByb8=', 2),
('IS4ojkmdnT7pNhlIpl8xCREdTv5LmnLNJobAMRIUkkY=', 2),
('jgHehPDqSstcvVuqXXc83ZAS69PDRcBKjc5ywcTDY1w=', 3),
('nmFOZekYlbJINVEK+IUcoWuzuEjOhoRz9xr1CqTc0Qo=', 4);
