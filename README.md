# BoOom

A Java-based multiplayer arcade game for up to 4 players over a local network, inspired by classic Bomberman-style games.

---

## Description

BoOom is a 2D multiplayer game where up to 4 players compete against each other simultaneously. The goal is to eliminate all other players using bombs and be the last one standing. The game runs on a client-server architecture over a local network (LAN).

---

## Features

- **Up to 4 players** simultaneously via LAN multiplayer
- **3 different maps** (Graveyard, Hospital, Supermarket) each with matching background music
- **5 power-up types** that spawn on the map and can be collected
- **Sudden Death** — the playfield shrinks over time
- **Scoreboard** with a win counter per player
- **In-game GUI** with timer, player icons and power-up display
- **Victory screen** at the end of each round
- **Sound** — background music and sound effects (each independently mutable)
- **Multilingual** — German and English

---

## Requirements

- Java 11 or higher
- Local network (LAN) for multiplayer

---

## Getting Started

### Run as JAR

```bash
java -jar zombieman.jar
```

### Run in Eclipse

1. Import the project into Eclipse (`File → Import → Existing Projects into Workspace`)
2. Select the `zombieman` project
3. Run `GameFrame.java` as the main class

---

## How to Play

### Main Menu

| Option | Description |
|--------|-------------|
| **Host** | Start a server and wait for players |
| **Join** | Join an existing server |
| **Help** | View the in-game manual |

### Controls (Keyboard)

| Key | Action |
|-----|--------|
| `W` / `↑` | Move up |
| `S` / `↓` | Move down |
| `A` / `←` | Move left |
| `D` / `→` | Move right |
| `Space` / `Enter` | Place bomb |

### Power-Ups

| Icon | Effect |
|------|--------|
| Power-Up 0 | Increases bomb count |
| Power-Up 1 | Increases explosion range |
| Power-Up 2 | Increases movement speed |
| Power-Up 3 | Special effect |
| Power-Up 4 | Special effect |

---

## Project Structure

```
zombieman/
├── src/
│   ├── entity/       # Players, sprites, constants, power-ups
│   ├── main/         # UI panels (menu, game, loading, help, host, join, victory)
│   ├── net/          # Networking (server, client, sender, receiver)
│   └── test/         # JUnit tests
├── res/
│   ├── map/          # Map sprites and power-up icons
│   ├── menu/         # Menu graphics and backgrounds
│   ├── person/       # Player skins
│   ├── sound/        # Music and sound effects
│   └── digits/       # Number sprites for the timer
└── zombieman.jar     # Executable JAR file
```

---

## Technologies

- **Java Swing** — GUI and rendering
- **Java Sound API** — music and sound effects
- **TCP Sockets** — client-server communication over LAN
- **JUnit** — unit testing

---

## Authors

Developed by team **BoOom** at the University of Wuppertal.
