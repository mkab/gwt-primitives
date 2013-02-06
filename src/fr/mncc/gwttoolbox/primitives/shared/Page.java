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
import com.google.java.contract.Invariant;

import java.util.ArrayList;
import java.util.List;

@Invariant("totalNumberOfEntities_ >= 0")
public class Page<E extends Entity> extends SerializableObject<ArrayList<E>> {

  // Total number of entities (if they were not filtered)
  private Long totalNumberOfEntities_ = 0L;

  public Page() {
    setObject(new ArrayList<E>());
  }

  public Page(ArrayList<E> entities, long totalNumberOfEntities) {
    setEntities(entities);
    setTotalNumberOfEntities(totalNumberOfEntities);
  }

  public List<E> getEntities() {
    return getObject();
  }

  public void setEntities(ArrayList<E> entities) {
    if (entities != null)
      setObject(entities);
  }

  public void add(E entity) {
    getObject().add(entity);
  }

  public long getTotalNumberOfEntities() {
    return totalNumberOfEntities_;
  }

  public void setTotalNumberOfEntities(long totalNumberOfEntities) {
    this.totalNumberOfEntities_ = totalNumberOfEntities < 0 ? 0 : totalNumberOfEntities;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getEntities(), totalNumberOfEntities_);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof Page))
      return false;

    Page page = (Page)o;
    return Objects.equal(getEntities(), page.getEntities()) && Objects.equal(totalNumberOfEntities_, page.getTotalNumberOfEntities());
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("totalNumberOfEntities_", totalNumberOfEntities_)
        .add("entities_", getEntities())
        .omitNullValues().toString();
  }
}
