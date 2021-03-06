package hotel.boundary;

import javax.swing.JOptionPane;


import hotel.control.ClienteControl;
import hotel.entidades.Cliente;
import hotel.entidades.Produto;
import hotel.interfaces.BoundaryContent;
import hotel.interfaces.Executor;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ClienteBoundary implements BoundaryContent, EventHandler<ActionEvent>{
	private ClienteControl control = new ClienteControl();
	private TextField txtNome = new TextField();
	private TextField txtEmail = new TextField();
	private TextField txtTelefone = new TextField();
	private TextField txtEnd = new TextField();
	private TextField txtCpf = new TextField();
	private Executor executor;

	private ComboBox<String> sexo = new ComboBox<>();
	private VBox painelCentral = new VBox();
	private BorderPane painelPrincipal = new BorderPane();
	private GridPane painelCampos = new GridPane();
	private FlowPane painelBotoes = new FlowPane();	
	
	private Button btnAdicionar = new Button(" Adicionar ");
	private Button btnEditar = new Button(" Alterar ");
	private Button btnPesquisar = new Button(" Pesquisar ");
	private Button btnApagar = new Button(" Apagar ");
	private Button btnMenu = new Button(" Voltar ao Menu");
	private TableView<Cliente> table;
	
	
	@SuppressWarnings("static-access")	
	public ClienteBoundary(Executor e) {
		this.setExecutor(e);
		
		ObservableList<String> tipoSexo = 
				FXCollections.observableArrayList("F", "M");
		sexo.setItems(tipoSexo);	

		painelPrincipal.setStyle("-fx-padding:20px");

		Label labtitulo = new Label("GERENCIAR CLIENTES");
		labtitulo.setOpaqueInsets( new Insets(15));
		labtitulo.setUnderline(true);	
		labtitulo.setFont(Font.font("Arial", FontWeight.BLACK, 25));
		
		painelBotoes.setHgap(15);
		painelBotoes.setVgap(10);
		painelCampos.setHgap(8);
		painelCampos.setVgap(25);
		
		painelCampos.add(new Label("Nome"), 0, 0);
		painelCampos.add(txtNome, 1, 0);
		painelCampos.add(new Label("Sexo"), 0, 1);
		painelCampos.add(sexo, 1, 1);
		painelCampos.add(new Label("CPF"), 0, 2);
		painelCampos.add(txtCpf, 1, 2);
		painelCampos.add(new Label("E-mail"), 2, 0);
		painelCampos.add(txtEmail, 3, 0);
		painelCampos.add(new Label("Telefone"), 2, 1);
		painelCampos.add(txtTelefone, 3, 1);
		painelCampos.add(new Label("Endereco"), 2, 2);
		painelCampos.add(txtEnd, 3, 2);
		painelCampos.setVgap(20);
		
		painelBotoes.getChildren().addAll(btnAdicionar, btnPesquisar,btnEditar,btnApagar,btnMenu);
		painelBotoes.setMargin(painelBotoes, new Insets(15));
		
		painelCentral.setMargin(painelBotoes, new Insets(15));
		painelCentral.getChildren().addAll(painelCampos,painelBotoes);
		table = generateTable();
		
		table = configuraTabela() ;
		
		painelPrincipal.setTop(labtitulo);
		painelPrincipal.setCenter(painelCentral);
		painelPrincipal.setBottom(table);
		
		painelPrincipal.setMargin(painelCentral, new Insets(15));
		painelPrincipal.setMargin(labtitulo, new Insets(15));

		btnAdicionar.addEventHandler(ActionEvent.ANY, this);
		btnPesquisar.addEventHandler(ActionEvent.ANY, this);
		btnEditar.addEventHandler(ActionEvent.ANY, this);
		btnApagar.addEventHandler(ActionEvent.ANY, this);
		btnMenu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				executor.executar("Menu principal");
			}
		});
	}

	public ClienteBoundary() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle(ActionEvent event) {
		
		if (event.getTarget() == btnAdicionar) { 
			control.manterCliente( boundaryEntidade() );
			limpar();
		} else if (event.getTarget() == btnEditar){
			Cliente clienteSelect = table.getSelectionModel().getSelectedItem();
			control.alterarCliente(boundaryEntidade(),clienteSelect.getCPF());
			ajustartabela();
		} else if (event.getTarget() == btnApagar) {
			Cliente clienteSelect = table.getSelectionModel().getSelectedItem();
			control.apagar(clienteSelect.getCPF());
			ajustartabela();
		} else if (event.getTarget() == btnPesquisar) {
			String cpfCliente = txtCpf.getText();
			control.buscarCliente(cpfCliente);
		}
	}


	public Cliente boundaryEntidade(){
		Cliente c = new Cliente();
		String tiposexo = sexo.getSelectionModel().getSelectedItem().toString();
		try {
			c.setCPF(txtCpf.getText());
			c.setEmail(txtEmail.getText());
			c.setEndereco(txtEnd.getText());
			c.setSexo(tiposexo);
			c.setNome(txtNome.getText());
			c.setTelefone(txtTelefone.getText());
			return c;
		} catch (Exception e) {
			Alert dialogoErro = new Alert(Alert.AlertType.ERROR);
			dialogoErro.setTitle("Erro ao adicionar");
			dialogoErro.setHeaderText(e.getMessage());
			dialogoErro.showAndWait();
		}
		return null;
	}
	
	TableView<Cliente> generateTable() {
		TableView<Cliente> table = new TableView<>();

	
		TableColumn<Cliente, String> coluna1 = new TableColumn<>("Nome");
		coluna1.setCellValueFactory(new PropertyValueFactory<Cliente, String>("Nome"));

		TableColumn<Cliente, String> coluna2 = new TableColumn<>("Email");
		coluna2.setCellValueFactory(new PropertyValueFactory<Cliente, String>("Email"));
		
		TableColumn<Cliente, String> coluna3 = new TableColumn<>("Sexo");
		coluna3.setCellValueFactory(new PropertyValueFactory<Cliente, String>("Sexo"));
		
		TableColumn<Cliente, String> coluna4 = new TableColumn<>("Telefone");
		coluna4.setCellValueFactory(new PropertyValueFactory<Cliente, String>("Telefone"));
		
		TableColumn<Cliente, String> coluna5 = new TableColumn<>("Endereco");
		coluna5.setCellValueFactory(new PropertyValueFactory<Cliente, String>("Endereco"));
		
		TableColumn<Cliente, String> coluna6 = new TableColumn<>("CPF");
		coluna6.setCellValueFactory(new PropertyValueFactory<Cliente, String>("CPF"));

		

		table.getColumns().addAll(coluna1, coluna2, coluna3, coluna4, coluna5, coluna6);
		table.setItems(control.getLista());
		return table;

	}
	
	private TableView<Cliente> configuraTabela() {
		BooleanBinding camposPreenchidos = txtNome.textProperty().isEmpty()
				.or(txtEmail.textProperty().isEmpty())
				.or(txtEnd.textProperty().isEmpty())
				.or(txtCpf.textProperty().isEmpty())
				.or(txtTelefone.textProperty().isEmpty());
		BooleanBinding algoSelecionado = table.getSelectionModel().selectedItemProperty().isNull();
		BooleanBinding cpfpreenchido = txtCpf.textProperty().isEmpty();
		
		btnApagar.disableProperty().bind(algoSelecionado);
		btnEditar.disableProperty().bind(algoSelecionado);
		btnAdicionar.disableProperty().bind(camposPreenchidos);
		btnPesquisar.disableProperty().bind(cpfpreenchido);

		
		table.getSelectionModel().selectedItemProperty().addListener((observable, old, n) -> {
			if (n != null) {
				txtNome.setText(n.getNome());
				txtEmail.setText(n.getEmail());
				txtEnd.setText(n.getEndereco());
				txtCpf.setText(n.getCPF());
				txtTelefone.setText(n.getTelefone());
			}
		});
		return table;
	}
	
	private void ajustartabela() {
		control.atualizarTabela();
	}

	private void limpar() {
		table.getSelectionModel().select(null);
		txtNome.setText(null);
		txtEmail.setText(null);
		txtEnd.setText(null);
		txtCpf.setText(null);
		txtTelefone.setText(null);
	}
	
	@Override
	public void setExecutor(Executor e) {
		this.executor = e;
	}
	
	@Override
	public Executor getExecutor() {
		return this.executor;
	}
	
	@Override
	public Pane gerarTela() {
		// TODO Auto-generated method stub
		limpar();
		ajustartabela();
		return painelPrincipal;
	}

}
