package hotel.control;



import hotel.entidades.Produto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class EstoqueController {
	private ObservableList<Produto> lista = FXCollections.observableArrayList();
	
	
	public Produto adicionar(Produto prod) {
		System.out.println(prod.getCodigo());
		if(lista.isEmpty()){
			for(Produto p1 : lista ){
				if(lista.contains(p1.getCodigo())){
					return p1;
				}
			}
		}	
		lista.add(prod);
		return null;
	}

	public Produto buscarProduto(String cod) {
		return lista.stream().filter(p -> p.getCodigo().equals(cod)).findFirst()
				.orElseThrow(() -> new Error("Produto nao encontrado"));
	}
	
	public void SetQtd(Produto p,int q) throws Exception {
		p.setQtd(q);
	}

	public ObservableList<Produto> getLista() {
		return lista;
	}

	public void apagar(String codigo) {
		Produto produto = buscarProduto(codigo);
		lista.remove(produto);
	}
	


}
