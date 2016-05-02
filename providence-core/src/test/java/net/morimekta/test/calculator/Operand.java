package net.morimekta.test.calculator;

@SuppressWarnings("unused")
public class Operand
        implements net.morimekta.providence.PUnion<Operand>, java.io.Serializable, Comparable<Operand> {
    private final static long serialVersionUID = -7034870678901672325L;

    private final static double kDefaultNumber = 0.0d;

    private final net.morimekta.test.calculator.Operation mOperation;
    private final double mNumber;
    private final net.morimekta.test.number.Imaginary mImaginary;

    private final _Field tUnionField;
    
    private volatile int tHashCode;

    private Operand(_Builder builder) {
        tUnionField = builder.tUnionField;

        mOperation = tUnionField == _Field.OPERATION ? builder.mOperation : null;
        mNumber = tUnionField == _Field.NUMBER ? builder.mNumber : kDefaultNumber;
        mImaginary = tUnionField == _Field.IMAGINARY ? builder.mImaginary : null;
    }

    public static Operand withOperation(net.morimekta.test.calculator.Operation value) {
        return new _Builder().setOperation(value).build();
    }

    public static Operand withNumber(double value) {
        return new _Builder().setNumber(value).build();
    }

    public static Operand withImaginary(net.morimekta.test.number.Imaginary value) {
        return new _Builder().setImaginary(value).build();
    }

    public boolean hasOperation() {
        return tUnionField == _Field.OPERATION && mOperation != null;
    }

    public net.morimekta.test.calculator.Operation getOperation() {
        return mOperation;
    }

    public boolean hasNumber() {
        return tUnionField == _Field.NUMBER;
    }

    public double getNumber() {
        return mNumber;
    }

    public boolean hasImaginary() {
        return tUnionField == _Field.IMAGINARY && mImaginary != null;
    }

    public net.morimekta.test.number.Imaginary getImaginary() {
        return mImaginary;
    }

    @Override
    public _Field unionField() {
        return tUnionField;
    }

    @Override
    public boolean has(int key) {
        switch(key) {
            case 1: return hasOperation();
            case 2: return hasNumber();
            case 3: return hasImaginary();
            default: return false;
        }
    }

    @Override
    public int num(int key) {
        switch(key) {
            case 1: return hasOperation() ? 1 : 0;
            case 2: return hasNumber() ? 1 : 0;
            case 3: return hasImaginary() ? 1 : 0;
            default: return 0;
        }
    }

    @Override
    public Object get(int key) {
        switch(key) {
            case 1: return getOperation();
            case 2: return getNumber();
            case 3: return getImaginary();
            default: return null;
        }
    }

    @Override
    public boolean compact() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Operand)) return false;
        Operand other = (Operand) o;
        return java.util.Objects.equals(tUnionField, other.tUnionField) &&
               java.util.Objects.equals(mOperation, other.mOperation) &&
               java.util.Objects.equals(mNumber, other.mNumber) &&
               java.util.Objects.equals(mImaginary, other.mImaginary);
    }

    @Override
    public int hashCode() {
        if (tHashCode == 0) {
            tHashCode = java.util.Objects.hash(
                    Operand.class,
                    _Field.OPERATION, mOperation,
                    _Field.NUMBER, mNumber,
                    _Field.IMAGINARY, mImaginary);
        }
        return tHashCode;
    }

    @Override
    public String toString() {
        return "calculator.Operand" + asString();
    }

    @Override
    public String asString() {
        StringBuilder out = new StringBuilder();
        out.append("{");

        switch (tUnionField) {
            case OPERATION: {
                out.append("operation:");
                out.append(mOperation.asString());
                break;
            }
            case NUMBER: {
                out.append("number:");
                out.append(net.morimekta.providence.util.TypeUtils.toString(mNumber));
                break;
            }
            case IMAGINARY: {
                out.append("imaginary:");
                out.append(mImaginary.asString());
                break;
            }
        }
        out.append('}');
        return out.toString();
    }

    @Override
    public int compareTo(Operand other) {
        int c = Integer.compare(tUnionField.getKey(), other.tUnionField.getKey());
        if (c != 0) return c;

        switch (tUnionField) {
            case OPERATION:
                return mOperation.compareTo(other.mOperation);
            case NUMBER:
                return Double.compare(mNumber, other.mNumber);
            case IMAGINARY:
                return mImaginary.compareTo(other.mImaginary);
            default: return 0;
        }
    }

    public enum _Field implements net.morimekta.providence.descriptor.PField {
        OPERATION(1, net.morimekta.providence.descriptor.PRequirement.DEFAULT, "operation", net.morimekta.test.calculator.Operation.provider(), null),
        NUMBER(2, net.morimekta.providence.descriptor.PRequirement.DEFAULT, "number", net.morimekta.providence.descriptor.PPrimitive.DOUBLE.provider(), null),
        IMAGINARY(3, net.morimekta.providence.descriptor.PRequirement.DEFAULT, "imaginary", net.morimekta.test.number.Imaginary.provider(), null),
        ;

        private final int mKey;
        private final net.morimekta.providence.descriptor.PRequirement mRequired;
        private final String mName;
        private final net.morimekta.providence.descriptor.PDescriptorProvider mTypeProvider;
        private final net.morimekta.providence.descriptor.PValueProvider<?> mDefaultValue;

        _Field(int key, net.morimekta.providence.descriptor.PRequirement required, String name, net.morimekta.providence.descriptor.PDescriptorProvider typeProvider, net.morimekta.providence.descriptor.PValueProvider<?> defaultValue) {
            mKey = key;
            mRequired = required;
            mName = name;
            mTypeProvider = typeProvider;
            mDefaultValue = defaultValue;
        }

        @Override
        public int getKey() { return mKey; }

        @Override
        public net.morimekta.providence.descriptor.PRequirement getRequirement() { return mRequired; }

        @Override
        public net.morimekta.providence.PType getType() { return getDescriptor().getType(); }

        @Override
        public net.morimekta.providence.descriptor.PDescriptor getDescriptor() { return mTypeProvider.descriptor(); }

        @Override
        public String getName() { return mName; }

        @Override
        public boolean hasDefaultValue() { return mDefaultValue != null; }

        @Override
        public Object getDefaultValue() {
            return hasDefaultValue() ? mDefaultValue.get() : null;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("Operand._Field(")
                   .append(mKey)
                   .append(": ");
            if (mRequired != net.morimekta.providence.descriptor.PRequirement.DEFAULT) {
                builder.append(mRequired.label).append(" ");
            }
            builder.append(getDescriptor().getQualifiedName(null))
                   .append(' ')
                   .append(mName)
                   .append(')');
            return builder.toString();
        }

        public static _Field forKey(int key) {
            switch (key) {
                case 1: return _Field.OPERATION;
                case 2: return _Field.NUMBER;
                case 3: return _Field.IMAGINARY;
                default: return null;
            }
        }

        public static _Field forName(String name) {
            switch (name) {
                case "operation": return _Field.OPERATION;
                case "number": return _Field.NUMBER;
                case "imaginary": return _Field.IMAGINARY;
            }
            return null;
        }
    }

    public static net.morimekta.providence.descriptor.PUnionDescriptorProvider<Operand,_Field> provider() {
        return new _Provider();
    }

    @Override
    public net.morimekta.providence.descriptor.PUnionDescriptor<Operand,_Field> descriptor() {
        return kDescriptor;
    }

    public static final net.morimekta.providence.descriptor.PUnionDescriptor<Operand,_Field> kDescriptor;

    private static class _Descriptor
            extends net.morimekta.providence.descriptor.PUnionDescriptor<Operand,_Field> {
        public _Descriptor() {
            super("calculator", "Operand", new _Factory(), false);
        }

        @Override
        public _Field[] getFields() {
            return _Field.values();
        }

        @Override
        public _Field getField(String name) {
            return _Field.forName(name);
        }

        @Override
        public _Field getField(int key) {
            return _Field.forKey(key);
        }
    }

    static {
        kDescriptor = new _Descriptor();
    }

    private final static class _Provider extends net.morimekta.providence.descriptor.PUnionDescriptorProvider<Operand,_Field> {
        @Override
        public net.morimekta.providence.descriptor.PUnionDescriptor<Operand,_Field> descriptor() {
            return kDescriptor;
        }
    }

    private final static class _Factory
            extends net.morimekta.providence.PMessageBuilderFactory<Operand> {
        @Override
        public _Builder builder() {
            return new _Builder();
        }
    }

    @Override
    public _Builder mutate() {
        return new _Builder(this);
    }

    public static _Builder builder() {
        return new _Builder();
    }

    public static class _Builder
            extends net.morimekta.providence.PMessageBuilder<Operand> {
        private _Field tUnionField;

        private net.morimekta.test.calculator.Operation mOperation;
        private double mNumber;
        private net.morimekta.test.number.Imaginary mImaginary;


        public _Builder() {
            mNumber = kDefaultNumber;
        }

        public _Builder(Operand base) {
            this();

            tUnionField = base.tUnionField;

            mOperation = base.mOperation;
            mNumber = base.mNumber;
            mImaginary = base.mImaginary;
        }

        public _Builder setOperation(net.morimekta.test.calculator.Operation value) {
            tUnionField = _Field.OPERATION;
            mOperation = value;
            return this;
        }
        public boolean isSetOperation() {
            return tUnionField == _Field.OPERATION;
        }
        public _Builder clearOperation() {
            if (tUnionField == _Field.OPERATION) tUnionField = null;
            mOperation = null;
            return this;
        }
        public _Builder setNumber(double value) {
            tUnionField = _Field.NUMBER;
            mNumber = value;
            return this;
        }
        public boolean isSetNumber() {
            return tUnionField == _Field.NUMBER;
        }
        public _Builder clearNumber() {
            if (tUnionField == _Field.NUMBER) tUnionField = null;
            mNumber = kDefaultNumber;
            return this;
        }
        public _Builder setImaginary(net.morimekta.test.number.Imaginary value) {
            tUnionField = _Field.IMAGINARY;
            mImaginary = value;
            return this;
        }
        public boolean isSetImaginary() {
            return tUnionField == _Field.IMAGINARY;
        }
        public _Builder clearImaginary() {
            if (tUnionField == _Field.IMAGINARY) tUnionField = null;
            mImaginary = null;
            return this;
        }
        @Override
        public _Builder set(int key, Object value) {
            if (value == null) return clear(key);
            switch (key) {
                case 1: setOperation((net.morimekta.test.calculator.Operation) value); break;
                case 2: setNumber((double) value); break;
                case 3: setImaginary((net.morimekta.test.number.Imaginary) value); break;
            }
            return this;
        }

        @Override
        public _Builder addTo(int key, Object value) {
            switch (key) {
                default: break;
            }
            return this;
        }

        @Override
        public _Builder clear(int key) {
            switch (key) {
                case 1: clearOperation(); break;
                case 2: clearNumber(); break;
                case 3: clearImaginary(); break;
            }
            return this;
        }

        @Override
        public boolean isValid() {
            return tUnionField != null;
        }

        @Override
        public Operand build() {
            return new Operand(this);
        }
    }
}
