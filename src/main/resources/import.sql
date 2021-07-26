INSERT INTO sensor (id, mac, name) VALUES
(1, 'A0:B0:C0:D0:E0:F0', 'Tomato'),
(2, 'A1:B1:C1:D1:E1:F1', 'Potato'),
(3, 'A2:B2:C2:D2:E2:F2', 'Beans'),
(4, 'A3:B3:C3:D3:E3:F3', 'Strawberry'),
(5, 'A4:B4:C4:D4:E4:F4', 'Onion'),
(6, 'A5:B5:C5:D5:E5:F5', 'Garlic'),
(7, 'A6:B6:C6:D6:E6:F6', 'Salad'),
(8, 'A7:B7:C7:D7:E7:F7', 'Carrot'),
(9, 'A8:B8:C8:D8:E8:F8', 'Cabagge'),
(10, 'A9:B9:C9:D9:E9:F9', 'Rhubarb');

INSERT INTO current (moisture, updated, mac_id) VALUES
(0, '1999-01-01 12:00:00.000000', 1),
(0, '1999-01-01 12:00:00.000000', 2),
(0, '1999-01-01 12:00:00.000000', 3),
(0, '1999-01-01 12:00:00.000000', 4),
(0, '1999-01-01 12:00:00.000000', 5),
(0, '1999-01-01 12:00:00.000000', 6),
(0, '1999-01-01 12:00:00.000000', 7),
(0, '1999-01-01 12:00:00.000000', 8),
(0, '1999-01-01 12:00:00.000000', 9),
(0, '1999-01-01 12:00:00.000000', 10);

INSERT INTO avg_hour (avg_moisture, created, mac_id) VALUES
(10, '2021-07-27 10:00:00.000000', 1),
(12, '2021-07-27 11:00:00.000000', 1),
(20, '2021-07-27 10:00:00.000000', 2),
(21, '2021-07-27 11:00:00.000000', 2);
