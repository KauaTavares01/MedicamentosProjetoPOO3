package org.pacientesys.service;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Validators {
    public static void codigo(String c){
        if (c==null || !c.matches("[A-Za-z0-9]{7}"))
            throw new ValidationException("Código deve ter 7 caracteres alfanuméricos.");
    }
    public static void nome(String n){
        if (n==null || n.trim().length()<2) throw new ValidationException("Nome muito curto.");
    }
    public static void validade(LocalDate d){
        if (d==null || d.isBefore(LocalDate.now())) throw new ValidationException("Validade não pode ser passada.");
    }
    public static void quantidade(int q){
        if (q<0) throw new ValidationException("Quantidade não pode ser negativa.");
    }
    public static void preco(BigDecimal p){
        if (p==null || p.signum()<=0) throw new ValidationException("Preço deve ser positivo.");
    }
    public static void cnpj(String cnpj){
        if (cnpj==null) throw new ValidationException("CNPJ obrigatório.");
        String digits = cnpj.replaceAll("\\D","");
        if (digits.length()!=14 || digits.chars().distinct().count()==1) throw new ValidationException("CNPJ inválido.");
        int[] w1={5,4,3,2,9,8,7,6,5,4,3,2}, w2={6,5,4,3,2,9,8,7,6,5,4,3,2};
        int d1 = dv(digits.substring(0,12), w1);
        int d2 = dv(digits.substring(0,12)+d1, w2);
        if (!(digits.endsWith(""+d1+d2))) throw new ValidationException("CNPJ inválido.");
    }
    private static int dv(String base, int[] w){
        int s=0; for(int i=0;i<w.length;i++) s += (base.charAt(i)-'0')*w[i];
        int r = s%11; return (r<2)?0:11-r;
    }
}
