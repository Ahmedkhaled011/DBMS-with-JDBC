package dbms.datatypes;

import java.sql.Date;

public class DBDate implements DBDatatype {

    /**
     * Key identifier to DBDate.
     */
    public static final String KEY = "Date";

    static {
        DatatypeFactory.getFactory().register(KEY, DBDate.class);
    }

    private Date value;

    public DBDate() {

    }

    /**
     * @param value {@link Date} sets the the local date to the fiven value.
     */
    public DBDate(final Date value) {
        this.value = value;
    }

    @Override
    public Object toObj(final String s) {
        try {
            return Date.valueOf(s);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int compareTo(final DBDatatype data) {
        return value.compareTo((Date) data.getValue());
    }

    @Override
    public Date getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DBDate dbDate = (DBDate) o;
        return value != null ? value.equals(dbDate.value)
                : dbDate.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
