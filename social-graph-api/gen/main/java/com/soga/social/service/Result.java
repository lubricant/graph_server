// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: social_graph.proto

package com.soga.social.service;

/**
 * Protobuf type {@code social.Result}
 */
public  final class Result extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:social.Result)
    ResultOrBuilder {
private static final long serialVersionUID = 0L;
  // Use Result.newBuilder() to construct.
  private Result(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private Result() {
    state_ = 0;
    hint_ = "";
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private Result(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          default: {
            if (!parseUnknownFieldProto3(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
          case 8: {
            int rawValue = input.readEnum();

            state_ = rawValue;
            break;
          }
          case 18: {
            java.lang.String s = input.readStringRequireUtf8();

            hint_ = s;
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.soga.social.service.SocialGraph.internal_static_social_Result_descriptor;
  }

  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.soga.social.service.SocialGraph.internal_static_social_Result_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.soga.social.service.Result.class, com.soga.social.service.Result.Builder.class);
  }

  /**
   * Protobuf enum {@code social.Result.Status}
   */
  public enum Status
      implements com.google.protobuf.ProtocolMessageEnum {
    /**
     * <code>SUCCESS = 0;</code>
     */
    SUCCESS(0),
    /**
     * <code>FAILURE = 1;</code>
     */
    FAILURE(1),
    /**
     * <code>IGNORED = 2;</code>
     */
    IGNORED(2),
    UNRECOGNIZED(-1),
    ;

    /**
     * <code>SUCCESS = 0;</code>
     */
    public static final int SUCCESS_VALUE = 0;
    /**
     * <code>FAILURE = 1;</code>
     */
    public static final int FAILURE_VALUE = 1;
    /**
     * <code>IGNORED = 2;</code>
     */
    public static final int IGNORED_VALUE = 2;


    public final int getNumber() {
      if (this == UNRECOGNIZED) {
        throw new java.lang.IllegalArgumentException(
            "Can't get the number of an unknown enum value.");
      }
      return value;
    }

    /**
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @java.lang.Deprecated
    public static Status valueOf(int value) {
      return forNumber(value);
    }

    public static Status forNumber(int value) {
      switch (value) {
        case 0: return SUCCESS;
        case 1: return FAILURE;
        case 2: return IGNORED;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<Status>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static final com.google.protobuf.Internal.EnumLiteMap<
        Status> internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<Status>() {
            public Status findValueByNumber(int number) {
              return Status.forNumber(number);
            }
          };

    public final com.google.protobuf.Descriptors.EnumValueDescriptor
        getValueDescriptor() {
      return getDescriptor().getValues().get(ordinal());
    }
    public final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptorForType() {
      return getDescriptor();
    }
    public static final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptor() {
      return com.soga.social.service.Result.getDescriptor().getEnumTypes().get(0);
    }

    private static final Status[] VALUES = values();

    public static Status valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      if (desc.getIndex() == -1) {
        return UNRECOGNIZED;
      }
      return VALUES[desc.getIndex()];
    }

    private final int value;

    private Status(int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:social.Result.Status)
  }

  public static final int STATE_FIELD_NUMBER = 1;
  private int state_;
  /**
   * <code>.social.Result.Status state = 1;</code>
   */
  public int getStateValue() {
    return state_;
  }
  /**
   * <code>.social.Result.Status state = 1;</code>
   */
  public com.soga.social.service.Result.Status getState() {
    com.soga.social.service.Result.Status result = com.soga.social.service.Result.Status.valueOf(state_);
    return result == null ? com.soga.social.service.Result.Status.UNRECOGNIZED : result;
  }

  public static final int HINT_FIELD_NUMBER = 2;
  private volatile java.lang.Object hint_;
  /**
   * <code>string hint = 2;</code>
   */
  public java.lang.String getHint() {
    java.lang.Object ref = hint_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      hint_ = s;
      return s;
    }
  }
  /**
   * <code>string hint = 2;</code>
   */
  public com.google.protobuf.ByteString
      getHintBytes() {
    java.lang.Object ref = hint_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      hint_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  private byte memoizedIsInitialized = -1;
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (state_ != com.soga.social.service.Result.Status.SUCCESS.getNumber()) {
      output.writeEnum(1, state_);
    }
    if (!getHintBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, hint_);
    }
    unknownFields.writeTo(output);
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (state_ != com.soga.social.service.Result.Status.SUCCESS.getNumber()) {
      size += com.google.protobuf.CodedOutputStream
        .computeEnumSize(1, state_);
    }
    if (!getHintBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, hint_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.soga.social.service.Result)) {
      return super.equals(obj);
    }
    com.soga.social.service.Result other = (com.soga.social.service.Result) obj;

    boolean result = true;
    result = result && state_ == other.state_;
    result = result && getHint()
        .equals(other.getHint());
    result = result && unknownFields.equals(other.unknownFields);
    return result;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + STATE_FIELD_NUMBER;
    hash = (53 * hash) + state_;
    hash = (37 * hash) + HINT_FIELD_NUMBER;
    hash = (53 * hash) + getHint().hashCode();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.soga.social.service.Result parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.soga.social.service.Result parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.soga.social.service.Result parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.soga.social.service.Result parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.soga.social.service.Result parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.soga.social.service.Result parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.soga.social.service.Result parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.soga.social.service.Result parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.soga.social.service.Result parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.soga.social.service.Result parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.soga.social.service.Result parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.soga.social.service.Result parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.soga.social.service.Result prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code social.Result}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:social.Result)
      com.soga.social.service.ResultOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.soga.social.service.SocialGraph.internal_static_social_Result_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.soga.social.service.SocialGraph.internal_static_social_Result_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.soga.social.service.Result.class, com.soga.social.service.Result.Builder.class);
    }

    // Construct using com.soga.social.service.Result.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    public Builder clear() {
      super.clear();
      state_ = 0;

      hint_ = "";

      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.soga.social.service.SocialGraph.internal_static_social_Result_descriptor;
    }

    public com.soga.social.service.Result getDefaultInstanceForType() {
      return com.soga.social.service.Result.getDefaultInstance();
    }

    public com.soga.social.service.Result build() {
      com.soga.social.service.Result result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public com.soga.social.service.Result buildPartial() {
      com.soga.social.service.Result result = new com.soga.social.service.Result(this);
      result.state_ = state_;
      result.hint_ = hint_;
      onBuilt();
      return result;
    }

    public Builder clone() {
      return (Builder) super.clone();
    }
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return (Builder) super.setField(field, value);
    }
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return (Builder) super.clearField(field);
    }
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return (Builder) super.clearOneof(oneof);
    }
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return (Builder) super.setRepeatedField(field, index, value);
    }
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return (Builder) super.addRepeatedField(field, value);
    }
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.soga.social.service.Result) {
        return mergeFrom((com.soga.social.service.Result)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.soga.social.service.Result other) {
      if (other == com.soga.social.service.Result.getDefaultInstance()) return this;
      if (other.state_ != 0) {
        setStateValue(other.getStateValue());
      }
      if (!other.getHint().isEmpty()) {
        hint_ = other.hint_;
        onChanged();
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    public final boolean isInitialized() {
      return true;
    }

    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      com.soga.social.service.Result parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.soga.social.service.Result) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private int state_ = 0;
    /**
     * <code>.social.Result.Status state = 1;</code>
     */
    public int getStateValue() {
      return state_;
    }
    /**
     * <code>.social.Result.Status state = 1;</code>
     */
    public Builder setStateValue(int value) {
      state_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>.social.Result.Status state = 1;</code>
     */
    public com.soga.social.service.Result.Status getState() {
      com.soga.social.service.Result.Status result = com.soga.social.service.Result.Status.valueOf(state_);
      return result == null ? com.soga.social.service.Result.Status.UNRECOGNIZED : result;
    }
    /**
     * <code>.social.Result.Status state = 1;</code>
     */
    public Builder setState(com.soga.social.service.Result.Status value) {
      if (value == null) {
        throw new NullPointerException();
      }
      
      state_ = value.getNumber();
      onChanged();
      return this;
    }
    /**
     * <code>.social.Result.Status state = 1;</code>
     */
    public Builder clearState() {
      
      state_ = 0;
      onChanged();
      return this;
    }

    private java.lang.Object hint_ = "";
    /**
     * <code>string hint = 2;</code>
     */
    public java.lang.String getHint() {
      java.lang.Object ref = hint_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        hint_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string hint = 2;</code>
     */
    public com.google.protobuf.ByteString
        getHintBytes() {
      java.lang.Object ref = hint_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        hint_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string hint = 2;</code>
     */
    public Builder setHint(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      hint_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string hint = 2;</code>
     */
    public Builder clearHint() {
      
      hint_ = getDefaultInstance().getHint();
      onChanged();
      return this;
    }
    /**
     * <code>string hint = 2;</code>
     */
    public Builder setHintBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      hint_ = value;
      onChanged();
      return this;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFieldsProto3(unknownFields);
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:social.Result)
  }

  // @@protoc_insertion_point(class_scope:social.Result)
  private static final com.soga.social.service.Result DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.soga.social.service.Result();
  }

  public static com.soga.social.service.Result getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<Result>
      PARSER = new com.google.protobuf.AbstractParser<Result>() {
    public Result parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new Result(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<Result> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<Result> getParserForType() {
    return PARSER;
  }

  public com.soga.social.service.Result getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

