/**
 * Copyright (c) 2012 MNCC
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * @author http://www.mncc.fr
 */
package fr.mncc.gwttoolbox.primitives.shared;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.java.contract.Ensures;
import com.google.java.contract.Invariant;
import com.google.java.contract.Requires;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Invariant({
    "id_ != null && id_ >= 0",
    "kind_ != null",
    "properties_ != null"
})
public class Entity implements Comparable<Entity>, Serializable, IsSerializable, HasId {

  private Long id_ = DefaultValues.longDefaultValue();  // Unique identifier
  private String kind_ = DefaultValues.stringDefaultValue();  // Entity type
  private HashMap<String, String> properties_ = new HashMap<String, String>();  // List of properties (key, "TYPE:VALUE")

  protected Entity() {

  }

  @Requires("entity != null")
  @Ensures({
      "getKind().equals(entity.getKind())",
      "getId() == entity.getId()"
  })
  public Entity(Entity entity) {
    this(entity.getKind(), entity.getId(), entity.getProperties());
  }

  @Requires("kind != null")
  @Ensures("getKind().equals(kind)")
  public Entity(String kind) {
    setKind(kind);
  }

  @Requires({
      "kind != null",
      "id >= 0"
  })
  @Ensures({
      "getKind().equals(kind)",
      "getId() == id"
  })
  public Entity(String kind, long id) {
    setKind(kind);
    setId(id);
  }

  @Requires({
      "kind != null",
      "id >= 0",
      "properties != null"
  })
  @Ensures({
      "getKind().equals(kind)",
      "getId() == id"
  })
  public Entity(String kind, long id, Map<String, String> properties) {
    setId(id);
    setKind(kind);
    setProperties(new HashMap<String, String>(properties)); // Defensive copy...
  }

  @Ensures("result >= 0")
  @Override
  public long getId() {
    return id_;
  }

  @Requires("id >= 0")
  @Ensures("getId() == id")
  public void setId(long id) {
    id_ = id;
  }

  @Ensures("result != null")
  public Map<String, String> getProperties() {
    return properties_;
  }

  @Requires({
      "propertyName != null",
      "propertyValue != null",
      "!ObjectUtils.toString(propertyValue).equals(ObjectUtils.toString(null))"
  })
  @Ensures({
      "properties_.containsKey(propertyName)",
      "ObjectUtils.toString(getAsObject(propertyName)).equals(ObjectUtils.toString(propertyValue))"
  })
  public void put(String propertyName, Object propertyValue) {
    properties_.put(propertyName, ObjectUtils.toString(propertyValue));
  }

  @Requires("propertyName != null")
  @Ensures({
      "!properties_.containsKey(propertyName)",
      "getAsObject(propertyName) == null"
  })
  public void remove(String propertyName) {
    properties_.remove(propertyName);
  }

  public Object getAsObject(String propertyName) {
    return
        propertyName == null ? null :
        !properties_.containsKey(propertyName) ? null :
            ObjectUtils.fromString(properties_.get(propertyName));
  }

  @Requires("propertyName != null")
  @Ensures("result != null")
  public String getAsString(String propertyName) {
    return ObjectUtils.asString(properties_.get(propertyName));
  }

  @Requires("propertyName != null")
  public int getAsInt(String propertyName) {
    Object object = getAsObject(propertyName);
    return
        object instanceof Integer ? (Integer)object :
        object instanceof Long ? ((Long)object).intValue() :
        object instanceof Double ? ((Double)object).intValue() :
        object instanceof Float ? ((Float)object).intValue() :
        object instanceof String ? StringUtils.parseInt((String)object) :
            DefaultValues.intDefaultValue();
  }

  @Requires("propertyName != null")
  public long getAsLong(String propertyName) {
    Object object = getAsObject(propertyName);
    return
        object instanceof Long ? (Long)object :
        object instanceof Integer ? ((Integer)object).longValue() :
        object instanceof Double ? ((Double)object).longValue() :
        object instanceof Float ? ((Float)object).longValue() :
        object instanceof String ? StringUtils.parseLong((String)object) :
            DefaultValues.longDefaultValue();
  }

  @Requires("propertyName != null")
  public double getAsDouble(String propertyName) {
    Object object = getAsObject(propertyName);
    return
        object instanceof Double ? (Double)object :
        object instanceof Float ? ((Float)object).doubleValue() :
        object instanceof Long ? ((Long)object).doubleValue() :
        object instanceof Integer ? ((Integer)object).doubleValue() :
        object instanceof String ? StringUtils.parseDouble((String)object) :
            DefaultValues.doubleDefaultValue();
  }

  @Requires("propertyName != null")
  public float getAsFloat(String propertyName) {
    Object object = getAsObject(propertyName);
    return
        object instanceof Float ? (Float)object :
        object instanceof Double ? ((Double)object).floatValue() :
        object instanceof Long ? ((Long)object).floatValue() :
        object instanceof Integer ? ((Integer)object).floatValue() :
        object instanceof String ? StringUtils.parseFloat((String)object) :
            DefaultValues.floatDefaultValue();
  }

  @Requires("propertyName != null")
  public boolean getAsBoolean(String propertyName) {
    Object object = getAsObject(propertyName);
    return
        object instanceof Boolean ? (Boolean)object :
        object instanceof String ? StringUtils.parseBoolean((String)object) :
            DefaultValues.boolDefaultValue();
  }

  @Requires("propertyName != null")
  public Date getAsDate(String propertyName) {
    Object object = getAsObject(propertyName);
    return
        object instanceof Timestamp ? new Date(((Timestamp) object).getTime()) :
        object instanceof Time ? new Date(((Time) object).getTime()) :
        object instanceof Date ? (Date)object :
            DefaultValues.dateDefaultValue();

  }

  @Requires("propertyName != null")
  public Time getAsTime(String propertyName) {
    Object object = getAsObject(propertyName);
    return
        object instanceof Timestamp ? new Time(((Timestamp) object).getTime()) :
        object instanceof Time ? (Time) object :
        object instanceof Date ? new Time(((Date) object).getTime()) :
            DefaultValues.timeDefaultValue();
  }

  @Requires("propertyName != null")
  public Timestamp getAsTimestamp(String propertyName) {
    Object object = getAsObject(propertyName);
    return
        object instanceof Timestamp ? (Timestamp) object :
        object instanceof Time ? new Timestamp(((Time) object).getTime()) :
        object instanceof Date ? new Timestamp(((Date) object).getTime()) :
            DefaultValues.timestampDefaultValue();
  }

  @Ensures("result != null")
  public String getKind() {
    return kind_;
  }

  @Requires("kind != null")
  @Ensures("getKind().equals(kind)")
  private void setKind(String kind) {
    kind_ = kind;
  }

  @Requires("properties != null")
  @Ensures("getProperties() == properties")
  private void setProperties(HashMap<String, String> properties) {
    properties_ = properties;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id_, kind_, properties_);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof Entity))
      return false;

    Entity entity = (Entity)o;
    return Objects.equal(id_, entity.getId()) &&
        Objects.equal(kind_, entity.getKind()) &&
        Objects.equal(properties_, entity.getProperties());
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("id_", id_)
        .add("kind_", kind_)
        .add("properties_", properties_)
        .toString();
  }

  @Override
  public int compareTo(Entity entity) {
    return ComparisonChain.start()
        .compare(getId(), entity.getId())
        .compare(getKind(), entity.getKind())
        .result();
  }
}
