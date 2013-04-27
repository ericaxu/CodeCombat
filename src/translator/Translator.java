package translator;

import java.util.HashMap;

import translator.Parser.Tree;

public class Translator
{   
    Tree parseTree;

    HashMap<String, String> symbolTable;
    HashMap<String, Integer> varStack;
    int varCounter;
    int loopCount = 0;
}
