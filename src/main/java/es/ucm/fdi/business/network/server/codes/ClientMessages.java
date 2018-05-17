package es.ucm.fdi.business.network.server.codes;

public class ClientMessages {
    
    public static final int LOG_IN = 1;
    public static final int LOG_OUT = 2;
    public static final int REGISTER = 3;
    
    public final int type;
    public final String msg;
    
    public String field1;
    public String field2;
    
    private ClientMessages(int type){
        this.type = type;
        this.msg = "";
    }
    
    private ClientMessages(int type, String msg){
        this.type = type;
        this.msg = msg;
    }
    
    public int getType(){
        return type;
    }
    public String getMessage(){
        return msg;
    }
   
    
}
