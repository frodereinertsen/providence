package net.morimekta.providence.jax.rs.test_web_app;

import net.morimekta.test.calculator.CalculateException;
import net.morimekta.test.calculator.Calculator;
import net.morimekta.test.calculator.Operand;
import net.morimekta.test.calculator.Operation;
import net.morimekta.test.number.Imaginary;

import java.util.List;

/**
 * Test service implementation for dropwizard testing.
 */
public class TestCalculator implements Calculator.Iface {
    @Override
    public void iamalive() {
        // empty.
    }

    @Override
    public Operand calculate(Operation pOp) throws CalculateException {
        switch (pOp.getOperator()) {
            case ADD:
                return add(pOp.getOperands());
            default:
                throw new CalculateException("Unsupported operation: " + pOp.getOperator(), pOp);
        }
    }

    private Operand add(List<Operand> ops) throws CalculateException {
        Imaginary img = null;
        double dbl = 0;

        for (Operand op : ops) {
            Operand operand = op;
            if (operand.unionField() == Operand._Field.OPERATION) {
                operand = calculate(operand.getOperation());
            }
            switch (operand.unionField()) {
                case IMAGINARY:
                    if (img == null) {
                        img = new Imaginary(dbl, 0d);
                        dbl = 0d;
                    }
                    img = addImaginary(img, operand.getImaginary());
                    break;
                case NUMBER:
                    if (img != null) {
                        img = addImaginary(img, new Imaginary(operand.getNumber(), 0d));
                    } else {
                        dbl = dbl + operand.getNumber();
                    }
                    break;
                default:
                    throw CalculateException.builder()
                                            .setMessage("Unknown operand: " + operand.unionField())
                                            .setOperation(op.getOperation())
                                            .build();
            }
        }

        if (img != null) {
            return Operand.withImaginary(img);
        } else {
            return Operand.withNumber(dbl);
        }
    }

    private Imaginary addImaginary(Imaginary a, Imaginary b) {
        return new Imaginary(a.getV() + b.getV(), a.getI() + b.getI());
    }
}
