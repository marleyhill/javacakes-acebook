require 'pg'

def setup_test_database
  connection = PG.connect(dbname: 'acebook_test')
  connection.exec("TRUNCATE TABLE users CASCADE;")
end
