package org.postgresql.core.v3;

import java.util.Arrays;

public class ProcessCompositeQuery {
  public static String[] processKey(String key) {
    String[] listOfQueries = key.split(";");
    return Arrays.stream(listOfQueries).map(s -> s + ";").toArray(String[]::new);
  }

  public void processStatementName() {


  }
}
