# clap-kotlin in Kotlin

[![GitHub link](https://img.shields.io/badge/GitHub-KotlinMania%2Fclap--kotlin-blue.svg)](https://github.com/KotlinMania/clap-kotlin)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.kotlinmania/clap-kotlin)](https://central.sonatype.com/artifact/io.github.kotlinmania/clap-kotlin)
[![Build status](https://img.shields.io/github/actions/workflow/status/KotlinMania/clap-kotlin/ci.yml?branch=main)](https://github.com/KotlinMania/clap-kotlin/actions)

This is a Kotlin Multiplatform line-by-line transliteration port of [`clap-rs/clap`](https://github.com/clap-rs/clap).

**Original Project:** This port is based on [`clap-rs/clap`](https://github.com/clap-rs/clap). All design credit and project intent belong to the upstream authors; this repository is a faithful port to Kotlin Multiplatform with no behavioural changes intended.

### Porting status

This is an **in-progress port**. The goal is feature parity with the upstream Rust crate while providing a native Kotlin Multiplatform API. Every Kotlin file carries a `// port-lint: source <path>` header naming its upstream Rust counterpart so the AST-distance tool can track provenance.

---

## Upstream README — `clap-rs/clap`

> The text below is reproduced and lightly edited from [`https://github.com/clap-rs/clap`](https://github.com/clap-rs/clap). It is the upstream project's own description and remains under the upstream authors' authorship; links have been rewritten to absolute upstream URLs so they continue to resolve from this repository.

## clap

> **Command Line Argument Parser for Rust**

[![Crates.io](https://img.shields.io/crates/v/clap?style=flat-square)](https://crates.io/crates/clap)
[![Crates.io](https://img.shields.io/crates/d/clap?style=flat-square)](https://crates.io/crates/clap)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue?style=flat-square)](LICENSE-APACHE)
[![License](https://img.shields.io/badge/license-MIT-blue?style=flat-square)](LICENSE-MIT)
[![Build Status](https://img.shields.io/github/actions/workflow/status/clap-rs/clap/ci.yml?branch=master&style=flat-square)](https://github.com/clap-rs/clap/actions/workflows/ci.yml?query=branch%3Amaster)
[![Coverage Status](https://img.shields.io/coveralls/github/clap-rs/clap/master?style=flat-square)](https://coveralls.io/github/clap-rs/clap?branch=master)
[![Contributors](https://img.shields.io/github/contributors/clap-rs/clap?style=flat-square)](https://github.com/clap-rs/clap/graphs/contributors)

Dual-licensed under [Apache 2.0](https://github.com/clap-rs/clap/blob/HEAD/LICENSE-APACHE) or [MIT](https://github.com/clap-rs/clap/blob/HEAD/LICENSE-MIT).

## Get Started

```console
$ cargo add clap
```

## About

Create your command-line parser, with all of the bells and whistles, declaratively or procedurally.

For more details, see:
- [docs.rs](https://docs.rs/clap/latest/clap/)
- [examples](https://github.com/clap-rs/clap/blob/HEAD/examples/)

## Sponsors

<!-- omit in TOC -->
### Gold

[![](https://opencollective.com/clap/tiers/gold.svg?width=890)](https://opencollective.com/clap)

<!-- omit in TOC -->
### Silver

[![](https://opencollective.com/clap/tiers/silver.svg?width=890)](https://opencollective.com/clap)

<!-- omit in TOC -->
### Bronze

[![](https://opencollective.com/clap/tiers/bronze.svg?width=890)](https://opencollective.com/clap)

<!-- omit in TOC -->
### Backer

[![](https://opencollective.com/clap/tiers/backer.svg?width=890)](https://opencollective.com/clap)

---

## About this Kotlin port

### Installation

```kotlin
dependencies {
    implementation("io.github.kotlinmania:clap-kotlin:0.1.0")
}
```

### Building

```bash
./gradlew build
./gradlew test
```

### Targets

- macOS arm64
- Linux x64
- Windows mingw-x64
- iOS arm64 / simulator-arm64 (Swift export + XCFramework)
- JS (browser + Node.js)
- Wasm-JS (browser + Node.js)
- Android (API 24+)

### Porting guidelines

See [AGENTS.md](AGENTS.md) and [CLAUDE.md](CLAUDE.md) for translator discipline, port-lint header convention, and Rust → Kotlin idiom mapping.

### License

This Kotlin port is distributed under the same MIT license as the upstream [`clap-rs/clap`](https://github.com/clap-rs/clap). See [LICENSE](LICENSE) (and any sibling `LICENSE-*` / `NOTICE` files mirrored from upstream) for the full text.

Original work copyrighted by the clap authors.  
Kotlin port: Copyright (c) 2026 Sydney Renee and The Solace Project.

### Acknowledgments

Thanks to the [`clap-rs/clap`](https://github.com/clap-rs/clap) maintainers and contributors for the original Rust implementation. This port reproduces their work in Kotlin Multiplatform; bug reports about upstream design or behavior should go to the upstream repository.
