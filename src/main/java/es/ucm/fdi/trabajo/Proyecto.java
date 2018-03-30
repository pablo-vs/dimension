package es.ucm.fdi.trabajo;

/**
 * 
 * @author Eduardo Amaya
 * @author Javier Galiana
 *
 */


public class Proyecto {
	private Grafico[] listaGraficos;
	private String ID;
	
	public Proyecto(String iD) {
		//super();
		ID = iD;
	}

	/**
	 * crea un nuevo objeto y lo a�ade a la lista
	 * 
	 * @param g
	 */
	public void AnyadeObj(Grafico g){
		
	}
	
	/**
	 * modifica el objeto listaGraficos[pos]
	 * 
	 * @param pos
	 */
	public void ModifObj(int pos){
		
	}
	
	/**
	 * descripci�n del funcionamiento
	 * 
	 * @param pos
	 */
	public void VisualizarObj(int pos){
		
	}

	@Override
	public String toString() {
		return "Proyecto [ID=" + ID + "]";
	}
	
	
}
