package org.postgresql.core.v3;

import org.postgresql.core.Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

  public class ProcessCompositeQuery {
    public static String[] processKey(String sql) {
      if (sql == null || sql.isEmpty()) {
        return new String[0];
      }
      char[] aChars = sql.toCharArray();
      List<String> statements = new ArrayList<>();
      StringBuilder sb = new StringBuilder(sql.length());
      boolean standardConformingStrings = true;

      for (int i = 0; i < aChars.length; i++) {
        char c = aChars[i];

        if (c == '\'') {
          int end = Parser.parseSingleQuotes(aChars, i, standardConformingStrings);
          sb.append(aChars, i, end - i + 1);
          i = end;

        } else if (c == '"') {
          int end = Parser.parseDoubleQuotes(aChars, i);
          sb.append(aChars, i, end - i + 1);
          i = end;

        } else if (c == '-' && i + 1 < aChars.length && aChars[i + 1] == '-') {
          int end = Parser.parseLineComment(aChars, i);
          sb.append(aChars, i, end - i + 1);
          i = end;

        } else if (c == '/' && i + 1 < aChars.length && aChars[i + 1] == '*') {
          int end = Parser.parseBlockComment(aChars, i);
          sb.append(aChars, i, end - i + 1);
          i = end;

        } else if (c == '$') {
          int end = Parser.parseDollarQuotes(aChars, i);
          sb.append(aChars, i, end - i + 1);
          i = end;

        } else if (c == ';') {
          sb.append(c);
          String stmt = sb.toString().trim();
          if (!stmt.isEmpty()) {
            statements.add(stmt);
          }
          sb.setLength(0);

        } else {
          sb.append(c);
        }
      }

      String tail = sb.toString().trim();
      if (!tail.isEmpty()) {
        statements.add(tail);
      }

      return statements.toArray(new String[0]);
    }
  }

