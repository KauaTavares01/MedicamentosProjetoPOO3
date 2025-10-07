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
    @FXML private Button btnRelatorios;
    @FXML private DatePicker txtValidade; // Para selecionar a data de validade
    @FXML private TextField txtEstoque;   // Para definir a quantidade em estoque
    @FXML private CheckBox chkControlado;  // Declara o CheckBox



    // === infraestrutura ===
    private MedicamentoService service;
    private final ObservableList<Medicamento> dados = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        service = new MedicamentoService(new MedicamentoRepositoryCsv("medicamentos.csv"));

        // fornecedores “mock”
        var fornecedores = FXCollections.observableArrayList(
                new Fornecedor("40.688.134/0001-61", "Farmacorp LTDA", "(11)1111-1111", "contato@farmacorp.com", "SP", "SP"),
                new Fornecedor("43.008.421/0001-10", "Saude+ Distribuidora", "(21)2222-2222", "vendas@saudemais.com", "Rio", "RJ"),
                new Fornecedor("12.345.678/0001-95", "Union Med Hospitalar", "(31)3333-3333", "suporte@unionmedhospitalar.com", "Aparecida de Goiania", "GO")
        );
        cbCategoria.setItems(fornecedores);
        cbCategoria.setConverter(new StringConverter<>() {
            @Override public String toString(Fornecedor f) { return f == null ? "" : f.getRazaoSocial(); }
            @Override public Fornecedor fromString(String s) { return null; }
        });

        // Colunas da tabela
        tabelaProdutos.getColumns().clear();
        var cCod  = new TableColumn<Medicamento,String>("Código");
        cCod.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getCodigo()));

        var cNome = new TableColumn<Medicamento,String>("Nome");
        cNome.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getNome()));

        var cPreco = new TableColumn<Medicamento,String>("Preço");
        cPreco.setCellValueFactory(c -> new ReadOnlyStringWrapper("R$ " + c.getValue().getPreco()));

        var cForn = new TableColumn<Medicamento,String>("Fornecedor");
        cForn.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getFornecedor().getRazaoSocial()));

// **Coluna de Validade**
        var cValidade = new TableColumn<Medicamento,String>("Validade");
        cValidade.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getDataValidade().toString()));

// **Coluna de Quantidade em Estoque**
        var cEstoque = new TableColumn<Medicamento,String>("Quantidade");
        cEstoque.setCellValueFactory(c -> new ReadOnlyStringWrapper(String.valueOf(c.getValue().getQuantidadeEstoque())));

        tabelaProdutos.getColumns().addAll(cCod, cNome, cPreco, cForn, cValidade, cEstoque);

        // Carrega CSV
        dados.setAll(service.listar());
        tabelaProdutos.setItems(dados);
        tabelaProdutos.setPlaceholder(new Label("Não há conteúdo na tabela"));

        // Ações dos botões
        btnCadastrar.setOnAction(e -> cadastrar());
        btnExcluir.setOnAction(e -> excluir());
        btnConsultar.setOnAction(e -> consultar());
        btnListar.setOnAction(e -> dados.setAll(service.listar()));
        btnRelatorios.setOnAction(e -> gerarRelatorios());
        tabelaProdutos.setPlaceholder(new Label("Não há conteúdo na tabela"));
    }

    private void cadastrar() {
        try {
            // Lendo os valores dos campos
            String codigo = txtCodigo.getText().trim();
            String nome = txtNome.getText().trim();
            BigDecimal preco = new BigDecimal(txtPreco.getText().trim().replace(",", "."));
            LocalDate validade = txtValidade.getValue(); // Ler a validade
            int estoque = Integer.parseInt(txtEstoque.getText().trim()); // Ler a quantidade em estoque
            Fornecedor f = cbCategoria.getValue();

            // Lê o valor do CheckBox para determinar se é controlado
            boolean controlado = chkControlado.isSelected();  // Se o CheckBox estiver marcado, será 'true'

            // Validações
            if (f == null) throw new ValidationException("Selecione um fornecedor.");
            if (validade == null) throw new ValidationException("Validade não pode ser vazia.");

            // Criando o objeto Medicamento
            Medicamento m = new Medicamento(
                    codigo, nome, "", "",
                    validade, estoque, preco, controlado, f // Passando o valor controlado aqui
            );

            // Chamando o serviço para cadastrar o medicamento
            service.cadastrar(m);

            // Atualizando a lista de medicamentos e limpando os campos
            dados.add(m);
            limpar();

        } catch (ValidationException ve) {
            alert(ve.getMessage());  // Exibindo a mensagem de erro de validação
        } catch (Exception ex) {
            alert("Erro ao cadastrar: " + ex.getMessage());
            ex.printStackTrace();  // Exibindo detalhes do erro
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
    private void gerarRelatorios() {
        // Relatório 1: Medicamentos próximos ao vencimento (30 dias)
        alert("Próximos ao vencimento (30 dias):\n" +
                service.proximosAoVencimento(30).stream()
                        .map(m -> m.getCodigo() + " - " + m.getNome() + " (vence " + m.getDataValidade() + ")")
                        .reduce((a, b) -> a + "\n" + b)
                        .orElse("Nenhum medicamento próximo ao vencimento"));

        // Relatório 2: Medicamentos com estoque baixo (<5 unidades)
        alert("Estoque baixo (< 5 unidades):\n" +
                service.estoqueBaixo(5).stream()
                        .map(m -> m.getCodigo() + " - " + m.getNome() + " (qtd " + m.getQuantidadeEstoque() + ")")
                        .reduce((a, b) -> a + "\n" + b)
                        .orElse("Nenhum medicamento com estoque baixo"));

        // Relatório 3: Valor total do estoque por fornecedor
        alert("Valor total por fornecedor:\n" +
                service.valorTotalPorFornecedor().entrySet().stream()
                        .map(e -> e.getKey() + ": R$ " + e.getValue())
                        .reduce((a, b) -> a + "\n" + b)
                        .orElse("Nenhum fornecedor com estoque"));

        // Relatório 4: Medicamentos controlados vs não controlados
        alert("Medicamentos controlados vs. não controlados:\n" +
                service.controladosVsNao().entrySet().stream()
                        .map(e -> e.getKey() ? "Controlados: " + e.getValue().size() : "Não controlados: " + e.getValue().size())
                        .reduce((a, b) -> a + "\n" + b)
                        .orElse("Sem medicamentos"));
    }



    private void alert(String m) {
        new Alert(Alert.AlertType.INFORMATION, m, ButtonType.OK).showAndWait();
    }



}
