CREATE TABLE card_data (
	id SERIAL PRIMARY KEY,
	card_number VARCHAR(16) UNIQUE,
	card_valid_till VARCHAR(7),
	card_cvv INTEGER
)