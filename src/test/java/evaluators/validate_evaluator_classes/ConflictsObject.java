package evaluators.validate_evaluator_classes;

import it.phibonachos.andromeda.Validate;
import it.phibonachos.andromeda.types.StringValue;

public class ConflictsObject {
    private String prop;
    private String conflictProp;

    @Validate(with = StringValue.class, conflicts = "getConflictProp")
    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    @Validate(with = StringValue.class, conflicts = "getProp")
    public String getConflictProp() {
        return conflictProp;
    }

    public void setConflictProp(String conflictProp) {
        this.conflictProp = conflictProp;
    }
}