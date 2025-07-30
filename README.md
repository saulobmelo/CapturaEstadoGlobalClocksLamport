# 🧠 Simulação de Captura de Estado Global com Clocks de Lamport (Java)

Este projeto é uma simulação da captura de estado global em um sistema distribuído, utilizando **Clocks de Lamport** e o algoritmo de **Chandy-Lamport**.

> 📚 Atividade desenvolvida para a disciplina de **Sistemas Distribuídos** – Curso de Análise e Desenvolvimento de Sistemas – IFBA

---

## ⚙️ Tecnologias Utilizadas

- Java 21
- Sockets (TCP/IP)
- Threads
- Clocks de Lamport
- Algoritmo de Chandy-Lamport
- Logs em arquivos (`log_P1.txt`, `log_P2.txt`, etc.)

---

## 🏗️ Estrutura do Projeto

```
├── Main.java
├── Processo.java
├── Mensagem.java
├── Util.java
├────────
├── log_P1.txt
├── log_P2.txt
├── log_P3.txt
```

---

## 🧪 Como Executar

1. **Clone o repositório** ou copie os arquivos para seu projeto.
2. Compile com sua IDE ou via terminal.
3. Execute `Main.java`.

O programa:
- Cria 3 processos (`P1`, `P2` e `P3`)
- Simula envio e recebimento de mensagens entre eles
- Gera eventos internos
- Após alguns segundos, inicia o snapshot global
- Captura o estado local e mensagens em trânsito
- Finaliza automaticamente após 1 minuto

---

## 📄 Logs

Cada processo gera um log com seus eventos:

Exemplo:
```txt
[P1] Evento interno. Clock: 11
[P1] Enviado para localhost/127.0.0.1:5002 [13] -> Mensagem de P1
```

## 🧠 O que está sendo simulado
- Eventos internos: Incremento do clock de Lamport.

- Mensagens entre processos: Envio e recebimento com timestamps.

- Snapshot Global:

    - Estado local de cada processo (clock, contador, estado ON/OFF).

    - Mensagens em trânsito (recebidas antes do marcador do canal).

## 📌 Observações
Cada processo é executado em uma Thread separada, simulando paralelismo.

O snapshot é iniciado por apenas um processo e se propaga.

O programa é finalizado automaticamente após 60 segundos.

## 👨‍💻 Autores
- Saulo Melo

- Flávio Costa

- Vinícius Xavier