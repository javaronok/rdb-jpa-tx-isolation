package com.redsoft.gda.liquibase.rdb;

import liquibase.database.DatabaseConnection;
import liquibase.database.core.FirebirdDatabase;
import liquibase.exception.DatabaseException;

public class RedDatabase extends FirebirdDatabase {
  @Override
  public boolean isCorrectDatabaseImplementation(DatabaseConnection conn) throws DatabaseException {
    return conn.getDatabaseProductName().startsWith("Red Database");
  }

  @Override
  public String getShortName() {
    return "RedDatabase";
  }
}
