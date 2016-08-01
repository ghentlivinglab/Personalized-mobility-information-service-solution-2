DELETE FROM eventtype;
INSERT INTO eventtype(type, relevant_transportation_types) VALUES ('ACCIDENT', '{"car", "bus"}');
INSERT INTO eventtype(type, relevant_transportation_types) VALUES ('JAM', '{"car", "bus"}');
INSERT INTO eventtype(type, relevant_transportation_types) VALUES ('WEATHERHAZARD', '{"car", "bus", "streetcar", "train", "bike"}');
INSERT INTO eventtype(type, relevant_transportation_types) VALUES ('HAZARD', '{"car", "bus", "train", "streetcar", "bike"}');
INSERT INTO eventtype(type, relevant_transportation_types) VALUES ('MISC', '{"car", "bus", "train", "streetcar", "bike"}');
INSERT INTO eventtype(type, relevant_transportation_types) VALUES ('CONSTRUCTION', '{"car", "bus", "streetcar", "bike"}');
INSERT INTO eventtype(type, relevant_transportation_types) VALUES ('ROAD_CLOSED', '{"car", "bus", "streetcar", "bike"}');