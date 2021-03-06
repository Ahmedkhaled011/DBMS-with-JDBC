package dbms.sqlparser.syntax;

import java.util.regex.Pattern;

public class WhereSyntax implements SQLSyntax {
    public static final String SUPPORTED_OPERATORS = "(>|<|>=|<=|=|!=){1}";
    public static final String VALUE_FORMAT = "("
            + SyntaxUtil.DATE_FORMAT
            + "|" + SyntaxUtil.NUMBER_FORMAT
            + "|" + SyntaxUtil.MULTIPLE_WORDS_SINGLE_QUOTES
            + "|" + SyntaxUtil.MULTIPLE_WORDS_DOUBLE_QUOTES
            + "|" + SyntaxUtil.COLUMN_NAME + ")";
    private static WhereSyntax instance = null;
    private final String BOOLEAN_OPERATORS = "(and|or)";
    private final String WHERE_REGEX = "(\\s+where\\s+(TRUE|([(]\\s*)*\\s*("
            + SyntaxUtil.COLUMN_NAME + ")\\s*"
            + SUPPORTED_OPERATORS + "\\s*"
            + VALUE_FORMAT + "\\s*(\\s*[)])*\\s*(\\s+"
            + BOOLEAN_OPERATORS + "(\\s*([(]\\s*)*\\s*("
            + SyntaxUtil.COLUMN_NAME + ")\\s*"
            + SUPPORTED_OPERATORS + "\\s*"
            + VALUE_FORMAT + "\\s*(\\s*[)])*\\s*))*))?"
            + SyntaxUtil.SEMI_COLON
            + "$";
    private Pattern wherePattern = null;

    private WhereSyntax() {
    }

    public static WhereSyntax getInstance() {
        if (instance == null) {
            instance = new WhereSyntax();
        }
        return instance;
    }

    public static void main(String[] args) {
        System.out.print(getInstance().getRegex());
    }

    @Override
    public Pattern getPattern() {
        if (wherePattern == null) {
            wherePattern = Pattern.compile(WHERE_REGEX);
        }
        return wherePattern;
    }

    @Override
    public String getRegex() {
        return WHERE_REGEX;
    }
}
