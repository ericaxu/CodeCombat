package translator;

import java.util.ArrayList;

public class Parser
{   
    class Tree {
          public String rule;
          public ArrayList<String> tokens;
          public ArrayList<Tree> children;
      };
      
      private final String[] terminals = {
              "BOF", "BECOMES", "COMMA", "ELSE", "EOF", "EQ", "GE", "GT", "ID",
              "IF", "INT", "LBRACE", "LE", "LPAREN", "LT", "MINUS", "NE", "NUM",
              "PCT", "PLUS", "PRINTLN", "RBRACE", "RETURN", "RPAREN", "SEMI",
              "SLASH", "STAR", "WAIN", "WHILE", "AMP", "LBRACK", "RBRACK", "NEW",
              "DELETE", "NULL"
            };
      
      private boolean isTerminal(String sym) {
          int idx;
          for(String str : terminals)
            if(str.equals(sym)) return true;
          return false;
        }
      
      Tree readParse(String lhs) {
          // Read a line from standard input.
          String line;
          getline(cin, line);
          if(cin.fail())
            bail("ERROR: Unexpected end of file.");
          Tree ret = new Tree();
          // Tokenize the line.
          stringstream ss;
          ss << line;
          while(!ss.eof()) {
            string token;
            ss >> token;
            if(token == "") continue;
            ret.tokens.add(token);
          }
          // Ensure that the rule is separated by single spaces.
          for(int idx=0; idx<ret.tokens.size(); idx++) {
            if(idx > 0) ret.rule += " ";
            ret.rule += ret.tokens.get(idx);
          }
          // Recurse if lhs is a nonterminal.
          if(!isTerminal(lhs)) {
            // Skip the lhs
            for(int idx=1; idx<ret.tokens.size(); idx++) {
              ret.children.add(readParse(ret.tokens.get(idx)));
            }
          }
          return ret;
        }
}
