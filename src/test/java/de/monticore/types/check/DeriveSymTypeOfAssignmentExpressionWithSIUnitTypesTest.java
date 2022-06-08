/* (c) https://github.com/MontiCore/monticore */

package de.monticore.types.check;

import de.monticore.expressions.combineexpressionswithliterals.CombineExpressionsWithLiteralsMill;
import de.monticore.expressions.combineexpressionswithliterals._parser.CombineExpressionsWithLiteralsParser;
import de.monticore.expressions.combineexpressionswithsiunitliterals.CombineExpressionsWithSIUnitLiteralsMill;
import de.monticore.expressions.combineexpressionswithsiunitliterals._parser.CombineExpressionsWithSIUnitLiteralsParser;
import de.monticore.expressions.combineexpressionswithsiunitliterals._symboltable.ICombineExpressionsWithSIUnitLiteralsScope;
import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.expressions.expressionsbasis._visitor.ExpressionsBasisTraverser;
import de.monticore.siunits.SIUnitsMill;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import static de.monticore.types.check.DefsTypeBasic.add2scope;
import static de.monticore.types.check.DefsTypeBasic.field;

public class DeriveSymTypeOfAssignmentExpressionWithSIUnitTypesTest extends DeriveSymTypeAbstractTest {

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

    private ICombineExpressionsWithSIUnitLiteralsScope scope;

    @Before
    public void setupForEach() {
        CombineExpressionsWithSIUnitLiteralsMill.reset();
        CombineExpressionsWithSIUnitLiteralsMill.init();
        CombineExpressionsWithSIUnitLiteralsMill.globalScope();
        SIUnitsMill.initializeSIUnits();

        scope = CombineExpressionsWithSIUnitLiteralsMill.scope();
        scope.setEnclosingScope(null)  ;     // No enclosing Scope: Search ending here
        scope.setExportingSymbols(true);
        scope.setAstNode(null);

        // SIUnits
        SymTypeExpression s = SIUnitSymTypeExpressionFactory.createSIUnit("s", scope);
        SymTypeExpression m = SIUnitSymTypeExpressionFactory.createSIUnit("m", scope);
        SymTypeExpression kMe2perH = SIUnitSymTypeExpressionFactory.createSIUnit("km^2/h", scope);
        SymTypeExpression kMe2perHMSe4 = SIUnitSymTypeExpressionFactory.createSIUnit("km^2/(h*ms^4)", scope);

        // Constants
        SymTypePrimitive d = SymTypeExpressionFactory.createPrimitive("double");
        SymTypePrimitive i = SymTypeExpressionFactory.createPrimitive("int");
        SymTypePrimitive l = SymTypeExpressionFactory.createPrimitive("long");

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

    /*--------------------------------------------------- TESTS ---------------------------------------------------------*/

    /**
     * test IncSuffixExpression
     */
    @Test
    public void deriveFromIncSuffixExpressionWithSIUnits() throws IOException {
        //example with siunit literal
        check("4km++", "(int,km)");
    }

    @Test
    public void testInvalidIncSuffixExpressionWithSIUnits() throws IOException {
        //not possible because ++ is not defined for siunits
        checkError("varS++", "0xA0170");
    }

    /**
     * test DecSuffixExpression
     */
    @Test
    public void deriveFromDecSuffixExpressionWithSIUnits() throws IOException {
        //example with siunit literal
        check("4.2km--", "(double,km)");
    }

    @Test
    public void testInvalidDecSuffixExpressionWithSIUnits() throws IOException {
        //not possible because -- is not defined for siunits
        checkError("varS--", "0xA0171");
    }

    /**
     * test IncPrefixExpression
     */
    @Test
    public void deriveFromIncPrefixExpressionWithSIUnits() throws IOException {
        //example with siunit literal
        check("++4km", "(int,km)");
    }

    @Test
    public void testInvalidIncPrefixExpressionWithSIUnits() throws IOException {
        //not possible because ++ is not defined for siunits
        checkError("++varS", "0xA0172");
    }

    /**
     * test DecPrefixExpression
     */
    @Test
    public void deriveFromDecPrefixExpressionWithSIUnits() throws IOException {
        //example with siunit literal
        check("--4.1km", "(double,km)");
    }

    @Test
    public void testInvalidDecPrefixExpressionWithSIUnits() throws IOException {
        //not possible because -- is not defined for siunits
        checkError("--varS", "0xA0173");
    }

    /**
     * test MinusPrefixExpression
     */
    @Test
    public void deriveFromMinusPrefixExpressionWithSIUnits() throws IOException {
        //example with siunit literal
        check("-4.3km", "(double,km)");
    }

    @Test
    public void testInvalidMinusPrefixExpressionWithSIUnits() throws IOException {
        //not possible because - is not defined for siunits
        checkError("-varS", "0xA0175");
    }

    /**
     * test PlusPrefixExpression
     */
    @Test
    public void deriveFromPlusPrefixExpressionWithSIUnits() throws IOException {
        //example with siunit literal
        check("+4km", "(int,km)");
    }

    @Test
    public void testInvalidPlusPrefixExpressionWithSIUnits() throws IOException {
        //not possible because + is not defined for siunits
        checkError("+varS", "0xA0174");
    }

    /**
     * test PlusAssignmentExpression
     */
    @Test
    public void deriveFromPlusAssignmentExpressionWithSIUnits() throws IOException {
        //example with double m += int m
        check("varD_M+=4km", "(double,m)");

        //example with int m += double m
        check("varI_M+=4.2km", "(int,m)");

        //example with double m += int m
        check("varI_M+=4km", "(int,m)");
    }


    @Test
    public void testInvalidPlusAssignmentExpressionWithSIUnits1() throws IOException {
        //not possible because s = s + m cannot be calculated
        checkError("varD_S+=4km", "0xA0176");
    }

    @Test
    public void testInvalidPlusAssignmentExpressionWithSIUnits2() throws IOException {
        //not possible because += is not defined for siunits
        checkError("varS+=4km", "0xA0176");
    }

    /**
     * test MinusAssignmentExpression
     */
    @Test
    public void deriveFromMinusAssignmentExpressionWithSIUnits() throws IOException {
        //example with siunit literal long m^2/s -= int m^2/s
        check("varL_KMe2perH-=4m^2/s", "(long,km^2/h)");
    }

    @Test
    public void testInvalidMinusAssignmentExpressionWithSIUnits1() throws IOException {
        //not possible because s = s - m cannot be calculated
        checkError("varD_S-=4km", "0xA0177");
    }

    @Test
    public void testInvalidMinusAssignmentExpressionWithSIUnits2() throws IOException {
        //not possible because -= is not defined for siunits
        checkError("varS-=4km", "0xA0177");
    }

    /**
     * test MultAssignmentExpression
     */
    @Test
    public void deriveFromMultAssignmentExpressionWithSIUnits() throws IOException {
        //example with siunit literal int m * double
        check("varI_M*=5.3", "(int,m)");

        //example with siunit literal double m * double
        check("varD_M*=5.3", "(double,m)");

        //example with siunit literal double m * int
        check("varD_M*=5", "(double,m)");
    }

    @Test
    public void testInvalidMultAssignmentExpressionWithSIUnits1() throws IOException {
        //not possible because int m = int m * (int m) String returns a casting error
        checkError("varI_M*=\"Hello\"", "0xA0178");
    }

    @Test
    public void testInvalidMultAssignmentExpressionWithSIUnits2() throws IOException {
        //not possible because m != m * m
        checkError("varI_M*=varI_M", "0xA0178");
    }

    @Test
    public void testInvalidMultAssignmentExpressionWithSIUnits3() throws IOException {
        //not possible because m != m * m
        checkError("varM*=varM", "0xA0178");
    }

    /**
     * test DivideAssignmentExpression
     */
    @Test
    public void deriveFromDivideAssignmentExpressionWithSIUnits() throws IOException {
        //example with siunit literal int m / int
        check("varI_M/=5", "(int,m)");

        //example with siunit literal int m / double
        check("varI_M/=5.2", "(int,m)");

        //example with siunit literal double m / int
        check("varD_M/=5", "(double,m)");
    }

    @Test
    public void testInvalidDivideAssignmentExpressionWithSIUnits1() throws IOException {
        //not possible because int m = int m / (int m) String returns a casting error
        checkError("varI_M/=\"Hello\"", "0xA0179");
    }

    @Test
    public void testInvalidDivideAssignmentExpressionWithSIUnits2() throws IOException {
        //not possible because m != m / m
        checkError("varI_M/=varI_M", "0xA0179");
    }

    @Test
    public void testInvalidDivideAssignmentExpressionWithSIUnits3() throws IOException {
        //not possible because m != m / m
        checkError("varM/=varM", "0xA0179");
    }

    /**
     * test ModuloAssignmentExpression
     */
    @Test
    public void deriveFromModuloAssignmentExpressionWithSIUnits() throws IOException {
        //example with siunit literal int m %= double m
        check("varI_M%=9.2 m", "(int,m)");

        //example with siunit literal double m %= int m
        check("varD_M%=9 m", "(double,m)");
    }

    @Test
    public void testInvalidModuloAssignmentExpressionWithSIUnits1() throws IOException {
        //not possible because s = s % m cannot be calculated
        checkError("varD_S%=3m", "0xA0189");
    }

    @Test
    public void testInvalidModuloAssignmentExpressionWithSIUnits2() throws IOException {
        //not possible because s = s % int cannot be calculated
        checkError("varD_S%=3", "0xA0189");
    }

    @Test
    public void testInvalidModuloAssignmentExpressionWithSIUnits3() throws IOException {
        //not possible because s = s % int cannot be calculated
        checkError("varS%=3", "0xA0189");
    }

    @Test
    public void deriveFromAndAssignmentExpressionWithSIUnits() throws IOException {
        //example with int m &= int
        check("varI_M&=9", "(int,m)");
        //example with long m &= int
        check("varL_KMe2perH&=9", "(long,km^2/h)");
    }

    @Test
    public void testInvalidAndAssignmentExpressionWithSIUnits1() throws IOException {
        //not possible because int m = int m & int m cannot be calculated
        checkError("varI_M&=varI_M", "0xA0183");
    }

    @Test
    public void testInvalidAndAssignmentExpressionWithSIUnits2() throws IOException {
        //not possible because int m = int m & int m cannot be calculated
        checkError("varM&=2", "0xA0183");
    }

    @Test
    public void deriveFromOrAssignmentExpressionWithSIUnits() throws IOException {
        //example with int m &= int
        check("varI_M|=9", "(int,m)");
        //example with long m &= int
        check("varL_KMe2perH|=9", "(long,km^2/h)");
    }


    @Test
    public void testInvalidOrAssignmentExpressionWithSIUnits1() throws IOException {
        //not possible because int m = int m | int m cannot be calculated
        checkError("varI_M|=3 m", "0xA0184");
    }


    @Test
    public void testInvalidOrAssignmentExpressionWithSIUnits2() throws IOException {
        //not possible because int m = int m | int m cannot be calculated
        checkError("varM|=3", "0xA0184");
    }

    @Test
    public void deriveFromBinaryXorAssignmentExpressionWithSIUnits() throws IOException {
        //example with int m &= int
        check("varI_M^=9", "(int,m)");
        //example with long m &= int
        check("varL_KMe2perH^=9", "(long,km^2/h)");
    }

    @Test
    public void testInvalidBinaryXorAssignmentExpressionWithSIUnits1() throws IOException {
        //not possible because int m = int m ^ int m cannot be calculated
        checkError("varI_M^=3 m", "0xA0185");
    }

    @Test
    public void testInvalidBinaryXorAssignmentExpressionWithSIUnits2() throws IOException {
        //not possible because int m = int m ^ int m cannot be calculated
        checkError("varM^=3 m", "0xA0185");
    }

    @Test
    public void deriveFromDoubleLeftAssignmentExpressionWithSIUnits() throws IOException {
        //example with int m - int
        check("varI_M<<=9", "(int,m)");
        //example with int m - char
        check("varI_M<<=\'c\'", "(int,m)");
    }

    @Test
    public void testInvalidDoubleLeftAssignmentExpressionWithSIUnits1() throws IOException {
        //not possible because int m = int m << int m cannot be calculated
        checkError("varI_M<<=3 m", "0xA0187");
    }

    @Test
    public void testInvalidDoubleLeftAssignmentExpressionWithSIUnits2() throws IOException {
        //not possible because int m = int m << int m cannot be calculated
        checkError("varM<<=3", "0xA0187");
    }

    /**
     * test DoubleRightAssignmentExpression
     */
    @Test
    public void deriveFromDoubleRightAssignmentExpressionWithSIUnits() throws IOException {
        //example with int m - int
        check("varI_M>>=9", "(int,m)");
        //example with int m - char
        check("varI_M>>=\'c\'", "(int,m)");
    }

    @Test
    public void testInvalidDoubleRightAssignmentExpressionWithSIUnits1() throws IOException {
        //not possible because int m = int m >> int m cannot be calculated
        checkError("varI_M>>=3m", "0xA0186");
    }

    @Test
    public void testInvalidDoubleRightAssignmentExpressionWithSIUnits2() throws IOException {
        //not possible because int m = int m >> int m cannot be calculated
        checkError("varM>>=varM", "0xA0186");
    }

    /**
     * test LogicalRightAssignmentExpression
     */
    @Test
    public void deriveFromLogicalRightAssignmentExpressionWithSIUnits() throws IOException {
        //example with int - int
        check("varI_M>>>=9", "(int,m)");
        //example with char - char
        check("varI_M>>>=\'3\'", "(int,m)");
    }

    @Test
    public void testInvalidLogicalRightAssignmentExpressionWithSIUnits1() throws IOException {
        //not possible because int m = int m >>> int m cannot be calculated
        checkError("varI_M>>>=varI_M", "0xA0188");
    }

    @Test
    public void testInvalidLogicalRightAssignmentExpressionWithSIUnits2() throws IOException {
        //not possible because int m = int m >>> int m cannot be calculated
        checkError("varM>>>=2", "0xA0188");
    }

    /**
     * test RegularAssignmentExpression
     */
    @Test
    public void deriveFromRegularAssignmentExpressionWithSIUnits() throws IOException {
        //example with km - km
        check("varD_KMe2perHmSe4 = 3 mm^2/ks^3h^2", "(double,km^2/(h*ms^4))");


        //example with m^2/s
        check("varKMe2perH = varM*varM/varS", "km^2/h");
    }


    @Test
    public void testInvalidRegularAssignmentExpressionWithSIUnits1() throws IOException {
        //not possible because m^2/s^5 and m/s^5 are incompatible types
        checkError("varD_KMe2perHmSe4 = 3 mm/ks^3h^2", "0xA0182");
    }

    @Test
    public void testInvalidRegularAssignmentExpressionWithSIUnits2() throws IOException {
        //not possible because m^2/s^5 and m/s^5 are incompatible types
        checkError("varM = 3 m", "0xA0182");
    }
}
