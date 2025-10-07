package org.pacientesys.repository;

import org.pacientesys.model.*;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class MedicamentoRepositoryCsv {
    private final Path dir;
    private final Path path;

    public MedicamentoRepositoryCsv(String fileName) {

        // CRIEI A PASTA: "infoCSV" dentro do diretório do projeto , PRA FICAR MAIS ORGAIZADO APRA MIM
        this.dir  = Paths.get(System.getProperty("user.dir"), "infoCSV");
        this.path = dir.resolve(fileName);
        try {
            Files.createDirectories(dir); // garante que a pasta exista
        } catch (IOException e) {
            throw new UncheckedIOException("Não foi possível criar a pasta infoCSV", e);
        }
    }

    public List<Medicamento> load() {
        if (!Files.exists(path)) return new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)){
            return br.lines().filter(l -> !l.isBlank()).map(this::fromCsv).collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e){ throw new UncheckedIOException(e); }
    }
    public void saveAll(List<Medicamento> meds){
        try (BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)){
            for (Medicamento m: meds) bw.write(toCsv(m) + System.lineSeparator());
        } catch (IOException e){ throw new UncheckedIOException(e); }
    }

    private String toCsv(Medicamento m){
        Fornecedor f = m.getFornecedor();
        return String.join(";",
                esc(m.getCodigo()), esc(m.getNome()), esc(m.getDescricao()),
                esc(m.getPrincipioAtivo()), m.getDataValidade().toString(),
                String.valueOf(m.getQuantidadeEstoque()), m.getPreco().toPlainString(),
                String.valueOf(m.isControlado()),
                esc(f.getCnpj()), esc(f.getRazaoSocial()), esc(f.getTelefone()),
                esc(f.getEmail()), esc(f.getCidade()), esc(f.getEstado())
        );
    }
    private Medicamento fromCsv(String line){
        String[] v = line.split(";", -1);
        Fornecedor f = new Fornecedor(v[8], v[9], v[10], v[11], v[12], v[13]);
        return new Medicamento(v[0], v[1], v[2], v[3],
                java.time.LocalDate.parse(v[4]),
                Integer.parseInt(v[5]),
                new BigDecimal(v[6]),
                Boolean.parseBoolean(v[7]),
                f);
    }
    private String esc(String s){ return s==null? "": s.replace(";", ","); }




}

