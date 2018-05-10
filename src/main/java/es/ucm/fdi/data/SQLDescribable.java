package es.ucm.fdi.data;

public abstract class SQLDescribable {

    public SQLDescribable(Object [] data) throws IllegalArgumentException;
    
    public Object [] getData();

}
