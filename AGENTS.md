# AGENTS.md

## Cursor Cloud specific instructions

### Repository overview

This repository currently contains **no application code**. Across all branches
(`master`, `newsahil`) and the full git history, the only tracked files are:

- `.github/workflows/blank.yml` — a starter GitHub Actions CI workflow whose only
  steps echo `Hello, world!` and a couple of placeholder lines.
- `.github/workflows/1.png` — a static marketing banner image.

### Consequences for development setup

There is nothing to install, build, lint, test, or run as a local application:

- No package manifest (`package.json`, `requirements.txt`, `pyproject.toml`,
  `go.mod`, `Cargo.toml`, etc.).
- No source code, no test suite, no build tooling, no runnable service.
- The update script is intentionally a no-op; there are no dependencies to refresh.

### What you *can* verify

- The workflow YAML is valid and defines a single `build` job:
  `python3 -c "import yaml; yaml.safe_load(open('.github/workflows/blank.yml'))"`.
- The workflow's runnable content is just shell `echo` steps; running them locally
  reproduces the CI output (`echo Hello, world!`).

If/when real application code is added, replace this section with concrete
install/build/lint/test/run instructions for the new stack.
