# Simulação de Captura de Estado Global com Clocks de Lamport

## 📌 Descrição

Este projeto é uma simulação distribuída que implementa o algoritmo de **Chandy-Lamport** para **captura de estado global** em um sistema com 3 processos interligados, cada um utilizando **relógios lógicos de Lamport** para controle de causalidade. A comunicação entre processos é realizada por meio de **Java RMI**.

---

## 🎯 Objetivos

- Simular comunicação distribuída entre processos com RMI
- Controlar eventos com Relógios de Lamport
- Implementar algoritmo de Captura de Estado Global (Chandy-Lamport)
- Registrar logs de eventos com timestamps
- Demonstrar mensagens em trânsito no momento da captura

---

## 📂 Estrutura do Projeto

    src/
    ├── InterfaceProcesso.java // Interface RMI dos processos
    ├── Mensagem.java // Estrutura de mensagem com timestamp
    ├── Estado.java // Estado local + mensagens em trânsito
    ├── Registro.java // Log de eventos
    ├── Processo.java // Implementação dos processos RMI
    ├── Main.java // Inicialização dos processos e execução


---

## ⚙️ Tecnologias Utilizadas

- Java 8+
- Java RMI (Remote Method Invocation)
- Threads para simulação de concorrência
- Relógios de Lamport
- Algoritmo de Chandy-Lamport

---

## 🔄 Como Funciona

1. O programa inicia três processos (`P1`, `P2`, `P3`) conectados via RMI.
2. Cada processo gera eventos internos e envia mensagens periodicamente.
3. Após 10 segundos, o processo `P1` inicia a **captura de estado global**.
4. Cada processo registra:
   - Seu clock atual
   - As mensagens em trânsito
5. Após 30 segundos, o programa é encerrado automaticamente.

---

## 📋 Exemplo de Saída (Logs)

    [P1] Evento interno | Clock: 1
    [P2] Enviou mensagem: Ping | Clock: 2
    [P3] Recebeu [2] de P2: Ping | Clock: 3
    [P1] Captura iniciada! | Clock: 5
    [P2] Captura iniciada! | Clock: 6
    [P3] Captura iniciada! | Clock: 6
    Encerrando execução após 30 segundos...


---

## 📌 Observações

- O método `iniciarCaptura()` é protegido contra chamadas múltiplas.
- O código pode ser facilmente expandido para mais processos.
- A simulação usa apenas terminal, sem interface gráfica.

---

## 📦 Como Executar

1. Compile todos os arquivos `.java`:
   ```bash
   javac *.java
    java Main

- Nota: Não é necessário executar rmiregistry separadamente, pois o Main.java já cria o registry localmente.

## 👨‍💻 Autores

- Saulo Melo
- Flávio Costa
- Vinícius Xavier

Trabalho desenvolvido para a disciplina de Sistemas Distribuídos, sob orientação do professor Felipe Silva — IFBA, campus Santo Antônio de Jesus.