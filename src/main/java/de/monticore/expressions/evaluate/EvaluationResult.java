/* (c) https://github.com/MontiCore/monticore */
package de.monticore.expressions.evaluate;

import java.util.Optional;

public class EvaluationResult {
    Optional<Double> result = Optional.empty();

    public boolean isPresentResult() {
        return result.isPresent();
    }

    public Double getResult() {
        return result.get();
    }

    public void setResult(Double result) {
        this.result = Optional.ofNullable(result);
    }

}
