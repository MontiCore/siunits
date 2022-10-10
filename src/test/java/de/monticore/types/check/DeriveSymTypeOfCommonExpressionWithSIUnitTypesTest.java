/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.expressions.combineexpressionswithliterals.CombineExpressionsWithLiteralsMill;
import de.monticore.expressions.combineexpressionswithliterals._parser.CombineExpressionsWithLiteralsParser;
import de.monticore.expressions.combineexpressionswithsiunitliterals.CombineExpressionsWithSIUnitLiteralsMill;
import de.monticore.expressions.combineexpressionswithsiunitliterals._parser.CombineExpressionsWithSIUnitLiteralsParser;
import de.monticore.expressions.combineexpressionswithsiunitliterals._symboltable.ICombineExpressionsWithSIUnitLiteralsScope;
import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.expressions.expressionsbasis._visitor.ExpressionsBasisTraverser;
import de.monticore.symbols.basicsymbols.BasicSymbolsMill;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import static de.monticore.types.check.DefsTypeBasic.add2scope;
import static de.monticore.types.check.DefsTypeBasic.field;

public class DeriveSymTypeOfCommonExpressionWithSIUnitTypesTest extends DeriveSymTypeAbstractTest {

    /**
     * Focus: Deriving Type of SIUnitLiterals, here:
     * lang/literals/SIUnitLiterals.mc4
     */

    @Override
    public void setupTypeCheck() {
        // This is an auxiliary
        AbstractDerive derLit = new DeriveSymTypeOfCombineExpressionsWithSIUnitTypesDelegator();

        // other arguments not used (and therefore deliberately null)
        setTypeCheck(new TypeCalculator(null, derLit));
    }

    CombineExpressionsWithSIUnitLiteralsParser p = new CombineExpressionsWithSIUnitLiteralsParser();
    @Override
    protected Optional<ASTExpression> parseStringExpression(String expression) throws IOException {
        return p.parse_StringExpression(expression);
    }

    @Override
    protected ExpressionsBasisTraverser getUsedLanguageTraverser() {
        return CombineExpressionsWithSIUnitLiteralsMill.traverser();
    }

    /*--------------------------------------------------- TESTS ---------------------------------------------------------*/

    /**
     * test correctness of addition
     */
    @Test
    public void deriveFromPlusExpression() throws IOException {
        // example with siunit
        check("4.1m/h + 12m/s", "(double,m/h)");
    }

    @Test
    public void testInvalidPlusExpression1() throws IOException {
        checkError("4.1km/h + 12m", "0xA0168");
    }

    @Test
    public void testInvalidPlusExpression2() throws IOException {
        checkError("4.1km/h + 12", "0xA0168");
    }

    @Test
    public void testInvalidPlusExpression3() throws IOException {
        checkError("varM + 2 m", "0xA0168");
    }

    /**
     * test correctness of subtraction
     */
    @Test
    public void deriveFromMinusExpression() throws IOException {
        // example with siunit
        check("4.1km/h - 12m/s", "(double,km/h)");
    }

    @Test
    public void testInvalidMinusExpression1() throws IOException {
        checkError("4.1km/h - 12m", "0xA0168");
    }

    @Test
    public void testInvalidMinusExpression2() throws IOException {
        checkError("4.1km/h - 12", "0xA0168");
    }

    @Test
    public void testInvalidMinusExpression3() throws IOException {
        checkError("varM - 2 m", "0xA0168");
    }

    /**
     * test correctness of multiplication
     */
    @Test
    public void deriveFromMultExpression() throws IOException {
        // example with siunit
        check("4.1km * 12s", "(double,km*s)");

        // example with siunit
        check("4.1km * 12.3", "(double,km)");

        // example with siunit
        check("4.1km * 12.3km^-1", "double");

        // example with siunit
        check("varM * varS", "m*s");

        // example with siunit
        check("varI_M * varS", "(int,m*s)");

        // example with siunit
        check("varM * 3.2", "(double,m)");
    }

    @Test
    public void testInvalidMultExpression() throws IOException {
        checkError("3 m*true", "0xA0168");
    }

    /**
     * test correctness of division
     */
    @Test
    public void deriveFromDivideExpression() throws IOException {
        // example with siunit
        check("4.1km / 12s", "(double,km/s)");

        // example with siunit
        check("4.1km / 12.3", "(double,km)");

        check("4km / 12 m", "(int,km/m)");

        // example with siunit
        check("varM / varM", "int");

        // example with siunit
        check("3 m/varM", "int");

        // example with siunit
        check("varM / varS", "m/s");

        // example with siunit
        check("3.2 / varS", "(double,1/s)");
    }

    @Test
    public void testInvalidDivideExpression() throws IOException {
        checkError("3 m/true", "0xA0168");
    }

    /**
     * tests correctness of modulo
     */
    @Test
    public void deriveFromModuloExpression() throws IOException {
        //example with two ints
        check("3 m%2m", "(int,m)");
    }

    @Test
    public void testInvalidModuloExpression() throws IOException {
        checkError("3 m%2", "0xA0168");
    }

    /**
     * test LessEqualExpression
     */
    @Test
    public void deriveFromLessEqualExpression() throws IOException {
        // example with siunit
        check("4.1km <= 12m", "boolean");
    }

    @Test
    public void testInvalidLessEqualExpression1() throws IOException {
        checkError("4.1km <= 12s", "0xA0167");
    }

    @Test
    public void testInvalidLessEqualExpression2() throws IOException {
        checkError("varS <= varM", "0xA0167");
    }

    /**
     * test GreaterEqualExpression
     */
    @Test
    public void deriveFromGreaterEqualExpression() throws IOException {
        // example with siunit
        check("4.1km >= 12m", "boolean");
    }

    @Test
    public void testInvalidGreaterEqualExpression1() throws IOException {
        checkError("4.1km >= 12s", "0xA0167");
    }

    @Test
    public void testInvalidGreaterEqualExpression2() throws IOException {
        checkError("varM >= 3 m", "0xA0167");
    }

    /**
     * test LessThanExpression
     */
    @Test
    public void deriveFromLessThanExpression() throws IOException {
        // example with siunit
        check("4.1km < 12m", "boolean");
    }

    @Test
    public void testInvalidLessThanExpression1() throws IOException {
        checkError("4.1km < 12s", "0xA0167");
    }

    @Test
    public void testInvalidLessThanExpression2() throws IOException {
        checkError("varS < 12s", "0xA0167");
    }

    /**
     * test GreaterThanExpression
     */
    @Test
    public void deriveFromGreaterThanExpression() throws IOException {
        // example with siunit
        check("4.1km > 12m", "boolean");
    }

    @Test
    public void testInvalidGreaterThanExpression1() throws IOException {
        checkError("4.1km > 12s", "0xA0167");
    }

    @Test
    public void testInvalidGreaterThanExpression2() throws IOException {
        checkError("4.1km > varM", "0xA0167");
    }

    private ICombineExpressionsWithSIUnitLiteralsScope scope;

    /**
     * initialize basic scope and a few symbols for testing
     */
    @Before
    public void init_basic() {
        scope = CombineExpressionsWithSIUnitLiteralsMill.scope();
        scope.setEnclosingScope(null);     // No enclosing Scope: Search ending here
        scope.setExportingSymbols(true);
        scope.setAstNode(null);
        BasicSymbolsMill.reset();
        BasicSymbolsMill.initializePrimitives();

        // SIUnits
        SymTypeExpression s = SIUnitSymTypeExpressionFactory.createSIUnit("s", scope);
        SymTypeExpression m = SIUnitSymTypeExpressionFactory.createSIUnit("m", scope);
        SymTypeExpression kMe2perH = SIUnitSymTypeExpressionFactory.createSIUnit("km^2/h", scope);
        SymTypeExpression kMe2perHMSe4 = SIUnitSymTypeExpressionFactory.createSIUnit("km^2/(h*ms^4)", scope);

        // Constants
        SymTypePrimitive d = SIUnitSymTypeExpressionFactory.createPrimitive("double");
        SymTypePrimitive i = SIUnitSymTypeExpressionFactory.createPrimitive("int");
        SymTypePrimitive l = SIUnitSymTypeExpressionFactory.createPrimitive("long");

        // SIUnitLiterals
        add2scope(scope, field("varD_S", SIUnitSymTypeExpressionFactory.createNumericWithSIUnitType(d, s, scope)));
        add2scope(scope, field("varI_M", SIUnitSymTypeExpressionFactory.createNumericWithSIUnitType(i, m, scope)));
        add2scope(scope, field("varD_M", SIUnitSymTypeExpressionFactory.createNumericWithSIUnitType(d, m, scope)));
        add2scope(scope, field("varL_KMe2perH", SIUnitSymTypeExpressionFactory.createNumericWithSIUnitType(l, kMe2perH, scope)));
        add2scope(scope, field("varD_KMe2perHmSe4", SIUnitSymTypeExpressionFactory.createNumericWithSIUnitType(d, kMe2perHMSe4, scope)));

        add2scope(scope, field("varS", s));
        add2scope(scope, field("varM", m));
        add2scope(scope, field("varKMe2perH", kMe2perH));
        add2scope(scope, field("varKMe2perHMSe4", kMe2perHMSe4));

        setFlatExpressionScopeSetter(scope);
    }

    /**
     * test EqualsExpression
     */
    @Test
    public void deriveFromEqualsExpression() throws IOException {
        //initialize symbol table
        init_basic();

        //example with two siunit types
        check("3 km/s==2 m^2/1 sm", "boolean");

        //example with two siunit types
        check("varI_M==3 m", "boolean");

        //example with two siunit types
        check("varM==varS", "boolean");
    }

    @Test
    public void testInvalidEqualsExpression1() throws IOException {
        init_basic();

        checkError("varM==3 m", "0xA0166");
    }

    @Test
    public void testInvalidEqualsExpression2() throws IOException {
        init_basic();

        //person1 has the type Person, foo is a boolean
        checkError("varI_M==3", "0xA0166");
    }

    /**
     * test NotEqualsExpression
     */
    @Test
    public void deriveFromNotEqualsExpression() throws IOException {
        //initialize symbol table
        init_basic();

        //example with two siunit types
        check("varI_M/varD_S!=5m^2/sm", "boolean");

        //example with two siunit types
        check("varM!=varS", "boolean");
    }

    @Test
    public void testInvalidNotEqualsExpression() throws IOException {
        init_basic();

        checkError("varM!=varI_M", "0xA0166");
    }

    @Test
    public void testInvalidNotEqualsExpression2() throws IOException {
        init_basic();
        //person1 is a Person, foo is a boolean
        checkError("varM!=3", "0xA0166");
    }

    /**
     * test BooleanAndOpExpression
     */
    @Test
    public void deriveFromBooleanAndOpExpression() throws IOException {
        check("(3 km/h<=4m/s&&5>6)", "boolean");
    }

    @Test
    public void testInvalidAndOpExpression() throws IOException {
        //only possible with two booleans
        checkError("3 km &&true", "0xA0167");
    }

    /**
     * test BooleanOrOpExpression
     */
    @Test
    public void deriveFromBooleanOrOpExpression() throws IOException {
        check("(3 km/h<=4m/s||5>6)", "boolean");
    }

    @Test
    public void testInvalidOrOpExpression() throws IOException {
        //only possible with two booleans
        checkError("3 m||true", "0xA0167");
    }

    @Test
    public void testInvalidLogicalNotExpression() throws IOException {
        //only possible with a boolean as inner expression
        checkError("!4 m", "0xA0171");
    }

    /**
     * test BracketExpression
     */
    @Test
    public void deriveFromBracketExpression() throws IOException {
        //initialize symbol table
        init_basic();

        //test with siunits
        check("(varS*3 s/(2*varM*varM-4 km^2))", "(int,s^2/m^2)");
    }

    /**
     * test ConditionalExpression
     */
    @Test
    public void deriveFromConditionalExpression() throws IOException {
        //initialize symbol table
        init_basic();

        //test with siunits
        check("3<9?varM:varM*varM/varM", "m");
    }

    @Test
    public void testInvalidConditionalExpression() throws IOException {
        //initialize symbol table
        init_basic();

        //true and 7 are not of the same type
        checkError("3<4?varS:varM", "0xA0234");
    }
}
