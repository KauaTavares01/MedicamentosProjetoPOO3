# Gerenciador de Medicamentos

Este é um sistema de gerenciamento de medicamentos, desenvolvido em Java com JavaFX, que permite cadastrar, consultar, excluir e listar medicamentos. O projeto utiliza persistência de dados em um arquivo CSV e realiza operações CRUD básicas com validações.

## Funcionalidades

- Cadastro de Medicamentos com informações como código, nome, preço, validade e fornecedor.
- Validações para campos como código, nome, preço, e validade.
- Persistência dos dados em um arquivo CSV (`infoCSV/medicamentos.csv`).
- Relatórios básicos com a utilização da API Stream (estoque baixo, próximos ao vencimento, total por fornecedor).
- Interface gráfica utilizando JavaFX.

## Requisitos

- JDK 17 ou superior
- JavaFX 17.x
- Maven (para gerenciamento de dependências)

## Como rodar

1. Clone o repositório:
   ```bash
   git clone https://github.com/seuusuario/gerenciador-medicamentos.git
