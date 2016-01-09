package net.morimekta.providence.model;

import java.io.Serializable;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.morimekta.providence.PMessage;
import net.morimekta.providence.PMessageBuilder;
import net.morimekta.providence.PMessageBuilderFactory;
import net.morimekta.providence.PType;
import net.morimekta.providence.descriptor.PDefaultValueProvider;
import net.morimekta.providence.descriptor.PDescriptor;
import net.morimekta.providence.descriptor.PDescriptorProvider;
import net.morimekta.providence.descriptor.PField;
import net.morimekta.providence.descriptor.PList;
import net.morimekta.providence.descriptor.PPrimitive;
import net.morimekta.providence.descriptor.PRequirement;
import net.morimekta.providence.descriptor.PStructDescriptor;
import net.morimekta.providence.descriptor.PStructDescriptorProvider;
import net.morimekta.providence.descriptor.PValueProvider;
import net.morimekta.providence.util.PTypeUtils;

/** (oneway)? <return_type> <name>'('<param>*')' (throws '(' <exception>+ ')')? */
@SuppressWarnings("unused")
public class ServiceMethod
        implements PMessage<ServiceMethod>, Serializable {
    private final static long serialVersionUID = -8952857258512990537L;

    private final static boolean kDefaultIsOneway = false;

    private final String mComment;
    private final boolean mIsOneway;
    private final String mReturnType;
    private final String mName;
    private final List<ThriftField> mParams;
    private final List<ThriftField> mExceptions;

    private ServiceMethod(_Builder builder) {
        mComment = builder.mComment;
        mIsOneway = builder.mIsOneway;
        mReturnType = builder.mReturnType;
        mName = builder.mName;
        mParams = Collections.unmodifiableList(new LinkedList<>(builder.mParams));
        mExceptions = Collections.unmodifiableList(new LinkedList<>(builder.mExceptions));
    }

    public ServiceMethod(String pComment,
                         boolean pIsOneway,
                         String pReturnType,
                         String pName,
                         List<ThriftField> pParams,
                         List<ThriftField> pExceptions) {
        mComment = pComment;
        mIsOneway = pIsOneway;
        mReturnType = pReturnType;
        mName = pName;
        mParams = Collections.unmodifiableList(new LinkedList<>(pParams));
        mExceptions = Collections.unmodifiableList(new LinkedList<>(pExceptions));
    }

    public boolean hasComment() {
        return mComment != null;
    }

    public String getComment() {
        return mComment;
    }

    public boolean hasIsOneway() {
        return mIsOneway != kDefaultIsOneway;
    }

    public boolean isIsOneway() {
        return mIsOneway;
    }

    public boolean hasReturnType() {
        return mReturnType != null;
    }

    public String getReturnType() {
        return mReturnType;
    }

    public boolean hasName() {
        return mName != null;
    }

    public String getName() {
        return mName;
    }

    public int numParams() {
        return mParams != null ? mParams.size() : 0;
    }

    public List<ThriftField> getParams() {
        return mParams;
    }

    public int numExceptions() {
        return mExceptions != null ? mExceptions.size() : 0;
    }

    public List<ThriftField> getExceptions() {
        return mExceptions;
    }

    @Override
    public boolean has(int key) {
        switch(key) {
            case 1: return hasComment();
            case 2: return true;
            case 3: return hasReturnType();
            case 4: return hasName();
            case 5: return numParams() > 0;
            case 6: return numExceptions() > 0;
            default: return false;
        }
    }

    @Override
    public int num(int key) {
        switch(key) {
            case 1: return hasComment() ? 1 : 0;
            case 2: return 1;
            case 3: return hasReturnType() ? 1 : 0;
            case 4: return hasName() ? 1 : 0;
            case 5: return numParams();
            case 6: return numExceptions();
            default: return 0;
        }
    }

    @Override
    public Object get(int key) {
        switch(key) {
            case 1: return getComment();
            case 2: return isIsOneway();
            case 3: return getReturnType();
            case 4: return getName();
            case 5: return getParams();
            case 6: return getExceptions();
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
        if (o == null || !(o instanceof ServiceMethod)) return false;
        ServiceMethod other = (ServiceMethod) o;
        return PTypeUtils.equals(mComment, other.mComment) &&
               PTypeUtils.equals(mIsOneway, other.mIsOneway) &&
               PTypeUtils.equals(mReturnType, other.mReturnType) &&
               PTypeUtils.equals(mName, other.mName) &&
               PTypeUtils.equals(mParams, other.mParams) &&
               PTypeUtils.equals(mExceptions, other.mExceptions);
    }

    @Override
    public int hashCode() {
        return ServiceMethod.class.hashCode() +
               PTypeUtils.hashCode(_Field.COMMENT, mComment) +
               PTypeUtils.hashCode(_Field.IS_ONEWAY, mIsOneway) +
               PTypeUtils.hashCode(_Field.RETURN_TYPE, mReturnType) +
               PTypeUtils.hashCode(_Field.NAME, mName) +
               PTypeUtils.hashCode(_Field.PARAMS, mParams) +
               PTypeUtils.hashCode(_Field.EXCEPTIONS, mExceptions);
    }

    @Override
    public String toString() {
        return descriptor().getQualifiedName(null) + PTypeUtils.toString(this);
    }

    public enum _Field implements PField {
        COMMENT(1, PRequirement.DEFAULT, "comment", PPrimitive.STRING.provider(), null),
        IS_ONEWAY(2, PRequirement.DEFAULT, "is_oneway", PPrimitive.BOOL.provider(), new PDefaultValueProvider<>(kDefaultIsOneway)),
        RETURN_TYPE(3, PRequirement.DEFAULT, "return_type", PPrimitive.STRING.provider(), null),
        NAME(4, PRequirement.REQUIRED, "name", PPrimitive.STRING.provider(), null),
        PARAMS(5, PRequirement.DEFAULT, "params", PList.provider(ThriftField.provider()), null),
        EXCEPTIONS(6, PRequirement.DEFAULT, "exceptions", PList.provider(ThriftField.provider()), null),
        ;

        private final int mKey;
        private final PRequirement mRequired;
        private final String mName;
        private final PDescriptorProvider<?> mTypeProvider;
        private final PValueProvider<?> mDefaultValue;

        _Field(int key, PRequirement required, String name, PDescriptorProvider<?> typeProvider, PValueProvider<?> defaultValue) {
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
        public PRequirement getRequirement() { return mRequired; }

        @Override
        public PType getType() { return getDescriptor().getType(); }

        @Override
        public PDescriptor<?> getDescriptor() { return mTypeProvider.descriptor(); }

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
            builder.append(ServiceMethod.class.getSimpleName())
                   .append('{')
                   .append(mKey)
                   .append(": ");
            if (mRequired != PRequirement.DEFAULT) {
                builder.append(mRequired.label).append(" ");
            }
            builder.append(getDescriptor().getQualifiedName(null))
                   .append(' ')
                   .append(mName)
                   .append('}');
            return builder.toString();
        }

        public static _Field forKey(int key) {
            switch (key) {
                case 1: return _Field.COMMENT;
                case 2: return _Field.IS_ONEWAY;
                case 3: return _Field.RETURN_TYPE;
                case 4: return _Field.NAME;
                case 5: return _Field.PARAMS;
                case 6: return _Field.EXCEPTIONS;
                default: return null;
            }
        }

        public static _Field forName(String name) {
            switch (name) {
                case "comment": return _Field.COMMENT;
                case "is_oneway": return _Field.IS_ONEWAY;
                case "return_type": return _Field.RETURN_TYPE;
                case "name": return _Field.NAME;
                case "params": return _Field.PARAMS;
                case "exceptions": return _Field.EXCEPTIONS;
            }
            return null;
        }
    }

    public static PStructDescriptorProvider<ServiceMethod,_Field> provider() {
        return new _Provider();
    }

    @Override
    public PStructDescriptor<ServiceMethod,_Field> descriptor() {
        return kDescriptor;
    }

    public static final PStructDescriptor<ServiceMethod,_Field> kDescriptor;

    private static class _Descriptor
            extends PStructDescriptor<ServiceMethod,_Field> {
        public _Descriptor() {
            super(null, "model", "ServiceMethod", new _Factory(), false, false);
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

    private final static class _Provider extends PStructDescriptorProvider<ServiceMethod,_Field> {
        @Override
        public PStructDescriptor<ServiceMethod,_Field> descriptor() {
            return kDescriptor;
        }
    }

    private final static class _Factory
            extends PMessageBuilderFactory<ServiceMethod> {
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
            extends PMessageBuilder<ServiceMethod> {
        private BitSet optionals;

        private String mComment;
        private boolean mIsOneway;
        private String mReturnType;
        private String mName;
        private List<ThriftField> mParams;
        private List<ThriftField> mExceptions;


        public _Builder() {
            optionals = new BitSet(6);
            mIsOneway = kDefaultIsOneway;
            mParams = new LinkedList<>();
            mExceptions = new LinkedList<>();
        }

        public _Builder(ServiceMethod base) {
            this();

            if (base.hasComment()) {
                optionals.set(0);
                mComment = base.mComment;
            }
            optionals.set(1);
            mIsOneway = base.mIsOneway;
            if (base.hasReturnType()) {
                optionals.set(2);
                mReturnType = base.mReturnType;
            }
            if (base.hasName()) {
                optionals.set(3);
                mName = base.mName;
            }
            if (base.numParams() > 0) {
                optionals.set(4);
                mParams.addAll(base.mParams);
            }
            if (base.numExceptions() > 0) {
                optionals.set(5);
                mExceptions.addAll(base.mExceptions);
            }
        }

        public _Builder setComment(String value) {
            optionals.set(0);
            mComment = value;
            return this;
        }
        public _Builder clearComment() {
            optionals.set(0, false);
            mComment = null;
            return this;
        }
        public _Builder setIsOneway(boolean value) {
            optionals.set(1);
            mIsOneway = value;
            return this;
        }
        public _Builder clearIsOneway() {
            optionals.set(1, false);
            mIsOneway = kDefaultIsOneway;
            return this;
        }
        public _Builder setReturnType(String value) {
            optionals.set(2);
            mReturnType = value;
            return this;
        }
        public _Builder clearReturnType() {
            optionals.set(2, false);
            mReturnType = null;
            return this;
        }
        public _Builder setName(String value) {
            optionals.set(3);
            mName = value;
            return this;
        }
        public _Builder clearName() {
            optionals.set(3, false);
            mName = null;
            return this;
        }
        public _Builder setParams(Collection<ThriftField> value) {
            optionals.set(4);
            mParams.clear();
            mParams.addAll(value);
            return this;
        }
        public _Builder addToParams(ThriftField... values) {
            optionals.set(4);
            for (ThriftField item : values) {
                mParams.add(item);
            }
            return this;
        }

        public _Builder clearParams() {
            optionals.set(4, false);
            mParams.clear();
            return this;
        }
        public _Builder setExceptions(Collection<ThriftField> value) {
            optionals.set(5);
            mExceptions.clear();
            mExceptions.addAll(value);
            return this;
        }
        public _Builder addToExceptions(ThriftField... values) {
            optionals.set(5);
            for (ThriftField item : values) {
                mExceptions.add(item);
            }
            return this;
        }

        public _Builder clearExceptions() {
            optionals.set(5, false);
            mExceptions.clear();
            return this;
        }
        @Override
        public _Builder set(int key, Object value) {
            if (value == null) return clear(key);
            switch (key) {
                case 1: setComment((String) value); break;
                case 2: setIsOneway((boolean) value); break;
                case 3: setReturnType((String) value); break;
                case 4: setName((String) value); break;
                case 5: setParams((List<ThriftField>) value); break;
                case 6: setExceptions((List<ThriftField>) value); break;
            }
            return this;
        }

        @Override
        public _Builder clear(int key) {
            switch (key) {
                case 1: clearComment(); break;
                case 2: clearIsOneway(); break;
                case 3: clearReturnType(); break;
                case 4: clearName(); break;
                case 5: clearParams(); break;
                case 6: clearExceptions(); break;
            }
            return this;
        }

        @Override
        public boolean isValid() {
            return optionals.get(3);
        }

        @Override
        public ServiceMethod build() {
            return new ServiceMethod(this);
        }
    }
}