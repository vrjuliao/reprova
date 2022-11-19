package br.ufmg.engsoft.reprova.model;

import java.util.Map;
import java.util.Objects;
import java.util.HashMap;


/**
 * A semester class.
 * The semester is composed of an year and a reference (1 or 2).
 */
public class Semester {
  /**
   * The reference part of a semester.
   * Either 1 or 2.
   */
  public enum Reference {
    _1(1),
    _2(2);
	  
    /**
     * The mapping of integers to Semester.Reference.
     */
    protected static final Map<Integer, Reference> VALUE_MAP =
      new HashMap<Integer, Reference>(); static {
      for (var ref : Reference.values()) {
        VALUE_MAP.put(ref.value, ref);
      }
    }

    public final int value;
    Reference(int referenceValue) {
      this.value = referenceValue;
    }

    /**
     * Convert a int to a Semester.Reference.
     */
    public static Reference fromInt(int referenceValue) {
      Reference ref = VALUE_MAP.get(Integer.valueOf(referenceValue));

      if (ref == null) {
        throw new IllegalArgumentException();
      }

      return ref;
    }
  }


  /**
   * The year of the semester.
   */
  public final int year;
  /**
   * The reference of the semester.
   */
  public final Reference ref;



  /**
   * Construct a Semester.
   * @param year  the year
   * @param ref   the reference
   * @throws IllegalArgumentException  if any parameter is null
   */
  public Semester(int year, Reference ref) {
    if (ref == null) {
      throw new IllegalArgumentException("ref mustn't be null");
    }

    this.year = year;
    this.ref = ref;
  }



  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof Semester)) {
      return false;
    }

    var semester = (Semester) obj;

    return this.year == semester.year
        && this.ref == semester.ref;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.year, this.ref);
  }

  /**
   * Convert a Semester to String for visualization purposes.
   */
  @Override
  public String toString() {
    return String.format(
      "%d/%d",
      this.year,
      this.ref.value);
  }
}
