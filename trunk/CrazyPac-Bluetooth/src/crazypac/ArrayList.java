package crazypac;
/**
 * Implementação do ArrayList adiquirida em:
 * http://www.guj.com.br/posts/list/93767.java
 * @author Norxs
 */
public class ArrayList {  
    private Object[] storedObjects;  
    private int growthFactor;  
    private int size;  
      
    public ArrayList() {  
        this( 10, 75 );  
    }  
      
    public ArrayList( int initialCapacity ) {  
        this( initialCapacity, 75 );  
    }  
  
    public ArrayList( int initialCapacity, int growthFactor ) {  
        this.storedObjects = new Object[ initialCapacity ];  
        this.growthFactor = growthFactor;  
    }  
      
    public int size() {  
        return this.size;  
    }  
      
    public boolean contains( Object element ) {  
      
        if (element == null) {  
            throw new IllegalArgumentException( "ArrayList cannot contain a null element.");  
        }  
        for (int i = 0; i < this.size; i++) {  
            Object object = this.storedObjects[i];  
            if ( object.equals( element ) ) {  
                return true;  
            }  
        }  
        return false;  
    }  
      
    public int indexOf(Object element) {  
        if (element == null) {  
            throw new IllegalArgumentException( "ArrayList cannot contain a null element.");  
        }  
        for (int i = 0; i < this.size; i++) {  
            Object object = this.storedObjects[i];  
            if ( object.equals( element ) ) {  
                return i;  
            }  
        }  
        return -1;  
    }  
      
    public Object get( int index ) {  
        if (index < 0 || index >= this.size ) {  
            throw new IndexOutOfBoundsException("the index [" + index + "] is not valid for this list with the size [" + this.size + "].");  
        }  
        return this.storedObjects[ index ];  
    }  
  
    public Object remove( int index ) {  
        if (index < 0 || index >= this.size ) {  
            throw new IndexOutOfBoundsException("the index [" + index + "] is not valid for this list with the size [" + this.size + "].");  
        }  
        Object removed = this.storedObjects[ index ];  
        for (int i = index+1; i < this.size; i++) {  
            this.storedObjects[ i-1 ] = this.storedObjects[ i ];  
        }  
        this.size--;  
        this.storedObjects[ this.size ] = null;  
        return removed;  
    }  
      
    public boolean remove( Object element ) {  
        if (element == null) {  
            throw new IllegalArgumentException( "ArrayList cannot contain null.");  
        }  
        int index = -1;  
        for (int i = 0; i < this.size; i++) {  
            Object object = this.storedObjects[i];  
            if ( object.equals( element ) ) {  
                index = i;  
                break;  
            }  
        }  
        if (index == -1) {  
            return false;  
        }  
        for (int i = index+1; i < this.size; i++) {  
            this.storedObjects[ i-1 ] = this.storedObjects[ i ];  
        }  
        this.size--;  
        this.storedObjects[ this.size ] = null;  
        return true;   
    }  
  
    public void clear() {  
        for (int i = 0; i < this.size; i++) {  
            this.storedObjects[i] = null;  
        }  
        this.size = 0;  
    }  
  
    public void add( Object element) {  
        if (element == null) {  
            throw new IllegalArgumentException( "ArrayList cannot contain null.");  
        }  
        if (this.size >= this.storedObjects.length) {  
            increaseCapacity();  
        }  
        this.storedObjects[ this.size ] = element;  
        this.size++;  
    }  
  
    public void add( int index, Object element ) {  
        if (element == null) {  
            throw new IllegalArgumentException( "ArrayList cannot contain null.");  
        }  
        if (index < 0 || index > this.size ) {  
            throw new IndexOutOfBoundsException("the index [" + index + "] is not valid for this list with the size [" + this.size + "].");  
        }  
        if (this.size >= this.storedObjects.length) {  
            increaseCapacity();  
        }  
        // shift all following elements one position to the back:  
        for (int i = this.size; i > index; i--) {  
            this.storedObjects[i] = this.storedObjects[ i-1 ];  
        }   
        // insert the given element:  
        this.storedObjects[ index ] = element;  
        this.size++;  
    }  
  
    public Object set( int index, Object element ) {  
        if (index < 0 || index >= this.size ) {  
            throw new IndexOutOfBoundsException("the index [" + index + "] is not valid for this list with the size [" + this.size + "].");  
        }  
        Object replaced = this.storedObjects[ index ];  
        this.storedObjects[ index ] = element;  
        return replaced;  
    }  
  
    public String toString() {  
        StringBuffer buffer = new StringBuffer( this.size * 23 );  
        buffer.append( super.toString() ).append( "{\n" );  
        for (int i = 0; i < this.size; i++) {  
            buffer.append( this.storedObjects[i] );  
            buffer.append('\n');  
        }  
        buffer.append('}');  
        return buffer.toString();  
    }  
  
    public Object[] toArray() {  
        Object[] copy = new Object[ this.size ];  
        System.arraycopy( this.storedObjects, 0, copy, 0, this.size );  
        return copy;  
    }  
  
    public Object[] toArray( Object[] target ) {  
        System.arraycopy( this.storedObjects, 0, target, 0, this.size );  
        return target;  
    }  
  
    public void trimToSize() {  
        if (this.storedObjects.length != this.size ) {  
            Object[] newStore = new Object[ this.size ];  
            System.arraycopy( this.storedObjects, 0, newStore, 0, this.size );  
            this.storedObjects = newStore;  
        }  
    }  
  
    private void increaseCapacity() {  
        int currentCapacity = this.storedObjects.length;  
        int newCapacity = currentCapacity + ((currentCapacity * this.growthFactor) / 100);  
        if (newCapacity == currentCapacity ) {  
            newCapacity++;  
        }  
        Object[] newStore = new Object[ newCapacity ];  
        System.arraycopy( this.storedObjects, 0, newStore, 0, this.size );  
        this.storedObjects = newStore;  
    }  
  
    public Object[] getInternalArray() {  
        return this.storedObjects;  
    }  
}  
