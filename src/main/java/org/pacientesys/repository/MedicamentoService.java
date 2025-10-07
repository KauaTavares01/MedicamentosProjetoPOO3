package org.pacientesys.service;

import org.pacientesys.model.*; import org.pacientesys.repository.MedicamentoRepositoryCsv;
import java.math.BigDecimal; import java.time.LocalDate; import java.util.*; import java.util.stream.Collectors;

public class MedicamentoService {
    private final MedicamentoRepositoryCsv repo;
    private final List<Medicamento> cache;

    public MedicamentoService(MedicamentoRepositoryCsv repo){
        this.repo = repo;
        this.cache = repo.load();
    }

    public List<Medicamento> listar(){ return Collections.unmodifiableList(cache); }
    public Optional<Medicamento> consultar(String codigo){
        return cache.stream().filter(m -> m.getCodigo().equalsIgnoreCase(codigo)).findFirst();
    }

    public void cadastrar(Medicamento m){
        Validators.codigo(m.getCodigo());
        Validators.nome(m.getNome());
        Validators.validade(m.getDataValidade());
        Validators.quantidade(m.getQuantidadeEstoque());
        Validators.preco(m.getPreco());
        Validators.cnpj(m.getFornecedor().getCnpj());

        consultar(m.getCodigo()).ifPresent(x -> { throw new ValidationException("Código já cadastrado."); });
        cache.add(m); repo.saveAll(cache);
    }

    public void excluir(String codigo){
        boolean removed = cache.removeIf(m -> m.getCodigo().equalsIgnoreCase(codigo));
        if (!removed) throw new ValidationException("Código não encontrado.");
        repo.saveAll(cache);
    }

    // Relatórios
    public List<Medicamento> proximosAoVencimento(int dias){
        LocalDate limite = LocalDate.now().plusDays(dias);
        return cache.stream()
                .filter(m -> !m.getDataValidade().isBefore(LocalDate.now()))
                .filter(m -> !m.getDataValidade().isAfter(limite))
                .sorted(Comparator.comparing(Medicamento::getDataValidade))
                .toList();
    }
    public List<Medicamento> estoqueBaixo(int limite){
        return cache.stream().filter(m -> m.getQuantidadeEstoque() < limite).toList();
    }
    public Map<String, BigDecimal> valorTotalPorFornecedor(){
        return cache.stream().collect(Collectors.groupingBy(
                m -> m.getFornecedor().getRazaoSocial(),
                Collectors.reducing(BigDecimal.ZERO,
                        m -> m.getPreco().multiply(BigDecimal.valueOf(m.getQuantidadeEstoque())),
                        BigDecimal::add)));
    }
    public Map<Boolean, List<Medicamento>> controladosVsNao() {
        return cache.stream()
                .collect(Collectors.groupingBy(Medicamento::isControlado)); // Agrupa por controlado (true ou false)
    }



}
