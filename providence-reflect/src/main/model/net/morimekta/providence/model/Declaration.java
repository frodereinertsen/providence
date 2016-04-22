package net.morimekta.providence.model;

/** ( <enum> | <typedef> | <struct> | <service> | <const> ) */
@SuppressWarnings("unused")
public class Declaration
        implements net.morimekta.providence.PUnion<Declaration>, java.io.Serializable, Comparable<Declaration> {
    private final static long serialVersionUID = -6998763195276182553L;

    private final net.morimekta.providence.model.EnumType mDeclEnum;
    private final net.morimekta.providence.model.TypedefType mDeclTypedef;
    private final net.morimekta.providence.model.StructType mDeclStruct;
    private final net.morimekta.providence.model.ServiceType mDeclService;
    private final net.morimekta.providence.model.ThriftField mDeclConst;

    private final _Field tUnionField;
    
    private volatile int tHashCode;

    private Declaration(_Builder builder) {
        tUnionField = builder.tUnionField;

        mDeclEnum = tUnionField == _Field.DECL_ENUM ? builder.mDeclEnum : null;
        mDeclTypedef = tUnionField == _Field.DECL_TYPEDEF ? builder.mDeclTypedef : null;
        mDeclStruct = tUnionField == _Field.DECL_STRUCT ? builder.mDeclStruct : null;
        mDeclService = tUnionField == _Field.DECL_SERVICE ? builder.mDeclService : null;
        mDeclConst = tUnionField == _Field.DECL_CONST ? builder.mDeclConst : null;
    }

    public static Declaration withDeclEnum(net.morimekta.providence.model.EnumType value) {
        return new _Builder().setDeclEnum(value).build();
    }

    public static Declaration withDeclTypedef(net.morimekta.providence.model.TypedefType value) {
        return new _Builder().setDeclTypedef(value).build();
    }

    public static Declaration withDeclStruct(net.morimekta.providence.model.StructType value) {
        return new _Builder().setDeclStruct(value).build();
    }

    public static Declaration withDeclService(net.morimekta.providence.model.ServiceType value) {
        return new _Builder().setDeclService(value).build();
    }

    public static Declaration withDeclConst(net.morimekta.providence.model.ThriftField value) {
        return new _Builder().setDeclConst(value).build();
    }

    public boolean hasDeclEnum() {
        return tUnionField == _Field.DECL_ENUM && mDeclEnum != null;
    }

    public net.morimekta.providence.model.EnumType getDeclEnum() {
        return mDeclEnum;
    }

    public boolean hasDeclTypedef() {
        return tUnionField == _Field.DECL_TYPEDEF && mDeclTypedef != null;
    }

    public net.morimekta.providence.model.TypedefType getDeclTypedef() {
        return mDeclTypedef;
    }

    public boolean hasDeclStruct() {
        return tUnionField == _Field.DECL_STRUCT && mDeclStruct != null;
    }

    public net.morimekta.providence.model.StructType getDeclStruct() {
        return mDeclStruct;
    }

    public boolean hasDeclService() {
        return tUnionField == _Field.DECL_SERVICE && mDeclService != null;
    }

    public net.morimekta.providence.model.ServiceType getDeclService() {
        return mDeclService;
    }

    public boolean hasDeclConst() {
        return tUnionField == _Field.DECL_CONST && mDeclConst != null;
    }

    public net.morimekta.providence.model.ThriftField getDeclConst() {
        return mDeclConst;
    }

    @Override
    public _Field unionField() {
        return tUnionField;
    }

    @Override
    public boolean has(int key) {
        switch(key) {
            case 1: return hasDeclEnum();
            case 2: return hasDeclTypedef();
            case 3: return hasDeclStruct();
            case 4: return hasDeclService();
            case 5: return hasDeclConst();
            default: return false;
        }
    }

    @Override
    public int num(int key) {
        switch(key) {
            case 1: return hasDeclEnum() ? 1 : 0;
            case 2: return hasDeclTypedef() ? 1 : 0;
            case 3: return hasDeclStruct() ? 1 : 0;
            case 4: return hasDeclService() ? 1 : 0;
            case 5: return hasDeclConst() ? 1 : 0;
            default: return 0;
        }
    }

    @Override
    public Object get(int key) {
        switch(key) {
            case 1: return getDeclEnum();
            case 2: return getDeclTypedef();
            case 3: return getDeclStruct();
            case 4: return getDeclService();
            case 5: return getDeclConst();
            default: return null;
        }
    }

    @Override
    public boolean isCompact() {
        return false;
    }

    @Override
    public boolean isSimple() {
        return descriptor().isSimple();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Declaration)) return false;
        Declaration other = (Declaration) o;
        return java.util.Objects.equals(tUnionField, other.tUnionField) &&
               java.util.Objects.equals(mDeclEnum, other.mDeclEnum) &&
               java.util.Objects.equals(mDeclTypedef, other.mDeclTypedef) &&
               java.util.Objects.equals(mDeclStruct, other.mDeclStruct) &&
               java.util.Objects.equals(mDeclService, other.mDeclService) &&
               java.util.Objects.equals(mDeclConst, other.mDeclConst);
    }

    @Override
    public int hashCode() {
        if (tHashCode == 0) {
            tHashCode = java.util.Objects.hash(
                    Declaration.class,
                    _Field.DECL_ENUM, mDeclEnum,
                    _Field.DECL_TYPEDEF, mDeclTypedef,
                    _Field.DECL_STRUCT, mDeclStruct,
                    _Field.DECL_SERVICE, mDeclService,
                    _Field.DECL_CONST, mDeclConst);
        }
        return tHashCode;
    }

    @Override
    public String toString() {
        return "model.Declaration" + asString();
    }

    @Override
    public String asString() {
        StringBuilder out = new StringBuilder();
        out.append("{");

        switch (tUnionField) {
            case DECL_ENUM: {
                out.append("decl_enum:");
                out.append(mDeclEnum.asString());
                break;
            }
            case DECL_TYPEDEF: {
                out.append("decl_typedef:");
                out.append(mDeclTypedef.asString());
                break;
            }
            case DECL_STRUCT: {
                out.append("decl_struct:");
                out.append(mDeclStruct.asString());
                break;
            }
            case DECL_SERVICE: {
                out.append("decl_service:");
                out.append(mDeclService.asString());
                break;
            }
            case DECL_CONST: {
                out.append("decl_const:");
                out.append(mDeclConst.asString());
                break;
            }
        }
        out.append('}');
        return out.toString();
    }

    @Override
    public int compareTo(Declaration other) {
        int c = Integer.compare(tUnionField.getKey(), other.tUnionField.getKey());
        if (c != 0) return c;

        switch (tUnionField) {
            case DECL_ENUM:
                return mDeclEnum.compareTo(other.mDeclEnum);
            case DECL_TYPEDEF:
                return mDeclTypedef.compareTo(other.mDeclTypedef);
            case DECL_STRUCT:
                return mDeclStruct.compareTo(other.mDeclStruct);
            case DECL_SERVICE:
                return mDeclService.compareTo(other.mDeclService);
            case DECL_CONST:
                return mDeclConst.compareTo(other.mDeclConst);
            default: return 0;
        }
    }

    public enum _Field implements net.morimekta.providence.descriptor.PField {
        DECL_ENUM(1, net.morimekta.providence.descriptor.PRequirement.DEFAULT, "decl_enum", net.morimekta.providence.model.EnumType.provider(), null),
        DECL_TYPEDEF(2, net.morimekta.providence.descriptor.PRequirement.DEFAULT, "decl_typedef", net.morimekta.providence.model.TypedefType.provider(), null),
        DECL_STRUCT(3, net.morimekta.providence.descriptor.PRequirement.DEFAULT, "decl_struct", net.morimekta.providence.model.StructType.provider(), null),
        DECL_SERVICE(4, net.morimekta.providence.descriptor.PRequirement.DEFAULT, "decl_service", net.morimekta.providence.model.ServiceType.provider(), null),
        DECL_CONST(5, net.morimekta.providence.descriptor.PRequirement.DEFAULT, "decl_const", net.morimekta.providence.model.ThriftField.provider(), null),
        ;

        private final int mKey;
        private final net.morimekta.providence.descriptor.PRequirement mRequired;
        private final String mName;
        private final net.morimekta.providence.descriptor.PDescriptorProvider<?> mTypeProvider;
        private final net.morimekta.providence.descriptor.PValueProvider<?> mDefaultValue;

        _Field(int key, net.morimekta.providence.descriptor.PRequirement required, String name, net.morimekta.providence.descriptor.PDescriptorProvider<?> typeProvider, net.morimekta.providence.descriptor.PValueProvider<?> defaultValue) {
            mKey = key;
            mRequired = required;
            mName = name;
            mTypeProvider = typeProvider;
            mDefaultValue = defaultValue;
        }

        @Override
        public String getComment() { return null; }

        @Override
        public int getKey() { return mKey; }

        @Override
        public net.morimekta.providence.descriptor.PRequirement getRequirement() { return mRequired; }

        @Override
        public net.morimekta.providence.PType getType() { return getDescriptor().getType(); }

        @Override
        public net.morimekta.providence.descriptor.PDescriptor<?> getDescriptor() { return mTypeProvider.descriptor(); }

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
            builder.append("Declaration._Field(")
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
                case 1: return _Field.DECL_ENUM;
                case 2: return _Field.DECL_TYPEDEF;
                case 3: return _Field.DECL_STRUCT;
                case 4: return _Field.DECL_SERVICE;
                case 5: return _Field.DECL_CONST;
                default: return null;
            }
        }

        public static _Field forName(String name) {
            switch (name) {
                case "decl_enum": return _Field.DECL_ENUM;
                case "decl_typedef": return _Field.DECL_TYPEDEF;
                case "decl_struct": return _Field.DECL_STRUCT;
                case "decl_service": return _Field.DECL_SERVICE;
                case "decl_const": return _Field.DECL_CONST;
            }
            return null;
        }
    }

    public static net.morimekta.providence.descriptor.PUnionDescriptorProvider<Declaration,_Field> provider() {
        return new _Provider();
    }

    @Override
    public net.morimekta.providence.descriptor.PUnionDescriptor<Declaration,_Field> descriptor() {
        return kDescriptor;
    }

    public static final net.morimekta.providence.descriptor.PUnionDescriptor<Declaration,_Field> kDescriptor;

    private static class _Descriptor
            extends net.morimekta.providence.descriptor.PUnionDescriptor<Declaration,_Field> {
        public _Descriptor() {
            super(null, "model", "Declaration", new _Factory(), false);
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

    private final static class _Provider extends net.morimekta.providence.descriptor.PUnionDescriptorProvider<Declaration,_Field> {
        @Override
        public net.morimekta.providence.descriptor.PUnionDescriptor<Declaration,_Field> descriptor() {
            return kDescriptor;
        }
    }

    private final static class _Factory
            extends net.morimekta.providence.PMessageBuilderFactory<Declaration> {
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
            extends net.morimekta.providence.PMessageBuilder<Declaration> {
        private _Field tUnionField;

        private net.morimekta.providence.model.EnumType mDeclEnum;
        private net.morimekta.providence.model.TypedefType mDeclTypedef;
        private net.morimekta.providence.model.StructType mDeclStruct;
        private net.morimekta.providence.model.ServiceType mDeclService;
        private net.morimekta.providence.model.ThriftField mDeclConst;


        public _Builder() {
        }

        public _Builder(Declaration base) {
            this();

            tUnionField = base.tUnionField;

            mDeclEnum = base.mDeclEnum;
            mDeclTypedef = base.mDeclTypedef;
            mDeclStruct = base.mDeclStruct;
            mDeclService = base.mDeclService;
            mDeclConst = base.mDeclConst;
        }

        public _Builder setDeclEnum(net.morimekta.providence.model.EnumType value) {
            tUnionField = _Field.DECL_ENUM;
            mDeclEnum = value;
            return this;
        }
        public boolean isSetDeclEnum() {
            return tUnionField == _Field.DECL_ENUM;
        }
        public _Builder clearDeclEnum() {
            if (tUnionField == _Field.DECL_ENUM) tUnionField = null;
            mDeclEnum = null;
            return this;
        }
        public _Builder setDeclTypedef(net.morimekta.providence.model.TypedefType value) {
            tUnionField = _Field.DECL_TYPEDEF;
            mDeclTypedef = value;
            return this;
        }
        public boolean isSetDeclTypedef() {
            return tUnionField == _Field.DECL_TYPEDEF;
        }
        public _Builder clearDeclTypedef() {
            if (tUnionField == _Field.DECL_TYPEDEF) tUnionField = null;
            mDeclTypedef = null;
            return this;
        }
        public _Builder setDeclStruct(net.morimekta.providence.model.StructType value) {
            tUnionField = _Field.DECL_STRUCT;
            mDeclStruct = value;
            return this;
        }
        public boolean isSetDeclStruct() {
            return tUnionField == _Field.DECL_STRUCT;
        }
        public _Builder clearDeclStruct() {
            if (tUnionField == _Field.DECL_STRUCT) tUnionField = null;
            mDeclStruct = null;
            return this;
        }
        public _Builder setDeclService(net.morimekta.providence.model.ServiceType value) {
            tUnionField = _Field.DECL_SERVICE;
            mDeclService = value;
            return this;
        }
        public boolean isSetDeclService() {
            return tUnionField == _Field.DECL_SERVICE;
        }
        public _Builder clearDeclService() {
            if (tUnionField == _Field.DECL_SERVICE) tUnionField = null;
            mDeclService = null;
            return this;
        }
        public _Builder setDeclConst(net.morimekta.providence.model.ThriftField value) {
            tUnionField = _Field.DECL_CONST;
            mDeclConst = value;
            return this;
        }
        public boolean isSetDeclConst() {
            return tUnionField == _Field.DECL_CONST;
        }
        public _Builder clearDeclConst() {
            if (tUnionField == _Field.DECL_CONST) tUnionField = null;
            mDeclConst = null;
            return this;
        }
        @Override
        public _Builder set(int key, Object value) {
            if (value == null) return clear(key);
            switch (key) {
                case 1: setDeclEnum((net.morimekta.providence.model.EnumType) value); break;
                case 2: setDeclTypedef((net.morimekta.providence.model.TypedefType) value); break;
                case 3: setDeclStruct((net.morimekta.providence.model.StructType) value); break;
                case 4: setDeclService((net.morimekta.providence.model.ServiceType) value); break;
                case 5: setDeclConst((net.morimekta.providence.model.ThriftField) value); break;
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
                case 1: clearDeclEnum(); break;
                case 2: clearDeclTypedef(); break;
                case 3: clearDeclStruct(); break;
                case 4: clearDeclService(); break;
                case 5: clearDeclConst(); break;
            }
            return this;
        }

        @Override
        public boolean isValid() {
            return tUnionField != null;
        }

        @Override
        public Declaration build() {
            return new Declaration(this);
        }
    }
}