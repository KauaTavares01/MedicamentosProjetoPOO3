package org.pacientesys.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.pacientesys.model.Fornecedor;
import org.pacientesys.model.Medicamento;
import org.pacientesys.repository.MedicamentoRepositoryCsv;
import org.pacientesys.service.MedicamentoService;
import org.pacientesys.service.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProdutoController {

    // === campos ligados ao FXML (mesmos fx:id do seu arquivo) ===
    @FXML private TextField txtCodigo;
    @FXML private TextField txtNome;
    @FXML private TextField txtPreco;
    @FXML private ComboBox<Fornecedor> cbCategoria; // usamos como Fornecedor
    @FXML private Button btnCadastrar;
    @FXML private Button btnExcluir;
    @FXML private Button btnConsultar;
    @FXML private Button btnListar;
    @FXML private TableView<Medicamento> tabelaProdutos;

    // === infraestrutura ===
    private MedicamentoService service;
    private final ObservableList<Medicamento> dados = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        service = new MedicamentoService(new MedicamentoRepositoryCsv("medicamentos.csv"));

        // fornecedores “mock”
        var fornecedores = FXCollections.observableArrayList(
                new Fornecedor("40.688.134/0001-61","Farmacorp LTDA","(11)1111-1111","contato@farmacorp.com","SP","SP"),
                new Fornecedor("43.008.421/0001-10","Saude+ Distribuidora","(21)2222-2222","vendas@saudemais.com","Rio","RJ"),
                new Fornecedor("12.345.678/0001-95","Union Med Hospitalar","(31)3333-3333","suporte@unionmedhospitalar.com","Aparecida de Goiania","GO")
        );
        cbCategoria.setItems(fornecedores);
        cbCategoria.setConverter(new StringConverter<>() {
            @Override public String toString(Fornecedor f){ return f==null ? "" : f.getRazaoSocial(); }
            @Override public Fornecedor fromString(String s){ return null; }
        });

        // Colunas da tabela
        tabelaProdutos.getColumns().clear();
        var cCod  = new TableColumn<Medicamento,String>("Código");
        cCod.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getCodigo()));
        var cNome = new TableColumn<Medicamento,String>("Nome");
        cNome.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getNome()));
        var cPreco = new TableColumn<Medicamento,String>("Preço");
        cPreco.setCellValueFactory(c -> new ReadOnlyStringWrapper("R$ " + c.getValue().getPreco()));
        var cForn = new TableColumn<Medicamento,String>("Categoria"); // rótulo mantido
        cForn.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getFornecedor().getRazaoSocial()));
        tabelaProdutos.getColumns().addAll(cCod, cNome, cPreco, cForn);

        // Carrega CSV
        dados.setAll(service.listar());
        tabelaProdutos.setItems(dados);
        tabelaProdutos.setPlaceholder(new Label("Não há conteúdo na tabela"));

        // Ações dos botões
        btnCadastrar.setOnAction(e -> cadastrar());
        btnExcluir.setOnAction(e -> excluir());
        btnConsultar.setOnAction(e -> consultar());
        btnListar.setOnAction(e -> dados.setAll(service.listar()));
    }

    private void cadastrar() {
        try {
            String codigo = txtCodigo.getText().trim();
            String nome   = txtNome.getText().trim();
            BigDecimal preco = new BigDecimal(txtPreco.getText().trim().replace(",", "."));
            Fornecedor f = cbCategoria.getValue();
            if (f == null) throw new ValidationException("Selecione um fornecedor.");

            Medicamento m = new Medicamento(
                    codigo, nome, "", "",
                    LocalDate.now().plusYears(1), // validade padrão
                    10,                            // quantidade padrão
                    preco,
                    false,                         // controlado
                    f
            );
            service.cadastrar(m);
            dados.add(m);
            limpar();
        } catch (ValidationException ve){
            alert(ve.getMessage());
        } catch (Exception ex){
            alert("Erro ao cadastrar: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void excluir() {
        Medicamento sel = tabelaProdutos.getSelectionModel().getSelectedItem();
        if (sel == null){ alert("Selecione um item na tabela."); return; }
        try {
            service.excluir(sel.getCodigo());
            dados.remove(sel);
        } catch (ValidationException ve){
            alert(ve.getMessage());
        }
    }

    private void consultar() {
        String cod = txtCodigo.getText().trim();
        if (cod.isBlank()){ alert("Informe o código para consultar."); return; }
        service.consultar(cod).ifPresentOrElse(
                m -> tabelaProdutos.getSelectionModel().select(m),
                () -> alert("Não encontrado.")
        );
    }

    private void limpar() {
        txtCodigo.clear();
        txtNome.clear();
        txtPreco.clear();
        cbCategoria.getSelectionModel().clearSelection();
    }

    private void alert(String m) {
        new Alert(Alert.AlertType.INFORMATION, m, ButtonType.OK).showAndWait();
    }
}
