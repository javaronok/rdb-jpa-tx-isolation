package com.redsoft.gda.liquibase;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;

import java.sql.Connection;
import java.sql.SQLException;

public class LiquibaseUpdater extends SpringLiquibase {

  public LiquibaseUpdater() {
    super();
    setShouldRun(false);
  }

  public void update() throws LiquibaseException {
    Connection c;
    Liquibase liquibase = null;
    try {
      c = getDataSource().getConnection();
      liquibase = createLiquibase(c);
      performUpdate(liquibase);
    } catch (SQLException e) {
      throw new DatabaseException(e);
    } finally {
      Database database = null;
      if (liquibase != null) {
        database = liquibase.getDatabase();
      }
      if (database != null) {
        database.close();
      }
    }
  }

  public void rollback() throws LiquibaseException {
    Connection c;
    Liquibase liquibase = null;
    try {
      c = getDataSource().getConnection();
      liquibase = createLiquibase(c);
      liquibase.rollback(1, getContexts());
    } catch (SQLException e) {
      throw new DatabaseException(e);
    } finally {
      Database database = null;
      if (liquibase != null) {
        database = liquibase.getDatabase();
      }
      if (database != null) {
        database.close();
      }
    }
  }
}
