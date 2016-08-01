-- password = Adminpas1
insert into account (email, first_name, salt, password, email_validated, is_admin)
values (
  'admin@mobiligent.be',
  'admin',
  E'\\x72135e5070f218ece742bae60a088b6ee2eb290fd5370178322ed8f5b88e56d1',
  '169d1d5e10688fb0962366012fbf2fcbfe4c9e950d7ec851ba0dcc736381893e',
  true,
  true
);
-- password = Operatorpas1
insert into account (email, first_name, salt, password, email_validated, is_operator)
values (
  'operator@mobiligent.be',
  'operator',
  E'\\x5b605830da1f86e4ce7d7846da660c09ce5b12695835a722a2fbac29ba1c30ae',
  '4b8bbb9b1c681306bbf53bc063ad874f0f9e1e0c2e925a93c4cee91b1f7c3cd3',
  true,
  true
);
