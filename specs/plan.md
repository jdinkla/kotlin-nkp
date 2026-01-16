# kotlin-nkp Remaining Work

**Last Updated:** 2026-01-16

## High Priority (Next)

| Item | Description | Effort |
|------|-------------|--------|
| **Check/update grammar-tools** | Review upstream activity, consider alternatives (PSI, kotlin-analysis-api, Tree-sitter) | Low |

## Medium Priority

| Item | Description | Effort |
|------|-------------|--------|
| **HTML reports** | Standalone HTML with embedded Mermaid diagrams, navigation, filtering | Medium |
| **Streaming JSON** | Write files to JSON as parsed, reduce memory | Medium |
| **Chunked processing** | Process packages/files in batches | Medium |
| **Lazy loading** | Don't deserialize entire model for subset queries | Medium |

## Analysis Capabilities

| Feature | Effort |
|---------|--------|
| Cyclomatic complexity | Medium |
| LOC metrics | Low |
| Code duplication detection | High |
| Unused import detection | Low |
| Large class detection | Low |
| Long method detection | Low |
| Deep inheritance detection | Low |

## Other Ideas

- **Better error recovery** - Partial parse results instead of skipping entire file
- **Configuration file support** - `.nkp.yml` or `nkp.config.json` for project-specific rules
- **Watch mode** - Monitor source directories for changes, incrementally update model
- **Comparison/diff mode** - Compare two model.json files, show changes for PR reviews

## Completed

- [x] Parser improvements (extension functions, modifiers)
- [x] Multi-source directory support
- [x] Gradle plugin
- [x] Circular dependency detection
- [x] Incremental parsing (`--incremental` flag)
