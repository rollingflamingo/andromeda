package com.evaluators;

import com.annotation.validate.ValidateEvaluator;
import com.annotation.validate.exception.ConflictFieldException;
import com.annotation.validate.exception.CyclicRequirementException;
import com.annotation.validate.exception.InvalidFieldException;
import com.annotation.validate.exception.RequirementsException;
import com.testClasses.validate_evaluator_classes.*;
import com.wikicasa.RealEstate;
import org.junit.jupiter.api.Test;

class ValidateEvaluatorTest {

    /* POSITIVE TEST */

    @Test
    void successfulRealEstateValidation() throws Exception {
        RealEstate re = new RealEstate();

        re.setExternalId("ext-ID");
        re.setDescription("A valid description");
        re.setRent(true);
        re.setPriceRent(1d);

        assert new ValidateEvaluator<>(re).evaluate();
    }

    @Test
    void validationAlternative() throws Exception {
        AlternativeObject ao = new AlternativeObject();
        ao.setAlternativeProp("this is a valid alternative string");

        assert new com.annotation.validate.ValidateEvaluator<>(ao).evaluate();
    }

    @Test
    void validationWithRequirements() throws Exception {
        RequirementsObject ro = new RequirementsObject();
        ro.setProp("this property require another field initialized to be parsed as valid");
        ro.setRequiredProp(12d);

        assert new com.annotation.validate.ValidateEvaluator<>(ro).evaluate();
    }

    @Test
    void validationWithConflicts() throws Exception {
        ConflictsObject co = new ConflictsObject();
        co.setConflictProp("this property pass validation as its conflicts is not set");

        assert new com.annotation.validate.ValidateEvaluator<>(co).evaluate();
    }

    /* NEGATIVE TEST */
    @Test
    void plainValidationFail() {
        try {
            SimpleObject so = new SimpleObject();
            assert new com.annotation.validate.ValidateEvaluator<>(so).evaluate();
        } catch (Exception ife) {
            assert ife instanceof InvalidFieldException;
            return;
        }
        assert false;
    }

    @Test
    void validationAlternativeFail() {
        try {
            AlternativeObject ao = new AlternativeObject();
            assert new com.annotation.validate.ValidateEvaluator<>(ao).evaluate();
        } catch (Exception e) {
            assert e instanceof InvalidFieldException;
            return;
        }
        assert false;
    }

    @Test
    void validationWithRequirementsFail() {
        try {
            FailRequirementsObject fro = new FailRequirementsObject();
            fro.setProp("this property require another field initialized to be parsed as valid");
            assert new com.annotation.validate.ValidateEvaluator<>(fro).evaluate();
        } catch (Exception e) {
            assert e instanceof RequirementsException;
            return;
        }
        assert false;
    }

    @Test
    void validationWithConflictsFail() {
        try {
            ConflictsObject co = new ConflictsObject();
            co.setProp("this prop conflicts with conflictProp");
            co.setConflictProp("this prop conflict with prop");
            assert new ValidateEvaluator<>(co).evaluate();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            assert e instanceof ConflictFieldException;
            return;
        }
        assert false;
    }

    /**
     * If a node n1 require a second one n2, n2 cannot have n1 as its own dependency
     */
    @Test
    void cyclicRequirements() {
        try {
            CyclicRequirementsObject cro = new CyclicRequirementsObject();
            cro.setProp("require prop1");
            cro.setProp1("require prop");
            assert new ValidateEvaluator<>(cro).evaluate();
        } catch (Exception e){
            System.err.println(e.getMessage());
            assert e instanceof CyclicRequirementException;
            return;
        }
        assert false;
    }

    /**
     * Required nodes inherits parent mandatoriness
     */
    @Test
    void cascadeRequirements() {
        CascadeRequirementsObject cro = new CascadeRequirementsObject();
        com.annotation.validate.ValidateEvaluator evaluator = new com.annotation.validate.ValidateEvaluator<>(cro);
        cro.setProp("this is a mandatory property");

        System.out.println("first validation");

        try {
            assert evaluator.evaluate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert e instanceof RequirementsException;
        }

        cro.setReq1("this is a field required from prop");

        System.out.println("second validation");
        try {
            assert evaluator.evaluate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert e instanceof RequirementsException;
        }

        cro.setReq2("this is a field required from req1");

        System.out.println("third validation");
        try {
            assert evaluator.evaluate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert e instanceof RequirementsException;
        }

        cro.setReq3("this is a field required from req2");

        System.out.println("fourth validation");
        try {
           assert evaluator.evaluate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert e instanceof RequirementsException;
        }
    }

    /**
     * Child method alternatives are discarded
     */
    @Test
    void cascadeAlternatives() {
        try {
            CascadeAlternativeObject cao = new CascadeAlternativeObject();
            cao.setAProp1("at least this must be set");

            assert new ValidateEvaluator<>(cao).evaluate();
        } catch (Exception e){
            assert e instanceof InvalidFieldException;
            return;
        }
        assert false;
    }

}