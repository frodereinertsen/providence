package net.morimekta.providence;

/**
 * Base exception thrown on non-declared exceptions on a service call, and
 * other server-side service call issues.
 */
@SuppressWarnings("unused")
public class PApplicationException
        extends java.io.IOException
        implements net.morimekta.providence.PMessage<PApplicationException,PApplicationException._Field>,
                   net.morimekta.providence.PException,
                   Comparable<PApplicationException>,
                   net.morimekta.providence.serializer.rw.BinaryWriter {
    private final static long serialVersionUID = -8724424103018535688L;

    private final static net.morimekta.providence.PApplicationExceptionType kDefaultId = net.morimekta.providence.PApplicationExceptionType.UNKNOWN;

    private final String mMessage;
    private final net.morimekta.providence.PApplicationExceptionType mId;

    private volatile int tHashCode;

    public PApplicationException(String pMessage,
                                 net.morimekta.providence.PApplicationExceptionType pId) {
        super(pMessage);

        mMessage = pMessage;
        mId = pId;
    }

    private PApplicationException(_Builder builder) {
        super(builder.mMessage);

        mMessage = builder.mMessage;
        mId = builder.mId;
    }

    public boolean hasMessage() {
        return mMessage != null;
    }

    /**
     * Exception message.
     *
     * @return The field value
     */
    public String getMessage() {
        return mMessage;
    }

    public boolean hasId() {
        return mId != null;
    }

    /**
     * The application exception type.
     *
     * @return The field value
     */
    public net.morimekta.providence.PApplicationExceptionType getId() {
        return hasId() ? mId : kDefaultId;
    }

    @Override
    public boolean has(int key) {
        switch(key) {
            case 1: return hasMessage();
            case 2: return hasId();
            default: return false;
        }
    }

    @Override
    public int num(int key) {
        switch(key) {
            case 1: return hasMessage() ? 1 : 0;
            case 2: return hasId() ? 1 : 0;
            default: return 0;
        }
    }

    @Override
    public Object get(int key) {
        switch(key) {
            case 1: return getMessage();
            case 2: return getId();
            default: return null;
        }
    }

    @Override
    public boolean compact() {
        return false;
    }

    @Override
    public String origGetMessage() {
        return super.getMessage();
    }

    @Override
    public String origGetLocalizedMessage() {
        return super.getLocalizedMessage();
    }

    @Override
    public PApplicationException initCause(Throwable cause) {
        return (PApplicationException) super.initCause(cause);
    }

    @Override
    public PApplicationException fillInStackTrace() {
        return (PApplicationException) super.fillInStackTrace();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null || !o.getClass().equals(getClass())) return false;
        PApplicationException other = (PApplicationException) o;
        return java.util.Objects.equals(mMessage, other.mMessage) &&
               java.util.Objects.equals(mId, other.mId);
    }

    @Override
    public int hashCode() {
        if (tHashCode == 0) {
            tHashCode = java.util.Objects.hash(
                    PApplicationException.class,
                    _Field.MESSAGE, mMessage,
                    _Field.ID, mId);
        }
        return tHashCode;
    }

    @Override
    public String toString() {
        return "service.PApplicationException" + asString();
    }

    @Override
    public String asString() {
        StringBuilder out = new StringBuilder();
        out.append("{");

        boolean first = true;
        if (hasMessage()) {
            first = false;
            out.append("message:")
               .append('\"')
               .append(net.morimekta.util.Strings.escape(mMessage))
               .append('\"');
        }
        if (hasId()) {
            if (!first) out.append(',');
            out.append("id:")
               .append(mId.asString());
        }
        out.append('}');
        return out.toString();
    }

    @Override
    public int compareTo(PApplicationException other) {
        int c;

        c = Boolean.compare(mMessage != null, other.mMessage != null);
        if (c != 0) return c;
        if (mMessage != null) {
            c = mMessage.compareTo(other.mMessage);
            if (c != 0) return c;
        }

        c = Boolean.compare(mId != null, other.mId != null);
        if (c != 0) return c;
        if (mId != null) {
            c = Integer.compare(mId.ordinal(), mId.ordinal());
            if (c != 0) return c;
        }

        return 0;
    }

    @Override
    public int writeBinary(net.morimekta.util.io.BigEndianBinaryWriter writer) throws java.io.IOException {
        int length = 0;

        if (hasMessage()) {
            length += writer.writeByte((byte) 11);
            length += writer.writeShort((short) 1);
            net.morimekta.util.Binary tmp_1 = net.morimekta.util.Binary.wrap(mMessage.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            length += writer.writeUInt32(tmp_1.length());
            length += writer.writeBinary(tmp_1);
        }

        if (hasId()) {
            length += writer.writeByte((byte) 8);
            length += writer.writeShort((short) 2);
            length += writer.writeInt(mId.getValue());
        }

        length += writer.writeByte((byte) 0);
        return length;
    }

    @Override
    public _Builder mutate() {
        return new _Builder(this);
    }

    public enum _Field implements net.morimekta.providence.descriptor.PField {
        MESSAGE(1, net.morimekta.providence.descriptor.PRequirement.DEFAULT, "message", net.morimekta.providence.descriptor.PPrimitive.STRING.provider(), null),
        ID(2, net.morimekta.providence.descriptor.PRequirement.DEFAULT, "id", net.morimekta.providence.PApplicationExceptionType.provider(), new net.morimekta.providence.descriptor.PDefaultValueProvider<>(kDefaultId)),
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
            return net.morimekta.providence.descriptor.PField.toString(this);
        }

        public static _Field forKey(int key) {
            switch (key) {
                case 1: return _Field.MESSAGE;
                case 2: return _Field.ID;
            }
            return null;
        }

        public static _Field forName(String name) {
            switch (name) {
                case "message": return _Field.MESSAGE;
                case "id": return _Field.ID;
            }
            return null;
        }
    }

    public static net.morimekta.providence.descriptor.PExceptionDescriptorProvider<PApplicationException,_Field> provider() {
        return new _Provider();
    }

    @Override
    public net.morimekta.providence.descriptor.PExceptionDescriptor<PApplicationException,_Field> descriptor() {
        return kDescriptor;
    }

    public static final net.morimekta.providence.descriptor.PExceptionDescriptor<PApplicationException,_Field> kDescriptor;

    private static class _Descriptor
            extends net.morimekta.providence.descriptor.PExceptionDescriptor<PApplicationException,_Field> {
        public _Descriptor() {
            super("service", "PApplicationException", new _Factory(), true);
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

    private final static class _Provider extends net.morimekta.providence.descriptor.PExceptionDescriptorProvider<PApplicationException,_Field> {
        @Override
        public net.morimekta.providence.descriptor.PExceptionDescriptor<PApplicationException,_Field> descriptor() {
            return kDescriptor;
        }
    }

    private final static class _Factory
            extends net.morimekta.providence.PMessageBuilderFactory<PApplicationException,_Field> {
        @Override
        public _Builder builder() {
            return new _Builder();
        }
    }

    /**
     * Make a service.PApplicationException builder.
     * @return The builder instance.
     */
    public static _Builder builder() {
        return new _Builder();
    }

    /**
     * Base exception thrown on non-declared exceptions on a service call, and
     * other server-side service call issues.
     */
    public static class _Builder
            extends net.morimekta.providence.PMessageBuilder<PApplicationException,_Field>
            implements net.morimekta.providence.serializer.rw.BinaryReader {
        private java.util.BitSet optionals;

        private String mMessage;
        private net.morimekta.providence.PApplicationExceptionType mId;

        /**
         * Make a service.PApplicationException builder.
         */
        public _Builder() {
            optionals = new java.util.BitSet(2);
        }

        /**
         * Make a mutating builder off a base service.PApplicationException.
         *
         * @param base The base PApplicationException
         */
        public _Builder(PApplicationException base) {
            this();

            if (base.hasMessage()) {
                optionals.set(0);
                mMessage = base.mMessage;
            }
            if (base.hasId()) {
                optionals.set(1);
                mId = base.mId;
            }
        }

        @Override
        public _Builder merge(PApplicationException from) {
            if (from.hasMessage()) {
                optionals.set(0);
                mMessage = from.getMessage();
            }

            if (from.hasId()) {
                optionals.set(1);
                mId = from.getId();
            }
            return this;
        }

        /**
         * Exception message.
         *
         * @param value The new value
         * @return The builder
         */
        public _Builder setMessage(String value) {
            optionals.set(0);
            mMessage = value;
            return this;
        }

        /**
         * Exception message.
         *
         * @return True iff message has been set.
         */
        public boolean isSetMessage() {
            return optionals.get(0);
        }

        /**
         * Exception message.
         *
         * @return The builder
         */
        public _Builder clearMessage() {
            optionals.clear(0);
            mMessage = null;
            return this;
        }

        /**
         * Exception message.
         *
         * @return The field value
         */
        public String getMessage() {
            return mMessage;
        }

        /**
         * The application exception type.
         *
         * @param value The new value
         * @return The builder
         */
        public _Builder setId(net.morimekta.providence.PApplicationExceptionType value) {
            optionals.set(1);
            mId = value;
            return this;
        }

        /**
         * The application exception type.
         *
         * @return True iff id has been set.
         */
        public boolean isSetId() {
            return optionals.get(1);
        }

        /**
         * The application exception type.
         *
         * @return The builder
         */
        public _Builder clearId() {
            optionals.clear(1);
            mId = null;
            return this;
        }

        /**
         * The application exception type.
         *
         * @return The field value
         */
        public net.morimekta.providence.PApplicationExceptionType getId() {
            return isSetId() ? mId : kDefaultId;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) return true;
            if (o == null || !o.getClass().equals(getClass())) return false;
            PApplicationException._Builder other = (PApplicationException._Builder) o;
            return java.util.Objects.equals(optionals, other.optionals) &&
                   java.util.Objects.equals(mMessage, other.mMessage) &&
                   java.util.Objects.equals(mId, other.mId);
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(
                    PApplicationException.class, optionals,
                    _Field.MESSAGE, mMessage,
                    _Field.ID, mId);
        }

        @Override
        @SuppressWarnings("unchecked")
        public net.morimekta.providence.PMessageBuilder mutator(int key) {
            switch (key) {
                default: throw new IllegalArgumentException("Not a message field ID: " + key);
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public _Builder set(int key, Object value) {
            if (value == null) return clear(key);
            switch (key) {
                case 1: setMessage((String) value); break;
                case 2: setId((net.morimekta.providence.PApplicationExceptionType) value); break;
                default: break;
            }
            return this;
        }

        @Override
        public boolean isSet(int key) {
            switch (key) {
                case 1: return optionals.get(0);
                case 2: return optionals.get(1);
                default: break;
            }
            return false;
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
                case 1: clearMessage(); break;
                case 2: clearId(); break;
                default: break;
            }
            return this;
        }

        @Override
        public boolean valid() {
            return true;
        }

        @Override
        public void validate() {
        }

        @Override
        public net.morimekta.providence.descriptor.PExceptionDescriptor<PApplicationException,_Field> descriptor() {
            return kDescriptor;
        }

        @Override
        public void readBinary(net.morimekta.util.io.BigEndianBinaryReader reader, boolean strict) throws java.io.IOException {
            byte type = reader.expectByte();
            while (type != 0) {
                int field = reader.expectShort();
                switch (field) {
                    case 1: {
                        if (type == 11) {
                            int len_1 = reader.expectUInt32();
                            mMessage = new String(reader.expectBytes(len_1), java.nio.charset.StandardCharsets.UTF_8);
                            optionals.set(0);
                        } else {
                            throw new net.morimekta.providence.serializer.SerializerException("Wrong type " + type + " for service.PApplicationException.message, should be 12");
                        }
                        break;
                    }
                    case 2: {
                        if (type == 8) {
                            mId = net.morimekta.providence.PApplicationExceptionType.forValue(reader.expectInt());
                            optionals.set(1);
                        } else {
                            throw new net.morimekta.providence.serializer.SerializerException("Wrong type " + type + " for service.PApplicationException.id, should be 12");
                        }
                        break;
                    }
                    default: {
                        if (strict) {
                            throw new net.morimekta.providence.serializer.SerializerException("No field with id " + field + " exists in service.PApplicationException");
                        } else {
                            net.morimekta.providence.serializer.rw.BinaryFormatUtils.readFieldValue(reader, new net.morimekta.providence.serializer.rw.BinaryFormatUtils.FieldInfo(field, type), null, false);
                        }
                        break;
                    }
                }
                type = reader.expectByte();
            }
        }

        @Override
        public PApplicationException build() {
            return new PApplicationException(this);
        }
    }
}
