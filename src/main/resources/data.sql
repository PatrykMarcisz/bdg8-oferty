-- inicjalizacja ról w tabelce role
REPLACE INTO role(role_id, role_name) VALUES (1, "ROLE_USER");
REPLACE INTO role(role_id, role_name) VALUES (2, "ROLE_COMPANY");
REPLACE INTO role(role_id, role_name) VALUES (3, "ROLE_ADMIN");

-- inicjalizacja kategorii w tabelce category
REPLACE INTO category(category_id, category_name) VALUES (1, "DOKUMENTACJA URZĘDOWA");
REPLACE INTO category(category_id, category_name) VALUES (2, "FAKTURY");