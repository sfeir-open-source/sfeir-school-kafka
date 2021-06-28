/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.sfeir.kafka;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class CardPayment extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -2015215522660309401L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"CardPayment\",\"namespace\":\"com.sfeir.kafka\",\"fields\":[{\"name\":\"id\",\"type\":\"string\"},{\"name\":\"paidPrice\",\"type\":\"double\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<CardPayment> ENCODER =
      new BinaryMessageEncoder<CardPayment>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<CardPayment> DECODER =
      new BinaryMessageDecoder<CardPayment>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<CardPayment> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<CardPayment> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<CardPayment> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<CardPayment>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this CardPayment to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a CardPayment from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a CardPayment instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static CardPayment fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

   private java.lang.CharSequence id;
   private double paidPrice;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public CardPayment() {}

  /**
   * All-args constructor.
   * @param id The new value for id
   * @param paidPrice The new value for paidPrice
   */
  public CardPayment(java.lang.CharSequence id, java.lang.Double paidPrice) {
    this.id = id;
    this.paidPrice = paidPrice;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return id;
    case 1: return paidPrice;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: id = (java.lang.CharSequence)value$; break;
    case 1: paidPrice = (java.lang.Double)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'id' field.
   * @return The value of the 'id' field.
   */
  public java.lang.CharSequence getId() {
    return id;
  }


  /**
   * Sets the value of the 'id' field.
   * @param value the value to set.
   */
  public void setId(java.lang.CharSequence value) {
    this.id = value;
  }

  /**
   * Gets the value of the 'paidPrice' field.
   * @return The value of the 'paidPrice' field.
   */
  public double getPaidPrice() {
    return paidPrice;
  }


  /**
   * Sets the value of the 'paidPrice' field.
   * @param value the value to set.
   */
  public void setPaidPrice(double value) {
    this.paidPrice = value;
  }

  /**
   * Creates a new CardPayment RecordBuilder.
   * @return A new CardPayment RecordBuilder
   */
  public static com.sfeir.kafka.CardPayment.Builder newBuilder() {
    return new com.sfeir.kafka.CardPayment.Builder();
  }

  /**
   * Creates a new CardPayment RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new CardPayment RecordBuilder
   */
  public static com.sfeir.kafka.CardPayment.Builder newBuilder(com.sfeir.kafka.CardPayment.Builder other) {
    if (other == null) {
      return new com.sfeir.kafka.CardPayment.Builder();
    } else {
      return new com.sfeir.kafka.CardPayment.Builder(other);
    }
  }

  /**
   * Creates a new CardPayment RecordBuilder by copying an existing CardPayment instance.
   * @param other The existing instance to copy.
   * @return A new CardPayment RecordBuilder
   */
  public static com.sfeir.kafka.CardPayment.Builder newBuilder(com.sfeir.kafka.CardPayment other) {
    if (other == null) {
      return new com.sfeir.kafka.CardPayment.Builder();
    } else {
      return new com.sfeir.kafka.CardPayment.Builder(other);
    }
  }

  /**
   * RecordBuilder for CardPayment instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<CardPayment>
    implements org.apache.avro.data.RecordBuilder<CardPayment> {

    private java.lang.CharSequence id;
    private double paidPrice;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.sfeir.kafka.CardPayment.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.paidPrice)) {
        this.paidPrice = data().deepCopy(fields()[1].schema(), other.paidPrice);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
    }

    /**
     * Creates a Builder by copying an existing CardPayment instance
     * @param other The existing instance to copy.
     */
    private Builder(com.sfeir.kafka.CardPayment other) {
      super(SCHEMA$);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.paidPrice)) {
        this.paidPrice = data().deepCopy(fields()[1].schema(), other.paidPrice);
        fieldSetFlags()[1] = true;
      }
    }

    /**
      * Gets the value of the 'id' field.
      * @return The value.
      */
    public java.lang.CharSequence getId() {
      return id;
    }


    /**
      * Sets the value of the 'id' field.
      * @param value The value of 'id'.
      * @return This builder.
      */
    public com.sfeir.kafka.CardPayment.Builder setId(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.id = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'id' field has been set.
      * @return True if the 'id' field has been set, false otherwise.
      */
    public boolean hasId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'id' field.
      * @return This builder.
      */
    public com.sfeir.kafka.CardPayment.Builder clearId() {
      id = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'paidPrice' field.
      * @return The value.
      */
    public double getPaidPrice() {
      return paidPrice;
    }


    /**
      * Sets the value of the 'paidPrice' field.
      * @param value The value of 'paidPrice'.
      * @return This builder.
      */
    public com.sfeir.kafka.CardPayment.Builder setPaidPrice(double value) {
      validate(fields()[1], value);
      this.paidPrice = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'paidPrice' field has been set.
      * @return True if the 'paidPrice' field has been set, false otherwise.
      */
    public boolean hasPaidPrice() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'paidPrice' field.
      * @return This builder.
      */
    public com.sfeir.kafka.CardPayment.Builder clearPaidPrice() {
      fieldSetFlags()[1] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public CardPayment build() {
      try {
        CardPayment record = new CardPayment();
        record.id = fieldSetFlags()[0] ? this.id : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.paidPrice = fieldSetFlags()[1] ? this.paidPrice : (java.lang.Double) defaultValue(fields()[1]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<CardPayment>
    WRITER$ = (org.apache.avro.io.DatumWriter<CardPayment>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<CardPayment>
    READER$ = (org.apache.avro.io.DatumReader<CardPayment>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeString(this.id);

    out.writeDouble(this.paidPrice);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.id = in.readString(this.id instanceof Utf8 ? (Utf8)this.id : null);

      this.paidPrice = in.readDouble();

    } else {
      for (int i = 0; i < 2; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.id = in.readString(this.id instanceof Utf8 ? (Utf8)this.id : null);
          break;

        case 1:
          this.paidPrice = in.readDouble();
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










