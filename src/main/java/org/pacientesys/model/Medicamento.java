package org.pacientesys.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Medicamento {
    private String codigo, nome, descricao, principioAtivo;
    private LocalDate dataValidade;
    private int quantidadeEstoque;
    private BigDecimal preco;
    private boolean controlado;
    private Fornecedor fornecedor;

    // Construtor
    public Medicamento(String codigo, String nome, String descricao, String principioAtivo,
                       LocalDate dataValidade, int quantidadeEstoque, BigDecimal preco,
                       boolean controlado, Fornecedor fornecedor) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.principioAtivo = principioAtivo;
        this.dataValidade = dataValidade;
        this.quantidadeEstoque = quantidadeEstoque;
        this.preco = preco;
        this.controlado = controlado;
        this.fornecedor = fornecedor;
    }

    // Gets
    public String getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getPrincipioAtivo() { return principioAtivo; }
    public LocalDate getDataValidade() { return dataValidade; }
    public int getQuantidadeEstoque() { return quantidadeEstoque; }
    public BigDecimal getPreco() { return preco; }
    public boolean isControlado() { return controlado; }
    public Fornecedor getFornecedor() { return fornecedor; }

    // Sets (caso precise modificar valores depois da criação)
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public void setNome(String nome) { this.nome = nome; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setPrincipioAtivo(String principioAtivo) { this.principioAtivo = principioAtivo; }
    public void setDataValidade(LocalDate dataValidade) { this.dataValidade = dataValidade; }
    public void setQuantidadeEstoque(int quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }
    public void setControlado(boolean controlado) { this.controlado = controlado; }
    public void setFornecedor(Fornecedor fornecedor) { this.fornecedor = fornecedor; }

    // Método toString para exibição na TableView
    @Override
    public String toString() {
        return nome + " (" + codigo + ")";
    }
}
