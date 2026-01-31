# 🐾 PetAdote - App de Adoção de Pets

O **PetAdote** é um ecossistema Android moderno desenvolvido para facilitar o processo de adoção de animais. O projeto utiliza uma arquitetura robusta que integra sincronização de dados via API, persistência local para funcionamento offline e um sistema completo de autenticação de usuários.

---

## 🚀 Funcionalidades

* **Autenticação Local:** Cadastro (com Nome, E-mail e Senha) e Login de usuários persistidos no banco de dados **Room**.
* **Validação Inteligente:** Sistema de verificação de e-mail (Regex) e força de senha (mínimo 6 caracteres).
* **Sincronização com Backend:** Consome uma API em **Node.js** para listar pets disponíveis e atualizar dados via Retrofit.
* **Modo Offline (Single Source of Truth):** Graças ao Room, os pets baixados ficam salvos e acessíveis mesmo sem internet.
* **Busca e Filtros:** Barra de pesquisa por nome/raça e filtragem por categorias (Cães/Gatos) otimizada com `derivedStateOf`.
* **Gestão de Adoção:** Interface para adotar, cancelar adoção e visualizar detalhes específicos de cada pet.

---

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** [Kotlin](https://kotlinlang.org/)
* **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Interface declarativa)
* **Arquitetura:** MVVM (Model-View-ViewModel)
* **Banco de Dados:** [Room Persistence Library](https://developer.android.com/training/data-storage/room)
* **Rede:** [Retrofit 2](https://square.github.io/retrofit/) & Gson (Consumo de API JSON)
* **Carregamento de Imagens:** [Coil](https://coil-kt.github.io/coil/) (Carregamento assíncrono de URLs)
* **Navegação:** Compose Navigation (NavHost)
* **Backend:** Node.js + Express

---

## 🏗️ Arquitetura do Projeto

O projeto segue o padrão **MVVM** com foco em reatividade:

1.  **UI (Compose):** Observa os estados (`StateFlow` e `MutableState`) expostos pelos ViewModels.
2.  **AuthViewModel:** Gerencia o fluxo de segurança, validações e acesso à tabela de usuários.
3.  **PetViewModel:** Gerencia a lógica de negócio dos pets, filtros e comunicação com a API.
4.  **Local Data (Room):** Armazena Pets e Usuários localmente.
5.  **Remote Data (Retrofit):** Interface de comunicação com o servidor externo.



---

## 📋 Como Executar

### 1. Configurar o Backend (Node.js)
Navegue até a pasta do seu servidor e execute:
```bash
npm install
node server.js
