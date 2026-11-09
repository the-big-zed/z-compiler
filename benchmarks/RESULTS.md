# Z benchmark results

Generated on `2026-11-09T12:38:11Z` from commit `74a363b` (`Merge pull request #234 from the-big-zed/changelog/update`) on branch `main`.

Positive `Z vs C` means Z is faster. The headline uses the fastest run of each binary. The target is a geometric mean of at least 1.20x across the suite.

## Summary

| Metric | Value |
| --- | ---: |
| Cases | 18 |
| Geometric mean speedup (best) | 1.210x |
| Median case speedup (best) | 1.000x |
| Geometric mean speedup (median) | 1.208x |
| Z faster | 13 |
| C faster | 5 |
| Ties | 0 |
| Total C best | 3695.589 ms |
| Total Z best | 3504.943 ms |
| Highest C RSD | 28.92% (string_concat) |
| Highest Z RSD | 7.28% (ptr_accum) |

Run-to-run variance above 5%: `string_concat`, `ptr_accum`. Treat those speedups as indicative and rerun on an idle machine.

## Categories

| Category | Cases | Geometric mean (best) | Geometric mean (median) |
| --- | ---: | ---: | ---: |
| alias | 1 | 1.001x | 1.000x |
| allocation | 1 | 4.147x | 4.068x |
| calls | 1 | 1.000x | 1.000x |
| compute | 7 | 1.086x | 1.086x |
| float | 1 | 0.841x | 0.841x |
| integer | 4 | 0.972x | 0.972x |
| memory | 1 | 1.877x | 1.877x |
| pointers | 1 | 1.467x | 1.467x |
| strings | 1 | 2.018x | 2.016x |

## Results

| Benchmark | Category | C best (ms) | Z best (ms) | Z vs C | C median (ms) | Z median (ms) | C RSD | Z RSD |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| fibonacci | compute | 164.348 | 164.361 | -0.0% | 164.408 | 164.421 | 0.03% | 0.02% |
| collatz | compute | 264.639 | 264.622 | +0.0% | 264.687 | 264.667 | 0.01% | 0.01% |
| pi | compute | 114.267 | 64.110 | +43.9% | 114.292 | 64.169 | 0.02% | 0.06% |
| primes | compute | 214.458 | 214.463 | -0.0% | 214.528 | 214.557 | 0.02% | 0.02% |
| ackermann | compute | 64.132 | 64.080 | +0.1% | 64.170 | 64.156 | 0.04% | 0.06% |
| mandelbrot | compute | 114.280 | 114.226 | +0.0% | 114.312 | 114.300 | 0.03% | 0.08% |
| hash | compute | 214.497 | 214.516 | -0.0% | 214.558 | 214.561 | 0.02% | 0.02% |
| alias | alias | 64.123 | 64.067 | +0.1% | 64.169 | 64.154 | 0.11% | 0.06% |
| alloc_churn | allocation | 31.698 | 7.643 | +75.9% | 31.857 | 7.831 | 0.39% | 1.28% |
| string_concat | strings | 31.793 | 15.758 | +50.4% | 31.964 | 15.853 | 28.92% | 0.35% |
| fib | calls | 414.975 | 414.918 | +0.0% | 415.035 | 415.033 | 0.01% | 0.02% |
| collatz | integer | 264.489 | 264.475 | +0.0% | 264.524 | 264.503 | 0.01% | 0.01% |
| gcd_sum | integer | 314.753 | 314.742 | +0.0% | 314.788 | 314.821 | 0.04% | 0.01% |
| primes | integer | 214.508 | 214.470 | +0.0% | 214.534 | 214.531 | 0.01% | 0.02% |
| digit_sum | integer | 414.960 | 465.039 | -12.1% | 415.055 | 465.158 | 0.01% | 0.01% |
| monte_carlo | float | 264.568 | 314.711 | -19.0% | 264.648 | 314.780 | 0.02% | 0.01% |
| concat_chain | memory | 214.449 | 114.247 | +46.7% | 214.499 | 114.299 | 0.02% | 0.02% |
| ptr_accum | pointers | 314.654 | 214.493 | +31.8% | 314.755 | 214.547 | 0.02% | 7.28% |

`best` is the fastest timed run and is the headline comparison. `median` and RSD show how stable the sample is.

## Method

- C flags: `-O3 -march=native -flto -funroll-loops -mtune=native -std=c17 -fno-builtin`.
- Z pipeline: `opt -O3 -mcpu=native; clang -O3 -flto -march=native -funroll-loops -mtune=native`.
- Warmup runs per binary: 2.
- Timed runs per binary: 10.
- Pinning: CPU 0.
- Statistics: best-of-N and median wall-clock time, plus relative standard deviation; interleaved execution.
- Correctness: both binaries must exit successfully and print the same result before timing.

## Environment

| Field | Value |
| --- | --- |
| OS | Ubuntu 26.04.1 LTS |
| Kernel | 7.0.0-1019-azure |
| CPU | AMD EPYC 9V74 80-Core Processor |
| Cores | 4 |
| Affinity | [0, 1, 2, 3] |
| CPU governor | unavailable |
| Load average | [0.97, 0.51, 0.2] |
| Clang | Ubuntu clang version 22.1.2 (1ubuntu1) |
| opt | Ubuntu LLVM version 22.1.2 |
| Java | openjdk version "21.0.13" 2026-10-20 LTS |
| Working tree | clean |

## Reproduce

```sh
python3 benchmarks/bench.py --runs 10 --warmup 2
```
